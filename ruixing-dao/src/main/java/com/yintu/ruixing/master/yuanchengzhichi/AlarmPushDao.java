package com.yintu.ruixing.master.yuanchengzhichi;

import com.yintu.ruixing.yuanchengzhichi.AlarmPushEntity;

public interface AlarmPushDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AlarmPushEntity record);

    int insertSelective(AlarmPushEntity record);

    AlarmPushEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AlarmPushEntity record);

    int updateByPrimaryKey(AlarmPushEntity record);
}