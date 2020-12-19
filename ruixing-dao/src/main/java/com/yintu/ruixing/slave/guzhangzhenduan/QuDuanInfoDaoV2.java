package com.yintu.ruixing.slave.guzhangzhenduan;

import com.yintu.ruixing.guzhangzhenduan.QuDuanInfoEntityV2;
import com.yintu.ruixing.guzhangzhenduan.quduanEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface QuDuanInfoDaoV2 {



    int isTableExist(String tableName);

    QuDuanInfoEntityV2 selectFirstByCzId1(Integer czId, Integer qid, String tableName);

   //实时报表
    List<QuDuanInfoEntityV2> selectByCzIdAndTime1(Integer czId, Integer[] qids, Integer startTime, Integer endTime, String tableName);


    //日报表
    List<Map<String, Object>> selectStatisticsByCzIdAndTime(Integer czId, Integer[] properties, Integer s, Integer e, String tableName);


    //根据传过来的数据查询数据 展示在曲线上
    Integer findQuDuanDataByTime2(@Param("format") String format, @Param("name") String name);

    Integer findQuDuanData(@Param("starttimee") Long starttimee, @Param("shuxingname") String shuxingname,
                           @Param("quduanname") String quduanname, @Param("qdid") Integer qdid);

    List<Integer> findQuDuanData(@Param("starttime") Long starttime, @Param("endtime") Long endtime,
                                 @Param("shuxingname") String shuxingname, @Param("quduanname") String quduanname, @Param("qdid") Integer qdid);

    List<quduanEntity> findQuDuanDatas(@Param("starttime") long starttime, @Param("endtime") long endtime,
                                       @Param("shuxingname") String shuxingname, @Param("quduanname") String quduanname,
                                       @Param("qdid") Integer qdid, @Param("tableName") String tableName);

    List<QuDuanInfoEntityV2> findDianMaHuaDatasByCZid(@Param("czid") Integer czid, @Param("qids") Integer[] qids, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("tableName") String tableName);

    List<QuDuanInfoEntityV2> findDianMaHuaDatasByCZids(@Param("czid") Integer czid, @Param("qids") Integer[] qids, @Param("tableName") String tableName);

    List<quduanEntity> findOneQuDuanDatas(@Param("statrtime") long statrtime, @Param("endtime") long endtime,
                                          @Param("shuxingname") String shuxingname,
                                          @Param("quduanname") String quduanname,
                                          @Param("qdid") Integer qdid, @Param("tableName") String tableName);


    List<quduanEntity> findQuDuanShiShiData(@Param("shuxingname") String shuxingname, @Param("qdIds") Integer[] qdIds,
                                            @Param("qdid") Integer qdid, @Param("tableName") String tableName,@Param("Timee") long Timee);

    List<quduanEntity> findQuDuanDayData(@Param("statrtime") long statrtime, @Param("endtime") long endtime,
                                         @Param("shuxingname") String shuxingname,
                                         @Param("qdIds") Integer[] qdIds,
                                         @Param("qdid") Integer qdid, @Param("tableName") String tableName);


    List<quduanEntity> findDMHQuDuanShiShiData(@Param("shuxingname") String shuxingname, @Param("qdIds") Integer[] qdIds,
                                               @Param("qdid") Integer qdid, @Param("tableName") String tableName,@Param("Timee") long Timee);


    List<quduanEntity> findDMHQuDuanData(@Param("starttime") long starttime, @Param("endtime") long endtime,
                                         @Param("shuxingname") String shuxingname, @Param("qdIds") Integer[] qdIds,
                                         @Param("qdid") Integer qdid, @Param("tableName") String tableName);

}