package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentFullLifeCycleRraceabilityEntity;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/26 9:36
 */

public interface EquipmentFullLifeCycleRraceabilityDao {

    List<EquipmentFullLifeCycleRraceabilityEntity> selectEquipmentLife(Integer[] ids, String czName, String equipmentName);
}
