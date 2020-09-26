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
public class AuditConfigurationEntity implements Serializable {
    private static final long serialVersionUID = 6526899275441329948L;
    private Long id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;
    @NotBlank
    private String name;
    @NotNull
    private Long departmentId;

    private String workContext;

    private Short status;

    private DepartmentEntity departmentEntity;

    private List<UserEntity> userEntities;

}