package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationDeviceEntity;

public interface LineTechnologyStatusStationDeviceDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusStationDeviceEntity record);

    int insertSelective(LineTechnologyStatusStationDeviceEntity record);

    LineTechnologyStatusStationDeviceEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusStationDeviceEntity record);

    int updateByPrimaryKey(LineTechnologyStatusStationDeviceEntity record);
}