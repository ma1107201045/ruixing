package com.yintu.ruixing.danganguanli.impl;

import cn.hutool.core.bean.BeanUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.danganguanli.*;
import com.yintu.ruixing.master.danganguanli.CustomerDepartmentDao;
import com.yintu.ruixing.xitongguanli.PermissionEntity;
import com.yintu.ruixing.xitongguanli.PermissionEntityExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/20 13:54
 */
@Service
@Transactional
public class CustomerDepartmentServiceImpl implements CustomerDepartmentService {
    @Autowired
    private CustomerDepartmentDao customerDepartmentDao;
    @Autowired
    private CustomerTypeService customerTypeService;

    @Override
    public void add(CustomerDepartmentEntity entity) {
        customerDepartmentDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        customerDepartmentDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(CustomerDepartmentEntity entity) {
        customerDepartmentDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public CustomerDepartmentEntity findById(Integer id) {
        return customerDepartmentDao.selectByPrimaryKey(id);
    }

    @Override
    public void removeByIdAndIsFirst(Integer id, Boolean isFirst) {
        if (isFirst) {  //第一次掉用此方法，按照id查询权限信息，删除
            this.remove(id);
        }
        List<CustomerDepartmentEntity> customerDepartmentEntities = customerDepartmentDao.selectByParentIdAndTypeId(id, null);
        if (customerDepartmentEntities.size() > 0) {
            for (CustomerDepartmentEntity customerDepartmentEntity : customerDepartmentEntities) {
                this.remove(customerDepartmentEntity.getId());
                this.removeByIdAndIsFirst(customerDepartmentEntity.getId(), false);
            }
        }
    }

    @Override
    public List<TreeNodeUtil> findByParentIdAndTypeId(Integer parentId, Short type) {
        List<CustomerDepartmentEntity> customerDepartmentEntities = customerDepartmentDao.selectByParentIdAndTypeId(parentId, type);
        List<TreeNodeUtil> treeNodeUtils = new ArrayList<>();
        for (CustomerDepartmentEntity customerDepartmentEntity : customerDepartmentEntities) {
            TreeNodeUtil treeNodeUtil = new TreeNodeUtil();
            treeNodeUtil.setId(customerDepartmentEntity.getId().longValue());
            treeNodeUtil.setLabel(customerDepartmentEntity.getName());
            treeNodeUtil.setValue(customerDepartmentEntity.getId().toString());
            treeNodeUtil.setA_attr(BeanUtil.beanToMap(customerDepartmentEntity));
            treeNodeUtil.setChildren(this.findByParentIdAndTypeId(customerDepartmentEntity.getId(), customerDepartmentEntity.getTypeId()));
            treeNodeUtils.add(treeNodeUtil);
        }
        return treeNodeUtils;
    }

    @Override
    public List<TreeNodeUtil> findByParentIdAndCustomerIdAndTypeId(Integer parentId, Integer customerId, Short type) {
        List<CustomerDepartmentEntity> customerDepartmentEntities = customerDepartmentDao.selectByParentIdAndCustomerIdAndTypeId(parentId, customerId, type);
        List<TreeNodeUtil> treeNodeUtils = new ArrayList<>();
        for (CustomerDepartmentEntity customerDepartmentEntity : customerDepartmentEntities) {
            TreeNodeUtil treeNodeUtil = new TreeNodeUtil();
            treeNodeUtil.setId(customerDepartmentEntity.getId().longValue());
            treeNodeUtil.setLabel(customerDepartmentEntity.getName());
            treeNodeUtil.setValue(customerDepartmentEntity.getId().toString());
            treeNodeUtil.setA_attr(BeanUtil.beanToMap(customerDepartmentEntity));
            treeNodeUtils.add(treeNodeUtil);
        }
        return treeNodeUtils;
    }

    @Override
    public List<TreeNodeUtil> findByParentIdAndCustomerIdAndTypeId(Integer parentId, Integer customerId, Short type, List<TreeNodeUtil> treeNodeUtils) {
        List<CustomerDepartmentEntity> customerDepartmentEntities = customerDepartmentDao.selectByParentIdAndCustomerIdAndTypeId(parentId, customerId, type);
        for (CustomerDepartmentEntity customerDepartmentEntity : customerDepartmentEntities) {
            List<CustomerDepartmentEntity> customerDepartments = customerDepartmentDao.selectByParentIdAndCustomerIdAndTypeId(customerDepartmentEntity.getId(), customerId, type);
            if (customerDepartments.size() > 0) {
                findByParentIdAndCustomerIdAndTypeId(customerDepartmentEntity.getId(), customerId, type, treeNodeUtils);
            } else {
                TreeNodeUtil treeNodeUtil = new TreeNodeUtil();
                treeNodeUtil.setId(customerDepartmentEntity.getId().longValue());
                treeNodeUtil.setLabel(customerDepartmentEntity.getName());
                treeNodeUtil.setValue(customerDepartmentEntity.getId().toString());
                treeNodeUtil.setA_attr(BeanUtil.beanToMap(customerDepartmentEntity));
                treeNodeUtils.add(treeNodeUtil);
            }
        }
        return treeNodeUtils;
    }


    @Override
    public List<TreeNodeUtil> findCustomerTypeAndCustomerDepartmentTree() {
        List<CustomerTypeEntity> customerTypeEntities = customerTypeService.findAll();
        List<TreeNodeUtil> treeNodeUtils = new ArrayList<>();
        for (CustomerTypeEntity customerTypeEntity : customerTypeEntities) {
            TreeNodeUtil treeNodeUtil = new TreeNodeUtil();
            treeNodeUtil.setId(customerTypeEntity.getId().longValue());
            treeNodeUtil.setLabel(customerTypeEntity.getName());
            treeNodeUtil.setValue(String.valueOf(customerTypeEntity.getId() - 4));
            Map<String, Object> map = new HashMap<>();
            map.put("createBy", customerTypeEntity.getCreateBy());
            map.put("createTime", customerTypeEntity.getCreateTime());
            map.put("modifiedBy", customerTypeEntity.getModifiedBy());
            map.put("modifiedTime", customerTypeEntity.getModifiedTime());
            map.put("isFirst", true);
            treeNodeUtil.setA_attr(map);
            treeNodeUtil.setChildren(this.findByParentIdAndTypeId(-1, customerTypeEntity.getId().shortValue()));
            treeNodeUtils.add(treeNodeUtil);
        }
        return treeNodeUtils;
    }


}
