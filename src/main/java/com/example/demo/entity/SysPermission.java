package com.example.demo.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 权限标识
 * @author lei
 * @date 2019/08/16
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sys_permission")
public class SysPermission implements Serializable {

	private static final long serialVersionUID = 280565233032255804L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String permission;
	private String name;
	private Date createTime;
	private Date updateTime;

}
