package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationSafetyInformationEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface LineTechnologyStatusStationSafetyInformationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusStationSafetyInformationEntity record);

    int insertSelective(LineTechnologyStatusStationSafetyInformationEntity record);

    LineTechnologyStatusStationSafetyInformationEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusStationSafetyInformationEntity record);

    int updateByPrimaryKey(LineTechnologyStatusStationSafetyInformationEntity record);

    List<LineTechnologyStatusStationSafetyInformationEntity> selectByExample(Integer stationId);

    long countByStationId(Integer stationId);
}