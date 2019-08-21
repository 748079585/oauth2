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
 * 角色
 * @author lei
 * @date 2019/08/16
 */
@Table(name = "sys_role")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysRole implements Serializable {

	private static final long serialVersionUID = -2054359538140713354L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String code;
	private String name;
	private Date createTime;
	private Date updateTime;
}
