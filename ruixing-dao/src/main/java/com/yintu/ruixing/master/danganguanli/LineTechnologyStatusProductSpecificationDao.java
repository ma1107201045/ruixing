package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.LineTechnologyStatusProductSpecificationEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface LineTechnologyStatusProductSpecificationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusProductSpecificationEntity record);

    int insertSelective(LineTechnologyStatusProductSpecificationEntity record);

    LineTechnologyStatusProductSpecificationEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusProductSpecificationEntity record);

    int updateByPrimaryKey(LineTechnologyStatusProductSpecificationEntity record);

    List<LineTechnologyStatusProductSpecificationEntity> selectAll();
}