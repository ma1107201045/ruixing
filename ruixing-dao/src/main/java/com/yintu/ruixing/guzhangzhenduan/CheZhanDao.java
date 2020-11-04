package com.yintu.ruixing.guzhangzhenduan;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Qiao
 * @date 2020/5/25 16:48
 * @return 添加车站信息
 */
@Mapper
public interface CheZhanDao {
    /**
     * @param cheZhanEntity
     * @return 添加车站信息
     * @author Qiao
     * @date 2020/5/25 16:48
     */
    void insertChezhan(CheZhanEntity cheZhanEntity);

    /**
     * @return 根据id修改车站信息
     * @author Qiao
     * @date 2020/5/25 16:49
     */
    void updateCheZhan(CheZhanEntity cheZhanEntity);

    /**
     * @return 根据id查询车站信息
     * @author Qiao
     * @date 2020/5/25 16:49
     */
    CheZhanEntity selectByCheZhan(Long cid);

    /**
     * 根据id删除车站
     *
     * @param cid
     */
    void delCheZhan(Long cid);

    /**
     * 查询全部车站信息
     *
     * @return 车站信息集合
     */
    List<CheZhanEntity> selectAll();

    /**
     * 按照车站名查询
     *
     * @param czName 车站名名称
     * @return
     */
    List<CheZhanEntity> selectByCzName(String czName);

    /**
     * 按照车站专用id名查询json
     *
     * @param czId 车站专用id
     * @return
     */
    String selectJsonByCzId(Integer czId);

    /**
     * 按照车站专用id名查询车站信息
     *
     * @param czId 车站专用id
     * @return
     */
    CheZhanEntity selectByczId(Integer czId);


    List<CheZhanEntity> findallChezhanByName(String czname);


    Long findchezhanid(long parseLong);

    Integer findxianduanid(long parseLong);

    List<CheZhanEntity> findSomeCheZhanByXid(Integer xid);

    String findQDJsonByCid(Integer cid);

    String findDMHJsonByCid(Integer cid);

    Integer findFirstCZid(Integer xid);

    Integer findEndCZid(Integer xid);

    List<CheZhanEntity> findStartCheZhan(Integer firstCZid);

    List<CheZhanEntity> findEndCheZhan(Integer endCZid);

    List<CheZhanEntity> findCheZhanByIds(@Param("parseInt") Integer parseInt, @Param("parseInt1") Integer parseInt1);

    List<CheZhanEntity> findXiangLinOneCheZhanByXdid(Integer xdid);

    List<CheZhanEntity> findXiangLinTwoCheZhanByXdid(Integer xdid);

    List<CheZhanEntity> findXiangLinThreeCheZhanByXdid(Integer xdid);

    List<CheZhanEntity> findXiangLinFourCheZhanByXdid(Integer xdid);

    List<CheZhanEntity> findXiangLinFiveCheZhanByXdid(Integer xdid);

    List<CheZhanEntity> findXiangLinSixCheZhanByXdid(Integer xdid);

    Integer findNumBycid(Integer id);

    Integer findCzNumber(@Param("stationId") Integer stationId);

    List<CheZhanEntity> findczidBydwdName(@Param("dwdid") Integer dwdid);

    List<CheZhanEntity> findczidBydwdNamexdName(@Param("dwdid") Integer dwdid, @Param("xdid") Integer xdid);

    Integer findCzid(@Param("czid") Integer czid);

    List<CheZhanEntity> findCheZhanDatasByXid(@Param("xid") Integer xid);

    List<CheZhanEntity> findallChezhanByCZidAndXDid(@Param("parseLong") long parseLong, @Param("parseLong1") long parseLong1);

    /**
     * 通过车站id查询车站连接状态
     *
     * @param id           车站id或者车站专用id
     * @param isPrimaryKey true：车站id false 车站专用id
     * @return 连接状态 1.已连接 0.未连接
     */
    Integer selectCzStutrs(Long id, Boolean isPrimaryKey);

    /**
     * 通过车站id查询车站名称
     *
     * @param id           车站id或者车站专用id
     * @param isPrimaryKey true：车站id false 车站专用id
     * @return 车站名称
     */
    String selectNameById(Long id, Boolean isPrimaryKey);

    long selectStationConfigurationSum();

    long selectStationSum();

    Integer findjidianjiashidianmahuashebeinumberByCid(Integer cid);
}
