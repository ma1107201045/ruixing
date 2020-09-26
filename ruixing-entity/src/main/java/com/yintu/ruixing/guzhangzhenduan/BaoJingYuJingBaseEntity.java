package com.yintu.ruixing.guzhangzhenduan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaoJingYuJingBaseEntity {
    private Integer id;

    private Integer bjnumber;

    private String bjcontext;

    private Integer bjyjtype;

    private String userange;

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

    public Integer getBjnumber() {
        return bjnumber;
    }

    public void setBjnumber(Integer bjnumber) {
        this.bjnumber = bjnumber;
    }

    public String getBjcontext() {
        return bjcontext;
    }

    public void setBjcontext(String bjcontext) {
        this.bjcontext = bjcontext == null ? null : bjcontext.trim();
    }

    public Integer getBjyjtype() {
        return bjyjtype;
    }

    public void setBjyjtype(Integer bjyjtype) {
        this.bjyjtype = bjyjtype;
    }

    public String getUserange() {
        return userange;
    }

    public void setUserange(String userange) {
        this.userange = userange == null ? null : userange.trim();
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