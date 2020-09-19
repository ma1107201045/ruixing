package com.yintu.ruixing.xitongguanli;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface PermissionRoleDao {
    long countByExample(PermissionRoleEntityExample example);

    int deleteByExample(PermissionRoleEntityExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PermissionRoleEntity record);

    int insertSelective(PermissionRoleEntity record);

    List<PermissionRoleEntity> selectByExample(PermissionRoleEntityExample example);

    PermissionRoleEntity selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PermissionRoleEntity record, @Param("example") PermissionRoleEntityExample example);

    int updateByExample(@Param("record") PermissionRoleEntity record, @Param("example") PermissionRoleEntityExample example);

    int updateByPrimaryKeySelective(PermissionRoleEntity record);

    int updateByPrimaryKey(PermissionRoleEntity record);
}