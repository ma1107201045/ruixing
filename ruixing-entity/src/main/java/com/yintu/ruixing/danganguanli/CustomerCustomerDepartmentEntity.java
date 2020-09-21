package com.yintu.ruixing.danganguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCustomerDepartmentEntity implements Serializable {
    private static final long serialVersionUID = -6033416116857374850L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private Integer customerId;

    private Integer departmentId;

    private Integer customerAuditRecordId;

    private Integer customerAuditRecordDepartmentId;


}