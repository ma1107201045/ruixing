package com.yintu.ruixing.master.guzhangzhenduan;


import com.yintu.ruixing.guzhangzhenduan.*;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;


/**
 * @author:lcy
 * @date:2020-05-21 11
 * 数据统计
 */

public interface DataStatsDao {

    List<DataStatsEntity> findAll();

    List<DataStatsEntity> findTieLuJuById(Long tid);

    List<DataStatsEntity> findDianWuDuanCheZhanById(Long did);

    List<DataStatsEntity> findXianDuanCheZhanById(Long xid);

    List<DataStatsEntity> findCheZhanById(Long cid);

    void editStateByDid(DianWuDuanEntity dianWuDuanEntity);

    void editStateByXid(XianDuanEntity xianDuanEntity);

    void editStateByCid(CheZhanEntity cheZhanEntity);




/*
    List<DataStatsEntity> findDianWuDuanById(@Param("tid") Long tid, @Param("did") Long did);

    List<DataStatsEntity> findXianDuanById(@Param("tid") Long tid, @Param("did") Long did, @Param("xid") Long xid);

    List<DataStatsEntity> findCheZhanById(@Param("tid") Long tid, @Param("did") Long did, @Param("xid") Long xid, @Param("cid") Long cid);

    */

    int delCheZhanListById(int[] ids);

    List<DataStatsEntity> findAllCheZhan();


    List<TieLuJuEntity> findAllTieLuJu();

    List<DianWuDuanEntity> findDianWuDuanByTid(Integer tid);

    List<XianDuanEntity> findXianDuanByDid(Integer did);

    List<CheZhanEntity> findCheZhanByXid(Integer xid);

    List<QuDuanBaseEntity> findAllQuDuan();

    void qingChuaByDid(DianWuDuanEntity dianWuDuanEntity);

    void qingChuaByXid(XianDuanEntity xianDuanEntity);

    void qingChuaByCid(CheZhanEntity cheZhanEntity);

    void editDMHStaCteByCid(CheZhanEntity cheZhanEntity);

    void qingChuaDMHByCid(CheZhanEntity cheZhanEntity);

    List<DianWuDuanEntity> findAllDianWuDuan();


    TieLuJuEntity selectByTid(Long tid);

    DianWuDuanEntity selectByDid(Long did);

    XianDuanEntity selectByXid(Long xid);

    CheZhanEntity selectByCid(Long cid);

}
