package com.yintu.ruixing.weixiudaxiu;

import com.yintu.ruixing.guzhangzhenduan.CheZhanEntity;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuEntity;
import com.yintu.ruixing.guzhangzhenduan.XianDuanEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentFieldFaultInvestigationManagementEntity implements Serializable {
    private static final long serialVersionUID = -4270334940206970752L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;
    @NotNull
    private Integer railwaysBureauId;
    @NotNull
    private Integer signalDepotId;
    @NotNull
    private Integer specialRailwayLineId;
    @NotNull
    private Integer stationId;
    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;

    private String meetingTheme;

    private String documentName;

    private String documentFile;

    private String faultData;

    private String meetingContext;

    private TieLuJuEntity tieLuJuEntity;

    private DianWuDuanEntity dianWuDuanEntity;

    private XianDuanEntity xianDuanEntity;

    private CheZhanEntity cheZhanEntity;
}