package com.yintu.ruixing.anzhuangtiaoshi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnZhuangTiaoShiObjectAuditorEntity {
    private Integer id;

    private Integer objectId;

    private Integer auditorId;

    private Integer objecttype;

    private Integer isPass;

    private String reason;

    private String createname;

    private Date createtime;

    private String updatename;

    private Date updatetime;


}