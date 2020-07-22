package com.yintu.ruixing.service;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.entity.QuDuanBaseEntity;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/6/15 19:29
 */
public interface QuDuanBaseService extends BaseService<QuDuanBaseEntity, Integer> {


    /**
     * @param czId 车站id
     * @return 区段基础信息集合
     */
    List<QuDuanBaseEntity> findByCzId(Integer czId);
}
