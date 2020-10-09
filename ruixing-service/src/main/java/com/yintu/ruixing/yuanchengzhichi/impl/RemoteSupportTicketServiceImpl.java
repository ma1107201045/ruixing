package com.yintu.ruixing.yuanchengzhichi.impl;

import com.yintu.ruixing.yuanchengzhichi.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/10/9 14:07
 */
@Service
@Transactional
public class RemoteSupportTicketServiceImpl implements RemoteSupportTicketService {
    @Autowired
    private RemoteSupportTicketDao remoteSupportTicketDao;

    @Override
    public void add(RemoteSupportTicketEntity entity) {
        remoteSupportTicketDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        remoteSupportTicketDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(RemoteSupportTicketEntity entity) {
        remoteSupportTicketDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public RemoteSupportTicketEntity findById(Integer id) {
        return remoteSupportTicketDao.selectByPrimaryKey(id);
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public RemoteSupportTicketEntity findLastByAlarmId(Long alarmId) {
        return remoteSupportTicketDao.selectLastByAlarmId(alarmId);
    }

    @Override
    public List<RemoteSupportTicketEntity> findByCondition(Integer[] ids, Short status, Long alarmId) {
        return remoteSupportTicketDao.selectByCondition(ids, status, alarmId);
    }
}
