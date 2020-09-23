package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationUnitEntity;

public interface LineTechnologyStatusStationUnitDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusStationUnitEntity record);

    int insertSelective(LineTechnologyStatusStationUnitEntity record);

    LineTechnologyStatusStationUnitEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusStationUnitEntity record);

    int updateByPrimaryKey(LineTechnologyStatusStationUnitEntity record);
}