package com.yintu.ruixing.guzhangzhenduan;

import com.yintu.ruixing.guzhangzhenduan.QuDuanBaseEntity;
import com.yintu.ruixing.guzhangzhenduan.QuDuanShuXingEntity;
import com.yintu.ruixing.guzhangzhenduan.quduanEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author:lcy
 * @date:2020-06-11 17
 * 曲线相关
 */
public interface QuXianService {
    //List<SheBeiEntity> findSheBeiByCid(Integer id);

    List<String> findQuDuanById(Integer id);

    //List<QuDuanInfoEntity> findQuDuanDataByTime(Date time);

    List<QuDuanBaseEntity> findQuDuanDataByTime1(Date time);


    Integer findQuDuanData(Long starttimee, String shuxingname, String quduanname, Integer qdid);


    List<QuDuanShuXingEntity> shuXingMing();

    List<String> findShuXingName(Integer[] shuxingId);

    List<String> findShuXingHanZiName(Integer[] shuxingId);

    Integer findQDid(String quduanname, Integer czid);

    List<quduanEntity> findQuDuanDatas(long starttime, long endtime, String shuxingname,
                                       String quduanname, Integer qdid, String tableName);

    List<quduanEntity> findOneQuDuanDatas(long statrtime, long endtime, String shuxingname, String quduanname, Integer qdid, String tableName);

    List<String> findDMHQuDuanById(Integer id);


    List<quduanEntity> findQuDuanShiShiData(String shuxingname, String quduanname, Integer qdid, String tableName);

    List<quduanEntity> findQuDuanDayData(long statrtime, long endtime, String shuxingname, String quduanname, Integer qdid, String tableName);



    List<quduanEntity> findDMHQuDuanShiShiData(String shuxingname, String quduanname, Integer qdid, String tableName);

    List<quduanEntity> findDMHQuDuanData(long starttime, long endtime, String shuxingname, String quduanname, Integer qdid, String tableName);


    String findMaxNumber(Integer czid, Integer qdid, Integer mid);

    String findMinNumber(Integer czid, Integer qdid, Integer mid);
}
