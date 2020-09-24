package com.yintu.ruixing.danganguanli;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LineTechnologyStatusProductDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusProductEntityWithBLOBs record);

    int insertSelective(LineTechnologyStatusProductEntityWithBLOBs record);

    LineTechnologyStatusProductEntityWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusProductEntityWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(LineTechnologyStatusProductEntityWithBLOBs record);

    int updateByPrimaryKey(LineTechnologyStatusProductEntity record);
}