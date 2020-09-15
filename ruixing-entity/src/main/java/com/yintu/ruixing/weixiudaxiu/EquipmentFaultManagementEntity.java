package com.yintu.ruixing.weixiudaxiu;

import com.yintu.ruixing.guzhangzhenduan.CheZhanEntity;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuEntity;
import com.yintu.ruixing.guzhangzhenduan.XianDuanEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentFaultManagementEntity implements Serializable {
    private static final long serialVersionUID = -9049983778072100836L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private Date statisticsData;

    private Integer railwaysBureauId;

    private Integer signalDepotId;

    private Integer specialRailwayLineId;

    private Integer stationId;

    private String callPolice;

    private String faultData;

    private Integer equipmentNumberId;

    private EquipmentNumberEntity equipmentNumberEntity;

    private TieLuJuEntity tieLuJuEntity;

    private DianWuDuanEntity dianWuDuanEntity;

    private XianDuanEntity xianDuanEntity;

    private CheZhanEntity cheZhanEntity;

}