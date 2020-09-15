package com.yintu.ruixing.weixiudaxiu;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EquipmentIndexAnalysisDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentIndexAnalysisEntity record);

    int insertSelective(EquipmentIndexAnalysisEntity record);

    EquipmentIndexAnalysisEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentIndexAnalysisEntity record);

    int updateByPrimaryKey(EquipmentIndexAnalysisEntity record);

    List<EquipmentIndexAnalysisEntity> selectEquipmentByCondition(String equipmentNumber);

    List<EquipmentIndexAnalysisEntity> selectQuduanByCondition(String quDuanYunYingName);


}