package com.yintu.ruixing.guzhangzhenduan.impl;

import com.yintu.ruixing.guzhangzhenduan.*;
import com.yintu.ruixing.guzhangzhenduan.QuXianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private QuDuanInfoDao quDuanInfoDao;
    @Autowired
    private QuDuanInfoDaoV2 quDuanInfoDaoV2;
    @Autowired
    private QuDuanBaseDao quDuanBaseDao;

   /* @Override
    public List<SheBeiEntity> findSheBeiByCid(Integer id) {
        return quXianDao.findSheBeiByCid(id);
    }
*/
    @Override
    public List<String> findQuDuanById(Integer id) {
        return quXianDao.findQuDuanById(id);
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
    public Integer findQuDuanDataByTime2(String format,String name) {
        return quDuanInfoDao.findQuDuanDataByTime2(format,name);
    }



    @Override
    public  Integer findQuDuanData(Long starttimee,  String shuxingname, String quduanname,Integer qdid) {
        return quDuanInfoDaoV2.findQuDuanData(starttimee,shuxingname,quduanname,qdid);
    }

    @Override
    public List<QuDuanShuXingEntity> shuXingMing() {
        return quXianDao.shuXingMing();
    }

    @Override
    public List<String> findShuXingName(Integer[] shuxingId) {
       // List<String> name=null;
        List<String> list=new ArrayList<>();
        for (int i = 0; i < shuxingId.length; i++) {
            String shuXingName = quXianDao.findShuXingName(shuxingId[i]);
            list.add(i,shuXingName);
        }

        return list;
    }

    @Override
    public Integer findQDid(String quduanname) {
        return quDuanBaseDao.findQDid(quduanname);
    }

    @Override
    public List<String> findShuXingHanZiName(Integer[] shuxingId) {
        List<String> list=new ArrayList<>();
        for (int i = 0; i < shuxingId.length; i++) {
            String shuXingHanZiName = quXianDao.findShuXingHanZiName(shuxingId[i]);
            list.add(i,shuXingHanZiName);
        }
        return list;
    }

    @Override
    public BigDecimal findQuDuanShiShiData(long shishitimes, String shuxingname, String quduanname, Integer qdid, String tableName) {
        return quDuanInfoDaoV2.findQuDuanShiShiData(shishitimes,shuxingname,quduanname,qdid,tableName);
    }

    @Override
    public BigDecimal findOneQuDuanDatas(Long time, String shuxingname, String quduanname, Integer qdid, String tableName) {
        return quDuanInfoDaoV2.findOneQuDuanDatas(time,shuxingname,quduanname,qdid,tableName);
    }

    @Override
    public List<quduanEntity> findQuDuanDatas(long starttime, long endtime, String shuxingname, String quduanname, Integer qdid,String tableName) {
        return quDuanInfoDaoV2.findQuDuanDatas(starttime,endtime,shuxingname,quduanname,qdid,tableName);
    }
}
