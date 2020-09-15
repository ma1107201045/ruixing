package com.yintu.ruixing.anzhuangtiaoshi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AnZhuangTiaoShiXiangMuServiceStatusEntity {
    private Integer id;

    private String servicename;

    private String choose;

    private Integer timetype;

    private Integer isNotOver;

    private Date createtime;

    private String createname;

    private Date updatetime;

    private String updatename;

    private boolean checkbox;

    private AnZhuangTiaoShiXiangMuServiceStatusChooseEntity anZhuangTiaoShiXiangMuServiceStatusChooseEntity;

    private List list;


}