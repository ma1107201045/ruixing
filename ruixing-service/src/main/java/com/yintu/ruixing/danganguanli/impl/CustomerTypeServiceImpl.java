package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.danganguanli.CustomerTypeDao;
import com.yintu.ruixing.danganguanli.CustomerTypeEntity;
import com.yintu.ruixing.danganguanli.CustomerTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/20 11:26
 */
@Service
@Transactional
public class CustomerTypeServiceImpl implements CustomerTypeService {

    @Autowired
    private CustomerTypeDao customerTypeDao;

    @Override
    public void add(CustomerTypeEntity entity) {
        customerTypeDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        customerTypeDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(CustomerTypeEntity entity) {
        customerTypeDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public CustomerTypeEntity findById(Integer id) {
        return customerTypeDao.selectByPrimaryKey(id);
    }

    @Override
    public List<CustomerTypeEntity> findAll() {
        return customerTypeDao.selectAll();
    }
}
