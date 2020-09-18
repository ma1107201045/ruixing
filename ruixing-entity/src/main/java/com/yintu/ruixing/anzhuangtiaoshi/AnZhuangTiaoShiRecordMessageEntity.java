package com.yintu.ruixing.anzhuangtiaoshi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnZhuangTiaoShiRecordMessageEntity {
    private Integer id;

    private Integer typeid;

    private String operatorname;

    private Date operatortime;

    private String context;

    private Integer typenum;


}