package com.yintu.ruixing.common.impl;

import com.yintu.ruixing.common.*;
import com.yintu.ruixing.quartz.service.QuartzService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ScheduleJobServiceImpl implements ScheduleJobService {

    @Autowired
    private ScheduleJobDao scheduleJobDao;
    @Autowired
    private QuartzService quartzService;


    @Override
    public void add(ScheduleJobEntity scheduleJobEntity) {
        //此处省去数据验证
        scheduleJobDao.insertSelective(scheduleJobEntity);

        //加入job
        try {
            quartzService.addJob(scheduleJobEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Integer id) {
        //此处省去数据验证
        ScheduleJobEntity scheduleJobEntity = scheduleJobDao.selectByPrimaryKey(id);
        scheduleJobDao.deleteByPrimaryKey(id);
        //执行job
        try {
            quartzService.operateJob(JobOperateEnum.DELETE, scheduleJobEntity);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void edit(ScheduleJobEntity scheduleJobEntity) {
        //此处省去数据验证
        scheduleJobDao.updateByPrimaryKeySelective(scheduleJobEntity);
        //执行job
        try {
            quartzService.operateJob(JobOperateEnum.DELETE, scheduleJobEntity);
            quartzService.addJob(scheduleJobEntity);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ScheduleJobEntity findById(Integer id) {
        return scheduleJobDao.selectByPrimaryKey(id);
    }

    @Override
    public List<ScheduleJobEntity> findAll() {
        return scheduleJobDao.selectAll();
    }


    @Override
    public void start(Integer id) {
        //此处省去数据验证
        ScheduleJobEntity scheduleJobEntity = this.findById(id);
        scheduleJobEntity.setStatus(1);
        this.edit(scheduleJobEntity);

        //执行job
        try {
            quartzService.operateJob(JobOperateEnum.START, scheduleJobEntity);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause(Integer id) {
        //此处省去数据验证
        ScheduleJobEntity scheduleJobEntity = this.findById(id);
        scheduleJobEntity.setStatus(2);
        this.edit(scheduleJobEntity);

        //执行job
        try {
            quartzService.operateJob(JobOperateEnum.PAUSE, scheduleJobEntity);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void startAllJob() {
        //此处省去数据验证
        List<ScheduleJobEntity> scheduleJobEntities = this.findAll();


        //执行job
        try {
            quartzService.startAllJob();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pauseAllJob() {
        //此处省去数据验证
        ScheduleJobEntity job = new ScheduleJobEntity();
        //执行job
        try {
            quartzService.pauseAllJob();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
