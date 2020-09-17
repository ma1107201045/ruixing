package com.yintu.ruixing.weixiudaxiu;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.weixiudaxiu.EquipmentOverhaulManagementEntity;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/7/28 16:32
 */
public interface EquipmentOverhaulManagementService extends BaseService<EquipmentOverhaulManagementEntityWithBLOBs, Integer> {

    void remove(Integer[] ids);

    List<EquipmentOverhaulManagementEntityWithBLOBs> findByCondition(String equipmentNumber);

}
