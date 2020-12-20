package com.yintu.ruixing.xitongguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionEntity implements Serializable {

    private static final long serialVersionUID = -2742251652831479682L;
    private Long id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;
    @NotNull
    private Long parentId;
    @NotBlank
    private String name;

    private String url;

    private String method;

    private Short isMenu;

    private String path;

    private String iconCls;

    private String description;

    private String pathName;

    private List<RoleEntity> roleEntities;

}