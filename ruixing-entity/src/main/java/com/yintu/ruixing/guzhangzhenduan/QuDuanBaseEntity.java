package com.yintu.ruixing.guzhangzhenduan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuDuanBaseEntity implements Serializable {
    private static final long serialVersionUID = -8897336182977509111L;
    private Integer id;

    private Integer parentId;

    private Integer xid;//线段id

    private Integer cid;//车站id

    private Integer czid;//车站专用id

    private Integer sid; //设备id

    private Integer typeid;//类型id

    private Integer qdid;//区段专用id

    private String line;//线路情况： 分1,2,3,4,站内，电码化

    private String leftRight;//左右侧 ，站内和电码化为空

    private  Integer xingBie;//0：空 ，1：上行， 2：下行

    private Integer type;//0：空 ， 1：接近，2：离去

    private String zongheId;

    private String ofXianDuan;

    private String quduanshejiName;

    private String quduanyunyingName;

    private Integer quduanLength;

    private String carrier;

    private String diduanType;

    private String xianluqingkuang;

    private Integer bianjie;

    private String fenjiedianWhere;

    private Integer zhanqufenjie;

    private String jinzhanxinhaojiName;

    private String xinhaojiorbiaozhiming;

    private String xinhaobiaozhipaiWhere;

    private String xinhaojiWhere;

    private String zuocejueyuanType;

    private String youcejueyuanType;

    private String zhengxianhoufangquduanId;

    private String zhengxianqianfangquduanId;

    private String daochaguanlianquduan1Id;

    private String daochaguanlianquduan2Id;

    private String dianmahuaguihao;

    private Integer guineidizhi;

    private Date time;


    private String zongZuoBiao;
    private String hengXiangPianYi;
    private String turnoutSectionType;
    private String bend1ConnectionSectionID;
    private String bent1ConnectionObject;
    private String bent1OffsetOfBranchCenter;
    private String bent1Orientation;
    private String bend2ConnectionSectionID;
    private String bent2ConnectionObject;
    private String bent2OffsetOfBranchCenter;
    private String bent2Orientation;






    private String yuliu;

    private LineEntity lineEntity;
    private XianDuanEntity xianDuanEntity;
    private CheZhanEntity cheZhanEntity;
}