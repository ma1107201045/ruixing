package com.yintu.ruixing.weixiudaxiu;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

public interface EquipmentIndexAnalysisService extends BaseService<EquipmentIndexAnalysisEntity, Integer> {

    List<EquipmentIndexAnalysisEntity> findByCondition(String quDuanYunYingName);
}
