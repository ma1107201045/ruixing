package com.yintu.ruixing.master.yunxingweihu;

import com.yintu.ruixing.yunxingweihu.SparePartsInfoEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;


public interface SparePartsInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SparePartsInfoEntity record);

    int insertSelective(SparePartsInfoEntity record);

    SparePartsInfoEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SparePartsInfoEntity record);

    int updateByPrimaryKeyWithBLOBs(SparePartsInfoEntity record);

    int updateByPrimaryKey(SparePartsInfoEntity record);

    void insertMuch(List<SparePartsInfoEntity> sparePartsInfoEntities);

    List<SparePartsInfoEntity> selectByCondition(Integer[] ids, String context, Integer sparePartsId, Date date);

}