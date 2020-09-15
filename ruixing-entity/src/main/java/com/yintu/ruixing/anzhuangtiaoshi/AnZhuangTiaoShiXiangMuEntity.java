package com.yintu.ruixing.anzhuangtiaoshi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnZhuangTiaoShiXiangMuEntity {

    private Integer id;

    private Integer worksid; //作业项配置id

    private Date xianduantime;//线段创建的时间

    private String tljName;//铁路局名

    private String dwdName;//电务段名

    private String xdName;//线段名

    private String xdType;//项目类型

    private Integer xdFenlei;//线段状态 1：正在进行 ，2：已完成，3：长期停滞

    private String guanlianxiangmu;//关联项目名和编号

    private Date createtime;

    private String createname;

    private Date updatetime;

    private String updatename;


    private  Integer cheZhanTotal;//车站总数
    private  Integer jiGuiTotal;//机柜总数
    private  Integer indoorKaBanTotal;//室内卡板总数
    private  Integer outdoorSheBeiTotal;//室外设备总数
    private  Integer peiXianTotal;//完成配线总数
    private  Integer shangDianTotal;//具备上电总数
    private  Integer jingTaiYanShouTotal;//完成静态验收总数
    private  Integer dongTaiYanShouTotal;//完成动态验收总数
    private  Integer lianTiaoLianShiTotal;//完成联调联试总数
    private  Integer shiYunXingTotal;//完成试运行总数
    private  Integer kaiTongTotal;//完成开通总数


    private List titlelist;
    private List chooselist;

















    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getXianduantime() {
        return xianduantime;
    }

    public void setXianduantime(Date xianduantime) {
        this.xianduantime = xianduantime;
    }

    public String getTljName() {
        return tljName;
    }

    public void setTljName(String tljName) {
        this.tljName = tljName == null ? null : tljName.trim();
    }

    public String getDwdName() {
        return dwdName;
    }

    public void setDwdName(String dwdName) {
        this.dwdName = dwdName == null ? null : dwdName.trim();
    }

    public String getXdName() {
        return xdName;
    }

    public void setXdName(String xdName) {
        this.xdName = xdName == null ? null : xdName.trim();
    }

    public String getXdType() {
        return xdType;
    }

    public void setXdType(String xdType) {
        this.xdType = xdType == null ? null : xdType.trim();
    }

    public Integer getXdFenlei() {
        return xdFenlei;
    }

    public void setXdFenlei(Integer xdFenlei) {
        this.xdFenlei = xdFenlei;
    }

    public String getGuanlianxiangmu() {
        return guanlianxiangmu;
    }

    public void setGuanlianxiangmu(String guanlianxiangmu) {
        this.guanlianxiangmu = guanlianxiangmu == null ? null : guanlianxiangmu.trim();
    }
}