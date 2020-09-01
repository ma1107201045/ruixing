package com.yintu.ruixing.guzhangzhenduan;

import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.guzhangzhenduan.QuDuanInfoEntityV2;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author:mlf
 * @date:2020/6/3 11:52
 */
public interface QuDuanInfoService {

    /**
     * 按照车站查询区段详情
     *
     * @param czId 车站id
     * @return 区段详情
     */
    QuDuanInfoEntityV2 findLastBycZId(Integer czId);

    /**
     * 按照车站查询区段详情
     *
     * @param qid 区段id
     * @return 区段详情 多条
     */
    QuDuanInfoEntityV2 findLastByQid(Integer qid);


    /**
     * 实时报表
     *
     * @param czId 车站id
     * @param time 时间
     * @return
     */
    List<QuDuanInfoEntityV2> findByCzIdAndTime(Integer czId, Date time);

    /**
     * 日报表
     *
     * @param time 日期
     * @return 统计
     */
    List<Map<String, Object>> findStatisticsByCzIdAndTime(Integer czId, Date time);

    /**
     * 查询表是否存在
     *
     * @param tableName 表名称
     * @return
     */
    boolean isTableExist(String tableName);


    /**
     * 车站下每个区段最新的一条
     *
     * @param czId 车站id
     * @param qid  区段id
     * @return
     */
    QuDuanInfoEntityV2 findFirstByCzId1(Integer czId, Integer qid);

    /**
     * 每个车站下同一时刻每个区段的详情
     *
     * @param czId 车站id
     * @param time 时间戳
     * @return
     */
    List<QuDuanInfoEntityV2> findByCzIdAndTime1(Integer czId, Date time);

    /**
     * @param czId 车站id
     * @param time 时间
     * @return
     */
    List<JSONObject> findByCondition(Integer czId, Date time);

    /**
     * 查询车站下区段对应动态属性参数
     *
     * @param czId 车站id
     * @return
     */
    JSONObject findNullProperties(Integer czId);


    /**
     * 实时报表树
     *
     * @param czId 车站id
     * @return
     */
    List<TreeNodeUtil> findPropertiesTree(Integer czId);

    /**
     * @param properties 属性集合
     * @param czId       车站id
     * @return
     */
    JSONObject realTimeReport(Integer czId, Integer[] properties);


}
