package com.yintu.ruixing.weixiudaxiu;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EquipmentIndexAnalysisDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentIndexAnalysisEntity record);

    int insertSelective(EquipmentIndexAnalysisEntity record);

    EquipmentIndexAnalysisEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentIndexAnalysisEntity record);

    int updateByPrimaryKey(EquipmentIndexAnalysisEntity record);
}