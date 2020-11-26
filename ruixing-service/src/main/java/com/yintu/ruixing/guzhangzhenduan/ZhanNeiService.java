package com.yintu.ruixing.guzhangzhenduan;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * @author:lcy
 * @date:2020-06-06 19
 * 站内相关
 */
public interface ZhanNeiService {
    List<QuDuanBaseEntity> findAllDianMaHua(Long id);

    List<CheZhanEntity> findAllWangLuoLianJie();

    void editWangLuoLianJieById(CheZhanEntity cheZhanEntity);

    List<CheZhanEntity> findTieLuJuById(Integer page, Integer size);

    List<QuDuanInfoEntityV2> findDianMaHuaDatasByCZid(Integer czid, Date startTime, Date endTime);

    List<JSONObject> findDianMaHuaDatasByCZids(Integer czid, String tableName);
}
