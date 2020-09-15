package com.yintu.ruixing.weixiudaxiu;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.weixiudaxiu.EquipmentFaultManagementEntity;

import java.util.Date;
import java.util.List;

/**
 * @author:mlf
 * @date:2020/7/29 16:26
 */
public interface EquipmentFaultManagementService extends BaseService<EquipmentFaultManagementEntity, Integer> {
    List<EquipmentFaultManagementEntity> findByCondition(Date startDate, Date endDate);
}
