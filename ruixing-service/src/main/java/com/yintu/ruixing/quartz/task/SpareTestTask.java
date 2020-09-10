package com.yintu.ruixing.quartz.task;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component("testJob02")
@Transactional
public class SpareTestTask {

    public void execute() {

    }
}

