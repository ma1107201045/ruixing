package com.yintu.ruixing.common.impl;

import com.yintu.ruixing.common.QuartzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

public class JobSchedule implements CommandLineRunner {

    @Autowired
    private QuartzService taskService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("任务调度开始==============任务调度开始");
        taskService.timingTask();
        System.out.println("任务调度结束==============任务调度结束");
    }
}
