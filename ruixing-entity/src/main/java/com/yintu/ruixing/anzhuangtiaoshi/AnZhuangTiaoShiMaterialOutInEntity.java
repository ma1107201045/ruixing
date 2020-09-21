package com.yintu.ruixing.anzhuangtiaoshi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnZhuangTiaoShiMaterialOutInEntity {
    private Integer id;

    private String materialsname;

    private String materialsnumber;

    private Date createtime;

    private String materialsguige;

    private Integer materialsinnumber;

    private Integer materialsoutnumber;

    private Integer inoutstate;

    private String yuliu;


}