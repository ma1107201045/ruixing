package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiCheZhanXiangMuTypeEntity;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineBaseInformationEntity implements Serializable {
    private static final long serialVersionUID = -4440508152894094017L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private String name;

    private String shortName;

    private Double length;

    private Integer stationNum;

    private Integer quduanNum;

    private Integer dianmahuaNum;

    private Integer axlePoints;

    private Integer infoNum;

    private String technologyState;

    private Date openTime;

    private String guaranteePeriodInformation;

    private String auxiliaryProductInformation;

    private String manufacturerInformation;

    private String modifySituation;

    private String version;

    private Integer xiangmutypeId;

    private Integer tid;

    private TieLuJuEntity tieLuJuEntity;

    private AnZhuangTiaoShiCheZhanXiangMuTypeEntity anZhuangTiaoShiCheZhanXiangMuTypeEntity;

    private List<DianWuDuanEntity> dianWuDuanEntities;
}