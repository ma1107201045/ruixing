package com.yintu.ruixing.common.impl;

import com.yintu.ruixing.common.AuditTotalEntity;
import com.yintu.ruixing.common.AuditTotalService;
import com.yintu.ruixing.master.common.AuditTotalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: mlf
 * @Date: 2020/12/19 18:07
 * @Version: 1.0
 */
@Service
@Transactional
public class AuditTotalServiceImpl implements AuditTotalService {
    @Autowired
    private AuditTotalDao auditTotalDao;

    @Override
    public void add(AuditTotalEntity entity) {
        auditTotalDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        auditTotalDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(AuditTotalEntity entity) {
        auditTotalDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public AuditTotalEntity findById(Integer id) {
        return auditTotalDao.selectByPrimaryKey(id);
    }
}
