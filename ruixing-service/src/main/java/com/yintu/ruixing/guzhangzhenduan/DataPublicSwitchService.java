package com.yintu.ruixing.guzhangzhenduan;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @Author: mlf
 * @Date: 2020/12/13 16:05
 * @Version: 1.0
 */
public interface DataPublicSwitchService {


    boolean isTableExist(String tableName);

    DataPublicSwitchEntity findNewFirstData(Integer czId);


    /**
     * @param czId 车站id
     */
    List<JSONObject> findByCondition(Integer czId);

}
