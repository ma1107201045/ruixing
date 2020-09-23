package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LineTechnologyStatusStationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusStationEntity record);

    int insertSelective(LineTechnologyStatusStationEntity record);

    LineTechnologyStatusStationEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusStationEntity record);

    int updateByPrimaryKey(LineTechnologyStatusStationEntity record);
}