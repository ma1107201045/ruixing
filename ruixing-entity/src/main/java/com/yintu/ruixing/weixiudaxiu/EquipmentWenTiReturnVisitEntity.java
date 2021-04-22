package com.yintu.ruixing.weixiudaxiu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentWenTiReturnVisitEntity {
    private Integer id;

    private Integer renwubaseid;

    private String renwunumber;

    private String recordnumber;

    private String tljname;

    private String dwdname;

    private String xdname;

    private Integer returnuserid;

    private String returnedusername;

    private String returnedusernamephone;

    private Integer renwustate;

    private Date returntime;

    private String returnwenti;

    private String replymessage;

    private Integer implementstate;

    private Integer wentistate;

    private Integer pushstate;

    private Integer returncycletype;

    private String years;

    private Integer typenumber;

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

    public Integer getRenwubaseid() {
        return renwubaseid;
    }

    public void setRenwubaseid(Integer renwubaseid) {
        this.renwubaseid = renwubaseid;
    }

    public String getRenwunumber() {
        return renwunumber;
    }

    public void setRenwunumber(String renwunumber) {
        this.renwunumber = renwunumber == null ? null : renwunumber.trim();
    }

    public String getRecordnumber() {
        return recordnumber;
    }

    public void setRecordnumber(String recordnumber) {
        this.recordnumber = recordnumber == null ? null : recordnumber.trim();
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

    public Integer getRenwustate() {
        return renwustate;
    }

    public void setRenwustate(Integer renwustate) {
        this.renwustate = renwustate;
    }

    public Date getReturntime() {
        return returntime;
    }

    public void setReturntime(Date returntime) {
        this.returntime = returntime;
    }

    public String getReturnwenti() {
        return returnwenti;
    }

    public void setReturnwenti(String returnwenti) {
        this.returnwenti = returnwenti == null ? null : returnwenti.trim();
    }

    public String getReplymessage() {
        return replymessage;
    }

    public void setReplymessage(String replymessage) {
        this.replymessage = replymessage == null ? null : replymessage.trim();
    }

    public Integer getImplementstate() {
        return implementstate;
    }

    public void setImplementstate(Integer implementstate) {
        this.implementstate = implementstate;
    }

    public Integer getWentistate() {
        return wentistate;
    }

    public void setWentistate(Integer wentistate) {
        this.wentistate = wentistate;
    }

    public Integer getPushstate() {
        return pushstate;
    }

    public void setPushstate(Integer pushstate) {
        this.pushstate = pushstate;
    }

    public Integer getReturncycletype() {
        return returncycletype;
    }

    public void setReturncycletype(Integer returncycletype) {
        this.returncycletype = returncycletype;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years == null ? null : years.trim();
    }

    public Integer getTypenumber() {
        return typenumber;
    }

    public void setTypenumber(Integer typenumber) {
        this.typenumber = typenumber;
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