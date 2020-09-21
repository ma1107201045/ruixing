package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.DistrictEntity;
import com.yintu.ruixing.xitongguanli.UserEntity;
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
public class CustomerEntity implements Serializable {
    private static final long serialVersionUID = -2659080359990333071L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;
    @NotNull
    private Integer typeId;
    @NotNull
    private Integer dutyId;
    @NotBlank
    private String name;

    private String phone;

    private String specialPlane;

    private String email;
    @NotNull
    private Integer provinceId;
    @NotNull
    private Integer cityId;
    @NotNull
    private Integer districtId;

    private String detailedAddress;

    private Short status;

    private CustomerTypeEntity customerTypeEntity;

    private List<CustomerDepartmentEntity> customerDepartmentEntities;

    private CustomerDutyEntity customerDutyEntity;

    private DistrictEntity provinceEntity;

    private DistrictEntity cityEntity;

    private DistrictEntity districtEntity;

    private UserEntity userEntity;

}