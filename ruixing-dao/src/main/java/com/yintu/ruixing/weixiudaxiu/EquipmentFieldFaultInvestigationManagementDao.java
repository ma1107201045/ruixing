package com.yintu.ruixing.weixiudaxiu;

import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface EquipmentFieldFaultInvestigationManagementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentFieldFaultInvestigationManagementEntity record);

    int insertSelective(EquipmentFieldFaultInvestigationManagementEntity record);

    EquipmentFieldFaultInvestigationManagementEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentFieldFaultInvestigationManagementEntity record);

    int updateByPrimaryKeyWithBLOBs(EquipmentFieldFaultInvestigationManagementEntity record);

    int updateByPrimaryKey(EquipmentFieldFaultInvestigationManagementEntity record);

    List<EquipmentFieldFaultInvestigationManagementEntity> selectByCondition(Integer[] ids, Date startDate, Date endDate);
}