package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationSafetyInformationEntity;

public interface LineTechnologyStatusStationSafetyInformationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusStationSafetyInformationEntity record);

    int insertSelective(LineTechnologyStatusStationSafetyInformationEntity record);

    LineTechnologyStatusStationSafetyInformationEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusStationSafetyInformationEntity record);

    int updateByPrimaryKey(LineTechnologyStatusStationSafetyInformationEntity record);
}