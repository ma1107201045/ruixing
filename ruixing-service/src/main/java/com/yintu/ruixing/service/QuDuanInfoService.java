package com.yintu.ruixing.service;

import com.yintu.ruixing.entity.QuDuanInfoEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author:mlf
 * @date:2020/6/3 11:52
 */
public interface QuDuanInfoService {


    QuDuanInfoEntity findById(Integer id);

    /**
     * @param qid  区段信息id
     * @param time 时间
     * @return 区段详情集合
     */
    List<QuDuanInfoEntity> findQidAndTime(Integer qid, Date time);

    /**
     * @param xid       线段id
     * @param cid       车站id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    List<Integer> findByXidAndCidAndBetweenAndTime(Integer xid, Integer cid, Date startTime, Date endTime);


    /**
     * @param xid  线段id
     * @param cid  车站id
     * @param time 时间
     * @return
     */
    List<QuDuanInfoEntity> findByXidAndCidAndTime(Integer xid, Integer cid, Date time);

    /**
     * 日报表
     *
     * @param time 日期
     * @return 统计
     */
    List<Map<String, Object>> findStatisticsByDate(Integer xid, Integer cid, Date time);


}
