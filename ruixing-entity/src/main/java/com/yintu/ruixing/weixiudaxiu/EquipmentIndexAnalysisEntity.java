package com.yintu.ruixing.weixiudaxiu;

import com.yintu.ruixing.guzhangzhenduan.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentIndexAnalysisEntity implements Serializable {
    private static final long serialVersionUID = -702288365972713011L;

    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private Date statisticsData;

    private Short type;

    private Integer equipmentNumberId;

    private String equipmentMttf;

    private Integer railwaysBureauId;

    private Integer signalDepotId;

    private Integer specialRailwayLineId;

    private Integer stationId;

    private Integer quduanId;

    private String stationMtbf;

    private String quduanMtbf;

    private String interiorFaultMttr;

    private String outdoorFaultMttr;

    private TieLuJuEntity tieLuJuEntity;

    private DianWuDuanEntity dianWuDuanEntity;

    private XianDuanEntity xianDuanEntity;

    private CheZhanEntity cheZhanEntity;

    private QuDuanBaseEntity quDuanBaseEntity;

}