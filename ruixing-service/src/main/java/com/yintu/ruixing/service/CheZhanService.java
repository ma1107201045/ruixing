package com.yintu.ruixing.service;

import com.yintu.ruixing.entity.CheZhanEntity;
import com.yintu.ruixing.entity.QuDuanBaseEntity;

import java.util.List;

/**
 * @author Qiao
 * @date 2020/5/25 17:03
 * @return
 */
public interface CheZhanService {
    /**
     * @param cheZhanEntity
     * @return 添加车站信息
     * @author Qiao
     * @date 2020/5/25 17:04
     */
    void add(CheZhanEntity cheZhanEntity);

    /**
     * @return 根据id修改车站信息
     * @author Qiao
     * @date 2020/5/25 17:04
     */
    void update(CheZhanEntity cheZhanEntity);

    /**
     * @return 根据id查询车站信息
     * @author Qiao
     * @date 2020/5/25 17:05
     */
    CheZhanEntity findByCheZhanId(Long cid);

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
    List<CheZhanEntity> findAll();

    /**
     * 按照车站名查询
     *
     * @param czName 车站名名称
     * @return
     */
    List<CheZhanEntity> findByCzName(String czName);

    List<QuDuanBaseEntity> findQuDuanByCid(Long cid);

    /**
     * 按照车站专用id名查询
     *
     * @param czId 车站专用id
     * @return
     */
    String findJsonByCzId(Integer czId);
}
