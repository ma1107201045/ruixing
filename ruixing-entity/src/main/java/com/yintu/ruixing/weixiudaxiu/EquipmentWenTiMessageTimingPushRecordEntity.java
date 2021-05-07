package com.yintu.ruixing.weixiudaxiu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentWenTiMessageTimingPushRecordEntity {
    private Integer id;

    private Integer pid;

    private String recordnumber;

    private Date intime;

    private Date planouttime;

    private Date renwushedingtime;

    private Integer recorduserid;

    private String recordusername;

    private String phone;

    private String pushname;

    private Date pushtime;

    private String recordstate;

    private Integer pushtype;

    private Integer isnotsuccess;

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

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getRecordnumber() {
        return recordnumber;
    }

    public void setRecordnumber(String recordnumber) {
        this.recordnumber = recordnumber == null ? null : recordnumber.trim();
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Date getPlanouttime() {
        return planouttime;
    }

    public void setPlanouttime(Date planouttime) {
        this.planouttime = planouttime;
    }

    public Date getRenwushedingtime() {
        return renwushedingtime;
    }

    public void setRenwushedingtime(Date renwushedingtime) {
        this.renwushedingtime = renwushedingtime;
    }

    public Integer getRecorduserid() {
        return recorduserid;
    }

    public void setRecorduserid(Integer recorduserid) {
        this.recorduserid = recorduserid;
    }

    public String getRecordusername() {
        return recordusername;
    }

    public void setRecordusername(String recordusername) {
        this.recordusername = recordusername == null ? null : recordusername.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getPushname() {
        return pushname;
    }

    public void setPushname(String pushname) {
        this.pushname = pushname == null ? null : pushname.trim();
    }

    public Date getPushtime() {
        return pushtime;
    }

    public void setPushtime(Date pushtime) {
        this.pushtime = pushtime;
    }

    public String getRecordstate() {
        return recordstate;
    }

    public void setRecordstate(String recordstate) {
        this.recordstate = recordstate == null ? null : recordstate.trim();
    }

    public Integer getPushtype() {
        return pushtype;
    }

    public void setPushtype(Integer pushtype) {
        this.pushtype = pushtype;
    }

    public Integer getIsnotsuccess() {
        return isnotsuccess;
    }

    public void setIsnotsuccess(Integer isnotsuccess) {
        this.isnotsuccess = isnotsuccess;
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