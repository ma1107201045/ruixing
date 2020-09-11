package com.yintu.ruixing.common;

import cn.hutool.core.lang.UUID;
import com.yintu.ruixing.yunxingweihu.TaskEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/job")
public class ScheduleJobController extends SessionController {

    @Autowired
    private ScheduleJobService jobService;

    @GetMapping(value = "/hello")
    public String helloSpringBoot() {
        return "Hello quartz!";
    }

    @GetMapping("/add")
    public String add() {
        ScheduleJobEntity job = new ScheduleJobEntity();
        job.setCreateBy(this.getLoginUserName());
        job.setCreateTime(new Date());
        job.setModifiedBy(this.getLoginUserName());
        job.setModifiedTime(new Date());
        job.setJobName(UUID.fastUUID().toString());
        job.setCronExpression("0/2 * * * * ?");
        job.setBeanName(TaskEnum.MAINTENANCEPLAN.getValue());
        job.setMethodName("execute");
        jobService.add(job);
        return "新增定时任务成功";
    }

    @GetMapping("/start/{id}")
    public String start(@PathVariable("id") Integer id) {
        jobService.start(id);
        return "启动定时任务成功";
    }

    @GetMapping("/pause/{id}")
    public String pause(@PathVariable("id") Integer id) {
        jobService.pause(id);
        return "暂停定时任务成功";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        jobService.remove(id);
        return "删除定时任务成功";
    }

    @GetMapping("/startAllJob")
    public String startAllJob() {
        jobService.startAllJob();
        return "启动所有定时任务成功";
    }

    @GetMapping("/pauseAllJob")
    public String pauseAllJob() {
        jobService.pauseAllJob();
        return "暂停所有定时任务成功";
    }
}