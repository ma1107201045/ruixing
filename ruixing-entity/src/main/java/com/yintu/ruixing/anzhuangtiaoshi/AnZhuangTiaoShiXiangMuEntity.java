package com.yintu.ruixing.anzhuangtiaoshi;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnZhuangTiaoShiXiangMuEntity {

    private Integer id;

    private Integer worksid; //作业项配置id

    private String worksName; //作业项配置姓名

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date xianduantime;//线段创建的时间

    private String tljName;//铁路局名

    private String dwdName;//电务段名

    private Integer xdId; //线段id

    private String xdName;//线段名

    private String czName;//车站名

    private String xdType;//项目类型

    private Integer xdFenlei;//线段状态 1：正在进行 ，2：已完成，3：长期停滞

    private String guanlianxiangmu;//关联项目名和编号

    private Date createtime;

    private String createname;

    private Date updatetime;

    private String updatename;

    private Date opentime;//开通时间

    private Integer cheZhanTotal;//车站总数
    private Integer jiGuiTotal;//机柜总数
    private Integer indoorKaBanTotal;//室内卡板总数
    private Integer outdoorSheBeiTotal;//室外设备总数
    private Integer peiXianTotal;//完成配线总数
    private Integer shangDianTotal;//具备上电总数
    private Integer jingTaiYanShouTotal;//完成静态验收总数
    private Integer dongTaiYanShouTotal;//完成动态验收总数
    private Integer lianTiaoLianShiTotal;//完成联调联试总数
    private Integer shiYunXingTotal;//完成试运行总数
    private Integer kaiTongTotal;//完成开通总数


    private List titlelist;
    private List chooselist;

}