package com.yintu.ruixing.dao;

import com.github.pagehelper.Page;
import com.yintu.ruixing.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author:lcy
 * @date:2020-05-21 11
 * 数据统计
 */
@Mapper
public interface DataStatsDao {

    public List<DataStats> findAll();

    List<DataStats> getByPage();


}
