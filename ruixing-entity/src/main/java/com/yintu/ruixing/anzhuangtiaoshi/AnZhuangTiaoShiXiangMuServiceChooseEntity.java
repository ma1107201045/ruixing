package com.yintu.ruixing.anzhuangtiaoshi;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnZhuangTiaoShiXiangMuServiceChooseEntity {

    private Integer id;

    private Integer xdid;

    private Integer serid;

    private Integer choid;

    private Integer czid;

    private String chezhanname;

    private Integer typetime;

    private Integer isnot;

    private Integer isNotFinish;

    private Date planStartTime;

    private Date planEndTime;

    private Date actualStartTime;

    private Date actualEndTime;

    private Date planOpenTime;

    private Date actualOpenTime;

    private Date createtime;

    private String createname;

    private Date updatetime;

    private String updatename;

    private String remarks;

    private String servicename;
    private String chroosname;
    private Integer PlanToalTime;
    private Integer PlanOneTime;

    private List chooselist;
    private List chooselistt;

    private JSONObject list;
    private AnZhuangTiaoShiXiangMuEntity anZhuangTiaoShiXiangMuEntity;
    private AnZhuangTiaoShiXiangMuServiceStatusChooseEntity anZhuangTiaoShiXiangMuServiceStatusChooseEntity;
    private AnZhuangTiaoShiXiangMuServiceStatusEntity anZhuangTiaoShiXiangMuServiceStatusEntity;
    private String xdname;
}