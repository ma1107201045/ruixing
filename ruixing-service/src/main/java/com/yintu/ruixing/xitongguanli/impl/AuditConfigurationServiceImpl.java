package com.yintu.ruixing.xitongguanli.impl;

import cn.hutool.core.bean.BeanUtil;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.TreeNodeUtil;
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
    private AuditConfigurationUserService auditConfigurationUserService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;


    @Override
    public List<AuditConfigurationEntity> findAudit(short i, short i1, short i2) {
        return auditConfigurationDao.findAudit(i,i1,i2);
    }

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
    public void add(AuditConfigurationEntity auditConfigurationEntity, Long[] roles, Long[] userIds, Integer[] sorts) {
        this.add(auditConfigurationEntity);
        this.addOrEditAuditConfigurationUser(auditConfigurationEntity.getId(), roles, userIds, sorts, auditConfigurationEntity.getModifiedBy());
    }

    @Override
    public void remove(AuditConfigurationEntityExample auditConfigurationEntityExample) {
        auditConfigurationDao.deleteByExample(auditConfigurationEntityExample);
    }

    @Override
    public void edit(AuditConfigurationEntity auditConfigurationEntity, Long[] roles, Long[] userIds, Integer[] sorts) {
        this.edit(auditConfigurationEntity);
        this.addOrEditAuditConfigurationUser(auditConfigurationEntity.getId(), roles, userIds, sorts, auditConfigurationEntity.getModifiedBy());
    }

    @Override
    public List<AuditConfigurationEntity> findByExample(AuditConfigurationEntityExample auditConfigurationEntityExample) {
        return auditConfigurationDao.selectByExample(auditConfigurationEntityExample);
    }

    @Override
    public void addOrEditAuditConfigurationUser(Long id, Long[] roles, Long[] userIds, Integer[] sorts, String loginUserName) {
        //判断用户id不能重复
        long length = Arrays.stream(userIds).distinct().count();
        if (length != userIds.length)
            throw new BaseRuntimeException("用户id不能重复");

        AuditConfigurationUserEntityExample auditConfigurationUserEntityExample = new AuditConfigurationUserEntityExample();
        AuditConfigurationUserEntityExample.Criteria criteria = auditConfigurationUserEntityExample.createCriteria();
        criteria.andAuditConfigurationIdEqualTo(id);
        List<AuditConfigurationUserEntity> auditConfigurationUserEntities = auditConfigurationUserService.findByExample(auditConfigurationUserEntityExample);
        if (auditConfigurationUserEntities.size() > 0) {
            auditConfigurationUserService.remove(auditConfigurationUserEntityExample);
        }
        for (int i = 0; i < userIds.length; i++) {
            AuditConfigurationUserEntity auditConfigurationUserEntity = new AuditConfigurationUserEntity();
            auditConfigurationUserEntity.setCreateBy(loginUserName);
            auditConfigurationUserEntity.setCreateTime(new Date());
            auditConfigurationUserEntity.setModifiedBy(loginUserName);
            auditConfigurationUserEntity.setModifiedTime(new Date());
            auditConfigurationUserEntity.setAuditConfigurationId(id);
            auditConfigurationUserEntity.setRoleId(roles[i]);
            auditConfigurationUserEntity.setUserId(userIds[i]);
            auditConfigurationUserEntity.setSort(sorts[i]);
            auditConfigurationUserService.add(auditConfigurationUserEntity);
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
            auditConfigurationEntity.setAuditConfigurationUserEntities(auditConfigurationUserService.findByExample(new AuditConfigurationUserEntityExample()));
        }
        return auditConfigurationEntities;
    }

    @Override
    public List<TreeNodeUtil> findTree() {
        List<RoleEntity> roleEntities = roleService.findAll();
        List<TreeNodeUtil> firstTreeNodeUtils = new ArrayList<>();
        for (RoleEntity roleEntity : roleEntities) {
            List<UserEntity> userEntities = roleService.findUsersByIds(roleEntity.getId());
            List<TreeNodeUtil> secondTreeNodeUtils = new ArrayList<>();
            TreeNodeUtil firstTreeNodeUtil = new TreeNodeUtil();
            firstTreeNodeUtil.setId(roleEntity.getId());
            firstTreeNodeUtil.setLabel(roleEntity.getName());
            firstTreeNodeUtil.setChildren(secondTreeNodeUtils);
            firstTreeNodeUtils.add(firstTreeNodeUtil);
            for (UserEntity userEntity : userEntities) {
                TreeNodeUtil secondTreeNodeUtil = new TreeNodeUtil();
                secondTreeNodeUtil.setId(userEntity.getId());
                secondTreeNodeUtil.setLabel(userEntity.getTrueName());
                secondTreeNodeUtil.setA_attr(BeanUtil.beanToMap(roleEntity));
                secondTreeNodeUtils.add(secondTreeNodeUtil);
            }
        }
        return firstTreeNodeUtils;
    }

    @Override
    public List<List<TreeNodeUtil>> findTreeById(Long id) {
        List<Long> sorts = auditConfigurationUserService.findDistinctFieldByExample("sort", id, null);
        List<List<TreeNodeUtil>> lists = new ArrayList<>();
        for (Long sort : sorts) {

            List<TreeNodeUtil> firstTreeNodeUtils = new ArrayList<>();
            List<Long> roleIds = auditConfigurationUserService.findDistinctFieldByExample("roleId", id, sort.intValue());
            for (Long roleId : roleIds) {
                RoleEntity roleEntity = roleService.findById(roleId);
                AuditConfigurationUserEntityExample auditConfigurationUserEntityExample = new AuditConfigurationUserEntityExample();
                AuditConfigurationUserEntityExample.Criteria criteria = auditConfigurationUserEntityExample.createCriteria();
                criteria.andAuditConfigurationIdEqualTo(id);
                criteria.andSortEqualTo(sort.intValue());
                criteria.andRoleIdEqualTo(roleEntity.getId());
                List<AuditConfigurationUserEntity> auditConfigurationUserEntities = auditConfigurationUserService.findByExample(auditConfigurationUserEntityExample);

                List<TreeNodeUtil> secondTreeNodeUtils = new ArrayList<>();
                TreeNodeUtil firstTreeNodeUtil = new TreeNodeUtil();
                firstTreeNodeUtil.setId(roleEntity.getId());
                firstTreeNodeUtil.setLabel(roleEntity.getName());
                firstTreeNodeUtil.setChildren(secondTreeNodeUtils);
                firstTreeNodeUtils.add(firstTreeNodeUtil);
                for (AuditConfigurationUserEntity auditConfigurationUserEntity : auditConfigurationUserEntities) {
                    UserEntity userEntity = userService.findById(auditConfigurationUserEntity.getUserId());
                    TreeNodeUtil secondTreeNodeUtil = new TreeNodeUtil();
                    secondTreeNodeUtil.setId(userEntity.getId());
                    secondTreeNodeUtil.setLabel(userEntity.getTrueName());
                    secondTreeNodeUtil.setValue(auditConfigurationUserEntity.getSort().toString());
                    secondTreeNodeUtil.setA_attr(BeanUtil.beanToMap(roleEntity));
                    secondTreeNodeUtils.add(secondTreeNodeUtil);
                }
            }
            lists.add(firstTreeNodeUtils);
        }

        return lists;
    }

}
