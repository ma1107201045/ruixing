

package com.yintu.ruixing.guzhangzhenduan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 车站
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheZhanEntity {
    private long cid;

    private long tid;

    private long did;

    private long xid;

    private String czName;
    private String xdName;
    private String dwdName;
    private String tljName;

    private long czId;

    private long xdId;
    private long tljId;
    private long dwdId;

    private String czNameJianCheng;//图中简称

    private String czIp;//车站IP

    private String czType;//车站类型

    private Integer czLianJieType;//车站连接类型 0：已配置未连接  1：已配置已连接

    private long czStutrs;//车站连接状态  0：故障断开  1：连接中

    private long czOpenStutrs;//启用状态   0：未启用   1：启用

    private Date czLastTime;//最后连接时间

    private long xdCzId;
    private String czMiaoShu;//车站运用概况

    private Integer czCaoZuo;//操作 0：断开  ，1：连接

    private Integer czDuanTou;//是否为端头站   0：否  1：是

    private Integer czState; //车站配置状态  0：未配置    1：已配置
    private Integer xdState; //线段配置状态  0：未配置    1：已配置
    private Integer czdmhState; //站内电码化配置状态  0：未配置    1：已配置

    private String czJson; //车站储存json
    private String xdJson; //线段储存json
    private String czdmhJson; //站内电码化储存json

    private Integer czRSheBei;//站内是否有2000R设备  0：无   1：有

    private Integer czZhongJiZhan;//是否为中继站   0：不是   1：是

    private DianWuDuanEntity dianWuDuanEntity;

    private TieLuJuEntity tieLuJuEntity;

    private XianDuanEntity xianDuanEntity;



    private Integer publicMessage;//公共开关量信息

    private String yuliushebei1;

    private String yuliushebei2;

    private String yuliushebei3;

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

    private Integer alarmNumber;











}
