package com.yintu.ruixing.paigongguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiGongGuanLiBaoGongSecondaryEntity {
    private Integer id;

    private Integer bid;

    private String xianduan;

    private String content;

    private Integer baogongtype;

    private String createname;

    private Date createtime;

    private String updatename;

    private Date updatetime;

    private String signId;
}