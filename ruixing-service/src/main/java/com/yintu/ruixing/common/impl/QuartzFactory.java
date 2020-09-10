package com.yintu.ruixing.common.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.yintu.ruixing.common.ScheduleJobEntity;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.reflect.Method;

public class QuartzFactory implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        //获取调度数据
        ScheduleJobEntity scheduleJobEntity = (ScheduleJobEntity) jobExecutionContext.getMergedJobDataMap().get("scheduleJob");

        //获取对应的Bean
        Object object = SpringUtil.getBean(scheduleJobEntity.getBeanName());
        try {
            //利用反射执行对应方法
            Method method = object.getClass().getMethod(scheduleJobEntity.getMethodName());
            method.invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
