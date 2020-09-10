package com.yintu.ruixing.quartz.service;

import com.yintu.ruixing.common.JobOperateEnum;
import com.yintu.ruixing.common.ScheduleJobEntity;
import org.quartz.SchedulerException;

public interface QuartzService {

    void timingTask();

    void addJob(ScheduleJobEntity scheduleJobEntity);

    void operateJob(JobOperateEnum jobOperateEnum, ScheduleJobEntity scheduleJobEntity) throws SchedulerException;

    void startAllJob() throws SchedulerException;

    void pauseAllJob() throws SchedulerException;
}
