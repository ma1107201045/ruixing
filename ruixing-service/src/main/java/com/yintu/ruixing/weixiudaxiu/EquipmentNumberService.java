package com.yintu.ruixing.weixiudaxiu;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.weixiudaxiu.EquipmentNumberEntity;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/7/13 11:06
 */
public interface EquipmentNumberService extends BaseService<EquipmentNumberEntity, Integer> {

    void remove(Integer[] ids);

    List<EquipmentNumberEntity> findByCondition(Integer[] ids, String equipmentNumber);

}
