package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.CustomerDutyEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface CustomerDutyDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerDutyEntity record);

    int insertSelective(CustomerDutyEntity record);

    CustomerDutyEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerDutyEntity record);

    int updateByPrimaryKey(CustomerDutyEntity record);

    List<CustomerDutyEntity> selectByExample(String name);
}