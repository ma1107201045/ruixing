package com.yintu.ruixing.common;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

public interface ScheduleJobService extends BaseService<ScheduleJobEntity, Integer> {

    void batchAdd(List<ScheduleJobEntity> scheduleJobEntities);

    List<ScheduleJobEntity> findAll();

    void start(Integer id);

    void pause(Integer id);

    void startAllJob();

    void pauseAllJob();
}
