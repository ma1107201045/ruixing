package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.DistrictEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineTechnologyStatusSystemManufacturerEntity implements Serializable {
    private static final long serialVersionUID = -7392929883640015480L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;
    @NotBlank
    private String name;

    private String factory;

    private Integer provinceId;

    private Integer cityId;

    private Integer districtId;

    private String detailedAddress;

    private String contactPerson;

    private String contactPhone;

    private String remark;

    private Integer cid;

    private DistrictEntity provinceEntity;

    private DistrictEntity cityEntity;

    private DistrictEntity districtEntity;

}