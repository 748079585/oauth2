package com.example.demo.service;

import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.example.demo.entity.SysPermission;
import com.example.demo.entity.SysRole;

/**
 * @author lei
 * @date 2019/08/16
 */
public interface SysRoleService {

	/**
	 * 保存角色
	 * @param sysRole
	 */
	void save(SysRole sysRole);

	/**
	 * 更新角色
	 * @param sysRole
	 */
	void update(SysRole sysRole);

	/**
	 * 删除角色
	 * @param id
	 */
	void deleteRole(Long id);

	/**
	 * 设置角色权限
	 * @param id
	 * @param permissionIds
	 */
	void setPermissionToRole(Long id, Set<Long> permissionIds);

	/**
	 * 通过角色id查找
	 * @param id
	 * @return
	 */
	SysRole findById(Long id);

	/**
	 * 通过参数角色
	 * @param params
	 * @return
	 */
	Page<SysRole> findRoles(Map<String, Object> params);

	/**
	 * 查询权限
	 * @param roleId
	 * @return
	 */
	Set<SysPermission> findPermissionsByRoleId(Long roleId);
}
