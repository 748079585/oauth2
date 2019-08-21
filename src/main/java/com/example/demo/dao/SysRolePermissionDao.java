package com.example.demo.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.example.demo.entity.SysRolePermission;

/**
 * @author lei
 * @date 2019/08/16
 */
public interface SysRolePermissionDao extends JpaRepository<SysRolePermission, Long> {
	
	@Modifying
	@Transactional
	void deleteByRoleId(Long roleId);
	
	@Modifying
	@Transactional
	void deleteByPermissionId(Long permissionId);

	@Modifying
	@Transactional
	void deleteByRoleIdAndPermissionId(Long roleId, Long permissionId);
}
