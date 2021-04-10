package com.yintu.ruixing.paigongguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiGongGuanLiUserDaystateEntity {
    private Integer id;

    private Integer userid;

    private String username;

    private String riqi;

    private Integer daystate;

    private Date createtime;

    private String createname;

    private Date updatetime;

    private String updatename;

    private List<PaiGongGuanLiUserDaystateEntity> userlist;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getRiqi() {
        return riqi;
    }

    public void setRiqi(String riqi) {
        this.riqi = riqi == null ? null : riqi.trim();
    }

    public Integer getDaystate() {
        return daystate;
    }

    public void setDaystate(Integer daystate) {
        this.daystate = daystate;
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