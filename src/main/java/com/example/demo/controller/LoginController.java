package com.example.demo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.constans.SystemClientInfo;
import com.example.demo.util.HttpClientPostFs;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lei
 * @date 2019/08/16
 */
@Slf4j
@Controller
public class LoginController {
	
	@Value("${server.port}")
	private int port;
	
	//  forward:/oauth/token 会报未认证异常，因为进入接口前，需要经过ClientCredentialsTokenEndpointFilter过滤器的
	//  attemptAuthentication尝试认证方法生成个xxx的authentication ,重定向不会经过这个过滤器
	
	// 所以直接调用TokenEndpoint.class 的/oauth/token接口进行登陆验证和获取token

    @RequestMapping(value = "/sys/login" , method = RequestMethod.POST)
    public ModelAndView forwardTest(String username, String password) {
        Map<String, Object> model = new HashMap<>();
        model.put(OAuth2Utils.GRANT_TYPE, "password");
        model.put(OAuth2Utils.CLIENT_ID, SystemClientInfo.CLIENT_ID);
        model.put("client_secret", SystemClientInfo.CLIENT_SECRET);
        model.put(OAuth2Utils.SCOPE, SystemClientInfo.CLIENT_SCOPE);
        model.put("username", username);
        model.put("password", password);
        return new ModelAndView("forward:/oauth/token" ,model) ;
    }
	
	
	/**
     * 系统刷新refresh_token
     *
     * @param refresh_token
     * @return
     */
    @PostMapping("/sys/refresh_token")
    public String refresh_token(String refreshToken) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OAuth2Utils.GRANT_TYPE, "refresh_token");
        parameters.put(OAuth2Utils.CLIENT_ID, SystemClientInfo.CLIENT_ID);
        parameters.put("client_secret", SystemClientInfo.CLIENT_SECRET);
        parameters.put(OAuth2Utils.SCOPE, SystemClientInfo.CLIENT_SCOPE);
        parameters.put("refresh_token", refreshToken);

        StringBuffer buffer = new StringBuffer(OAuth2Utils.GRANT_TYPE + "=refresh_token&");
        buffer.append(OAuth2Utils.CLIENT_ID + "=" + SystemClientInfo.CLIENT_ID + "&");
        buffer.append("client_secret=" + SystemClientInfo.CLIENT_SECRET + "&");
        buffer.append(OAuth2Utils.SCOPE + "=" + SystemClientInfo.CLIENT_SCOPE + "&");
        buffer.append("refresh_token=" + refreshToken);
        return "forward:/oauth/token?" + buffer;
    }

	

	@Autowired
	private ConsumerTokenServices tokenServices;

	/**
	 * 注销登陆/退出 移除access_token和refresh_token<br>
	 * 用ConsumerTokenServices，该接口的实现类DefaultTokenServices已有相关实现，我们不再重复造轮子
	 * 
	 * @param access_token
	 */
	@GetMapping("/sys/logout")
	public void removeToken(String access_token,
			@RequestHeader(required = false, value = "Authorization") String token) {
		if (StringUtils.isBlank(access_token)) {
			if (StringUtils.isNoneBlank(token)) {
				access_token = token.substring(OAuth2AccessToken.BEARER_TYPE.length() + 1);
			}
		}
		boolean flag = tokenServices.revokeToken(access_token);
		if (flag) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			log.info("{}退出", authentication.getName());
		}
	}
}
