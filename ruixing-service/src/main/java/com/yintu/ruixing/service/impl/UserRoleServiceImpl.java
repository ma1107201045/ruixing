package com.yintu.ruixing.service.impl;

import com.yintu.ruixing.dao.UserRoleDao;
import com.yintu.ruixing.entity.PermissionRoleEntity;
import com.yintu.ruixing.entity.UserRoleEntity;
import com.yintu.ruixing.entity.UserRoleEntityExample;
import com.yintu.ruixing.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/5/19 9:32
 */
@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public void add(UserRoleEntity userRoleEntity) {
        userRoleDao.insertSelective(userRoleEntity);
    }

    @Override
    public void edit(UserRoleEntity userRoleEntity) {
        userRoleDao.updateByPrimaryKeySelective(userRoleEntity);
    }

    @Override
    public void remove(Long id) {
        userRoleDao.deleteByPrimaryKey(id);
    }


    @Override
    public UserRoleEntity findById(Long id) {
        return userRoleDao.selectByPrimaryKey(id);
    }


    @Override
    public List<UserRoleEntity> findByExample(UserRoleEntityExample userRoleEntityExample) {
        return userRoleDao.selectByExample(userRoleEntityExample);
    }

    @Override
    public void removeByExample(UserRoleEntityExample userRoleEntityExample) {
        userRoleDao.deleteByExample(userRoleEntityExample);
    }

}
