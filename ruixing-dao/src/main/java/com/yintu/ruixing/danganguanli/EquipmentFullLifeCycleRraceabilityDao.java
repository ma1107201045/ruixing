package com.yintu.ruixing.danganguanli;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/26 9:36
 */
@Mapper
public interface EquipmentFullLifeCycleRraceabilityDao {

    List<EquipmentFullLifeCycleRraceabilityEntity> selectEquipmentLife(Integer[] ids, String czName, String equipmentName);
}
