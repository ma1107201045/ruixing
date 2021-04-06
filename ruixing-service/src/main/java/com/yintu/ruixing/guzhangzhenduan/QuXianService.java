package com.yintu.ruixing.guzhangzhenduan;

import com.yintu.ruixing.guzhangzhenduan.QuDuanShuXingEntity;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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


    List<quduanEntity> findQuDuanShiShiData(String shuxingname, String quduanname, Integer qdid, String tableName );

    List<quduanEntity> findQuDuanDayData(long statrtime, long endtime, String shuxingname, String quduanname, Integer qdid, String tableName);



    List<quduanEntity> findDMHQuDuanShiShiData(String shuxingname, String quduanname, Integer qdid, String tableName );

    List<quduanEntity> findDMHQuDuanData(long starttime, long endtime, String shuxingname, String quduanname, Integer qdid, String tableName);


    String findMaxNumber(Integer czid, Integer qdid, Integer mid, Integer type);

    String findMinNumber(Integer czid, Integer qdid, Integer mid, Integer type);

    List<QuDuanShuXingEntity> kaiguanliang();

    String findMaxNumberK(Integer czid, Integer qdid, Integer mid, Integer type);

    String findMaxNumberZ(Integer czid, Integer qdid, Integer mid, Integer type);

    String findMinNumberK(Integer czid, Integer qdid, Integer mid, Integer type);

    String findMinNumberZ(Integer czid, Integer qdid, Integer mid, Integer type);

    LinkedHashMap findQuDuanShiShiDataa(StringBuilder sb, Integer qdid, String tableName);

    List<LinkedHashMap> findQuDuanDayDataa(StringBuilder sb, long statrtime, Long endtime, Integer qdiddd, String tableName);

    List<LinkedHashMap> findDMHDayData(StringBuilder sbb, long statrtime, Long endtime, Integer qdidd, String tableName);

    LinkedHashMap findDMHShiShiData(StringBuilder sb, Integer qdiddd, String tableName);

    LinkedHashMap findQuDuanShiShiDataaTime(StringBuilder sb, Integer qdiddd, String tableName, Integer time);

    String findUpLimitNumber(Integer czid, Integer qdid, Integer mid, Integer type);

    String findLowLimitNumber(Integer czid, Integer qdid, Integer mid, Integer type);

	LinkedHashMap findDMHShiShiDataTime(StringBuilder sb, Integer qdiddd, String tableName, Integer creatTime);
}
