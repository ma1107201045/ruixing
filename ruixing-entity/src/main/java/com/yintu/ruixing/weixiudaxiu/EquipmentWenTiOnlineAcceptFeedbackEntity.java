package com.yintu.ruixing.weixiudaxiu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentWenTiOnlineAcceptFeedbackEntity {
    private Integer id;

    private String number;

    private String tljname;

    private String dwdname;

    private String xdname;

    private Date feedbacktime;

    private String feedbackusername;

    private String feedbackusernamephone;

    private String wentitype;

    private String wentimiaoshu;

    private Integer feedbackstate;

    private Integer acceptuserid;

    private String acceptusername;

    private Date accepttime;

    private String replymessage;

    private Integer wentistate;

    private Integer pushstate;

    private Date createtime;

    private String createname;

    private Date updatetime;

    private String updatename;

    private Integer tljid;

    private Integer dwdid;

    private Integer xdid;


}