package com.yintu.ruixing.xitongguanli;

import com.yintu.ruixing.xitongguanli.AuditConfigurationUserEntity;
import com.yintu.ruixing.xitongguanli.AuditConfigurationUserEntityExample;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuditConfigurationUserDao {
    long countByExample(AuditConfigurationUserEntityExample example);

    int deleteByExample(AuditConfigurationUserEntityExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AuditConfigurationUserEntity record);

    int insertSelective(AuditConfigurationUserEntity record);

    List<AuditConfigurationUserEntity> selectByExample(AuditConfigurationUserEntityExample example);

    AuditConfigurationUserEntity selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AuditConfigurationUserEntity record, @Param("example") AuditConfigurationUserEntityExample example);

    int updateByExample(@Param("record") AuditConfigurationUserEntity record, @Param("example") AuditConfigurationUserEntityExample example);

    int updateByPrimaryKeySelective(AuditConfigurationUserEntity record);

    int updateByPrimaryKey(AuditConfigurationUserEntity record);
}