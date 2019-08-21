package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.AppUser;

/**
 * @author lei
 * @date 2019/08/16
 */
public interface AppUserDao extends JpaRepository<AppUser, Long> {
	
	AppUser findByUsername(String username);
	
}
