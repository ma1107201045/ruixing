package com.yintu.ruixing.xitongguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditConfigurationUserEntity implements Serializable {
    private static final long serialVersionUID = -5177049644592697332L;
    private Long id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private Long auditConfigurationId;

    private Long roleId;

    private Long userId;

    private Integer sort;

}