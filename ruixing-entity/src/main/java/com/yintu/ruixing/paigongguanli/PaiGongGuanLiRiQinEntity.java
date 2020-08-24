package com.yintu.ruixing.paigongguanli;

import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiGongGuanLiRiQinEntity {
    private Integer id;

    private Integer uid;

    private String userdongtai;

    private Date starttime;

    private Date endtime;

    private Date creattime;

    private Date updatetime;

    private String username;

    private String danwei;

    private String bumen;

    private String zhiwei;

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

    public String getUserdongtai() {
        return userdongtai;
    }

    public void setUserdongtai(String userdongtai) {
        this.userdongtai = userdongtai == null ? null : userdongtai.trim();
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Date getCreattime() {
        return creattime;
    }

    public void setCreattime(Date creattime) {
        this.creattime = creattime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}