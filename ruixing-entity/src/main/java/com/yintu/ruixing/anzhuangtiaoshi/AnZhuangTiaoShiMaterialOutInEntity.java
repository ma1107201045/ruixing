package com.yintu.ruixing.anzhuangtiaoshi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnZhuangTiaoShiMaterialOutInEntity {
    private Integer id;

    private String materialsname;

    private String materialsnumber;

    private Date createtime;

    private String materialsguige;

    private Integer materialsinnumber;

    private Integer materialsoutnumber;

    private Integer inoutstate;

    private String yuliu;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMaterialsname() {
        return materialsname;
    }

    public void setMaterialsname(String materialsname) {
        this.materialsname = materialsname == null ? null : materialsname.trim();
    }

    public String getMaterialsnumber() {
        return materialsnumber;
    }

    public void setMaterialsnumber(String materialsnumber) {
        this.materialsnumber = materialsnumber == null ? null : materialsnumber.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getMaterialsguige() {
        return materialsguige;
    }

    public void setMaterialsguige(String materialsguige) {
        this.materialsguige = materialsguige == null ? null : materialsguige.trim();
    }

    public Integer getMaterialsinnumber() {
        return materialsinnumber;
    }

    public void setMaterialsinnumber(Integer materialsinnumber) {
        this.materialsinnumber = materialsinnumber;
    }

    public Integer getMaterialsoutnumber() {
        return materialsoutnumber;
    }

    public void setMaterialsoutnumber(Integer materialsoutnumber) {
        this.materialsoutnumber = materialsoutnumber;
    }

    public Integer getInoutstate() {
        return inoutstate;
    }

    public void setInoutstate(Integer inoutstate) {
        this.inoutstate = inoutstate;
    }

    public String getYuliu() {
        return yuliu;
    }

    public void setYuliu(String yuliu) {
        this.yuliu = yuliu == null ? null : yuliu.trim();
    }
}