package com.yintu.ruixing.xitongguanli;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/26 14:12
 */
public interface AuditConfigurationService extends BaseService<AuditConfigurationEntity, Long> {

    void add(AuditConfigurationEntity auditConfigurationEntity, Long[] userIds);

    void remove(AuditConfigurationEntityExample auditConfigurationEntityExample);

    void edit(AuditConfigurationEntity auditConfigurationEntity, Long[] userIds);

    List<AuditConfigurationEntity> findByExample(AuditConfigurationEntityExample auditConfigurationEntityExample);

    void addOrEditAuditConfigurationUser(Long id, Long[] userId, String loginUserName);
}
