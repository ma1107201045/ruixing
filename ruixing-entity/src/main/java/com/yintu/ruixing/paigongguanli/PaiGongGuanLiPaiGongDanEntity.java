package com.yintu.ruixing.paigongguanli;

import java.util.Date;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiGongGuanLiPaiGongDanEntity {
    private Integer id;

    private Integer operatorid;

    private String paigongnumber;

    private String yewutype;

    private String tljname;

    private String dwdname;

    private String xdname;

    private String czname;

    private String xiangmutype;

    private String chuchaitype;

    private String workcontent;

    private String taskshuxing;

    private String contacts;

    private String contactsphonenum;

    private String chuchaicity;

    private String xiangxiadress;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date chuchaistarttime;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date chuchaiendtime;

    private String teshuask;

    private String guanliandanju;

    private Integer paigongpeople;

    private Integer gaipaipeople;

    private Integer paigongstate;

    private Integer paigongmode; //派工方式 1：手动派工  2：自动派工

    private Integer paigongpeoplenumber; //派工人员数量

    private String truename;
    private String provinceId;
    private String cityId;
    private String districtId;
    private Integer state;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createtime;

    private String createname;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatetime;

    private String updatename;

    private Integer taskSign;//'任务标记 0：暂未开始 1：开始 2：结束',


}