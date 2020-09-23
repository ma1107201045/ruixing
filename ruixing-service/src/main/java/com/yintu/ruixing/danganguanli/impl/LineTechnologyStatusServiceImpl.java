package com.yintu.ruixing.danganguanli.impl;

import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusDao;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusEntityWithBLOBs;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusService;
import com.yintu.ruixing.guzhangzhenduan.XianDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.XianDuanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public List<LineTechnologyStatusEntityWithBLOBs> findByExample(Integer xid) {
        return lineTechnologyStatusDao.selectByExample(xid);
    }

    @Override
    public Map<String, Object> findLineStatistics(Integer xid) {
        return lineTechnologyStatusDao.selectLineStatistics(xid);
    }


    @Override
    public JSONObject findLineInfoAndStatistics(Integer xid, String username) {
        JSONObject jo = null;
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
        jo = (JSONObject) JSONObject.toJSON(lineTechnologyStatusEntityWithBLOBs.get(0));
        Map<String, Object> map = this.findLineStatistics(xid);
        jo.putAll(map);
        XianDuanEntity xianDuanEntity = xianDuanService.findXianDuanById(xid.longValue());
        jo.put("xdName", xianDuanEntity.getXdName());
        jo.put("lengthMileage", 0);
        return jo;
    }
}
