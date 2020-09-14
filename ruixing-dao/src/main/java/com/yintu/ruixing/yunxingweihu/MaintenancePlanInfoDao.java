package com.yintu.ruixing.yunxingweihu;

import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface MaintenancePlanInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MaintenancePlanInfoEntity record);

    int insertSelective(MaintenancePlanInfoEntity record);

    MaintenancePlanInfoEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MaintenancePlanInfoEntity record);

    int updateByPrimaryKeyWithBLOBs(MaintenancePlanInfoEntity record);

    int updateByPrimaryKey(MaintenancePlanInfoEntity record);

    void insertMuch(List<MaintenancePlanInfoEntity> maintenancePlanInfoEntities);

    List<MaintenancePlanInfoEntity> selectByCondition(Integer[] ids, String context, Integer maintenancePlanId, Date date);

}