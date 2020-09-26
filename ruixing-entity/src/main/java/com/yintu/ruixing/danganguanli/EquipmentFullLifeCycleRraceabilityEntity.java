package com.yintu.ruixing.danganguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/26 9:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentFullLifeCycleRraceabilityEntity implements Serializable {
    private static final long serialVersionUID = 2645828415484258007L;

    private Integer id;

    private String czName;

    private String equipmentName;

    private String equipmentNumber;

    private Long reprocessedProductCount;
}
