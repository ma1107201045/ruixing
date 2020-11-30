package com.yintu.ruixing.master.danganguanli;

import com.yintu.ruixing.danganguanli.LineTechnologyStatusProductEntity;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusProductEntityWithBLOBs;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface LineTechnologyStatusProductDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LineTechnologyStatusProductEntityWithBLOBs record);

    int insertSelective(LineTechnologyStatusProductEntityWithBLOBs record);

    LineTechnologyStatusProductEntityWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LineTechnologyStatusProductEntityWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(LineTechnologyStatusProductEntityWithBLOBs record);

    int updateByPrimaryKey(LineTechnologyStatusProductEntity record);

    List<LineTechnologyStatusProductEntityWithBLOBs> selectByExample(Integer[] ids, String name, Integer cid);
}