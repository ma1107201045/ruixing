package com.yintu.ruixing.xitongguanli.impl;

import com.yintu.ruixing.xitongguanli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/26 14:14
 */
@Service
@Transactional
public class AuditConfigurationServiceImpl implements AuditConfigurationService {
    @Autowired
    private AuditConfigurationDao auditConfigurationDao;
    @Autowired
    private AuditConfigurationUserService auditConfigurationUserService;

    @Override
    public void add(AuditConfigurationEntity entity) {
        auditConfigurationDao.insertSelective(entity);
    }

    @Override
    public void remove(Long id) {
        auditConfigurationDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(AuditConfigurationEntity entity) {
        auditConfigurationDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public AuditConfigurationEntity findById(Long id) {
        AuditConfigurationEntityExample auditConfigurationEntityExample = new AuditConfigurationEntityExample();
        AuditConfigurationEntityExample.Criteria criteria = auditConfigurationEntityExample.createCriteria();
        criteria.andIdEqualTo(id);
        auditConfigurationDao.selectByExample(auditConfigurationEntityExample);
        return null;
    }

    @Override
    public void add(AuditConfigurationEntity auditConfigurationEntity, Long[] userIds) {
        this.add(auditConfigurationEntity);
        this.addOrEditAuditConfigurationUser(auditConfigurationEntity.getId(), userIds, auditConfigurationEntity.getModifiedBy());
    }

    @Override
    public void remove(AuditConfigurationEntityExample auditConfigurationEntityExample) {
        auditConfigurationDao.deleteByExample(auditConfigurationEntityExample);
    }

    @Override
    public void edit(AuditConfigurationEntity auditConfigurationEntity, Long[] userIds) {
        this.edit(auditConfigurationEntity);
        this.addOrEditAuditConfigurationUser(auditConfigurationEntity.getId(), userIds, auditConfigurationEntity.getModifiedBy());
    }

    @Override
    public List<AuditConfigurationEntity> findByExample(AuditConfigurationEntityExample auditConfigurationEntityExample) {
        return auditConfigurationDao.selectByExample(auditConfigurationEntityExample);
    }

    @Override
    public void addOrEditAuditConfigurationUser(Long id, Long[] userIds, String loginUserName) {
        AuditConfigurationUserEntityExample auditConfigurationUserEntityExample = new AuditConfigurationUserEntityExample();
        AuditConfigurationUserEntityExample.Criteria criteria = auditConfigurationUserEntityExample.createCriteria();
        criteria.andAuditConfigurationIdEqualTo(id);
        List<AuditConfigurationUserEntity> auditConfigurationUserEntities = auditConfigurationUserService.findByExample(auditConfigurationUserEntityExample);
        if (auditConfigurationUserEntities.size() > 0) {
            auditConfigurationUserService.remove(auditConfigurationUserEntityExample);
        }
        //去重
        Set<Long> set = new HashSet<>(Arrays.asList(userIds));
        for (Long userId : set) {
            AuditConfigurationUserEntity auditConfigurationUserEntity = new AuditConfigurationUserEntity();
            auditConfigurationUserEntity.setCreateBy(loginUserName);
            auditConfigurationUserEntity.setCreateTime(new Date());
            auditConfigurationUserEntity.setModifiedBy(loginUserName);
            auditConfigurationUserEntity.setModifiedTime(new Date());
            auditConfigurationUserEntity.setAuditConfigurationId(id);
            auditConfigurationUserEntity.setUserId(userId);
            auditConfigurationUserService.add(auditConfigurationUserEntity);
        }
    }
}
