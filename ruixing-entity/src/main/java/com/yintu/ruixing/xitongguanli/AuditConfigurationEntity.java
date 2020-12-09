package com.yintu.ruixing.xitongguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditConfigurationEntity implements Serializable {
    private static final long serialVersionUID = 3169526838873443366L;
    private Long id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private Short nameId;

    private Short status;

    private Short model;

    List<AuditConfigurationUserEntity> auditConfigurationUserEntities;

    List<List<UserEntity>> list;


}