package com.example.demo.dao;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.SysRole;

/**
 * @author lei
 * @date 2019/08/16
 */
public interface SysRoleDao extends JpaRepository<SysRole, Long>{


	/**
	 * 通过用户id查找他的属于的角色，  用户对应角色，角色对应权限
	 * @param userId
	 * @return
	 */
	@Query(value = "select r.* from sys_role_user ru inner join sys_role r on r.id = ru.role_id where ru.user_id = ?1" ,nativeQuery = true)
	Set<SysRole> findRolesByUserId(Long userId);
	
	SysRole findByCode(String code);
}
