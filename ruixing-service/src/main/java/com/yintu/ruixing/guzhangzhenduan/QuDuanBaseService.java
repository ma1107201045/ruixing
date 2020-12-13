package com.yintu.ruixing.guzhangzhenduan;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.guzhangzhenduan.QuDuanBaseEntity;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/6/15 19:29
 */
public interface QuDuanBaseService extends BaseService<QuDuanBaseEntity, Integer> {


    QuDuanBaseEntity findByCzIdAndQuduanyunyingName(Integer czId, String quDuanYunYingName);

    List<QuDuanBaseEntity> findByCzIdAndQdId(Integer czId, Integer qdId, String qName, Boolean isDianMaHua);

    List<QuDuanBaseEntity> findByQdIdAndQuDuanYunYingName(String quDuanYunYingName);

}
