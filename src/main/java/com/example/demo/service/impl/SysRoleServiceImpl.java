package com.example.demo.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.example.demo.dao.SysPermissionDao;
import com.example.demo.dao.SysRoleDao;
import com.example.demo.dao.SysRolePermissionDao;
import com.example.demo.dao.SysRoleUserDao;
import com.example.demo.entity.SysPermission;
import com.example.demo.entity.SysRole;
import com.example.demo.entity.SysRolePermission;
import com.example.demo.service.SysRoleService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lei
 * @date 2019/08/16
 */
@Slf4j
@Service
public class SysRoleServiceImpl implements SysRoleService {

	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private SysRoleUserDao userRoleDao;
	@Autowired
	private SysRolePermissionDao rolePermissionDao;
	@Autowired
	private SysPermissionDao sysPermissionDao;

	@Transactional(rollbackFor=Exception.class)
	@Override
	public void save(SysRole sysRole) {
		SysRole role = sysRoleDao.findByCode(sysRole.getCode());
		if (role != null) {
			throw new IllegalArgumentException("角色code已存在");
		}

		sysRole.setCreateTime(new Date());
		sysRole.setUpdateTime(sysRole.getCreateTime());

		sysRoleDao.save(sysRole);
		log.info("保存角色：{}", sysRole);
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public void update(SysRole sysRole) {
		sysRole.setUpdateTime(new Date());

		sysRoleDao.save(sysRole);
		log.info("修改角色：{}", sysRole);
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public void deleteRole(Long id) {
		SysRole sysRole = sysRoleDao.findById(id).get();

		sysRoleDao.deleteById(id);
		rolePermissionDao.deleteByRoleId(id);
		userRoleDao.deleteByRoleId(id);

		log.info("删除角色：{}", sysRole);
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public void setPermissionToRole(Long roleId, Set<Long> permissionIds) {
		Optional<SysRole> optional = sysRoleDao.findById(roleId);
		if (!optional.isPresent()) {
			throw new IllegalArgumentException("角色不存在");
		}

		Set<Long> set = new HashSet<>();
		set.add(roleId);
		// 查出角色对应的old权限
		Set<Long> oldPermissionIds = sysPermissionDao.findPermissionsByRoleIds(set).stream().map(p -> p.getId())
				.collect(Collectors.toSet());

		// 需要添加的权限
		Collection<Long> addPermissionIds = org.apache.commons.collections4.CollectionUtils.subtract(permissionIds,
				oldPermissionIds);
		if (!CollectionUtils.isEmpty(addPermissionIds)) {
			addPermissionIds.forEach(permissionId -> {
				rolePermissionDao.save(new SysRolePermission(null, roleId, permissionId));
			});
		}
		// 需要移除的权限
		Collection<Long> deletePermissionIds = org.apache.commons.collections4.CollectionUtils
				.subtract(oldPermissionIds, permissionIds);
		if (!CollectionUtils.isEmpty(deletePermissionIds)) {
			deletePermissionIds.forEach(permissionId -> {
				rolePermissionDao.deleteByRoleIdAndPermissionId(roleId, permissionId);
			});
		}

		log.info("给角色id：{}，分配权限：{}", roleId, permissionIds);
	}

	@Override
	public SysRole findById(Long id) {
		return sysRoleDao.findById(id).get();
	}

	@Override
	public Page<SysRole> findRoles(Map<String, Object> params) {
		Sort sort = new Sort(Direction.DESC, "id");
		PageRequest pageable = PageRequest.of(Integer.parseInt((String) params.get("start")),
				Integer.parseInt((String) params.get("lenth")), sort);
		Page<SysRole> list = sysRoleDao.findAll(pageable);
		return list;
	}

	@Override
	public Set<SysPermission> findPermissionsByRoleId(Long roleId) {
		Set<Long> set = new HashSet<>();
		set.add(roleId);
		return sysPermissionDao.findPermissionsByRoleIds(set);
	}
}
