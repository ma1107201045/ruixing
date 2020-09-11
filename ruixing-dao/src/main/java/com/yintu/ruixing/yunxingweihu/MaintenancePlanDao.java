package com.yintu.ruixing.yunxingweihu;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MaintenancePlanDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MaintenancePlanEntity record);

    int insertSelective(MaintenancePlanEntity record);

    MaintenancePlanEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MaintenancePlanEntity record);

    int updateByPrimaryKeyWithBLOBs(MaintenancePlanEntity record);

    int updateByPrimaryKey(MaintenancePlanEntity record);

    void muchInsert(List<MaintenancePlanEntity> maintenancePlanEntities);

    List<MaintenancePlanEntity> selectByExample(Integer[] ids, String name);
}