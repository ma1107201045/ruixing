package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.danganguanli.CustomerDutyDao;
import com.yintu.ruixing.danganguanli.CustomerDutyEntity;
import com.yintu.ruixing.danganguanli.CustomerDutyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/8/10 14:28
 */
@Service
@Transactional
public class CustomerDutyServiceImpl implements CustomerDutyService {

    @Autowired
    private CustomerDutyDao customerDutyDao;


    @Override
    public void add(CustomerDutyEntity entity) {
        customerDutyDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        customerDutyDao.deleteByPrimaryKey(id);
    }


    @Override
    public void edit(CustomerDutyEntity entity) {
        customerDutyDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public CustomerDutyEntity findById(Integer id) {
        return customerDutyDao.selectByPrimaryKey(id);
    }


    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public List<CustomerDutyEntity> findByExample(String name) {
        return customerDutyDao.selectByExample(name);
    }
}
