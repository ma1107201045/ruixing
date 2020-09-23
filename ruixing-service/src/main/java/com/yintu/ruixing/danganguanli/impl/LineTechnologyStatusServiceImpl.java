package com.yintu.ruixing.danganguanli.impl;

import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.danganguanli.*;
import com.yintu.ruixing.guzhangzhenduan.XianDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.XianDuanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/22 17:37
 */
@Service
@Transactional
public class LineTechnologyStatusServiceImpl implements LineTechnologyStatusService {
    @Autowired
    private LineTechnologyStatusDao lineTechnologyStatusDao;
    @Autowired
    private LineTechnologyStatusXiangemutypeService lineTechnologyStatusXiangemutypeService;
    @Autowired
    private XianDuanService xianDuanService;

    @Override
    public void add(LineTechnologyStatusEntityWithBLOBs entity) {
        lineTechnologyStatusDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        lineTechnologyStatusDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(LineTechnologyStatusEntityWithBLOBs entity) {
        lineTechnologyStatusDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public LineTechnologyStatusEntityWithBLOBs findById(Integer id) {
        return lineTechnologyStatusDao.selectByPrimaryKey(id);
    }

    @Override
    public void edit(LineTechnologyStatusEntityWithBLOBs lineTechnologyStatusEntityWithBLOBs, Integer[] xiangmutypeIds) {
        List<LineTechnologyStatusXiangemutypeEntity> lineTechnologyStatusXiangemutypeEntities = lineTechnologyStatusXiangemutypeService.findByExample(lineTechnologyStatusEntityWithBLOBs.getId());
        for (LineTechnologyStatusXiangemutypeEntity lineTechnologyStatusXiangemutypeEntity : lineTechnologyStatusXiangemutypeEntities) {
            lineTechnologyStatusXiangemutypeService.remove(lineTechnologyStatusXiangemutypeEntity.getId());
        }
        //去重
        Set<Integer> set = new HashSet<>(Arrays.asList(xiangmutypeIds));
        for (Integer xiangmutypeId : set) {
            LineTechnologyStatusXiangemutypeEntity lineTechnologyStatusXiangemutypeEntity = new LineTechnologyStatusXiangemutypeEntity();
            lineTechnologyStatusXiangemutypeEntity.setCreateBy(lineTechnologyStatusEntityWithBLOBs.getModifiedBy());
            lineTechnologyStatusXiangemutypeEntity.setCreateTime(lineTechnologyStatusEntityWithBLOBs.getModifiedTime());
            lineTechnologyStatusXiangemutypeEntity.setModifiedBy(lineTechnologyStatusEntityWithBLOBs.getModifiedBy());
            lineTechnologyStatusXiangemutypeEntity.setModifiedTime(lineTechnologyStatusEntityWithBLOBs.getModifiedTime());
            lineTechnologyStatusXiangemutypeEntity.setLineTechnologyStatusId(lineTechnologyStatusEntityWithBLOBs.getId());
            lineTechnologyStatusXiangemutypeEntity.setXiangmutypeId(xiangmutypeId);
            lineTechnologyStatusXiangemutypeService.add(lineTechnologyStatusXiangemutypeEntity);
        }
        this.edit(lineTechnologyStatusEntityWithBLOBs);
    }

    @Override
    public List<LineTechnologyStatusEntityWithBLOBs> findByExample(Integer xid) {
        return lineTechnologyStatusDao.selectByExample(xid);
    }

    @Override
    public Map<String, Object> findRailwaysBureauStatistics(Integer tid) {
        return lineTechnologyStatusDao.selectRailwaysBureauStatistics(tid);
    }

    @Override
    public Map<String, Object> findSignalDepotStatistics(Integer did) {
        return lineTechnologyStatusDao.selectSignalDepotStatistics(did);
    }

    @Override
    public Map<String, Object> findLineStatistics(Integer xid) {
        return lineTechnologyStatusDao.selectLineStatistics(xid);
    }


    @Override
    public JSONObject findLineInfoAndStatistics(Integer xid, String username) {
        List<LineTechnologyStatusEntityWithBLOBs> lineTechnologyStatusEntityWithBLOBs = this.findByExample(xid);
        if (lineTechnologyStatusEntityWithBLOBs.isEmpty()) {
            LineTechnologyStatusEntityWithBLOBs entity = new LineTechnologyStatusEntityWithBLOBs();
            entity.setCreateBy(username);
            entity.setCreateTime(new Date());
            entity.setModifiedBy(username);
            entity.setModifiedTime(new Date());
            entity.setXid(xid);
            this.add(entity);
            lineTechnologyStatusEntityWithBLOBs.add(entity);
        }
        JSONObject jo = (JSONObject) JSONObject.toJSON(lineTechnologyStatusEntityWithBLOBs.get(0));
        Map<String, Object> map = this.findLineStatistics(xid);
        jo.putAll(map);
        jo.put("lengthMileage", jo.get("lengthMileage") == null ? 0 : jo.get("lengthMileage"));
        return jo;
    }
}
