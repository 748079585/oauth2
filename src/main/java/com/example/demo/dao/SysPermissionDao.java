package com.example.demo.dao;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.SysPermission;

/**
 * @author lei
 * @date 2019/08/16
 */
public interface SysPermissionDao extends JpaRepository<SysPermission, Long>{

	/**
	 * 通过角色id，查询权限
	 * 
	 * @param roleIds
	 * @return
	 */
	@Query(value = "select p.* from sys_permission p inner join sys_role_permission rp on p.id = rp.permission_id where rp.role_id in(:ids)", nativeQuery = true)
	Set<SysPermission> findPermissionsByRoleIds(@Param("ids") Set<Long> roleIds);
	
	SysPermission findByPermission(String permission);
}
