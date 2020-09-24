package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationConfigurationDao;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationConfigurationEntity;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/24 9:58
 */
@Service
@Transactional
public class LineTechnologyStatusStationConfigurationServiceImpl implements LineTechnologyStatusStationConfigurationService {
    @Autowired
    private LineTechnologyStatusStationConfigurationDao lineTechnologyStatusStationConfigurationDao;

    @Override
    public void add(LineTechnologyStatusStationConfigurationEntity entity) {
        lineTechnologyStatusStationConfigurationDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        lineTechnologyStatusStationConfigurationDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(LineTechnologyStatusStationConfigurationEntity entity) {
        lineTechnologyStatusStationConfigurationDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public LineTechnologyStatusStationConfigurationEntity findById(Integer id) {
        return lineTechnologyStatusStationConfigurationDao.selectByPrimaryKey(id);
    }

    @Override
    public long countByStationId(Integer stationId) {
        return lineTechnologyStatusStationConfigurationDao.countByStationId(stationId);
    }

    @Override
    public List<LineTechnologyStatusStationConfigurationEntity> findAll() {
        return lineTechnologyStatusStationConfigurationDao.selectAll();
    }


}
