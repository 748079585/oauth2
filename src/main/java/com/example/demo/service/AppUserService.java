package com.example.demo.service;

import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.example.demo.entity.AppUser;
import com.example.demo.entity.LoginAppUser;
import com.example.demo.entity.SysRole;

/**
 * @author lei
 * @date 2019/08/16
 */
public interface AppUserService {

	/**
	 * 添加用户
	 * @param appUser
	 */
	void addAppUser(AppUser appUser);

	/**
	 * 修改用户
	 * @param appUser
	 */
	void updateAppUser(AppUser appUser);

	/**
	 * 通过用户名获取当前登陆对象LoginAppUser
	 * @param username
	 * @return
	 */
	LoginAppUser findByUsername(String username);
	
	/**
	 * 通过名字查询用户
	 * @param name
	 * @return
	 */
	AppUser findByName(String name);

	/**
	 * 通过id查询用户
	 * @param id
	 * @return
	 */
	AppUser findById(Long id);

	/**
	 * 给用户设置角色
	 * @param id
	 * @param roleIds
	 */
	void setRoleToUser(Long id, Set<Long> roleIds);

	/**
	 * 修改密码
	 * @param id
	 * @param oldPassword
	 * @param newPassword
	 */
	void updatePassword(Long id, String oldPassword, String newPassword);

	/**
	 * 查询用户，分页
	 * @param params  
	 * key1:start 页数,key2:length 每页长度
	 * @return
	 */
	Page<AppUser> findUsers(Map<String, Object> params);

	/**
	 * 查询用户角色信息
	 * @param userId
	 * @return
	 */
	Set<SysRole> findRolesByUserId(Long userId);

}
