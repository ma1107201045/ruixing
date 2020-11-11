package com.yintu.ruixing.xitongguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionRoleEntity implements Serializable {
    private static final long serialVersionUID = -4532159389679732714L;
    private Long id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private Long permissionId;

    private Long roleId;


}