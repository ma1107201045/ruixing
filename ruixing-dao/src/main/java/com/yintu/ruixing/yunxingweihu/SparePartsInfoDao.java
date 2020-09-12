package com.yintu.ruixing.yunxingweihu;

import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface SparePartsInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SparePartsInfoEntity record);

    int insertSelective(SparePartsInfoEntity record);

    SparePartsInfoEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SparePartsInfoEntity record);

    int updateByPrimaryKeyWithBLOBs(SparePartsInfoEntity record);

    int updateByPrimaryKey(SparePartsInfoEntity record);

    void insertMuch(List<SparePartsInfoEntity> sparePartsInfoEntities);

    List<SparePartsInfoEntity> selectByCondition(Integer[] ids, Integer sparePartsId, Date date);

}