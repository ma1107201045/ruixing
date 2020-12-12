package com.yintu.ruixing.xitongguanli;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.common.util.TreeNodeUtil;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/26 14:12
 */
public interface AuditConfigurationService extends BaseService<AuditConfigurationEntity, Long> {

    void add(AuditConfigurationEntity auditConfigurationEntity, Long[] roles, Long[] userIds, Integer[] sorts);

    void remove(AuditConfigurationEntityExample auditConfigurationEntityExample);

    void edit(AuditConfigurationEntity auditConfigurationEntity, Long[] roles, Long[] userIds, Integer[] sorts);

    List<AuditConfigurationEntity> findByExample(AuditConfigurationEntityExample auditConfigurationEntityExample);

    void addOrEditAuditConfigurationUser(Long id, Long[] roles, Long[] userIds, Integer[] sorts, String loginUserName);

    List<AuditConfigurationEntity> findByExample(Short nameId, Short status, Short model);

    List<TreeNodeUtil> findTree();

    List<List<TreeNodeUtil>> findTreeById(Long id);

    List<AuditConfigurationEntity> findAudit(short i, short i1, short i2);
}
