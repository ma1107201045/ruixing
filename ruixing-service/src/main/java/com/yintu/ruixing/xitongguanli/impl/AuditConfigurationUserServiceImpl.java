package com.yintu.ruixing.xitongguanli.impl;

import com.yintu.ruixing.xitongguanli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/26 14:27
 */
@Service
@Transactional
public class AuditConfigurationUserServiceImpl implements AuditConfigurationUserService {
    @Autowired
    private AuditConfigurationUserDao auditConfigurationUserDao;
    @Autowired
    private UserService userService;

    @Override
    public void add(AuditConfigurationUserEntity entity) {
        auditConfigurationUserDao.insertSelective(entity);
    }

    @Override
    public void remove(Long id) {
        auditConfigurationUserDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(AuditConfigurationUserEntity entity) {
        auditConfigurationUserDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public AuditConfigurationUserEntity findById(Long id) {
        return auditConfigurationUserDao.selectByPrimaryKey(id);
    }

    @Override
    public void remove(AuditConfigurationUserEntityExample auditConfigurationUserEntityExample) {
        auditConfigurationUserDao.deleteByExample(auditConfigurationUserEntityExample);
    }

    @Override
    public List<AuditConfigurationUserEntity> findByExample(AuditConfigurationUserEntityExample auditConfigurationUserEntityExample) {
        return auditConfigurationUserDao.selectByExample(auditConfigurationUserEntityExample);
    }

    @Override
    public List<UserEntity> findUserById(Long auditConfigurationId) {
        List<UserEntity> userEntities = new ArrayList<>();
        AuditConfigurationUserEntityExample auditConfigurationUserEntityExample = new AuditConfigurationUserEntityExample();
        AuditConfigurationUserEntityExample.Criteria criteria1 = auditConfigurationUserEntityExample.createCriteria();
        criteria1.andAuditConfigurationIdEqualTo(auditConfigurationId);
        List<AuditConfigurationUserEntity> auditConfigurationUserEntities = this.findByExample(auditConfigurationUserEntityExample);
        for (AuditConfigurationUserEntity auditConfigurationUserEntity : auditConfigurationUserEntities) {
            UserEntity userEntity = userService.findById(auditConfigurationUserEntity.getUserId());
            userEntity.setRoleEntities(null);
            userEntity.setDepartmentEntities(null);
            userEntities.add(userEntity);
        }
        userEntities = userEntities.stream()
                .sorted(Comparator.comparing(UserEntity::getId).reversed())
                .collect(Collectors.toList());
        return userEntities;
    }
}