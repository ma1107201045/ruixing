package com.yintu.ruixing.guzhangzhenduan;

import com.yintu.ruixing.guzhangzhenduan.CheZhanEntity;
import com.yintu.ruixing.guzhangzhenduan.QuDuanBaseEntity;
import com.yintu.ruixing.guzhangzhenduan.QuDuanInfoEntity;
import com.yintu.ruixing.guzhangzhenduan.QuDuanInfoEntityV2;

import java.util.List;

/**
 * @author:lcy
 * @date:2020-06-06 19
 * 站内相关
 */
public interface ZhanNeiService  {
    List<QuDuanBaseEntity> findAllDianMaHua(Long id);

    List<CheZhanEntity> findAllWangLuoLianJie();

    void editWangLuoLianJieById(CheZhanEntity cheZhanEntity);

    List<QuDuanInfoEntity> findDianMaHuaDatabById(Integer id);

    List<CheZhanEntity> findTieLuJuById(Integer page, Integer size);

    List<QuDuanInfoEntityV2> findDianMaHuaDatasByCZid(Integer czid, long time,String tableName);

    List<QuDuanInfoEntityV2> findDianMaHuaDatasByCZids(Integer czid,String tableName);
}
