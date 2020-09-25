package com.yintu.ruixing.danganguanli;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LineTechnologyStatusProductModelNumberDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusProductModelNumberEntity record);

    int insertSelective(LineTechnologyStatusProductModelNumberEntity record);

    LineTechnologyStatusProductModelNumberEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusProductModelNumberEntity record);

    int updateByPrimaryKey(LineTechnologyStatusProductModelNumberEntity record);

    List<LineTechnologyStatusProductModelNumberEntity> selectAll();
}