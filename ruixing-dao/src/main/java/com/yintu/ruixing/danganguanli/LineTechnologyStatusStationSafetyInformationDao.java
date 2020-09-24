package com.yintu.ruixing.danganguanli;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LineTechnologyStatusStationSafetyInformationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusStationSafetyInformationEntity record);

    int insertSelective(LineTechnologyStatusStationSafetyInformationEntity record);

    LineTechnologyStatusStationSafetyInformationEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusStationSafetyInformationEntity record);

    int updateByPrimaryKey(LineTechnologyStatusStationSafetyInformationEntity record);

    List<LineTechnologyStatusStationSafetyInformationEntity> selectAll();

    long countByStationId(Integer stationId);
}