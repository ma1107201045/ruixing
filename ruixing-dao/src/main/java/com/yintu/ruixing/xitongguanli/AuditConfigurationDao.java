package com.yintu.ruixing.xitongguanli;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuditConfigurationDao {
    long countByExample(AuditConfigurationEntityExample example);

    int deleteByExample(AuditConfigurationEntityExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AuditConfigurationEntity record);

    int insertSelective(AuditConfigurationEntity record);

    List<AuditConfigurationEntity> selectByExample(AuditConfigurationEntityExample example);

    AuditConfigurationEntity selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AuditConfigurationEntity record, @Param("example") AuditConfigurationEntityExample example);

    int updateByExample(@Param("record") AuditConfigurationEntity record, @Param("example") AuditConfigurationEntityExample example);

    int updateByPrimaryKeySelective(AuditConfigurationEntity record);

    int updateByPrimaryKey(AuditConfigurationEntity record);
}