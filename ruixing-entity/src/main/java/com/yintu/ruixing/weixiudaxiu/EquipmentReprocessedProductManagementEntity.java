package com.yintu.ruixing.weixiudaxiu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentReprocessedProductManagementEntity implements Serializable {
    private static final long serialVersionUID = -5100112449182904574L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private Date recordTime;

    private Date repairTime;

    private Date returnTime;

    private Integer equipmentNumberId;

    private String equipmentNumber;

    private String equipmentName;


}