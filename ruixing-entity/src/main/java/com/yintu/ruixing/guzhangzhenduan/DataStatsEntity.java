package com.yintu.ruixing.guzhangzhenduan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author:lcy
 * @date:2020-05-22 10
 * 封装数据统计
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataStatsEntity {


    private long tid;

    private long did;

    private long xid;

    private long cid;

    private String tljName;

    private String dwdName;

    private String xdName;

    private String czName;

    private int tljId;

    private int dwdId;

    private int xdId;

    private int czId;

    private String tljMiaoShu;

    private String dwdMiaoShu;

    private String xdMiaoShu;

    private String czMiaoShu;

    private Integer xdState;//'线段设置状态  0：未设置  1：已设置',

    private Integer czdmhState; //站内电码化配置状态  0：未配置    1：已配置

    private Integer czState;//'车站设置状态  0：未设置  1：已设置',

    private Integer dwdState;//'电务段设置状态  0：未设置  1：已设置',


    private String czNameJianCheng;

    private String czIp;


    private Long czStutrs;

    private Long czOpenStutrs;

    private Date czLastTime;

    private long xdCzId;


    private String yuliu1;

    private String yuliu2;

    private long czDuanTou;

    private String xtType;//系统类型

    private String czType;//车站类型

    private Integer czCaoZuo;//操作 0：断开  ，1：连接

    private Integer czRSheBei;//站内是否有2000R设备  0：无   1：有

    private Integer czZhongJiZhan;//是否为中继站   0：不是   1：是

    private DianWuDuanEntity dianWuDuanEntity;

    private TieLuJuEntity tieLuJuEntity;

    private XianDuanEntity xianDuanEntity;


    private String xdJson;//储存的json',
    private String czdmhJson; //站内电码化储存json
    private String czJson;//储存的json',


    private Integer publicMessage;//公共开关量信息

    private String yuliushebei1;

    private String yuliushebei2;

    private String yuliushebei3;

    private String yuliushebei4;

    private Integer tongxinbianmaguidaonumber;

    private Integer tongxinbianmazhanneioneguidaonumber;

    private Integer jidianonetooneguidaonumber;

    private Integer jidianntooneguidaonumber;

    private Integer jidianntooneshebeinumber;

    private Integer tongxinbianmadianmahuashebeinumber;

    private Integer jidianntoonedianmahuashebeinumber;

    private Integer jidianjiashiguidaonumber;

    private Integer jidianjiashidianmahuashebeinumber;

    private Integer jiDianDianMaHuaNumber;

    private String qujianbisaitype;

    private Integer linzhan1id;

    private String linzhan1name;

    private String linzhan1linetype;

    private String linzhan1ofxianduan;

    private String linzhan1isnobendwd;

    private Integer linzhan2id;

    private String linzhan2name;

    private String linzhan2linetype;

    private String linzhan2ofxianduan;

    private String linzhan2isnobendwd;

    private Integer linzhan3id;

    private String linzhan3name;

    private String linzhan3linetype;

    private String linzhan3ofxianduan;

    private String linzhan3isnobendwd;

    private Integer linzhan4id;

    private String linzhan4name;

    private String linzhan4linetype;

    private String linzhan4ofxianduan;

    private String linzhan4isnobendwd;

    private Integer linzhan5id;

    private String linzhan5name;

    private String linzhan5linetype;

    private String linzhan5ofxianduan;

    private String linzhan5isnobendwd;

    private Integer linzhan6id;

    private String linzhan6name;

    private String linzhan6linetype;

    private String linzhan6ofxianduan;

    private String linzhan6isnobendwd;


}
