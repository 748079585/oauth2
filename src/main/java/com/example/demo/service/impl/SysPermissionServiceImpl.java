package com.example.demo.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.SysPermissionDao;
import com.example.demo.dao.SysRolePermissionDao;
import com.example.demo.entity.SysPermission;
import com.example.demo.service.SysPermissionService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lei
 * @date 2019/08/16
 */
@Slf4j
@Service
public class SysPermissionServiceImpl implements SysPermissionService {

	@Autowired
	private SysPermissionDao sysPermissionDao;
	@Autowired
	private SysRolePermissionDao rolePermissionDao;

	@Override
	public Set<SysPermission> findByRoleIds(Set<Long> roleIds) {
		return sysPermissionDao.findPermissionsByRoleIds(roleIds);
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public void save(SysPermission sysPermission) {
		SysPermission permission = sysPermissionDao.findByPermission(sysPermission.getPermission());
		if (permission != null) {
			throw new IllegalArgumentException("权限标识已存在");
		}
		sysPermission.setCreateTime(new Date());
		sysPermission.setUpdateTime(sysPermission.getCreateTime());

		sysPermissionDao.save(sysPermission);
		log.info("保存权限标识：{}", sysPermission);
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public void update(SysPermission sysPermission) {
		sysPermission.setUpdateTime(new Date());
		sysPermissionDao.save(sysPermission);
		log.info("修改权限标识：{}", sysPermission);
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public void delete(Long id) {
		Optional<SysPermission> optional = sysPermissionDao.findById(id);
		if (!optional.isPresent()) {
			throw new IllegalArgumentException("权限标识不存在");
		}

		sysPermissionDao.deleteById(id);
		rolePermissionDao.deleteByPermissionId(id);
		log.info("删除权限标识：{}", optional.get());
	}

	@Override
	public Page<SysPermission> findPermissions(Map<String, Object> params) {
		Sort sort = new Sort(Direction.DESC, "id");
		PageRequest pageable = PageRequest.of(Integer.parseInt((String) params.get("start")),
				Integer.parseInt((String) params.get("lenth")), sort);
		Page<SysPermission> findAll = sysPermissionDao.findAll(pageable);
		return findAll;
	}
}
