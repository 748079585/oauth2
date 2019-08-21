package com.example.demo.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.example.demo.entity.SysRoleUser;

/**
 * @author lei
 * @date 2019/08/16
 */
public interface SysRoleUserDao extends JpaRepository<SysRoleUser, Long>{
	
	
	/*
	 * @Modifying作用：
	 * 
	 * （1）可以通过自定义的 JPQL 完成 UPDATE 和 DELETE 操作。 注意： JPQL 不支持使用 INSERT； （2）在 @Query
	 * 注解中编写 JPQL 语句， 但必须使用 @Modifying 进行修饰. 以通知 SpringData， 这是一个 UPDATE 或 DELETE 操作
	 * （3）UPDATE 或 DELETE 操作需要使用事务，此时需要定义 Service 层，在 Service 层的方法上添加事务操作； （4）默认情况下，
	 * SpringData 的每个方法上有事务， 但都是一个只读事务。 他们不能完成修改操作。
	 */
	
	/**
	 * 删除这个用户所对应的用户角色关系
	 * @param UserId
	 * @return
	 */
	@Modifying
	@Transactional
	int deleteByUserId(Long userId);
	
	/**
	 * 删除该角色下的所有用户
	 * @param roleId
	 * @return
	 */
	@Modifying
	@Transactional
	int deleteByRoleId(Long roleId);
}
