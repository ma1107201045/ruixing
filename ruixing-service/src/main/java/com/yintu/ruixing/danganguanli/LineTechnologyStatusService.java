package com.yintu.ruixing.danganguanli;

import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.common.util.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/22 17:37
 */
public interface LineTechnologyStatusService extends BaseService<LineTechnologyStatusEntityWithBLOBs, Integer> {


    void edit(LineTechnologyStatusEntityWithBLOBs lineTechnologyStatusEntityWithBLOBs, Integer[] xiangmutypeIds);

    List<LineTechnologyStatusEntityWithBLOBs> findByExample(Integer xid);

    Map<String, Object> findLineStatistics(Integer xid);

    /**
     * 查询线段基本信息以及统计信息
     *
     * @param xid 线段id
     * @return 线段总体信息
     */
    JSONObject findLineInfoAndStatistics(Integer xid, String username);
}
