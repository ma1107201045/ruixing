package com.yintu.ruixing.master.xitongguanli;

import com.yintu.ruixing.xitongguanli.RoleEntity;
import com.yintu.ruixing.xitongguanli.RoleEntityExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleDao {
    long countByExample(RoleEntityExample example);

    int deleteByExample(RoleEntityExample example);

    int deleteByPrimaryKey(Long id);

    int insert(RoleEntity record);

    int insertSelective(RoleEntity record);

    List<RoleEntity> selectByExample(RoleEntityExample example);

    RoleEntity selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") RoleEntity record, @Param("example") RoleEntityExample example);

    int updateByExample(@Param("record") RoleEntity record, @Param("example") RoleEntityExample example);

    int updateByPrimaryKeySelective(RoleEntity record);

    int updateByPrimaryKey(RoleEntity record);
}