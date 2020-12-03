package com.yintu.ruixing.xitongguanli.impl;

import com.github.pagehelper.PageHelper;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.master.xitongguanli.AuditConfigurationDao;
import com.yintu.ruixing.xitongguanli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    private AuditConfigurationRoleService auditConfigurationRoleService;


    @Override
    public void add(AuditConfigurationEntity entity) {
        AuditConfigurationEntityExample auditConfigurationEntityExample = new AuditConfigurationEntityExample();
        AuditConfigurationEntityExample.Criteria criteria = auditConfigurationEntityExample.createCriteria();
        criteria.andNameIdEqualTo(entity.getNameId());
        List<AuditConfigurationEntity> auditConfigurationEntities = this.findByExample(auditConfigurationEntityExample);
        if (auditConfigurationEntities.size() > 0)
            throw new BaseRuntimeException("此配置项已添加，无需重复添加");
        auditConfigurationDao.insertSelective(entity);
    }

    @Override
    public void remove(Long id) {
        auditConfigurationDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(AuditConfigurationEntity entity) {
        AuditConfigurationEntityExample auditConfigurationEntityExample = new AuditConfigurationEntityExample();
        AuditConfigurationEntityExample.Criteria criteria = auditConfigurationEntityExample.createCriteria();
        criteria.andNameIdEqualTo(entity.getNameId());
        List<AuditConfigurationEntity> auditConfigurationEntities = this.findByExample(auditConfigurationEntityExample);
        if (auditConfigurationEntities.size() > 0 && !entity.getNameId().equals(auditConfigurationEntities.get(0).getNameId()))
            throw new BaseRuntimeException("此配置项已添加，无需重复添加");
        auditConfigurationDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public AuditConfigurationEntity findById(Long id) {
        AuditConfigurationEntityExample auditConfigurationEntityExample = new AuditConfigurationEntityExample();
        AuditConfigurationEntityExample.Criteria criteria = auditConfigurationEntityExample.createCriteria();
        criteria.andIdEqualTo(id);
        List<AuditConfigurationEntity> auditConfigurationEntities = this.findByExample(auditConfigurationEntityExample);
        return auditConfigurationEntities.isEmpty() ? null : auditConfigurationEntities.get(0);
    }

    @Override
    public void add(AuditConfigurationEntity auditConfigurationEntity, Long[] roleIds) {
        this.add(auditConfigurationEntity);
        this.addOrEditAuditConfigurationUser(auditConfigurationEntity.getId(), roleIds, auditConfigurationEntity.getModifiedBy());
    }

    @Override
    public void remove(AuditConfigurationEntityExample auditConfigurationEntityExample) {
        auditConfigurationDao.deleteByExample(auditConfigurationEntityExample);
    }

    @Override
    public void edit(AuditConfigurationEntity auditConfigurationEntity, Long[] roleIds) {
        this.edit(auditConfigurationEntity);
        this.addOrEditAuditConfigurationUser(auditConfigurationEntity.getId(), roleIds, auditConfigurationEntity.getModifiedBy());
    }

    @Override
    public List<AuditConfigurationEntity> findByExample(AuditConfigurationEntityExample auditConfigurationEntityExample) {
        List<AuditConfigurationEntity> auditConfigurationEntities = auditConfigurationDao.selectByExample(auditConfigurationEntityExample);
        for (AuditConfigurationEntity auditConfigurationEntity : auditConfigurationEntities) {
            if (auditConfigurationEntity.getModel() == 1)
                auditConfigurationEntity.setRoleEntities(auditConfigurationRoleService.findRoleById(auditConfigurationEntity.getId()));
        }
        return auditConfigurationEntities;
    }

    @Override
    public void addOrEditAuditConfigurationUser(Long id, Long[] roleIds, String loginUserName) {
        AuditConfigurationRoleEntityExample auditConfigurationRoleEntityExample = new AuditConfigurationRoleEntityExample();
        AuditConfigurationRoleEntityExample.Criteria criteria = auditConfigurationRoleEntityExample.createCriteria();
        criteria.andAuditConfigurationIdEqualTo(id);
        List<AuditConfigurationRoleEntity> auditConfigurationRoleEntities = auditConfigurationRoleService.findByExample(auditConfigurationRoleEntityExample);
        if (auditConfigurationRoleEntities.size() > 0) {
            auditConfigurationRoleService.remove(auditConfigurationRoleEntityExample);
        }
        //去重
        Set<Long> set = new HashSet<>(Arrays.asList(roleIds));
        for (Long roleId : set) {
            AuditConfigurationRoleEntity auditConfigurationUserEntity = new AuditConfigurationRoleEntity();
            auditConfigurationUserEntity.setCreateBy(loginUserName);
            auditConfigurationUserEntity.setCreateTime(new Date());
            auditConfigurationUserEntity.setModifiedBy(loginUserName);
            auditConfigurationUserEntity.setModifiedTime(new Date());
            auditConfigurationUserEntity.setAuditConfigurationId(id);
            auditConfigurationUserEntity.setRoleId(roleId);
            auditConfigurationRoleService.add(auditConfigurationUserEntity);
        }
    }

    @Override
    public List<AuditConfigurationEntity> findByExample(Short nameId, Short status, Short model) {
        AuditConfigurationEntityExample auditConfigurationEntityExample = new AuditConfigurationEntityExample();
        AuditConfigurationEntityExample.Criteria criteria = auditConfigurationEntityExample.createCriteria();
        if (nameId != null)
            criteria.andNameIdEqualTo(nameId);
        if (status != null)
            criteria.andStatusEqualTo(status);
        if (model != null)
            criteria.andModelEqualTo(model);
        List<AuditConfigurationEntity> auditConfigurationEntities = this.findByExample(auditConfigurationEntityExample);
        for (AuditConfigurationEntity auditConfigurationEntity : auditConfigurationEntities) {
            auditConfigurationEntity.setRoleEntities(auditConfigurationRoleService.findRoleById(auditConfigurationEntity.getId()));
        }
        return auditConfigurationEntities;
    }

}
