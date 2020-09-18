package com.yintu.ruixing.weixiudaxiu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentReprocessedProductManagementEntityWithBLOBs extends EquipmentReprocessedProductManagementEntity {
    private static final long serialVersionUID = 2755332817900398343L;

    private String userDemand;

    private String dispositionOpinion;



}