package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.master.danganguanli.LineTechnologyStatusStationDeviceDao;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationDeviceEntity;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/24 9:47
 */
@Service
@Transactional
public class LineTechnologyStatusStationDeviceServiceImpl implements LineTechnologyStatusStationDeviceService {
    @Autowired
    private LineTechnologyStatusStationDeviceDao lineTechnologyStatusStationDeviceDao;

    @Override
    public void add(LineTechnologyStatusStationDeviceEntity entity) {
        lineTechnologyStatusStationDeviceDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        lineTechnologyStatusStationDeviceDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(LineTechnologyStatusStationDeviceEntity entity) {
        lineTechnologyStatusStationDeviceDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public LineTechnologyStatusStationDeviceEntity findById(Integer id) {
        return lineTechnologyStatusStationDeviceDao.selectByPrimaryKey(id);
    }

    @Override
    public long countByStationId(Integer stationId) {
        return lineTechnologyStatusStationDeviceDao.countByStationId(stationId);
    }

    @Override
    public List<LineTechnologyStatusStationDeviceEntity> findByExample(Integer stationId) {
        return lineTechnologyStatusStationDeviceDao.selectByExample(stationId);
    }
}
