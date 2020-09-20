package com.yintu.ruixing.anzhuangtiaoshi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnZhuangTiaoShiWenTiAuditorEntity {
    private Integer id;

    private Integer objectId;

    private Integer auditorId;

    private Integer objecttype;

    private Integer isPass;

    private String reason;

    private String doname;

    private Date dotime;
    private Date updatetime;

    private String updatename;

}