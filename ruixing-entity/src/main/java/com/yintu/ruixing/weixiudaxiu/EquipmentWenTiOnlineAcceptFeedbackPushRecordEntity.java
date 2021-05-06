package com.yintu.ruixing.weixiudaxiu;

import java.util.Date;

public class EquipmentWenTiOnlineAcceptFeedbackPushRecordEntity {
    private Integer id;

    private Integer fid;

    private String pushnumber;

    private String phone;

    private String pushname;

    private String department;

    private String position;

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

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public String getPushnumber() {
        return pushnumber;
    }

    public void setPushnumber(String pushnumber) {
        this.pushnumber = pushnumber == null ? null : pushnumber.trim();
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department == null ? null : department.trim();
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
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