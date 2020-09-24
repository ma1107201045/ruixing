package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationMaterialDao;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationMaterialEntity;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/24 9:53
 */
@Service
@Transactional
public class LineTechnologyStatusStationMaterialServiceImpl implements LineTechnologyStatusStationMaterialService {

    @Autowired
    private LineTechnologyStatusStationMaterialDao lineTechnologyStatusStationMaterialDao;

    @Override
    public void add(LineTechnologyStatusStationMaterialEntity entity) {
        lineTechnologyStatusStationMaterialDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        lineTechnologyStatusStationMaterialDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(LineTechnologyStatusStationMaterialEntity entity) {
        lineTechnologyStatusStationMaterialDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public LineTechnologyStatusStationMaterialEntity findById(Integer id) {
        return lineTechnologyStatusStationMaterialDao.selectByPrimaryKey(id);
    }

    @Override
    public long countByStationId(Integer stationId) {
        return lineTechnologyStatusStationMaterialDao.countByStationId(stationId);
    }

    @Override
    public List<LineTechnologyStatusStationMaterialEntity> findAll() {
        return lineTechnologyStatusStationMaterialDao.selectAll();
    }
}
