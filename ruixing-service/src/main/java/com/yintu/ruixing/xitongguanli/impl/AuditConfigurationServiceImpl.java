package com.yintu.ruixing.xitongguanli.impl;

import com.github.pagehelper.PageHelper;
import com.yintu.ruixing.common.enumobject.EnumFlag;
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
    private AuditConfigurationUserService auditConfigurationUserService;
    @Autowired
    private DepartmentService departmentService;

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
        List<AuditConfigurationEntity> auditConfigurationEntities = this.findByExample(auditConfigurationEntityExample);
        return auditConfigurationEntities.isEmpty() ? null : auditConfigurationEntities.get(0);
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
        List<AuditConfigurationEntity> auditConfigurationEntities = auditConfigurationDao.selectByExample(auditConfigurationEntityExample);
        for (AuditConfigurationEntity auditConfigurationEntity : auditConfigurationEntities) {
            auditConfigurationEntity.setDepartmentEntity(departmentService.findById(auditConfigurationEntity.getDepartmentId()));
            auditConfigurationEntity.setUserEntities(auditConfigurationUserService.findUserById(auditConfigurationEntity.getId()));
        }
        return auditConfigurationEntities;
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

    @Override
    public List<AuditConfigurationEntity> findByExample(Short status) {
        AuditConfigurationEntityExample auditConfigurationEntityExample = new AuditConfigurationEntityExample();
        AuditConfigurationEntityExample.Criteria criteria = auditConfigurationEntityExample.createCriteria();
        if (status != null)
            criteria.andStatusEqualTo(status);
        return this.findByExample(auditConfigurationEntityExample);
    }


    @Override
    public List<AuditConfigurationEntity> findByExample(Integer pageNumber, Integer pageSize, String orderBy, String name, String departmentName) {
        AuditConfigurationEntityExample auditConfigurationEntityExample = new AuditConfigurationEntityExample();
        AuditConfigurationEntityExample.Criteria criteria = auditConfigurationEntityExample.createCriteria();
        if (name != null && !"".equals(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (departmentName != null && !"".equals(departmentName)) {
            DepartmentEntityExample departmentEntityExample = new DepartmentEntityExample();
            DepartmentEntityExample.Criteria criteria1 = departmentEntityExample.createCriteria();
            criteria1.andNameLike("%" + departmentName + "%");
            List<DepartmentEntity> departmentEntities = departmentService.findByExample(departmentEntityExample);
            if (departmentEntities.isEmpty())
                return new ArrayList<>();
            criteria.andDepartmentIdIn(departmentEntities.stream().map(DepartmentEntity::getId).collect(Collectors.toList()));
        }
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        return this.findByExample(auditConfigurationEntityExample);
    }
}
