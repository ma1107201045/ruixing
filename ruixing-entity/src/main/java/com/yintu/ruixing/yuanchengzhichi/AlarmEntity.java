package com.yintu.ruixing.yuanchengzhichi;

public class AlarmEntity {
    private Integer id;

    private Integer msgId;

    private Integer sendNum;

    private Integer createtime;

    private Integer stationId;

    private Integer sectionId;

    private Integer alarmcode;

    private Integer reserved1;

    private Integer reserved2;

    private Integer alarmlevel;

    private Integer platformTime;

    private Integer recoverTime;

    private Boolean status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    public Integer getSendNum() {
        return sendNum;
    }

    public void setSendNum(Integer sendNum) {
        this.sendNum = sendNum;
    }

    public Integer getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Integer createtime) {
        this.createtime = createtime;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public Integer getAlarmcode() {
        return alarmcode;
    }

    public void setAlarmcode(Integer alarmcode) {
        this.alarmcode = alarmcode;
    }

    public Integer getReserved1() {
        return reserved1;
    }

    public void setReserved1(Integer reserved1) {
        this.reserved1 = reserved1;
    }

    public Integer getReserved2() {
        return reserved2;
    }

    public void setReserved2(Integer reserved2) {
        this.reserved2 = reserved2;
    }

    public Integer getAlarmlevel() {
        return alarmlevel;
    }

    public void setAlarmlevel(Integer alarmlevel) {
        this.alarmlevel = alarmlevel;
    }

    public Integer getPlatformTime() {
        return platformTime;
    }

    public void setPlatformTime(Integer platformTime) {
        this.platformTime = platformTime;
    }

    public Integer getRecoverTime() {
        return recoverTime;
    }

    public void setRecoverTime(Integer recoverTime) {
        this.recoverTime = recoverTime;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}