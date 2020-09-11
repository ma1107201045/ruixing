package com.yintu.ruixing.quartz.job;

import com.yintu.ruixing.common.ScheduleJobEntity;
import com.yintu.ruixing.common.util.SpringContextUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.lang.reflect.Method;

public class QuartzFactory implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        //获取调度数据
        ScheduleJobEntity scheduleJobEntity = (ScheduleJobEntity) jobExecutionContext.getMergedJobDataMap().get("scheduleJob");

        //获取对应的Bean
        Object job = SpringContextUtil.getBean(scheduleJobEntity.getBeanName());
        try {
            //利用反射执行对应方法
            Method method = job.getClass().getMethod(scheduleJobEntity.getMethodName(), Integer.class);
            method.invoke(job, Integer.valueOf(scheduleJobEntity.getJobName().split("-")[1]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
