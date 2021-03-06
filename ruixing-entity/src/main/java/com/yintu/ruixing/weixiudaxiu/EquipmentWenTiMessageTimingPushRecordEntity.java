package com.yintu.ruixing.weixiudaxiu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentWenTiMessageTimingPushRecordEntity {
    private Integer id;

    private Integer pid;

    private String recordnumber;

    private Date intime;

    private Date planouttime;

    private String renwushedingtime;

    private Integer recorduserid;

    private String recordusername;

    private String phone;

    private String pushname;

    private Date pushtime;

    private String recordstate;

    private Integer pushtype;

    private Integer isnotsuccess;

    private Date createtime;

    private String createname;

    private Date updatetime;

    private String updatename;


}