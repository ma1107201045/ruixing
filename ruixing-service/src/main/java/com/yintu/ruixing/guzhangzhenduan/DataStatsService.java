package com.yintu.ruixing.guzhangzhenduan;


import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.guzhangzhenduan.*;


import java.util.List;


/**
 * @author:lcy
 * @date:2020-05-21 11
 * 数据统计
 */

public interface DataStatsService {
    //查询所有数据
    List<DataStatsEntity> findAll();

    //分页查询
    PageInfo<DataStatsEntity> findPage(Integer page, Integer size);


    List<DataStatsEntity> findTieLuJuById(Long tid, Integer page, Integer size);

    List<DataStatsEntity> findDianWuDuanCheZhanById(Long did, Integer page, Integer size);

    List<DataStatsEntity> findXianDuanCheZhanById(Long xid, Integer page, Integer size);

    List<DataStatsEntity> findCheZhanById(Long cid, Integer page, Integer size);






  /*  List<DataStatsEntity> findDianWuDuanById(Long tid, Long did, Integer page, Integer size);

    List<DataStatsEntity> findXianDuanById(Long tid, Long did, Long xid, Integer page, Integer size);

    List<DataStatsEntity> findCheZhanById(Long tid, Long did, Long xid, Long cid, Integer page, Integer size);
    */

    int delCheZhanListById(int[] ids);


    List<DataStatsEntity> findAllCheZhan(Integer page, Integer size);

    void editStateByXid(XianDuanEntity xianDuanEntity);

    void editStateByCid(CheZhanEntity cheZhanEntity);

    List<TieLuJuEntity> findAllTieLuJu();

    List<DianWuDuanEntity> findDianWuDuanByTid(Integer tid);

    List<XianDuanEntity> findXianDuanByDid(Integer did);

    List<CheZhanEntity> findCheZhanByXid(Integer xid);

    List<QuDuanBaseEntity> findAllQuDuan(Integer page, Integer size);

    List<QuDuanBaseEntity> findAllDianMaHua(Integer page, Integer size);

    List<QuDuanBaseEntity> findAllQuDuanByCid(Integer cid, Integer page, Integer size);

    List<QuDuanBaseEntity> findAllDianMaHuaByCid(Integer cid, Integer page, Integer size);

    void addQuDuan(QuDuanBaseEntity quDuanBaseEntity);

    void editQuDuanById(QuDuanBaseEntity quDuanBaseEntity);

    void deletQuDuanById(Integer id);

    void deletQuDuanByIds(Integer[] ids);

    void qingChuaByXid(XianDuanEntity xianDuanEntity);

    void qingChuaByCid(CheZhanEntity cheZhanEntity);

    void addTieLuJU(TieLuJuEntity stringList);

    List<DianWuDuanEntity> findDianWuDuan();

    Long findTLJid(long parseLong);

    void addDianWuDuan(DianWuDuanEntity duanEntity);

    List<XianDuanEntity> findAllXianDuan();

    Long findDWDid(long parseLong,long parseLong1);

    void addXianDuan(XianDuanEntity xianDuanEntity1);

    List<CheZhanEntity> findallChezhan();

    Long findXDid(long parseLong,long parseLong1);

    void addCheZhan(CheZhanEntity cheZhan);

    List<TieLuJuEntity> findAllTieLuJuByName(String tljName);

    List<DianWuDuanEntity> findDianWuDuanByName(String dwdname);

    List<XianDuanEntity> findAllXianDuanByName(String xdname);

    List<CheZhanEntity> findallChezhanByName(String czname);

    Long findchezhanid(long parseLong);

    List<QuDuanBaseEntity> findQuDuanByCid(Long cid);

    Integer findLastParentid();

    Integer findxianduanid(long parseLong);

    List<XianDuanEntity> findAllXianDuanByDwdid(long parseLong, long parseLong1);

    List<DianWuDuanEntity> findDianWuDuanBydid(long parseLong, long parseLong1);

    List<QuDuanBaseEntity> findQuDuanByQuDuanYunYingName(String qudunyunyingname);

    List<CheZhanEntity> findSomeCheZhanByXid(Integer xid);

    XianDuanEntity findXDJsonByXid(Integer xid);

    String findQDJsonByCid(Integer cid);

    String findDMHJsonByCid(Integer cid);

    Integer findFirstCZid(Integer xid);

    Integer findEndCZid(Integer xid);

    List<CheZhanEntity> findStartCheZhan(Integer firstCZid);

    List<CheZhanEntity> findEndCheZhan(Integer endCZid);


    List<QuDuanBaseEntity> findQuDuanByIds(Integer parseInt, Integer parseInt1);

    List<CheZhanEntity> findCheZhanByIds(Integer parseInt, Integer parseInt1);

    List<QuDuanBaseEntity> findQuDuanByTid(Integer tid, Integer page, Integer size);

    List<QuDuanBaseEntity> findQuDuanByDid(Integer did, Integer page, Integer size);

    List<QuDuanBaseEntity> findQuDuanByXid(Integer xid, Integer page, Integer size);

    List<QuDuanBaseEntity> findQuDuanBycid(Integer cid, Integer page, Integer size);

    List<QuDuanBaseEntity> findDianMaHuaByTid(Integer tid, Integer page, Integer size);

    List<QuDuanBaseEntity> findDianMaHuaByDid(Integer did, Integer page, Integer size);

    List<QuDuanBaseEntity> findDianMaHuaByXid(Integer xid, Integer page, Integer size);

    List<QuDuanBaseEntity> findDianMaHuaBycid(Integer cid, Integer page, Integer size);

    List<QuDuanBaseEntity> findDianMaHuaByCid(Integer cid);

    void editDMHStaCteByCid(CheZhanEntity cheZhanEntity);

    void qingChuaDMHByCid(CheZhanEntity cheZhanEntity);

    Integer findNumBycid(Integer id);

    List<DianWuDuanEntity> findAllDianWuDuan();

    List<CheZhanEntity> findallChezhanByCZidAndXDid(long parseLong, long parseLong1);
}
