package com.yintu.ruixing.master.common;

import com.yintu.ruixing.common.AuditTotalEntity;

public interface AuditTotalDao1 {
    int deleteByPrimaryKey(Integer id);

    int insert(AuditTotalEntity record);

    int insertSelective(AuditTotalEntity record);

    AuditTotalEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AuditTotalEntity record);

    int updateByPrimaryKey(AuditTotalEntity record);
}