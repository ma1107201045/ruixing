package com.yintu.ruixing.master.weixiudaxiu;

import com.yintu.ruixing.weixiudaxiu.EquipmentFaultManagementEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

public interface EquipmentFaultManagementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentFaultManagementEntity record);

    int insertSelective(EquipmentFaultManagementEntity record);

    EquipmentFaultManagementEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentFaultManagementEntity record);

    int updateByPrimaryKey(EquipmentFaultManagementEntity record);

    List<EquipmentFaultManagementEntity> selectByCondition(Date startDate,Date endDate);
}