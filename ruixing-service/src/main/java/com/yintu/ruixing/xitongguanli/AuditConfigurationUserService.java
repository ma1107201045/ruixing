package com.yintu.ruixing.xitongguanli;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/26 14:27
 */
public interface AuditConfigurationUserService extends BaseService<AuditConfigurationUserEntity, Long> {

    void remove(AuditConfigurationUserEntityExample auditConfigurationUserEntityExample);

    List<AuditConfigurationUserEntity> findByExample(AuditConfigurationUserEntityExample auditConfigurationUserEntityExample);

    List<UserEntity> findUsersById(Long auditConfigurationId);

    List<Long> findDistinctFieldByExample(String field, Long auditConfigurationId, Integer sort);

    List<AuditConfigurationUserEntity> findByauditConfigurationId(Long id);
}


