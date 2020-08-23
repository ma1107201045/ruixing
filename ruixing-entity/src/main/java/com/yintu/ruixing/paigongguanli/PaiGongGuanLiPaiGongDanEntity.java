package com.yintu.ruixing.paigongguanli;

import java.util.Date;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiGongGuanLiPaiGongDanEntity {
    private Integer id;

    private String paigongnumber;

    private String yewutype;

    private String tljname;

    private String dwdname;

    private String xdname;

    private String czname;

    private String xiangmutype;

    private String chuchaitype;

    private String workcontent;

    private String taskshuxing;

    private String contacts;

    private String contactsphonenum;

    private String chuchaicity;

    private String xiangxiadress;

    private Date chuchaistarttime;

    private Date chuchaiendtime;

    private String teshuask;

    private String guanliandanju;

    private String paigongpeople;

    private String gaipaipeople;

    private Integer paigongstate;

    private Integer paigongmode; //派工方式 1：手动派工  2：自动派工

    private Integer paigongpeoplenumber; //派工人员数量

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPaigongnumber() {
        return paigongnumber;
    }

    public void setPaigongnumber(String paigongnumber) {
        this.paigongnumber = paigongnumber == null ? null : paigongnumber.trim();
    }

    public String getYewutype() {
        return yewutype;
    }

    public void setYewutype(String yewutype) {
        this.yewutype = yewutype == null ? null : yewutype.trim();
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

    public String getCzname() {
        return czname;
    }

    public void setCzname(String czname) {
        this.czname = czname == null ? null : czname.trim();
    }

    public String getXiangmutype() {
        return xiangmutype;
    }

    public void setXiangmutype(String xiangmutype) {
        this.xiangmutype = xiangmutype == null ? null : xiangmutype.trim();
    }

    public String getChuchaitype() {
        return chuchaitype;
    }

    public void setChuchaitype(String chuchaitype) {
        this.chuchaitype = chuchaitype == null ? null : chuchaitype.trim();
    }

    public String getWorkcontent() {
        return workcontent;
    }

    public void setWorkcontent(String workcontent) {
        this.workcontent = workcontent == null ? null : workcontent.trim();
    }

    public String getTaskshuxing() {
        return taskshuxing;
    }

    public void setTaskshuxing(String taskshuxing) {
        this.taskshuxing = taskshuxing == null ? null : taskshuxing.trim();
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts == null ? null : contacts.trim();
    }

    public String getContactsphonenum() {
        return contactsphonenum;
    }

    public void setContactsphonenum(String contactsphonenum) {
        this.contactsphonenum = contactsphonenum == null ? null : contactsphonenum.trim();
    }

    public String getChuchaicity() {
        return chuchaicity;
    }

    public void setChuchaicity(String chuchaicity) {
        this.chuchaicity = chuchaicity == null ? null : chuchaicity.trim();
    }

    public String getXiangxiadress() {
        return xiangxiadress;
    }

    public void setXiangxiadress(String xiangxiadress) {
        this.xiangxiadress = xiangxiadress == null ? null : xiangxiadress.trim();
    }

    public Date getChuchaistarttime() {
        return chuchaistarttime;
    }

    public void setChuchaistarttime(Date chuchaistarttime) {
        this.chuchaistarttime = chuchaistarttime;
    }

    public Date getChuchaiendtime() {
        return chuchaiendtime;
    }

    public void setChuchaiendtime(Date chuchaiendtime) {
        this.chuchaiendtime = chuchaiendtime;
    }

    public String getTeshuask() {
        return teshuask;
    }

    public void setTeshuask(String teshuask) {
        this.teshuask = teshuask == null ? null : teshuask.trim();
    }

    public String getGuanliandanju() {
        return guanliandanju;
    }

    public void setGuanliandanju(String guanliandanju) {
        this.guanliandanju = guanliandanju == null ? null : guanliandanju.trim();
    }

    public String getPaigongpeople() {
        return paigongpeople;
    }

    public void setPaigongpeople(String paigongpeople) {
        this.paigongpeople = paigongpeople == null ? null : paigongpeople.trim();
    }

    public String getGaipaipeople() {
        return gaipaipeople;
    }

    public void setGaipaipeople(String gaipaipeople) {
        this.gaipaipeople = gaipaipeople == null ? null : gaipaipeople.trim();
    }

    public Integer getPaigongstate() {
        return paigongstate;
    }

    public void setPaigongstate(Integer paigongstate) {
        this.paigongstate = paigongstate;
    }
}