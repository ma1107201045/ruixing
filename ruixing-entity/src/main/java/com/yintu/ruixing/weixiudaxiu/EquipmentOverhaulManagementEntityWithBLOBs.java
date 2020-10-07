package com.yintu.ruixing.weixiudaxiu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentOverhaulManagementEntityWithBLOBs extends EquipmentOverhaulManagementEntity {
    private static final long serialVersionUID = -1604665030018388910L;
    private String reason;

    private String evaluate;

    private String problemAnalysis;


}