package com.yintu.ruixing.weixiudaxiu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentWenTiReturnVisitBaseEntity {
    private Integer id;

    private Integer adduserid;

    private String renwunumber;

    private String tljname;

    private String dwdname;

    private String xdname;

    private String returnUserName;

    private Integer returnuserid;

    private String returnedusername;

    private String returnedusernamephone;

    private Integer returncycletype;

    private Date renwustarttime;

    private Date rewuendtime;

    private Integer implementstate;

    private Date createtime;

    private String createname;

    private Date updatetime;

    private String updatename;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdduserid() {
        return adduserid;
    }

    public void setAdduserid(Integer adduserid) {
        this.adduserid = adduserid;
    }

    public String getRenwunumber() {
        return renwunumber;
    }

    public void setRenwunumber(String renwunumber) {
        this.renwunumber = renwunumber == null ? null : renwunumber.trim();
    }

    public String getTljname() {
        return tljname;
    }

    public void setTljname(String tljname) {
        this.tljname = tljname == null ? null : tljname.trim();
    }

    public String getDwdname() {
        return dwdname;
    }

    public void setDwdname(String dwdname) {
        this.dwdname = dwdname == null ? null : dwdname.trim();
    }

    public String getXdname() {
        return xdname;
    }

    public void setXdname(String xdname) {
        this.xdname = xdname == null ? null : xdname.trim();
    }

    public Integer getReturnuserid() {
        return returnuserid;
    }

    public void setReturnuserid(Integer returnuserid) {
        this.returnuserid = returnuserid;
    }

    public String getReturnedusername() {
        return returnedusername;
    }

    public void setReturnedusername(String returnedusername) {
        this.returnedusername = returnedusername == null ? null : returnedusername.trim();
    }

    public String getReturnedusernamephone() {
        return returnedusernamephone;
    }

    public void setReturnedusernamephone(String returnedusernamephone) {
        this.returnedusernamephone = returnedusernamephone == null ? null : returnedusernamephone.trim();
    }

    public Integer getReturncycletype() {
        return returncycletype;
    }

    public void setReturncycletype(Integer returncycletype) {
        this.returncycletype = returncycletype;
    }

    public Date getRenwustarttime() {
        return renwustarttime;
    }

    public void setRenwustarttime(Date renwustarttime) {
        this.renwustarttime = renwustarttime;
    }

    public Date getRewuendtime() {
        return rewuendtime;
    }

    public void setRewuendtime(Date rewuendtime) {
        this.rewuendtime = rewuendtime;
    }

    public Integer getImplementstate() {
        return implementstate;
    }

    public void setImplementstate(Integer implementstate) {
        this.implementstate = implementstate;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getCreatename() {
        return createname;
    }

    public void setCreatename(String createname) {
        this.createname = createname == null ? null : createname.trim();
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getUpdatename() {
        return updatename;
    }

    public void setUpdatename(String updatename) {
        this.updatename = updatename == null ? null : updatename.trim();
    }
}