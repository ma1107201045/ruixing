package com.yintu.ruixing.jiejuefangan;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DesignLiaisonDao {
    int deleteByPrimaryKey(Integer id);

    int insert(DesignLiaisonEntity record);

    int insertSelective(DesignLiaisonEntity record);

    DesignLiaisonEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DesignLiaisonEntity record);

    int updateByPrimaryKey(DesignLiaisonEntity record);

    List<DesignLiaisonEntity> selectAll();

    List<DesignLiaisonEntity> selectByYear(Integer year);

    List<DesignLiaisonEntity> selectByExample(Integer year, String projectName);

    List<Integer> selectByDistinctProjectDate();
}