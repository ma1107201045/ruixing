package com.yintu.ruixing.xitongguanli.impl;

import com.yintu.ruixing.master.xitongguanli.AuditConfigurationUserDao;
import com.yintu.ruixing.master.xitongguanli.UserDao;
import com.yintu.ruixing.xitongguanli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
    public List<UserEntity> findUsersById(Long auditConfigurationId) {
        List<UserEntity> userEntities = new ArrayList<>();
        AuditConfigurationUserEntityExample auditConfigurationUserEntityExample = new AuditConfigurationUserEntityExample();
        AuditConfigurationUserEntityExample.Criteria criteria = auditConfigurationUserEntityExample.createCriteria();
        criteria.andAuditConfigurationIdEqualTo(auditConfigurationId);
        auditConfigurationUserEntityExample.setOrderByClause("sort");
        List<AuditConfigurationUserEntity> auditConfigurationUserEntities = this.findByExample(auditConfigurationUserEntityExample);
        for (AuditConfigurationUserEntity auditConfigurationUserEntity : auditConfigurationUserEntities) {
            UserEntity userEntity = userService.findById(auditConfigurationUserEntity.getUserId());
            userEntities.add(userEntity);
        }
        return userEntities;
    }

    @Override
    public List<AuditConfigurationUserEntity> findByauditConfigurationId(Long id) {
        return auditConfigurationUserDao.findByauditConfigurationId(id);
    }

    @Override
    public List<Long> findDistinctFieldByExample(String field, Long auditConfigurationId, Integer sort) {
        return auditConfigurationUserDao.selectDistinctFieldByExample(field, auditConfigurationId, sort);
    }
}
