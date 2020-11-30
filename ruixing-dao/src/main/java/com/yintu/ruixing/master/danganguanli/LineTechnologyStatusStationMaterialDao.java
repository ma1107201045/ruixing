package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationMaterialEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface LineTechnologyStatusStationMaterialDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusStationMaterialEntity record);

    int insertSelective(LineTechnologyStatusStationMaterialEntity record);

    LineTechnologyStatusStationMaterialEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusStationMaterialEntity record);

    int updateByPrimaryKey(LineTechnologyStatusStationMaterialEntity record);

    List<LineTechnologyStatusStationMaterialEntity> selectByExample(Integer stationId);


    long countByStationId(Integer stationId);
}