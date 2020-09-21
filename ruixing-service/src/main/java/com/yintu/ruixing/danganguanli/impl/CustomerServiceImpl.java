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
    @Autowired
    private CustomerAuditRecordService customerAuditRecordService;

    @Override
    public void add(CustomerEntity entity) {
        entity.setStatus((short) 1);
        customerDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        customerDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(CustomerEntity entity) {
        entity.setStatus((short) 2);
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
    public Long countExample(Integer typeId, String name) {
        return customerDao.count(typeId, name);
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
    public void edit(CustomerEntity customerEntity, Integer[] customerDepartmentIds, Integer auditorId) {
        CustomerEntity source = this.findById(customerEntity.getId());
        if (source != null) {
            source.setStatus((short) 2);//改为待审批状态
            this.edit(customerEntity);
            CustomerAuditRecordEntity customerAuditRecordEntity = new CustomerAuditRecordEntity();
            customerAuditRecordEntity.setCreateBy(customerEntity.getModifiedBy());
            customerAuditRecordEntity.setCreateTime(customerEntity.getModifiedTime());
            customerAuditRecordEntity.setModifiedBy(customerEntity.getModifiedBy());
            customerAuditRecordEntity.setModifiedTime(customerEntity.getModifiedTime());
            customerAuditRecordEntity.setTypeId(customerEntity.getTypeId());
            customerAuditRecordEntity.setDutyId(customerEntity.getDutyId());
            customerAuditRecordEntity.setName(customerEntity.getName());
            customerAuditRecordEntity.setPhone(customerEntity.getPhone());
            customerAuditRecordEntity.setSpecialPlane(customerEntity.getSpecialPlane());
            customerAuditRecordEntity.setEmail(customerEntity.getSpecialPlane());
            customerAuditRecordEntity.setProvinceId(customerEntity.getProvinceId());
            customerAuditRecordEntity.setCityId(customerEntity.getCityId());
            customerAuditRecordEntity.setDistrictId(customerEntity.getDistrictId());
            customerAuditRecordEntity.setDetailedAddress(customerEntity.getDetailedAddress());
            customerAuditRecordEntity.setCustomerId(customerEntity.getId());
            customerAuditRecordEntity.setAuditorId(auditorId);
            customerAuditRecordEntity.setAuditStatus((short) 1);
            customerAuditRecordService.add(customerAuditRecordEntity);
        }
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
            customerCustomerDepartmentEntity.setCreateBy(customerEntity.getModifiedBy());
            customerCustomerDepartmentEntity.setCreateTime(customerEntity.getModifiedTime());
            customerCustomerDepartmentEntity.setModifiedBy(customerEntity.getModifiedBy());
            customerCustomerDepartmentEntity.setModifiedTime(customerEntity.getModifiedTime());
            customerCustomerDepartmentEntity.setCustomerId(customerEntity.getId());
            customerCustomerDepartmentEntity.setDepartmentId(customerDepartmentId);
            customerCustomerDepartmentService.add(customerCustomerDepartmentEntity);
        }
    }


}
