package com.yintu.ruixing.weixiudaxiu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentWenTiReturnVisitPushRecordEntity {
    private Integer id;

    private Integer vid;

    private String pushnumber;

    private String phone;

    private String pushname;

    private String department;

    private String position;

    private Integer isnotsuccess;

    private Date createtime;

    private String createname;

    private Date updatetime;

    private String updatename;


}