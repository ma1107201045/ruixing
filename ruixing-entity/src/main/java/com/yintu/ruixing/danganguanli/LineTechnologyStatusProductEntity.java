package com.yintu.ruixing.danganguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineTechnologyStatusProductEntity implements Serializable {
    private static final long serialVersionUID = 2091054243950882869L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;
    @NotBlank
    private String name;

    private Integer quantity;

    private Integer modelNumberId;

    private Integer specificationId;

    private Integer cid;

    private LineTechnologyStatusProductModelNumberEntity lineTechnologyStatusProductModelNumberEntity;

    private LineTechnologyStatusProductSpecificationEntity lineTechnologyStatusProductSpecificationEntity;

}