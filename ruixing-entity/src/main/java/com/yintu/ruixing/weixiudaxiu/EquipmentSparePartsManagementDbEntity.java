package com.yintu.ruixing.weixiudaxiu;

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
public class EquipmentSparePartsManagementDbEntity implements Serializable {
    private static final long serialVersionUID = 3348372375784408525L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private String operator;

    private Integer quantity;

    private Integer provinceId;

    private Integer cityId;

    private Integer districtId;

    private String detailedAddress;

    private String contactPerson;

    private String contactPhone;

    private DistrictEntity provinceEntity;

    private DistrictEntity cityEntity;

    private DistrictEntity districtEntity;

    private Integer equipmentNumberId;

    private String equipmentNumber;

    private Integer equipmentSparePartsManagementId;

    private String equipmentName;

}