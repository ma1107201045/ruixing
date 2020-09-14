package com.yintu.ruixing.weixiudaxiu;

import com.yintu.ruixing.guzhangzhenduan.CheZhanEntity;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuEntity;
import com.yintu.ruixing.guzhangzhenduan.XianDuanEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentNumberEntity implements Serializable {
    private static final long serialVersionUID = 439398084879791102L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private Integer railwaysBureauId;

    private Integer signalDepotId;

    private Integer specialRailwayLineId;

    private Integer stationId;

    private String rackNumber;

    private String hierarchy;
    @NotBlank
    private String equipmentNumber;

    private String name;

    private Integer quantity;

    private String configuration;

    private TieLuJuEntity tieLuJuEntity;

    private DianWuDuanEntity dianWuDuanEntity;

    private XianDuanEntity xianDuanEntity;

    private CheZhanEntity cheZhanEntity;

    private EquipmentEntity equipmentEntity;

}