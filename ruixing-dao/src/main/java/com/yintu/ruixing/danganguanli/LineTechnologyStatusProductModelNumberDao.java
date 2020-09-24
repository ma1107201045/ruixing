package com.yintu.ruixing.danganguanli;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LineTechnologyStatusProductModelNumberDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusProductModelNumberEntity record);

    int insertSelective(LineTechnologyStatusProductModelNumberEntity record);

    LineTechnologyStatusProductModelNumberEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusProductModelNumberEntity record);

    int updateByPrimaryKey(LineTechnologyStatusProductModelNumberEntity record);
}