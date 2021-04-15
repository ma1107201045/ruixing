package com.yintu.ruixing.paigongguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiGongGuanLiBaoGongEntity {
    private Integer id;

    private Integer uid;

    private Integer userid;

    private Date datetime;

    private String workcontent;

    private String address;

    private Double longitude;

    private Double latitude;

    private Integer qita;

    private String filename;

    private String filepath;

    private String createname;

    private Date createtime;

    private String updatename;

    private Date updatetime;

    private String truename;

    private String xianduan;
    private String department;
    private String coordinationuser;
    private Integer coordinationuserid;
    private Integer isnotover;
    private String signId;
    private Integer isNeedAnyone;
    private String workNumber;

    List<PaiGongGuanLiBaoGongSecondaryEntity> Children;
    List<PaiGongGuanLiBaoGongCommentEntity> commentEntityList;

}