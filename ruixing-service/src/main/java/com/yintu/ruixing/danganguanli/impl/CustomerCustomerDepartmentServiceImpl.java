package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.danganguanli.CustomerCustomerDepartmentDao;
import com.yintu.ruixing.danganguanli.CustomerCustomerDepartmentEntity;
import com.yintu.ruixing.danganguanli.CustomerCustomerDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/20 17:27
 */
@Service
@Transactional
public class CustomerCustomerDepartmentServiceImpl implements CustomerCustomerDepartmentService {
    @Autowired
    private CustomerCustomerDepartmentDao customerCustomerDepartmentDao;

    @Override
    public void add(CustomerCustomerDepartmentEntity entity) {
        customerCustomerDepartmentDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        customerCustomerDepartmentDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(CustomerCustomerDepartmentEntity entity) {
        customerCustomerDepartmentDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public CustomerCustomerDepartmentEntity findById(Integer id) {
        return customerCustomerDepartmentDao.selectByPrimaryKey(id);
    }

    @Override
    public List<CustomerCustomerDepartmentEntity> findByCustomerId(Integer customerId) {
        return customerCustomerDepartmentDao.selectByCustomerId(customerId);
    }
}
