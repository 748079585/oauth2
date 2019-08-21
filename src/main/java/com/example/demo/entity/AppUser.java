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
 * @author lei
 * @date 2019/08/16
 */
@Table(name = "app_user")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppUser implements Serializable {

	private static final long serialVersionUID = 611197991672067628L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String password;
	private String nickname;
	private String headImgUrl;
	private String phone;
	private Integer sex;
	/**
	 * 状态
	 */
	private Boolean enabled;
	/**
	 * 用户类型
	 */
	private String type;
	private Date createTime;
	private Date updateTime;

}
