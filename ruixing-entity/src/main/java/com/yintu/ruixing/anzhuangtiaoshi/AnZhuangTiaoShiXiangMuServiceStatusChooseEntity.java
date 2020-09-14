package com.yintu.ruixing.anzhuangtiaoshi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnZhuangTiaoShiXiangMuServiceStatusChooseEntity {

    private Integer id;

    private Integer sid;

    private Object name;

    private Integer isNotDaoHuo;

    private Date createtime;

    private String createname;

    private Date updatetime;

    private String updatename;


    private Boolean isNotChoose;

}