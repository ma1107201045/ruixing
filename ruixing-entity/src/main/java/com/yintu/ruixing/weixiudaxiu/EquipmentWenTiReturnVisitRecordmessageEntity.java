package com.yintu.ruixing.weixiudaxiu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentWenTiReturnVisitRecordmessageEntity {
    private Integer id;

    private Integer typeid;

    private String operatorname;

    private Date operatortime;

    private String context;

    private Integer typenum;//类型  1:回访',


}