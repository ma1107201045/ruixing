package com.yintu.ruixing.paigongguanli;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PaiGongGuanLiBaoGongGuanLiEntity {
    private Integer id;

    private Integer uid;

    private String xiangmuname;

    private Date daytime;

    private String renyuandongtai;

    private String workcontent;

    private String workadress;

    private BigDecimal jingdu;

    private BigDecimal weidu;

    private String fileName;

    private String filePath;

    private Date creattime;

    private Date updattime;

    private String username;
    private String bumen;
    private String phone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getXiangmuname() {
        return xiangmuname;
    }

    public void setXiangmuname(String xiangmuname) {
        this.xiangmuname = xiangmuname == null ? null : xiangmuname.trim();
    }

    public Date getDaytime() {
        return daytime;
    }

    public void setDaytime(Date daytime) {
        this.daytime = daytime;
    }

    public String getRenyuandongtai() {
        return renyuandongtai;
    }

    public void setRenyuandongtai(String renyuandongtai) {
        this.renyuandongtai = renyuandongtai == null ? null : renyuandongtai.trim();
    }

    public String getWorkcontent() {
        return workcontent;
    }

    public void setWorkcontent(String workcontent) {
        this.workcontent = workcontent == null ? null : workcontent.trim();
    }

    public String getWorkadress() {
        return workadress;
    }

    public void setWorkadress(String workadress) {
        this.workadress = workadress == null ? null : workadress.trim();
    }

    public BigDecimal getJingdu() {
        return jingdu;
    }

    public void setJingdu(BigDecimal jingdu) {
        this.jingdu = jingdu;
    }

    public BigDecimal getWeidu() {
        return weidu;
    }

    public void setWeidu(BigDecimal weidu) {
        this.weidu = weidu;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    public Date getCreattime() {
        return creattime;
    }

    public void setCreattime(Date creattime) {
        this.creattime = creattime;
    }

    public Date getUpdattime() {
        return updattime;
    }

    public void setUpdattime(Date updattime) {
        this.updattime = updattime;
    }
}