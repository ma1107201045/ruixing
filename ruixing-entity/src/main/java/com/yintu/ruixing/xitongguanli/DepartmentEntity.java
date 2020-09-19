package com.yintu.ruixing.xitongguanli;

import com.yintu.ruixing.danganguanli.CustomerUnitsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentEntity implements Serializable {
    private static final long serialVersionUID = -1781483341931616568L;
    private Long id;
    @NotNull
    private Long parentId;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;
    @NotBlank
    private String name;

}