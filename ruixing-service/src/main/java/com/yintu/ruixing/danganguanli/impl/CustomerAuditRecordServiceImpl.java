package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.master.danganguanli.CustomerAuditRecordDao;
import com.yintu.ruixing.danganguanli.CustomerAuditRecordEntity;
import com.yintu.ruixing.danganguanli.CustomerAuditRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/21 15:40
 */
@Service
@Transactional
public class CustomerAuditRecordServiceImpl implements CustomerAuditRecordService {
    @Autowired
    private CustomerAuditRecordDao customerAuditRecordDao;

    @Override
    public void add(CustomerAuditRecordEntity entity) {
        customerAuditRecordDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        customerAuditRecordDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(CustomerAuditRecordEntity entity) {
        customerAuditRecordDao.updateByPrimaryKeyWithBLOBs(entity);
    }

    @Override
    public CustomerAuditRecordEntity findById(Integer id) {
        return null;
    }

    @Override
    public List<CustomerAuditRecordEntity> findByExample(Integer[] ids, Integer customerId) {
        return customerAuditRecordDao.selectByExample(ids, customerId);
    }

    @Override
    public List<CustomerAuditRecordEntity> findByCustomerIdAndAuditStatus(Integer customerId, Short auditStatus) {
        return customerAuditRecordDao.selectByCustomerIdAndAuditStatus(customerId, auditStatus);
    }
}
