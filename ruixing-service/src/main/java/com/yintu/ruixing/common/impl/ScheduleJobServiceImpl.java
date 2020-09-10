package com.yintu.ruixing.common.impl;

import com.yintu.ruixing.common.ScheduleJobDao;
import com.yintu.ruixing.common.ScheduleJobEntity;
import com.yintu.ruixing.common.ScheduleJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ScheduleJobServiceImpl implements ScheduleJobService {

    @Autowired
    private ScheduleJobDao scheduleJobDao;

    @Override
    public void add(ScheduleJobEntity entity) {
        scheduleJobDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        scheduleJobDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(ScheduleJobEntity entity) {
        scheduleJobDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public ScheduleJobEntity findById(Integer id) {
        return scheduleJobDao.selectByPrimaryKey(id);
    }

    @Override
    public List<ScheduleJobEntity> findAll() {
        return scheduleJobDao.selectAll();
    }
}
