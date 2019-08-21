package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色与权限关系表
 * @author lei
 * @date 2019/08/16
 */
@Table(name = "sys_role_permission")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysRolePermission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long roleId;
	
	private Long permissionId;
}
