package com.yintu.ruixing.weixiudaxiu;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.weixiudaxiu.EquipmentReprocessedProductManagementEntity;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/8/3 16:28
 */
public interface EquipmentReprocessedProductManagementService extends BaseService<EquipmentReprocessedProductManagementEntityWithBLOBs, Integer> {


    void remove(Integer[] ids);

    List<EquipmentReprocessedProductManagementEntityWithBLOBs> findByCondition(String equipmentNumber, String equipmentName);

}
