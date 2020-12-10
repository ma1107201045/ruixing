package com.yintu.ruixing.guzhangzhenduan;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class QuDuanInfoEntityV2 implements Serializable {
    private static final long serialVersionUID = 1928680102275598019L;
    private Integer id;

    private Integer cid;

    private Integer qid;

    private Integer time;

    private Integer type;

    private Integer typeid;

    private Integer dataZhengchang;

    private String designCarrier;

    private Integer direction;

    private Integer signalPosition;

    private String gjcollection;

    private String djcollection;

    private String vInAll;

    private String mvInZhu;

    private String mvInBing;

    private String mvInDiaoZhu;

    private String mvInDiaoBing;

    private String hzInLowZhu;

    private String hzInLowBing;

    private Integer gjDriveZhu;

    private Integer gjDriveBing;

    private String gjRearCollectionZhu;

    private String gjRearCollectionBing;

    private Integer baojingZhu;

    private Integer baojingBing;

    private String v31;

    private String v32;

    private String v33;

    private String v34;

    private String v35;

    private String v36;

    private String v37;

    private String v38;

    private String v39;

    private String vOutZhu;

    private String vOutBei;

    private String maOutZhu;

    private String maOutBei;

    private String hzUpZhu;

    private String hzUpBei;

    private String hzDownZhu;

    private String hzDownBei;

    private String hzLowZhu;

    private String hzLowBei;

    private Integer fbjDriveZhu;

    private Integer fbjDriveBei;

    private String fbjCollectionZhu;

    private String fbjCollectionBei;

    private String vSongduanCable;

    private String maSongduanCable;

    private String vShouduanCableHost;

    private String vShouduanCableSpare;

    private String maShouduanCable;

    private String maCableJbp;

    private String aLonginJbp;

    private String aLongoutJbp;

    private String aShortinJbp;

    private String aShortoutJbp;

    private Integer tJbp;

    private String maCableFbp;

    private String aLonginFbp;

    private String aLongoutFbp;

    private String aShortinFbp;

    private String aShortoutFbp;

    private Integer tFbp;

    private String aLonginFbaZhu;

    private String aLonginFbaDiao;

    private String aLongoutFbaZhu;

    private String aLongoutFbaDiao;

    private String aShortinFbaZhu;

    private String aShortinFbaDiao;

    private String aShortoutFbaZhu;

    private String aShortoutFbaDiao;

    private String aLonginJbaZhu;

    private String aLonginJbaDiao;

    private String aLongoutJbaZhu;

    private String aLongoutJbaDiao;

    private String aShortinJbaZhu;

    private String aShortinJbaDiao;

    private String aShortoutJbaZhu;

    private String aShortoutJbaDiao;

    private String rackAddress;

    private String yuliu;

    private QuDuanBaseEntity quDuanBaseEntity;

    private SheBeiEntity sheBeiEntity;


}