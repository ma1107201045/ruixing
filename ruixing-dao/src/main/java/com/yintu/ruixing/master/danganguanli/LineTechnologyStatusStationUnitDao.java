package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationUnitEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface LineTechnologyStatusStationUnitDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusStationUnitEntity record);

    int insertSelective(LineTechnologyStatusStationUnitEntity record);

    LineTechnologyStatusStationUnitEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusStationUnitEntity record);

    int updateByPrimaryKey(LineTechnologyStatusStationUnitEntity record);

    List<LineTechnologyStatusStationUnitEntity> selectByExample(Integer stationId);

    long countByStationId(Integer stationId);
}