package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.danganguanli.CustomerAuditRecordAuditorEntity;
import com.yintu.ruixing.danganguanli.CustomerAuditRecordAuditorService;
import com.yintu.ruixing.master.danganguanli.CustomerAuditRecordAuditorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: mlf
 * @Date: 2020/12/25 18:58
 * @Version: 1.0
 */
@Service
@Transactional
public class CustomerAuditRecordAuditorServiceImpl implements CustomerAuditRecordAuditorService {
    @Autowired
    private CustomerAuditRecordAuditorDao customerAuditRecordAuditorDao;

    @Override
    public void add(CustomerAuditRecordAuditorEntity entity) {
        customerAuditRecordAuditorDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        customerAuditRecordAuditorDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(CustomerAuditRecordAuditorEntity entity) {
        customerAuditRecordAuditorDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public CustomerAuditRecordAuditorEntity findById(Integer id) {
        return customerAuditRecordAuditorDao.selectByPrimaryKey(id);
    }


    @Override
    public List<CustomerAuditRecordAuditorEntity> findByCustomerAuditRecordId(Integer customerAuditRecordId) {
        return customerAuditRecordAuditorDao.selectByCustomerAuditRecordIdId(customerAuditRecordId);
    }

    @Override
    public List<CustomerAuditRecordAuditorEntity> findByExample(Integer customerAuditRecordId, Integer auditorId, Integer sort, Short activate) {
        return customerAuditRecordAuditorDao.selectByExample(customerAuditRecordId, auditorId, sort, activate);
    }

    @Override
    public void addMuch(List<CustomerAuditRecordAuditorEntity> customerAuditRecordAuditorEntities) {
        customerAuditRecordAuditorDao.insertMuch(customerAuditRecordAuditorEntities);
    }

    @Override
    public void removeByCustomerAuditRecordId(Integer customerAuditRecordId) {
        customerAuditRecordAuditorDao.deleteByCustomerAuditRecordIdId(customerAuditRecordId);
    }


}
