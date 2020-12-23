package com.yintu.ruixing.master.yuanchengzhichi;

import com.yintu.ruixing.yuanchengzhichi.AlarmPushUserEntity;

public interface AlarmPushUserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AlarmPushUserEntity record);

    int insertSelective(AlarmPushUserEntity record);

    AlarmPushUserEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AlarmPushUserEntity record);

    int updateByPrimaryKey(AlarmPushUserEntity record);
}