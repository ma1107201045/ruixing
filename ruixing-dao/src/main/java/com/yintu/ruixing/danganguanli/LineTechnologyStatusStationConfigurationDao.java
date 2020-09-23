package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationConfigurationEntity;

public interface LineTechnologyStatusStationConfigurationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusStationConfigurationEntity record);

    int insertSelective(LineTechnologyStatusStationConfigurationEntity record);

    LineTechnologyStatusStationConfigurationEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusStationConfigurationEntity record);

    int updateByPrimaryKey(LineTechnologyStatusStationConfigurationEntity record);
}