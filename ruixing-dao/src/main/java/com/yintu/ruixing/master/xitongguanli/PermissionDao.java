package com.yintu.ruixing.master.xitongguanli;

import com.yintu.ruixing.xitongguanli.PermissionEntity;
import com.yintu.ruixing.xitongguanli.PermissionEntity;
import com.yintu.ruixing.xitongguanli.PermissionEntityExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface PermissionDao {
    long countByExample(PermissionEntityExample example);

    int deleteByExample(PermissionEntityExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PermissionEntity record);

    int insertSelective(PermissionEntity record);

    List<PermissionEntity> selectByExample(PermissionEntityExample example);

    PermissionEntity selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PermissionEntity record, @Param("example") PermissionEntityExample example);

    int updateByExample(@Param("record") PermissionEntity record, @Param("example") PermissionEntityExample example);

    int updateByPrimaryKeySelective(PermissionEntity record);

    int updateByPrimaryKey(PermissionEntity record);

    List<PermissionEntity> selectPermissionAndRole();

}