package com.yintu.ruixing.weixiudaxiu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentWenTiMessageTimingPushEntity {
    private Integer id;

    private Integer adduserid;

    private String renwuName;

    private Date configTime;

    private String number;

    private String tljname;

    private String dwdname;

    private String xdname;

    private String pushusername;

    private String pushusernamephone;

    private Integer pushtype;

    private Integer implementState;

    private Date pushstarttime;

    private Date pushendtime;

    private Date pushtime;

    private Date createtime;

    private String createname;

    private Date updatetime;

    private String updatename;

    private Integer tljid;

    private Integer dwdid;

    private String xdid;


}