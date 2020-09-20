package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.danganguanli.*;
import com.yintu.ruixing.xitongguanli.DepartmentEntity;
import com.yintu.ruixing.xitongguanli.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author:mlf
 * @date:2020/8/10 14:28
 */
@Service
@Transactional
public class CustomerDutyServiceImpl implements CustomerDutyService {

    @Autowired
    private CustomerDutyDao customerDutyDao;
    @Autowired
    private DepartmentService departmentService;



    @Override
    public void add(CustomerDutyEntity entity) {
        entity.setCreateTime(new Date());
        entity.setModifiedTime(new Date());
        customerDutyDao.insertSelective(entity);
    }

    @Override
    public void remove(Long id) {
        customerDutyDao.deleteByPrimaryKey(id);
    }


    @Override
    public void edit(CustomerDutyEntity entity) {
        entity.setModifiedTime(new Date());
        customerDutyDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public CustomerDutyEntity findById(Long id) {
        CustomerDutyEntity customerDutyEntity = customerDutyDao.selectByPrimaryKey(id);
        if (customerDutyEntity != null) {
            customerDutyEntity.setDepartmentEntities(this.findDepartmentsById(customerDutyEntity.getId()));
//            customerDutyEntity.setCustomerUnitsEntity(customerUnitsService.findById(customerDutyEntity.getCustomerUnitsId()));
        }
        return customerDutyEntity;
    }


    @Override
    public void add(CustomerDutyEntity entity, Long[] departmentIds, String loginUserName) {
        this.add(entity);
        for (Long departmentId : departmentIds) {
            DepartmentCustomerDutyEntity departmentCustomerDutyEntity = new DepartmentCustomerDutyEntity();
            departmentCustomerDutyEntity.setCreateBy(loginUserName);
            departmentCustomerDutyEntity.setCreateTime(new Date());
            departmentCustomerDutyEntity.setModifiedBy(loginUserName);
            departmentCustomerDutyEntity.setModifiedTime(new Date());
            departmentCustomerDutyEntity.setDepartmentId(departmentId);
            departmentCustomerDutyEntity.setDutyId(entity.getId());
//            departmentCustomerDutyService.add(departmentCustomerDutyEntity);
        }
    }

    @Override
    public void removeByExample(CustomerDutyEntityExample customerDutyEntityExample) {
        customerDutyDao.deleteByExample(customerDutyEntityExample);
    }

    @Override
    public void edit(CustomerDutyEntity entity, Long[] departmentIds, String loginUserName) {
        this.edit(entity);
        DepartmentCustomerDutyEntityExample departmentCustomerDutyEntityExample = new DepartmentCustomerDutyEntityExample();
        DepartmentCustomerDutyEntityExample.Criteria criteria = departmentCustomerDutyEntityExample.createCriteria();
        criteria.andDutyIdEqualTo(entity.getId());
//        departmentCustomerDutyService.removeByExample(departmentCustomerDutyEntityExample);
        for (Long departmentId : departmentIds) {
            DepartmentCustomerDutyEntity departmentCustomerDutyEntity = new DepartmentCustomerDutyEntity();
            departmentCustomerDutyEntity.setCreateBy(loginUserName);
            departmentCustomerDutyEntity.setCreateTime(new Date());
            departmentCustomerDutyEntity.setModifiedBy(loginUserName);
            departmentCustomerDutyEntity.setModifiedTime(new Date());
            departmentCustomerDutyEntity.setDepartmentId(departmentId);
            departmentCustomerDutyEntity.setDutyId(entity.getId());
//            departmentCustomerDutyService.add(departmentCustomerDutyEntity);
        }
    }

    @Override
    public CustomerDutyEntity findSimpleById(Long id) {
        return customerDutyDao.selectByPrimaryKey(id);
    }

    @Override
    public List<CustomerDutyEntity> findByExample(CustomerDutyEntityExample customerDutyEntityExample) {
        return customerDutyDao.selectByExample(customerDutyEntityExample);
    }

    @Override
    public List<DepartmentEntity> findDepartmentsById(Long id) {
        DepartmentCustomerDutyEntityExample departmentCustomerDutyEntityExample = new DepartmentCustomerDutyEntityExample();
        DepartmentCustomerDutyEntityExample.Criteria criteria = departmentCustomerDutyEntityExample.createCriteria();
        criteria.andDutyIdEqualTo(id);
//        List<DepartmentCustomerDutyEntity> departmentCustomerDutyEntities = departmentCustomerDutyService.findByExample(departmentCustomerDutyEntityExample);
//        List<Long> departmentIds = departmentCustomerDutyEntities.stream().map(DepartmentCustomerDutyEntity::getDepartmentId).collect(Collectors.toList());
//        return departmentService.findByIds(departmentIds);

        return null;
    }

    @Override
    public List<CustomerDutyEntity> findByExample(CustomerDutyEntity entity) {
        CustomerDutyEntityExample customerDutyEntityExample = new CustomerDutyEntityExample();
        CustomerDutyEntityExample.Criteria criteria = customerDutyEntityExample.createCriteria();
        if (entity.getName() != null)
            criteria.andNameLike("%" + entity.getName() + "%");
        List<CustomerDutyEntity> customerDutyEntities = this.findByExample(customerDutyEntityExample);
        for (CustomerDutyEntity customerDutyEntity : customerDutyEntities) {
            if (customerDutyEntity != null) {
                customerDutyEntity.setDepartmentEntities(this.findDepartmentsById(customerDutyEntity.getId()));
//                customerDutyEntity.setCustomerUnitsEntity(customerUnitsService.findById(customerDutyEntity.getCustomerUnitsId()));
            }
        }
        return customerDutyEntities;
    }

    @Override
    public void removeByIds(Long[] ids) {
        CustomerDutyEntityExample customerDutyEntityExample = new CustomerDutyEntityExample();
        CustomerDutyEntityExample.Criteria criteria = customerDutyEntityExample.createCriteria();
        criteria.andIdIn(Arrays.asList(ids));
        this.removeByExample(customerDutyEntityExample);
    }
}
