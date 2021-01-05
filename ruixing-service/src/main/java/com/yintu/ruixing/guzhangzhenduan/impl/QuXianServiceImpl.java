package com.yintu.ruixing.guzhangzhenduan.impl;

import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.guzhangzhenduan.*;
import com.yintu.ruixing.guzhangzhenduan.QuXianService;
import com.yintu.ruixing.master.guzhangzhenduan.QuDuanBaseDao;
import com.yintu.ruixing.slave.guzhangzhenduan.QuDuanInfoDaoV2;
import com.yintu.ruixing.master.guzhangzhenduan.QuXianDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author:lcy
 * @date:2020-06-11 17
 * 曲线相关
 */
@Service
@Transactional
public class QuXianServiceImpl implements QuXianService {
    @Autowired
    private QuXianDao quXianDao;
    @Autowired
    private QuDuanInfoDaoV2 quDuanInfoDaoV2;
    @Autowired
    private QuDuanBaseDao quDuanBaseDao;
    @Autowired
    private QuDuanBaseService quDuanBaseService;
    @Autowired
    private MenXianService menXianService;


    /* @Override
         public List<SheBeiEntity> findSheBeiByCid(Integer id) {
             return quXianDao.findSheBeiByCid(id);
         }
     */
    @Override
    public List<String> findQuDuanById(Integer id) {
        return quXianDao.findQuDuanById(id);
    }

    @Override
    public String findMaxNumberK(Integer czid, Integer qdid, Integer mid, Integer type) {
        Integer typee = 0;
        if (type == 0) {
            typee = 1;
        } else {
            typee = 2;
        }
        return menXianService.findMaxNumberK(czid, qdid, mid, typee);
    }

    @Override
    public String findMaxNumberZ(Integer czid, Integer qdid, Integer mid, Integer type) {
        Integer typee = 0;
        if (type == 0) {
            typee = 1;
        } else {
            typee = 2;
        }
        return menXianService.findMaxNumberZ(czid, qdid, mid, typee);
    }

    @Override
    public String findMinNumberK(Integer czid, Integer qdid, Integer mid, Integer type) {
        Integer typee = 0;
        if (type == 0) {
            typee = 1;
        } else {
            typee = 2;
        }
        return menXianService.findMinNumberK(czid, qdid, mid, typee);
    }

    @Override
    public String findMinNumberZ(Integer czid, Integer qdid, Integer mid, Integer type) {
        Integer typee = 0;
        if (type == 0) {
            typee = 1;
        } else {
            typee = 2;
        }
        return menXianService.findMinNumberZ(czid, qdid, mid, typee);
    }

    @Override
    public String findMinNumber(Integer czid, Integer qdid, Integer mid, Integer type) {
        Integer typee = 0;
        if (type == 0) {
            typee = 1;
        } else {
            typee = 2;
        }
        return menXianService.findMinNumber(czid, qdid, mid, typee);
    }

    @Override
    public String findMaxNumber(Integer czid, Integer qdid, Integer mid, Integer type) {
        Integer typee = 0;
        if (type == 0) {
            typee = 1;
        } else {
            typee = 2;
        }
        return menXianService.findMaxNumber(czid, qdid, mid, typee);
    }

    /* @Override
         public List<QuDuanInfoEntity> findQuDuanDataByTime(Date time) {
             return quDuanInfoDao.findQuDuanDataByTime(time);
         }
     */
    @Override
    public List<QuDuanBaseEntity> findQuDuanDataByTime1(Date time) {
        return quXianDao.findQuDuanDataByTime1(time);
    }


    @Override
    public Integer findQuDuanData(Long starttimee, String shuxingname, String quduanname, Integer qdid) {
        return quDuanInfoDaoV2.findQuDuanData(starttimee, shuxingname, quduanname, qdid);
    }

    @Override
    public List<QuDuanShuXingEntity> kaiguanliang() {
        return quXianDao.kaiguanliang();
    }

    @Override
    public List<QuDuanShuXingEntity> shuXingMing() {
        return quXianDao.shuXingMing();
    }

