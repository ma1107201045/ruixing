package com.yintu.ruixing.danganguanli.impl;

import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationDao;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationEntity;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/23 14:56
 */
@Service
@Transactional
public class LineTechnologyStatusStationServiceImpl implements LineTechnologyStatusStationService {

    @Autowired
    private LineTechnologyStatusStationDao lineTechnologyStatusStationDao;

    @Override
    public void add(LineTechnologyStatusStationEntity entity) {
        lineTechnologyStatusStationDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        lineTechnologyStatusStationDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(LineTechnologyStatusStationEntity entity) {
        lineTechnologyStatusStationDao.updateByPrimaryKey(entity);
    }

    @Override
    public LineTechnologyStatusStationEntity findById(Integer id) {
        return lineTechnologyStatusStationDao.selectByPrimaryKey(id);
    }

    @Override
    public List<LineTechnologyStatusStationEntity> findByExample(Integer cid) {
        return lineTechnologyStatusStationDao.selectByExample(cid);
    }

    @Override
    public Map<String, Object> findLineInfoAndStatistics(Integer cid) {
        return lineTechnologyStatusStationDao.selectLineStatistics(cid);
    }


}
