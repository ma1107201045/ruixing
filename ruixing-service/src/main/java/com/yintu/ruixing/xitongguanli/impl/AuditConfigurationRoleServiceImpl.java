package com.yintu.ruixing.xitongguanli.impl;

import com.yintu.ruixing.master.xitongguanli.AuditConfigurationRoleDao;
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
public class AuditConfigurationRoleServiceImpl implements AuditConfigurationRoleService {
    @Autowired
    private AuditConfigurationRoleDao auditConfigurationRoleDao;
    @Autowired
    private RoleService roleService;

    @Override
    public void add(AuditConfigurationRoleEntity entity) {
        auditConfigurationRoleDao.insertSelective(entity);
    }

    @Override
    public void remove(Long id) {
        auditConfigurationRoleDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(AuditConfigurationRoleEntity entity) {
        auditConfigurationRoleDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public AuditConfigurationRoleEntity findById(Long id) {
        return auditConfigurationRoleDao.selectByPrimaryKey(id);
    }

    @Override
    public void remove(AuditConfigurationRoleEntityExample auditConfigurationRoleEntityExample) {
        auditConfigurationRoleDao.deleteByExample(auditConfigurationRoleEntityExample);
    }

    @Override
    public List<AuditConfigurationRoleEntity> findByExample(AuditConfigurationRoleEntityExample auditConfigurationRoleEntityExample) {
        return auditConfigurationRoleDao.selectByExample(auditConfigurationRoleEntityExample);
    }

    @Override
    public List<RoleEntity> findRoleById(Long auditConfigurationId) {
        List<RoleEntity> roleEntities = new ArrayList<>();
        AuditConfigurationRoleEntityExample auditConfigurationRoleEntityExample = new AuditConfigurationRoleEntityExample();
        AuditConfigurationRoleEntityExample.Criteria criteria1 = auditConfigurationRoleEntityExample.createCriteria();
        criteria1.andAuditConfigurationIdEqualTo(auditConfigurationId);
        List<AuditConfigurationRoleEntity> auditConfigurationRoleEntities = this.findByExample(auditConfigurationRoleEntityExample);
        for (AuditConfigurationRoleEntity auditConfigurationRoleEntity : auditConfigurationRoleEntities) {
            RoleEntity roleEntity = roleService.findById(auditConfigurationRoleEntity.getRoleId());
            roleEntities.add(roleEntity);
        }
        roleEntities = roleEntities.stream()
                .sorted(Comparator.comparing(RoleEntity::getId).reversed())
                .collect(Collectors.toList());
        return roleEntities;
    }
}
