package com.yintu.ruixing.danganguanli;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LineTechnologyStatusSystemManufacturerDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusSystemManufacturerEntity record);

    int insertSelective(LineTechnologyStatusSystemManufacturerEntity record);

    LineTechnologyStatusSystemManufacturerEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusSystemManufacturerEntity record);

    int updateByPrimaryKeyWithBLOBs(LineTechnologyStatusSystemManufacturerEntity record);

    int updateByPrimaryKey(LineTechnologyStatusSystemManufacturerEntity record);

    List<LineTechnologyStatusSystemManufacturerEntity> selectByExample(Integer[] ids, String name, Integer cid);

}