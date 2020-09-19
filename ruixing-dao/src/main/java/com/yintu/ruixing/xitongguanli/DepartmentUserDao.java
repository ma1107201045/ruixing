package com.yintu.ruixing.xitongguanli;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DepartmentUserDao {
    long countByExample(DepartmentUserEntityExample example);

    int deleteByExample(DepartmentUserEntityExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DepartmentUserEntity record);

    int insertSelective(DepartmentUserEntity record);

    List<DepartmentUserEntity> selectByExample(DepartmentUserEntityExample example);

    DepartmentUserEntity selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DepartmentUserEntity record, @Param("example") DepartmentUserEntityExample example);

    int updateByExample(@Param("record") DepartmentUserEntity record, @Param("example") DepartmentUserEntityExample example);

    int updateByPrimaryKeySelective(DepartmentUserEntity record);

    int updateByPrimaryKey(DepartmentUserEntity record);
}