package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constans.SystemClientInfo;
import com.example.demo.entity.AppUser;
import com.example.demo.entity.LoginAppUser;
import com.example.demo.entity.SysRole;
import com.example.demo.service.AppUserService;
import com.example.demo.util.AppUserUtil;

/**
 * 用户管理
 * @author lei
 * @date 2019/08/16
 */
@RestController
public class UserController {

	@Autowired
	private AppUserService appUserService;

	/**
	 * 当前登录用户 LoginAppUser
	 */
	@GetMapping("/users/current")
	public LoginAppUser getLoginAppUser() {
		return AppUserUtil.getLoginAppUser();
	}

	@GetMapping(value = "/users-anon/internal", params = "username")
	public LoginAppUser findByUsername(String username) {
		return appUserService.findByUsername(username);
	}

	/**
	 * 用户查询
	 *
	 * @param params
	 */
	@PreAuthorize("hasAuthority('back:user:query')")
	@GetMapping("/users")
	public Page<AppUser> findUsers(@RequestParam Map<String, Object> params) {
		return appUserService.findUsers(params);
	}

	@PreAuthorize("hasAuthority('back:user:query')")
	@GetMapping("/users/{id}")
	public AppUser findUserById(@PathVariable Long id) {
		return appUserService.findById(id);
	}

	/**
	 * 添加用户,根据用户名注册
	 *
	 * @param appUser
	 */
	@PostMapping("/users-anon/register")
	public AppUser register(@RequestBody AppUser appUser) {
		// 用户名等信息的判断逻辑挪到service了
		appUserService.addAppUser(appUser);

		return appUser;
	}

	/**
	 * 修改自己的个人信息
	 *
	 * @param appUser
	 */
	@PutMapping("/users/me")
	public AppUser updateMe(@RequestBody AppUser appUser) {
		AppUser user = AppUserUtil.getLoginAppUser();
		appUser.setId(user.getId());

		appUserService.updateAppUser(appUser);

		return appUser;
	}

	/**
	 * 修改密码
	 *
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 */
	@PutMapping(value = "/users/password", params = { "oldPassword", "newPassword" })
	public void updatePassword(String oldPassword, String newPassword) {
		if (StringUtils.isBlank(oldPassword)) {
			throw new IllegalArgumentException("旧密码不能为空");
		}
		if (StringUtils.isBlank(newPassword)) {
			throw new IllegalArgumentException("新密码不能为空");
		}

		AppUser user = AppUserUtil.getLoginAppUser();
		appUserService.updatePassword(user.getId(), oldPassword, newPassword);
	}

	/**
	 * 管理后台，给用户重置密码
	 *
	 * @param id          用户id
	 * @param newPassword 新密码
	 */
	@PreAuthorize("hasAuthority('back:user:password')")
	@PutMapping(value = "/users/{id}/password", params = { "newPassword" })
	public void resetPassword(@PathVariable Long id, String newPassword) {
		appUserService.updatePassword(id, null, newPassword);
	}

	/**
	 * 管理后台修改用户
	 *
	 * @param appUser
	 */
	@PreAuthorize("hasAuthority('back:user:update')")
	@PutMapping("/users")
	public void updateAppUser(@RequestBody AppUser appUser) {
		appUserService.updateAppUser(appUser);
	}

	/**
	 * 管理后台给用户分配角色
	 *
	 * @param id      用户id
	 * @param roleIds 角色ids
	 */
	@PreAuthorize("hasAuthority('back:user:role:set')")
	@PostMapping("/users/{id}/roles")
	public void setRoleToUser(@PathVariable Long id, @RequestBody Set<Long> roleIds) {
		appUserService.setRoleToUser(id, roleIds);
	}

	/**
	 * 获取用户的角色
	 *
	 * @param id 用户id
	 */
	@PreAuthorize("hasAnyAuthority('back:user:role:set','user:role:byuid')")
	@GetMapping("/users/{id}/roles")
	public Set<SysRole> findRolesByUserId(@PathVariable Long id) {
		return appUserService.findRolesByUserId(id);
	}
}
