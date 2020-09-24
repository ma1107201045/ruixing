package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationSafetyInformationDao;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationSafetyInformationEntity;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationSafetyInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/24 9:30
 */
@Service
@Transactional
public class LineTechnologyStatusStationSafetyInformationServiceImpl implements LineTechnologyStatusStationSafetyInformationService {
    @Autowired
    private LineTechnologyStatusStationSafetyInformationDao lineTechnologyStatusStationSafetyInformationDao;

    @Override
    public void add(LineTechnologyStatusStationSafetyInformationEntity entity) {
        lineTechnologyStatusStationSafetyInformationDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        lineTechnologyStatusStationSafetyInformationDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(LineTechnologyStatusStationSafetyInformationEntity entity) {
        lineTechnologyStatusStationSafetyInformationDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public LineTechnologyStatusStationSafetyInformationEntity findById(Integer id) {
        return lineTechnologyStatusStationSafetyInformationDao.selectByPrimaryKey(id);
    }

    @Override
    public long countByStationId(Integer stationId) {
        return lineTechnologyStatusStationSafetyInformationDao.countByStationId(stationId);
    }

    @Override
    public List<LineTechnologyStatusStationSafetyInformationEntity> findAll() {
        return lineTechnologyStatusStationSafetyInformationDao.selectAll();
    }
}
