package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.danganguanli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/20 17:22
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private CustomerCustomerDepartmentService customerCustomerDepartmentService;

    @Override
    public void add(CustomerEntity entity) {
        customerDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        customerDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(CustomerEntity entity) {
        customerDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public CustomerEntity findById(Integer id) {
        List<CustomerEntity> customerEntities = customerDao.selectByExample(new Integer[]{id}, null, null, null);
        return customerEntities.isEmpty() ? null : customerEntities.get(0);
    }

    @Override
    public List<CustomerEntity> findByExample(Integer[] ids, Integer typeId, Integer departmentId, String name) {
        return customerDao.selectByExample(ids, typeId, departmentId, name);
    }

    @Override
    public void add(CustomerEntity customerEntity, Integer[] customerDepartmentIds) {
        this.add(customerEntity);
        this.addOrEditCustomerCustomerDepartment(customerEntity, customerDepartmentIds);
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public void edit(CustomerEntity customerEntity, Integer[] customerDepartmentIds) {
        this.edit(customerEntity);
        this.addOrEditCustomerCustomerDepartment(customerEntity, customerDepartmentIds);
    }

    @Override
    public void addOrEditCustomerCustomerDepartment(CustomerEntity customerEntity, Integer[] customerDepartmentIds) {
        List<CustomerCustomerDepartmentEntity> customerCustomerDepartmentEntities = customerCustomerDepartmentService.findByCustomerId(customerEntity.getId());
        for (CustomerCustomerDepartmentEntity customerCustomerDepartmentEntity : customerCustomerDepartmentEntities) {
            customerCustomerDepartmentService.remove(customerCustomerDepartmentEntity.getId());
        }
        //去重
        Set<Integer> set = new HashSet<>(Arrays.asList(customerDepartmentIds));
        for (Integer customerDepartmentId : set) {
            CustomerCustomerDepartmentEntity customerCustomerDepartmentEntity = new CustomerCustomerDepartmentEntity();
            customerCustomerDepartmentEntity.setCreateBy(customerEntity.getCreateBy());
            customerCustomerDepartmentEntity.setCreateTime(customerEntity.getCreateTime());
            customerCustomerDepartmentEntity.setModifiedBy(customerEntity.getModifiedBy());
            customerCustomerDepartmentEntity.setModifiedTime(customerEntity.getModifiedTime());
            customerCustomerDepartmentEntity.setCustomerId(customerEntity.getId());
            customerCustomerDepartmentEntity.setDepartmentId(customerDepartmentId);
            customerCustomerDepartmentService.add(customerCustomerDepartmentEntity);
        }
    }


}
