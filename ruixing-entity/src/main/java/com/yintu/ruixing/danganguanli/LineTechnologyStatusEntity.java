package com.yintu.ruixing.danganguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineTechnologyStatusEntity implements Serializable {

    private static final long serialVersionUID = 4988773799714755203L;

    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private String managementUnit;

    private String deviceType;

    private String skillShaftCount;

    private String safetyInformation;

    private String technologyStatus;

    private Date openDate;

    private Date guaranteePeriod;

    private Integer xid;

}