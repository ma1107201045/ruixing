package com.yintu.ruixing.weixiudaxiu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentWenTiCustomerWenTiTypeEntity {
    private Integer id;

    private String wentitype;

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

    public String getWentitype() {
        return wentitype;
    }

    public void setWentitype(String wentitype) {
        this.wentitype = wentitype == null ? null : wentitype.trim();
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