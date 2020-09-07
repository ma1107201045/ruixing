package com.yintu.ruixing.guzhangzhenduan;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuDuanInfoPropertyDao {
    int deleteByPrimaryKey(Integer id);

    int insert(QuDuanInfoPropertyEntity record);

    int insertSelective(QuDuanInfoPropertyEntity record);

    QuDuanInfoPropertyEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QuDuanInfoPropertyEntity record);

    int updateByPrimaryKey(QuDuanInfoPropertyEntity record);

    List<QuDuanInfoPropertyEntity> selectByType(Short type);

    List<QuDuanInfoPropertyEntity> selectByIds(Integer[] ids);

    List<QuDuanInfoPropertyEntity> selectByName(String name);
}