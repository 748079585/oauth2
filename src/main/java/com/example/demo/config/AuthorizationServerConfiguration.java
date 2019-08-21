package com.example.demo.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import com.example.demo.entity.LoginAppUser;
import com.example.demo.service.RedisClientDetailsService;
import com.example.demo.service.impl.RandomAuthenticationKeyGenerator;
import com.example.demo.service.impl.RedisAuthorizationCodeServices;
import com.example.demo.service.impl.TokenServices;

/**
 * 授权服务器配置
 * 
 * @author lei
 * @date 2019/08/16
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private DataSource dataSource;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 登陆后返回的json数据是否追加当前用户信息<br>
     * 默认false
     */
    @Value("${access_token.add-userinfo:false}")
    private boolean addUserInfo;

    @Autowired
    private RedisAuthorizationCodeServices redisAuthorizationCodeServices;

    @Autowired
    private RedisClientDetailsService redisClientDetailsService;

    @Autowired
    private TokenServices tokenServices;

    /**
     * 令牌存储
     */
    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        // 2018.08.04添加,解决同一username每次登陆access_token都相同的问题
        // redisTokenStore.setAuthenticationKeyGenerator(new RandomAuthenticationKeyGenerator());

        return redisTokenStore;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 1、直接内存中存储两个clients,表示app端和pc端 ,app的token过期时间设置长点72000，pc端1小时
        clients.inMemory()
            .withClient("app").secret(passwordEncoder.encode("123456"))
            .authorizedGrantTypes("password", "refresh_token").scopes("all", "read", "write")
            .accessTokenValiditySeconds(72000)  
            .and().withClient("pc").secret(passwordEncoder.encode("123456"))
            .authorizedGrantTypes("password", "refresh_token").scopes("all", "read", "write")
            .accessTokenValiditySeconds(3600);

        // 2、数据库存储clients , 数据库中建个oauthClientDetails表，保存客户端信息
        // clients.jdbc(dataSource);
        // 3、这里优化一下，使用redis缓存clients，详细看下redisClientDetailsService这个实现类
        // clients.withClientDetails(redisClientDetailsService);
        // redisClientDetailsService.loadAllClientToCache();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(this.authenticationManager);
        // endpoints.tokenStore(new RedisTokenStore(redisConnectionFactory));
        endpoints.tokenStore(tokenStore());
        // 重写tokenServices，主要修改了其中的createAccessToken方法，修改为，每次登陆更新token，上一个登陆的token就失效了，就意味着单用户踢人模式
        endpoints.tokenServices(tokenServices); 
        // 授权码模式下，code存储
//        endpoints.authorizationCodeServices(redisAuthorizationCodeServices);
        // 将当前用户信息追加到登陆后返回数据里
        endpoints.tokenEnhancer((accessToken, authentication) -> {
            addLoginUserInfo(accessToken, authentication);
            return accessToken;
        });
    }

    /**
     * 将当前用户信息追加到登陆后返回的json数据里<br>
     * 通过参数access_token.add-userinfo控制<br>
     * 2018.07.13
     *
     * @param accessToken
     * @param authentication
     */
    private void addLoginUserInfo(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if (!addUserInfo) {
            return;
        }

        if (accessToken instanceof DefaultOAuth2AccessToken) {
            DefaultOAuth2AccessToken defaultOauth2AccessToken = (DefaultOAuth2AccessToken)accessToken;

            Authentication userAuthentication = authentication.getUserAuthentication();
            Object principal = userAuthentication.getPrincipal();
            if (principal instanceof LoginAppUser) {
                LoginAppUser loginUser = (LoginAppUser)principal;
                // 旧的附加参数
                Map<String, Object> map = new HashMap<>(defaultOauth2AccessToken.getAdditionalInformation());
                // 追加当前登陆用户
                map.put("loginUser", loginUser);

                defaultOauth2AccessToken.setAdditionalInformation(map);
            }
        }
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 允许表单认证
        security.allowFormAuthenticationForClients();
        security.checkTokenAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }
}
