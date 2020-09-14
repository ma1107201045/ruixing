package com.yintu.ruixing.weixiudaxiu;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

public interface EquipmentNumberRecordService extends BaseService<EquipmentNumberRecordEntity, Integer> {

    List<EquipmentNumberRecordEntity> findByCondition(Integer[] ids, Integer equipmentNumberId);

}
