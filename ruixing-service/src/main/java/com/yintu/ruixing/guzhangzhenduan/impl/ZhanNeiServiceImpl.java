package com.yintu.ruixing.guzhangzhenduan.impl;

import cn.hutool.core.date.DateUtil;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.StringUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private QuDuanInfoDao quDuanInfoDao;

    @Autowired
    private QuDuanInfoDaoV2 quDuanInfoDaoV2;
    @Autowired
    private QuDuanInfoService quDuanInfoService;

    @Override
    public List<QuDuanInfoEntityV2> findDianMaHuaDatasByCZids(Integer czid, String tableName) {
        if (quDuanInfoDaoV2.isTableExist(tableName) > 0)
            return quDuanInfoDaoV2.findDianMaHuaDatasByCZids(czid, tableName);
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
    public List<QuDuanInfoEntity> findDianMaHuaDatabById(Integer id) {
        return quDuanInfoDaoV2.findDianMaHuaDatabById(id);
    }

    @Override
    public List<CheZhanEntity> findTieLuJuById(Integer page, Integer size) {
        return zhanNeiDao.findAllWangLuoLianJie();
    }

    @Override
    public List<QuDuanInfoEntityV2> findDianMaHuaDatasByCZid(Integer czid, Date startTime, Date endTime) {
        if (startTime.after(endTime))
            throw new BaseRuntimeException("开始时间不能大于结束时间");
        if (DateUtil.month(startTime) == DateUtil.month(endTime)) {
            String tableName = StringUtil.getTableName(czid, startTime);
            return quDuanInfoService.isTableExist(tableName) ? quDuanInfoDaoV2.findDianMaHuaDatasByCZid(czid, startTime, endTime, tableName) : new ArrayList<>();
        } else {
            String firstTableName = StringUtil.getTableName(czid, startTime);
            List<QuDuanInfoEntityV2> firstQuDuanInfoEntityV2s =
                    quDuanInfoService.isTableExist(firstTableName) ? quDuanInfoDaoV2.findDianMaHuaDatasByCZid(czid, startTime, DateUtil.endOfDay(startTime), firstTableName) : new ArrayList<>();

            String lastTableName = StringUtil.getTableName(czid, endTime);
            List<QuDuanInfoEntityV2> lastQuDuanInfoEntityV2s =
                    quDuanInfoService.isTableExist(firstTableName) ? quDuanInfoDaoV2.findDianMaHuaDatasByCZid(czid, DateUtil.beginOfDay(endTime), endTime, lastTableName) : new ArrayList<>();
            firstQuDuanInfoEntityV2s.addAll(lastQuDuanInfoEntityV2s);
            return firstQuDuanInfoEntityV2s;
        }

    }

}
