package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.master.danganguanli.LineTechnologyStatusStationUnitDao;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationUnitEntity;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/24 9:26
 */
@Service
@Transactional
public class LineTechnologyStatusStationUnitServiceImpl implements LineTechnologyStatusStationUnitService {
    @Autowired
    private LineTechnologyStatusStationUnitDao lineTechnologyStatusStationUnitDao;

    @Override

    public void add(LineTechnologyStatusStationUnitEntity entity) {
        lineTechnologyStatusStationUnitDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        lineTechnologyStatusStationUnitDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(LineTechnologyStatusStationUnitEntity entity) {
        lineTechnologyStatusStationUnitDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public LineTechnologyStatusStationUnitEntity findById(Integer id) {
        return lineTechnologyStatusStationUnitDao.selectByPrimaryKey(id);
    }

    @Override
    public long countByStationId(Integer stationId) {
        return lineTechnologyStatusStationUnitDao.countByStationId(stationId);
    }

    @Override
    public List<LineTechnologyStatusStationUnitEntity> findByExample(Integer stationId) {
        return lineTechnologyStatusStationUnitDao.selectByExample(stationId);
    }
}
