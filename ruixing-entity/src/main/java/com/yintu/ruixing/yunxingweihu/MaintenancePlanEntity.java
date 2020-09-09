package com.yintu.ruixing.yunxingweihu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenancePlanEntity implements Serializable {
    private static final long serialVersionUID = 2779962063256506567L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;
    @NotBlank
    private String name;

    private Integer railwaysBureauId;

    private Integer signalDepotId;

    private Integer specialRailwayLineId;

    private Integer stationId;

    private Short executionMode;

    private Date executionTime;

    private Short cycleType;

    private String cycleValue;

    private String cycleDescription;

    private Short isStart;

    private String context;


}