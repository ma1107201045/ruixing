package com.yintu.ruixing.quartz.job;

import com.yintu.ruixing.common.ScheduleJobEntity;
import com.yintu.ruixing.common.util.SpringContextUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.reflect.Method;
import java.util.Arrays;

public class QuartzFactory implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        System.out.println(Arrays.toString(jobExecutionContext.getMergedJobDataMap().getKeys()));
        //获取调度数据
        ScheduleJobEntity scheduleJobEntity = (ScheduleJobEntity) jobExecutionContext.getMergedJobDataMap().get("scheduleJob");

        //获取对应的Bean
        Object testJob01 = SpringContextUtil.getBean(scheduleJobEntity.getBeanName());
        try {
            //利用反射执行对应方法
            Method method = testJob01.getClass().getMethod(scheduleJobEntity.getMethodName());
            method.invoke(testJob01);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
