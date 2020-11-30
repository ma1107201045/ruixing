package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.CustomerTypeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface CustomerTypeDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerTypeEntity record);

    int insertSelective(CustomerTypeEntity record);

    CustomerTypeEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerTypeEntity record);

    int updateByPrimaryKey(CustomerTypeEntity record);

    List<CustomerTypeEntity> selectAll();
}