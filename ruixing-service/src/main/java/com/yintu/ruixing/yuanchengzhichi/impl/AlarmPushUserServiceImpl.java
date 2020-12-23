package com.yintu.ruixing.yuanchengzhichi.impl;

import com.yintu.ruixing.master.yuanchengzhichi.AlarmPushUserDao;
import com.yintu.ruixing.yuanchengzhichi.AlarmPushUserEntity;
import com.yintu.ruixing.yuanchengzhichi.AlarmPushUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: mlf
 * @Date: 2020/12/23 11:43
 * @Version: 1.0
 */
@Service
@Transactional
public class AlarmPushUserServiceImpl implements AlarmPushUserService {
    @Autowired
    private AlarmPushUserDao alarmPushUserDao;

    @Override
    public void add(AlarmPushUserEntity entity) {
        alarmPushUserDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        alarmPushUserDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(AlarmPushUserEntity entity) {
        alarmPushUserDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public AlarmPushUserEntity findById(Integer id) {
        return alarmPushUserDao.selectByPrimaryKey(id);
    }
}
