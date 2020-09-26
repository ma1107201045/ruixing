package com.yintu.ruixing.xitongguanli.impl;

import com.yintu.ruixing.xitongguanli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
