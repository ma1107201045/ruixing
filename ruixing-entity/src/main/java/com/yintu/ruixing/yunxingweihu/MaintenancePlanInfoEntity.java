package com.yintu.ruixing.yunxingweihu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenancePlanInfoEntity implements Serializable {
    private static final long serialVersionUID = -1298317307341852171L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private Short isFinish;

    private String documentFiles;

    private Integer maintenancePlanId;

    private String context;

    private MaintenancePlanEntity maintenancePlanEntity;

}