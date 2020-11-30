package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.LineTechnologyStatusProductModelNumberEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface LineTechnologyStatusProductModelNumberDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusProductModelNumberEntity record);

    int insertSelective(LineTechnologyStatusProductModelNumberEntity record);

    LineTechnologyStatusProductModelNumberEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusProductModelNumberEntity record);

    int updateByPrimaryKey(LineTechnologyStatusProductModelNumberEntity record);

    List<LineTechnologyStatusProductModelNumberEntity> selectAll();
}