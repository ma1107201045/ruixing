package com.yintu.ruixing.quartz.task;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Component("testJob02")
@Transactional
public class SpareTestTask {

    public void execute() {
        System.out.println(Thread.currentThread().getName() + "-------------------TestJob02任务执行开始-------------------");
        System.out.println(new Date());
        System.out.println(Thread.currentThread().getName() + "-------------------TestJob02任务执行结束-------------------");
    }
}

