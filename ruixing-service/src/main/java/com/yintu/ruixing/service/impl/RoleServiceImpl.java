package com.yintu.ruixing.service.impl;
import com.yintu.ruixing.dao.PermissionRoleDao;
import com.yintu.ruixing.dao.RoleDao;
import com.yintu.ruixing.dao.UserRoleDao;
import com.yintu.ruixing.entity.*;
import com.yintu.ruixing.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:mlf
 * @date:2020/5/19 9:45
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private PermissionRoleDao permissionRoleDao;

    @Override
    public void add(RoleEntity roleEntity) {
        roleDao.insertSelective(roleEntity);
    }

    @Override
    public void edit(RoleEntity roleEntity) {
        roleDao.updateByPrimaryKeySelective(roleEntity);
    }

    @Override
    public void remove(Long id) {
        roleDao.deleteByPrimaryKey(id);
    }

    @Override
    public RoleEntity findById(Long id) {
        return roleDao.selectByPrimaryKey(id);
    }

    @Override
    public List<RoleEntity> findByIds(List<Long> ids) {
        RoleEntityExample roleEntityExample = new RoleEntityExample();
        RoleEntityExample.Criteria criteria = roleEntityExample.createCriteria();
        criteria.andIdIn(ids);
        return ids.size() == 0 ? new ArrayList<>() : roleDao.selectByExample(roleEntityExample);
    }

    @Override
    public List<RoleEntity> findByUserId(Long userId) {
        UserRoleEntityExample userRoleEntityExample = new UserRoleEntityExample();
        UserRoleEntityExample.Criteria criteria = userRoleEntityExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<UserRoleEntity> userRoleEntities = userRoleDao.selectByExample(userRoleEntityExample);
        List<Long> roleIds = new ArrayList<>();
        for (UserRoleEntity userRoleEntity : userRoleEntities) {
            roleIds.add(userRoleEntity.getUserId());
        }
        return this.findByIds(roleIds);
    }

    @Override
    public List<RoleEntity> findByPermissionId(Long permissionId) {
        PermissionRoleEntityExample permissionRoleEntityExample = new PermissionRoleEntityExample();
        PermissionRoleEntityExample.Criteria criteria = permissionRoleEntityExample.createCriteria();
        criteria.andPermissionIdEqualTo(permissionId);
        List<PermissionRoleEntity> permissionRoleEntities = permissionRoleDao.selectByExample(permissionRoleEntityExample);
        List<Long> roleIds = new ArrayList<>();
        for (PermissionRoleEntity permissionRoleEntity : permissionRoleEntities) {
            roleIds.add(permissionRoleEntity.getRoleId());
        }
        return this.findByIds(roleIds);
    }
}
