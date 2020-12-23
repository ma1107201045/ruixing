package com.yintu.ruixing.yuanchengzhichi.impl;

import com.yintu.ruixing.master.yuanchengzhichi.AlarmPushDao;
import com.yintu.ruixing.yuanchengzhichi.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: mlf
 * @Date: 2020/12/23 10:51
 * @Version: 1.0
 */
@Service
@Transactional
public class AlarmPushServiceImpl implements AlarmPushService {
    @Autowired
    private AlarmPushDao alarmPushDao;
    @Autowired
    private AlarmPushUserService alarmPushUserService;

    @Override
    public void add(AlarmPushEntity entity) {
        alarmPushDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        alarmPushDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(AlarmPushEntity entity) {
        alarmPushDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public AlarmPushEntity findById(Integer id) {
        return alarmPushDao.selectByPrimaryKey(id);
    }

    @Override
    public void add(AlarmPushEntity entity, Integer[] userIds) {
        this.add(entity);
        for (Integer userId : userIds) {
            AlarmPushUserEntity alarmPushUserEntity = new AlarmPushUserEntity();
            alarmPushUserEntity.setCreateBy(entity.getCreateBy());
            alarmPushUserEntity.setCreateTime(entity.getCreateTime());
            alarmPushUserEntity.setModifiedBy(entity.getModifiedBy());
            alarmPushUserEntity.setModifiedTime(entity.getModifiedTime());
            alarmPushUserEntity.setAlarmPushId(entity.getId());
            alarmPushUserEntity.setUserId(userId);
            alarmPushUserService.add(alarmPushUserEntity);
        }
    }
}
