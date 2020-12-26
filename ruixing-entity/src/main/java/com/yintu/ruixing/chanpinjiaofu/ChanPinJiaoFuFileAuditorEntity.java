package com.yintu.ruixing.chanpinjiaofu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author Mr.liu
 * @Date 2020/7/7 16:12
 * @Version 1.0
 * 需求:产品交付项目文件和审核人的中间表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChanPinJiaoFuFileAuditorEntity {
    private Integer id;

    private Integer objectId;

    private Integer auditorId;

    private Integer objecttype;

    private String doname;

    private Date dotime;

    private Integer sort;

    private Short activate;

    private Short auditStatus;

    private Date auditFinishTime;

    private String accessoryName;

    private String accessoryPath;

    private Short isDispose;

    private String context;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(Integer auditorId) {
        this.auditorId = auditorId;
    }

    public Integer getObjecttype() {
        return objecttype;
    }

    public void setObjecttype(Integer objecttype) {
        this.objecttype = objecttype;
    }

    public String getDoname() {
        return doname;
    }

    public void setDoname(String doname) {
        this.doname = doname == null ? null : doname.trim();
    }

    public Date getDotime() {
        return dotime;
    }

    public void setDotime(Date dotime) {
        this.dotime = dotime;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Short getActivate() {
        return activate;
    }

    public void setActivate(Short activate) {
        this.activate = activate;
    }

    public Short getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Short auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Date getAuditFinishTime() {
        return auditFinishTime;
    }

    public void setAuditFinishTime(Date auditFinishTime) {
        this.auditFinishTime = auditFinishTime;
    }

    public String getAccessoryName() {
        return accessoryName;
    }

    public void setAccessoryName(String accessoryName) {
        this.accessoryName = accessoryName == null ? null : accessoryName.trim();
    }

    public String getAccessoryPath() {
        return accessoryPath;
    }

    public void setAccessoryPath(String accessoryPath) {
        this.accessoryPath = accessoryPath == null ? null : accessoryPath.trim();
    }

    public Short getIsDispose() {
        return isDispose;
    }

    public void setIsDispose(Short isDispose) {
        this.isDispose = isDispose;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context == null ? null : context.trim();
    }
}