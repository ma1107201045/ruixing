package com.yintu.ruixing.master.xitongguanli;

import java.util.List;

import com.yintu.ruixing.xitongguanli.AuditConfigurationEntity;
import com.yintu.ruixing.xitongguanli.AuditConfigurationEntityExample;
import org.apache.ibatis.annotations.Param;

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

    List<AuditConfigurationEntity> findAudit(short i, short i1, short i2);
}