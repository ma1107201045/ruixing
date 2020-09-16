package com.yintu.ruixing.weixiudaxiu;

import com.yintu.ruixing.common.DistrictEntity;
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
public class EquipmentSparePartsManagementEntity implements Serializable {
    private static final long serialVersionUID = 7878081904159054884L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;
    @NotBlank
    private String materialNumber;
    @NotBlank
    private String equipmentName;
    @NotNull
    private Integer inventoryAmount;
    @NotNull
    private Integer thresholdAmount;
    @NotNull
    private Integer provinceId;
    @NotNull
    private Integer cityId;
    @NotNull
    private Integer districtId;

    private String detailedAddress;

    private String contactPerson;
    @NotNull
    private String contactPhone;

    private DistrictEntity provinceEntity;

    private DistrictEntity cityEntity;

    private DistrictEntity districtEntity;

}