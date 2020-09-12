package com.yintu.ruixing.quartz.service.impl;


import com.yintu.ruixing.common.JobOperateEnum;
import com.yintu.ruixing.quartz.job.QuartzFactory;
import com.yintu.ruixing.quartz.service.QuartzService;
import com.yintu.ruixing.common.ScheduleJobEntity;
import com.yintu.ruixing.common.ScheduleJobService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class QuartzServiceImpl implements QuartzService {

    /**
     * 调度器
     */
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ScheduleJobService scheduleJobService;

    /**
     * 开机自启执行任务
     */
    @Override
    public void timingTask() {
        //查询数据库是否存在需要定时的任务
        List<ScheduleJobEntity> scheduleJobEntities = scheduleJobService.findAll();
        if (scheduleJobEntities != null) {
            scheduleJobEntities.forEach(this::addJob);
        }
    }

    @Override
    public void addJob(ScheduleJobEntity scheduleJobEntity) {
        try {
            //创建触发器
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(scheduleJobEntity.getJobName())
                    .withSchedule(CronScheduleBuilder.cronSchedule(scheduleJobEntity.getCronExpression()))
                    // .startNow() 立即执行
                    .startAt(scheduleJobEntity.getExecutionTime())//延时执行
                    .build();

            //创建任务
            JobDetail jobDetail = JobBuilder.newJob(QuartzFactory.class)
                    .withIdentity(scheduleJobEntity.getJobName())
                    .build();

            //传入调度的数据，在QuartzFactory中需要使用
            jobDetail.getJobDataMap().put("scheduleJob", scheduleJobEntity);

            //调度作业
            scheduler.scheduleJob(jobDetail, trigger);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void operateJob(JobOperateEnum jobOperateEnum, ScheduleJobEntity scheduleJobEntity) throws SchedulerException {
        JobKey jobKey = new JobKey(scheduleJobEntity.getJobName());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            //抛异常
        }
        switch (jobOperateEnum) {
            case START:
                scheduler.resumeJob(jobKey);
                break;
            case PAUSE:
                scheduler.pauseJob(jobKey);
                break;
            case DELETE:
                scheduler.deleteJob(jobKey);
                break;
        }
    }

    @Override
    public void startAllJob() throws SchedulerException {
        scheduler.start();
    }

    @Override
    public void pauseAllJob() throws SchedulerException {
        scheduler.standby();
    }
}
