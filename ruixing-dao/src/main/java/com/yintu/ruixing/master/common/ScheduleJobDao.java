package com.yintu.ruixing.master.common;

import com.yintu.ruixing.common.ScheduleJobEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface ScheduleJobDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ScheduleJobEntity record);

    int insertSelective(ScheduleJobEntity record);

    ScheduleJobEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScheduleJobEntity record);

    int updateByPrimaryKey(ScheduleJobEntity record);

    void muchInsert(List<ScheduleJobEntity> scheduleJobEntities);

    void deleteByJobName(String jobName);

    List<ScheduleJobEntity> selectByJobName(String jobName);

    List<ScheduleJobEntity> selectAll();
}