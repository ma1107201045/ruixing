package com.yintu.ruixing.guzhangzhenduan.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.StringUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
import com.yintu.ruixing.slave.guzhangzhenduan.QuDuanInfoDaoV2;
import com.yintu.ruixing.master.guzhangzhenduan.ZhanNeiDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author:lcy
 * @date:2020-06-06 19
 * 站内相关
 */
@Service
@Transactional
public class ZhanNeiServiceImpl implements ZhanNeiService {
    @Autowired
    private ZhanNeiDao zhanNeiDao;

    @Autowired
    private QuDuanInfoDaoV2 quDuanInfoDaoV2;
    @Autowired
    private QuDuanInfoService quDuanInfoService;
    @Autowired
    private QuDuanBaseService quDuanBaseService;

    @Override
    public List<JSONObject> findDianMaHuaDatasByCZids(Integer czid, String tableName) {
        if (quDuanInfoDaoV2.isTableExist(tableName) > 0) {
            List<JSONObject> js = new ArrayList<>();
            Integer[] qids = quDuanBaseService.findByCzIdAndQdId(czid, null, null, true)
                    .stream()
                    .map(QuDuanBaseEntity::getQdid)
                    .toArray(Integer[]::new);
            List<QuDuanInfoEntityV2> quDuanInfoEntityV2List = quDuanInfoDaoV2.findDianMaHuaDatasByCZids(czid, qids, tableName);
            for (QuDuanInfoEntityV2 quDuanInfoEntityV2 : quDuanInfoEntityV2List) {
                JSONObject o = (JSONObject) JSONObject.toJSON(quDuanInfoEntityV2);
                String fbjCollectionBei = o.getString("fbjCollectionBei");
                if ("1".equals(fbjCollectionBei)) {
                    o.put("fbjCollectionBei", "吸起");
                }
                if ("1".equals(fbjCollectionBei)) {
                    o.put("fbjCollectionBei", "落下");
                } else {
                    o.put("fbjCollectionBei", "无效");
                }
                BigDecimal fbjCollectionZhu = o.getBigDecimal("fbjCollectionZhu");
                if ("1".equals(fbjCollectionZhu)) {
                    o.put("fbjCollectionZhu", "吸起");
                }
                if ("2".equals(fbjCollectionZhu)) {
                    o.put("fbjCollectionZhu", "落下");
                } else {
                    o.put("fbjCollectionZhu", "无效");
                }
                Integer fbjDriveBei = o.getInteger("fbjDriveBei");
                if (fbjDriveBei == 1) {
                    o.put("fbjDriveBei", "正常");
                }
                if (fbjDriveBei == 2) {
                    o.put("fbjDriveBei", "无");
                } else {
                    o.put("fbjDriveBei", "无效");
                }
                Integer fbjDriveZhu = o.getInteger("fbjDriveZhu");
                if (fbjDriveZhu == 1) {
                    o.put("fbjDriveZhu", "正常");
                }
                if (fbjDriveZhu == 2) {
                    o.put("fbjDriveZhu", "无");
                } else {
                    o.put("fbjDriveZhu", "无效");
                }
                js.add(o);
            }
            return js;
        }
        return new ArrayList<>();
    }

    @Override
    public List<QuDuanBaseEntity> findAllDianMaHua(Long id) {
        return zhanNeiDao.findAllDianMaHua(id);
    }

    @Override
    public List<CheZhanEntity> findAllWangLuoLianJie() {
        return zhanNeiDao.findAllWangLuoLianJie();
    }

    @Override
    public void editWangLuoLianJieById(CheZhanEntity cheZhanEntity) {
        zhanNeiDao.editWangLuoLianJieById(cheZhanEntity);
    }


    @Override
    public List<CheZhanEntity> findTieLuJuById(Integer page, Integer size) {
        return zhanNeiDao.findAllWangLuoLianJie();
    }

    @Override
    public List<QuDuanInfoEntityV2> findDianMaHuaDatasByCZid(Integer czid, Date startTime, Date endTime) {
        if (startTime.after(endTime))
            throw new BaseRuntimeException("开始时间不能大于结束时间");
        Integer[] qids = quDuanBaseService.findByCzIdAndQdId(czid, null, null, true)
                .stream()
                .map(QuDuanBaseEntity::getQdid)
                .toArray(Integer[]::new);
        if (DateUtil.month(startTime) == DateUtil.month(endTime)) {
            String tableName = StringUtil.getTableName(czid, startTime);
            return quDuanInfoService.isTableExist(tableName) ? quDuanInfoDaoV2.findDianMaHuaDatasByCZid(czid, qids, startTime, endTime, tableName) : new ArrayList<>();
        } else {
            String firstTableName = StringUtil.getTableName(czid, startTime);
            List<QuDuanInfoEntityV2> firstQuDuanInfoEntityV2s =
                    quDuanInfoService.isTableExist(firstTableName) ? quDuanInfoDaoV2.findDianMaHuaDatasByCZid(czid, qids, startTime, DateUtil.endOfDay(startTime), firstTableName) : new ArrayList<>();

            String lastTableName = StringUtil.getTableName(czid, endTime);
            List<QuDuanInfoEntityV2> lastQuDuanInfoEntityV2s =
                    quDuanInfoService.isTableExist(firstTableName) ? quDuanInfoDaoV2.findDianMaHuaDatasByCZid(czid, qids, DateUtil.beginOfDay(endTime), endTime, lastTableName) : new ArrayList<>();
            firstQuDuanInfoEntityV2s.addAll(lastQuDuanInfoEntityV2s);
            return firstQuDuanInfoEntityV2s;
        }

    }

}
