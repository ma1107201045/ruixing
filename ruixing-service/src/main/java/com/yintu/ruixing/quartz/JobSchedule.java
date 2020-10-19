package com.yintu.ruixing.quartz;

import com.yintu.ruixing.quartz.service.QuartzService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JobSchedule implements CommandLineRunner {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QuartzService taskService;

    @Override
    public void run(String... args) {
        logger.debug("服务器启动任务调度开始==============任务调度开始");
        taskService.timingTask();
        logger.debug("服务器启动任务调度结束==============任务调度结束");
    }
}
