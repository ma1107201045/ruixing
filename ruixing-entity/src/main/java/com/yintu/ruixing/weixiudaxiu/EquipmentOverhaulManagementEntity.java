package com.yintu.ruixing.weixiudaxiu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentOverhaulManagementEntity implements Serializable {
    private static final long serialVersionUID = 1043221803387269161L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;
    @NotNull
    private Integer amount;
    @NotNull
    private Integer equipmentNumberId;

    private EquipmentNumberEntity equipmentNumberEntity;

}