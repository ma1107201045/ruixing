package com.yintu.ruixing.danganguanli;

import java.util.Date;

public class LineTechnologyStatusStationEntity {
    private Integer id;

    private String skillShaftCount;

    private String maintenanceTerminal;

    private Date openDate;

    private String lenght;

    private Integer cid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSkillShaftCount() {
        return skillShaftCount;
    }

    public void setSkillShaftCount(String skillShaftCount) {
        this.skillShaftCount = skillShaftCount == null ? null : skillShaftCount.trim();
    }

    public String getMaintenanceTerminal() {
        return maintenanceTerminal;
    }

    public void setMaintenanceTerminal(String maintenanceTerminal) {
        this.maintenanceTerminal = maintenanceTerminal == null ? null : maintenanceTerminal.trim();
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public String getLenght() {
        return lenght;
    }

    public void setLenght(String lenght) {
        this.lenght = lenght == null ? null : lenght.trim();
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }
}