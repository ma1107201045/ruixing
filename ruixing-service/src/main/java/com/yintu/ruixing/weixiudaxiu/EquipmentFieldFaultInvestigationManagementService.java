package com.yintu.ruixing.weixiudaxiu;

import com.yintu.ruixing.common.util.BaseService;

import java.util.Date;
import java.util.List;

/**
 * @author:mlf
 * @date:2020/7/30 19:22
 */
public interface EquipmentFieldFaultInvestigationManagementService extends BaseService<EquipmentFieldFaultInvestigationManagementEntity, Integer> {
    void remove(Integer[] ids);

    List<EquipmentFieldFaultInvestigationManagementEntity> findByCondition(Date startDate, Date endDate);

}
