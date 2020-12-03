package com.yintu.ruixing.xitongguanli;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/26 14:12
 */
public interface AuditConfigurationService extends BaseService<AuditConfigurationEntity, Long> {

    void add(AuditConfigurationEntity auditConfigurationEntity, Long[] roleIds);

    void remove(AuditConfigurationEntityExample auditConfigurationEntityExample);

    void edit(AuditConfigurationEntity auditConfigurationEntity, Long[] roleIds);

    List<AuditConfigurationEntity> findByExample(AuditConfigurationEntityExample auditConfigurationEntityExample);


    void addOrEditAuditConfigurationUser(Long id, Long[] roleId, String loginUserName);

    List<AuditConfigurationEntity> findByExample(Short nameId, Short status, Short model);

}
