package com.yintu.ruixing.xitongguanli;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/26 14:27
 */
public interface AuditConfigurationRoleService extends BaseService<AuditConfigurationRoleEntity, Long> {

    void remove(AuditConfigurationRoleEntityExample auditConfigurationRoleEntityExample);

    List<AuditConfigurationRoleEntity> findByExample(AuditConfigurationRoleEntityExample auditConfigurationRoleEntityExample);

    List<RoleEntity> findRoleById(Long auditConfigurationId);
}


