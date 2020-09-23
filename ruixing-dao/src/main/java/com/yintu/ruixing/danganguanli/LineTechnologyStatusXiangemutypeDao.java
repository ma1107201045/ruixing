package com.yintu.ruixing.danganguanli;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LineTechnologyStatusXiangemutypeDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusXiangemutypeEntity record);

    int insertSelective(LineTechnologyStatusXiangemutypeEntity record);

    LineTechnologyStatusXiangemutypeEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusXiangemutypeEntity record);

    int updateByPrimaryKey(LineTechnologyStatusXiangemutypeEntity record);

    List<LineTechnologyStatusXiangemutypeEntity> selectByExample(Integer lineTechnologyStatusId);
}