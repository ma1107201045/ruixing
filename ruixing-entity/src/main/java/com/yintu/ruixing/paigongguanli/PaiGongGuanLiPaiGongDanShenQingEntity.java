package com.yintu.ruixing.paigongguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiGongGuanLiPaiGongDanShenQingEntity {
    private Integer id;

    private Integer pgId;

    private Integer userid;

    private Integer acceptuserid;

    private Integer shenqingState;

    private Integer shenqingType;

    private String userreason;

    private String acceptreason;

    private Integer isnothandle;

    private Date createtime;

    private String createname;

    private Date updatetime;

    private String updatename;

    private String username;



}