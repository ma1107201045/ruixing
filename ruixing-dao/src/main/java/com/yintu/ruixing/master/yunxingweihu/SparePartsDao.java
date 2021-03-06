package com.yintu.ruixing.master.yunxingweihu;

import com.yintu.ruixing.yunxingweihu.SparePartsEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface SparePartsDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SparePartsEntity record);

    int insertSelective(SparePartsEntity record);

    SparePartsEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SparePartsEntity record);

    int updateByPrimaryKeyWithBLOBs(SparePartsEntity record);

    int updateByPrimaryKey(SparePartsEntity record);

    void muchInsert(List<SparePartsEntity> sparePartsEntities);

    List<SparePartsEntity> selectByExample(Integer[] ids, String name);
}