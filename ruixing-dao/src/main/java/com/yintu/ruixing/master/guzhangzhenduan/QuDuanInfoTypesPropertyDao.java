package com.yintu.ruixing.master.guzhangzhenduan;

import com.yintu.ruixing.guzhangzhenduan.QuDuanInfoTypesPropertyEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface QuDuanInfoTypesPropertyDao {
    int deleteByPrimaryKey(Integer id);

    int insert(QuDuanInfoTypesPropertyEntity record);

    int insertSelective(QuDuanInfoTypesPropertyEntity record);

    QuDuanInfoTypesPropertyEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QuDuanInfoTypesPropertyEntity record);

    int updateByPrimaryKey(QuDuanInfoTypesPropertyEntity record);

    List<QuDuanInfoTypesPropertyEntity> connectSelectByCondition(String types);

    String countByType(List<Integer> types);

}