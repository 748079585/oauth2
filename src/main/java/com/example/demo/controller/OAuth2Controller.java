package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lei
 * @date 2019/08/16
 */
@Slf4j
@RestController
@RequestMapping
public class OAuth2Controller {

	/**
	 * 当前登陆用户信息<br>  其他微服务的oauth2指向该接口来获取用户信息，一样header里面 传token
	 * <p>
	 * security获取当前登录用户的方法是SecurityContextHolder.getContext().getAuthentication()<br>
	 * 返回值是接口org.springframework.security.core.Authentication，又继承了Principal<br>
	 * 这里的实现类是org.springframework.security.oauth2.provider.OAuth2Authentication<br>
	 * <p>
	 * 因此这只是一种写法，下面注释掉的三个方法也都一样，这四个方法任选其一即可，也只能选一个，毕竟uri相同，否则启动报错<br>
	 * 2018.05.23改为默认用这个方法，好理解一点
	 *
	 * @return
	 */
	@GetMapping("/user-me")
	public Authentication principal() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		log.debug("user-me:{}", authentication.getName());
		return authentication;
	}

//	@GetMapping("/user-me")
//	public Principal principal(Principal principal) {
//		log.debug("user-me:{}", principal.getName());
//		return principal;
//	}
//
//	@GetMapping("/user-me")
//	public Authentication principal(Authentication authentication) {
//		log.debug("user-me:{}", authentication.getName());
//		return authentication;
//	}
//
//	@GetMapping("/user-me")
//	public OAuth2Authentication principal(OAuth2Authentication authentication) {
//		log.debug("user-me:{}", authentication.getName());
//		return authentication;
//	}

	// @Autowired
//	private TokenStore tokenStore;
//
//	/**
//	 * 移除access_token和refresh_token
//	 *
//	 * @param access_token
//	 */
//	@DeleteMapping(value = "/remove_token", params = "access_token")
//	public void removeToken(Principal principal, String access_token) {
//		OAuth2AccessToken accessToken = tokenStore.readAccessToken(access_token);
//		if (accessToken != null) {
//			// 移除access_token
//			tokenStore.removeAccessToken(accessToken);
//
//			// 移除refresh_token
//			if (accessToken.getRefreshToken() != null) {
//				tokenStore.removeRefreshToken(accessToken.getRefreshToken());
//			}
//
//			saveLogoutLog(principal.getName());
//		}
//	}

	@Autowired
	private ConsumerTokenServices tokenServices;

	/**
	 * 注销登陆/退出 移除access_token和refresh_token<br>
	 * 2018.06.28
	 * 改为用ConsumerTokenServices，该接口的实现类DefaultTokenServices已有相关实现，我们不再重复造轮子
	 *
	 * @param access_token
	 */
	@DeleteMapping(value = "/remove_token", params = "access_token")
	public void removeToken(String accessToken) {
		boolean flag = tokenServices.revokeToken(accessToken);
		if (flag) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			log.info("用户：" + authentication.getName() + "退出登陆");
		}
	}

}