    @Override
    public List<String> findShuXingName(Integer[] shuxingId) {
        // List<String> name=null;
        List<String> list = new ArrayList<>();
        for (int i = 0; i < shuxingId.length; i++) {
            String shuXingName = quXianDao.findShuXingName(shuxingId[i]);
            list.add(i, shuXingName);
        }

        return list;
    }

    @Override
    public Integer findQDid(String quduanname, Integer czid) {
        return quDuanBaseDao.findQDid(quduanname, czid);
    }

    @Override
    public List<String> findShuXingHanZiName(Integer[] shuxingId) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < shuxingId.length; i++) {
            String shuXingHanZiName = quXianDao.findShuXingHanZiName(shuxingId[i]);
            list.add(i, shuXingHanZiName);
        }
        return list;
    }


    @Override
    public List<String> findDMHQuDuanById(Integer id) {
        return quXianDao.findDMHQuDuanById(id);
    }

    @Override
    public List<quduanEntity> findQuDuanDatas(long starttime, long endtime, String shuxingname, String quduanname, Integer qdid, String tableName) {
        if (quDuanInfoDaoV2.isTableExist(tableName) == 1) {
            return quDuanInfoDaoV2.findQuDuanDatas(starttime, endtime, shuxingname, quduanname, qdid, tableName);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<quduanEntity> findOneQuDuanDatas(long starttime, long endtime, String shuxingname, String quduanname, Integer qdid, String tableName) {
        if (quDuanInfoDaoV2.isTableExist(tableName) == 1) {
            return quDuanInfoDaoV2.findOneQuDuanDatas(starttime, endtime, shuxingname, quduanname, qdid, tableName);
        } else {
            return new ArrayList<>();
        }
    }


    @Override
    public List<quduanEntity> findQuDuanShiShiData(String shuxingname, String quduanname, Integer qdid, String tableName) {
        if (quDuanInfoDaoV2.isTableExist(tableName) == 1) {
            //Integer[] qdIds = quDuanBaseService.findByQdIdAndQuDuanYunYingName(quduanname).stream().map(QuDuanBaseEntity::getQdid).toArray(Integer[]::new);
            return quDuanInfoDaoV2.findQuDuanShiShiData(shuxingname, qdid, tableName);
        } else {
            throw new BaseRuntimeException("暂无数据");
        }
    }


    @Override
    public List<quduanEntity> findQuDuanDayData(long statrtime, long endtime, String shuxingname, String quduanname, Integer qdid, String tableName) {
        if (quDuanInfoDaoV2.isTableExist(tableName) == 1) {
            // Integer[] qdIds = quDuanBaseService.findByQdIdAndQuDuanYunYingName(quduanname).stream().map(QuDuanBaseEntity::getQdid).toArray(Integer[]::new);
            return quDuanInfoDaoV2.findQuDuanDayData(statrtime, endtime, shuxingname, qdid, tableName);
        } else {
            throw new BaseRuntimeException("暂无数据");
        }
    }


    @Override
    public List<quduanEntity> findDMHQuDuanShiShiData(String shuxingname, String quduanname, Integer qdid, String tableName) {
        if (quDuanInfoDaoV2.isTableExist(tableName) == 1) {
          //  Integer[] qdIds = quDuanBaseService.findByQdIdAndQuDuanYunYingName(quduanname).stream().map(QuDuanBaseEntity::getQdid).toArray(Integer[]::new);
            return quDuanInfoDaoV2.findDMHQuDuanShiShiData(shuxingname, qdid, tableName);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<quduanEntity> findDMHQuDuanData(long starttime, long endtime, String shuxingname, String quduanname, Integer qdid, String tableName) {
        if (quDuanInfoDaoV2.isTableExist(tableName) == 1) {
            Integer[] qdIds = quDuanBaseService.findByQdIdAndQuDuanYunYingName(quduanname).stream().map(QuDuanBaseEntity::getQdid).toArray(Integer[]::new);
            return quDuanInfoDaoV2.findDMHQuDuanData(starttime, endtime, shuxingname, qdIds, qdid, tableName);
        } else {
            return new ArrayList<>();
        }
    }


}
