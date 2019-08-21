package com.example.demo.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.entity.AppUser;
import com.example.demo.service.AppUserService;

/**
 * @author lei
 * @date 2019/08/16
 */
@Component
public class DataInit {

	@Autowired
	private AppUserService appUserService;
	/**
	 * 设置管理员用户
	 */
	@PostConstruct
	public void setAdminUser() {
		String name = "admin";
		AppUser user = appUserService.findByName(name);
		if(user == null) {
			System.out.println("已存在admin用户");
			AppUser appUser = new AppUser();
			appUser.setUsername("admin");
			appUser.setPassword("admin");
			appUserService.addAppUser(appUser);
		}
	}
	
}
