package com.yintu.ruixing.danganguanli.impl;

import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.danganguanli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    @Autowired
    private LineTechnologyStatusStationUnitService lineTechnologyStatusStationUnitService;
    @Autowired
    private LineTechnologyStatusStationSafetyInformationService lineTechnologyStatusStationSafetyInformationService;
    @Autowired
    private LineTechnologyStatusStationDeviceService lineTechnologyStatusStationDeviceService;
    @Autowired
    private LineTechnologyStatusStationMaterialService lineTechnologyStatusStationMaterialService;
    @Autowired
    private LineTechnologyStatusStationConfigurationService lineTechnologyStatusStationConfigurationService;

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
        lineTechnologyStatusStationDao.updateByPrimaryKeySelective(entity);
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
    public Map<String, Object> findStationStatistics(Integer cid) {
        return lineTechnologyStatusStationDao.selectStationStatistics(cid);
    }

    @Override
    public Map<String, Object> findStationInfoAndStatistics(Integer cid, String username) {
        List<LineTechnologyStatusStationEntity> lineTechnologyStatusStationEntities = this.findByExample(cid);
        if (lineTechnologyStatusStationEntities.isEmpty()) {
            LineTechnologyStatusStationEntity entity = new LineTechnologyStatusStationEntity();
            entity.setCreateBy(username);
            entity.setCreateTime(new Date());
            entity.setModifiedBy(username);
            entity.setModifiedTime(new Date());
            entity.setCid(cid);
            this.add(entity);
            lineTechnologyStatusStationEntities.add(entity);
        }
        JSONObject jo = (JSONObject) JSONObject.toJSON(lineTechnologyStatusStationEntities.get(0));
        Map<String, Object> map = this.findStationStatistics(cid);
        jo.putAll(map);
        jo.put("unitCount", lineTechnologyStatusStationUnitService.countByStationId(jo.getInteger("id")));
        jo.put("safetyInformationCount", lineTechnologyStatusStationSafetyInformationService.countByStationId(jo.getInteger("id")));
        jo.put("deviceCount", lineTechnologyStatusStationDeviceService.countByStationId(jo.getInteger("id")));
        jo.put("materialCount", lineTechnologyStatusStationMaterialService.countByStationId(jo.getInteger("id")));
        jo.put("configurationCount", lineTechnologyStatusStationConfigurationService.countByStationId(jo.getInteger("id")));
        return jo;
    }


}
