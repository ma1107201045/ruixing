package com.yintu.ruixing.master.xitongguanli;

import com.yintu.ruixing.xitongguanli.AuditConfigurationRoleEntity;
import com.yintu.ruixing.xitongguanli.AuditConfigurationRoleEntityExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface AuditConfigurationRoleDao {
    long countByExample(AuditConfigurationRoleEntityExample example);

    int deleteByExample(AuditConfigurationRoleEntityExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AuditConfigurationRoleEntity record);

    int insertSelective(AuditConfigurationRoleEntity record);

    List<AuditConfigurationRoleEntity> selectByExample(AuditConfigurationRoleEntityExample example);

    AuditConfigurationRoleEntity selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AuditConfigurationRoleEntity record, @Param("example") AuditConfigurationRoleEntityExample example);

    int updateByExample(@Param("record") AuditConfigurationRoleEntity record, @Param("example") AuditConfigurationRoleEntityExample example);

    int updateByPrimaryKeySelective(AuditConfigurationRoleEntity record);

    int updateByPrimaryKey(AuditConfigurationRoleEntity record);
}