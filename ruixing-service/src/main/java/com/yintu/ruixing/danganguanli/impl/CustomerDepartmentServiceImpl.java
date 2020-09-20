package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.danganguanli.CustomerDepartmentDao;
import com.yintu.ruixing.danganguanli.CustomerDepartmentEntity;
import com.yintu.ruixing.danganguanli.CustomerDepartmentService;
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
    public List<TreeNodeUtil> findByParentIdAndTypeId(Integer parentId, Short type) {
        List<CustomerDepartmentEntity> customerDepartmentEntities = customerDepartmentDao.selectByParentIdAndTypeId(parentId, type);
        List<TreeNodeUtil> treeNodeUtils = new ArrayList<>();
        for (CustomerDepartmentEntity customerDepartmentEntity : customerDepartmentEntities) {
            TreeNodeUtil treeNodeUtil = new TreeNodeUtil();
            treeNodeUtil.setId(customerDepartmentEntity.getId().longValue());
            treeNodeUtil.setLabel(customerDepartmentEntity.getName());
            treeNodeUtil.setValue(customerDepartmentEntity.getId().toString());
            Map<String, Object> map = new HashMap<>();
            map.put("parentId", customerDepartmentEntity.getParentId());
            map.put("createBy", customerDepartmentEntity.getCreateBy());
            map.put("createTime", customerDepartmentEntity.getCreateTime());
            map.put("modifiedBy", customerDepartmentEntity.getModifiedBy());
            map.put("modifiedTime", customerDepartmentEntity.getModifiedTime());
            treeNodeUtil.setA_attr(map);
            treeNodeUtil.setChildren(this.findByParentIdAndTypeId(customerDepartmentEntity.getId(), customerDepartmentEntity.getTypeId()));
            treeNodeUtils.add(treeNodeUtil);
        }
        return treeNodeUtils;
    }
}
