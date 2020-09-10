package com.yintu.ruixing.common;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScheduleJobDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ScheduleJobEntity record);

    int insertSelective(ScheduleJobEntity record);

    ScheduleJobEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScheduleJobEntity record);

    int updateByPrimaryKey(ScheduleJobEntity record);

    List<ScheduleJobEntity> selectAll();
}