package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.DistrictEntity;
import com.yintu.ruixing.xitongguanli.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAuditRecordEntity implements Serializable {
    private static final long serialVersionUID = 7302291123391423481L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private String operator;

    private Integer userId;

    private Integer typeId;

    private Integer dutyId;

    private String name;

    private String phone;

    private String specialPlane;

    private String email;

    private Integer provinceId;

    private Integer cityId;

    private Integer districtId;

    private String detailedAddress;

    private Integer customerId;

    private Short auditStatus;

    private Date auditFinishTime;


    private CustomerTypeEntity customerTypeEntity;

    private List<CustomerDepartmentEntity> customerDepartmentEntities;

    private CustomerDutyEntity customerDutyEntity;

    private DistrictEntity provinceEntity;

    private DistrictEntity cityEntity;

    private DistrictEntity districtEntity;

    private UserEntity userEntity;

}