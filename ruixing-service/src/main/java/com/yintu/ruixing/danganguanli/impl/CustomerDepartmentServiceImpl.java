package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.danganguanli.CustomerDepartmentEntity;
import com.yintu.ruixing.danganguanli.CustomerDepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/20 13:54
 */
@Service
@Transactional
public class CustomerDepartmentServiceImpl implements CustomerDepartmentService {
    @Override
    public void add(CustomerDepartmentEntity entity) {

    }

    @Override
    public void remove(Integer id) {

    }

    @Override
    public void edit(CustomerDepartmentEntity entity) {

    }

    @Override
    public CustomerDepartmentEntity findById(Integer id) {
        return null;
    }
}
