package com.yintu.ruixing.xitongguanli.impl;

import com.yintu.ruixing.master.xitongguanli.PermissionRoleDao;
import com.yintu.ruixing.xitongguanli.PermissionRoleEntity;
import com.yintu.ruixing.xitongguanli.PermissionRoleEntityExample;
import com.yintu.ruixing.xitongguanli.PermissionRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/5/19 12:30
 */
@Service
@Transactional
public class PermissionRoleServiceImpl implements PermissionRoleService {

    @Autowired
    private PermissionRoleDao permissionRoleDao;

    @Override
    public void add(PermissionRoleEntity permissionRoleEntity) {
        permissionRoleDao.insertSelective(permissionRoleEntity);
    }

    @Override
    public void edit(PermissionRoleEntity permissionRoleEntity) {
        permissionRoleDao.updateByPrimaryKeySelective(permissionRoleEntity);
    }

    @Override
    public void remove(Long id) {
        permissionRoleDao.deleteByPrimaryKey(id);
    }

    @Override
    public void removeByExample(PermissionRoleEntityExample permissionRoleEntityExample) {
        permissionRoleDao.deleteByExample(permissionRoleEntityExample);
    }

    @Override
    public PermissionRoleEntity findById(Long id) {
        return permissionRoleDao.selectByPrimaryKey(id);
    }

    @Override
    public List<PermissionRoleEntity> findByExample(PermissionRoleEntityExample permissionRoleEntityExample) {
        return permissionRoleDao.selectByExample(permissionRoleEntityExample);
    }
}
