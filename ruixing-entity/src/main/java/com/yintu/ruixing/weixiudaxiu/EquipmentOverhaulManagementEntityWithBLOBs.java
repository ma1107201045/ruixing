package com.yintu.ruixing.weixiudaxiu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentOverhaulManagementEntityWithBLOBs extends EquipmentOverhaulManagementEntity {
    private static final long serialVersionUID = -1604665030018388910L;
    private String reason;

    private String evaluate;

    private String problemAnalysis;

    private EquipmentNumberEntity equipmentNumberEntity;
}