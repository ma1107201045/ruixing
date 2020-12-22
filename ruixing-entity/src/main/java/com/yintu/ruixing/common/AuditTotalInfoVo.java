package com.yintu.ruixing.common;

import java.util.Date;

/**
 * @Author: mlf
 * @Date: 2020/12/21 16:01
 * @Version: 1.0
 */
public class AuditTotalInfoVo {

    /**
     * 审核人（用户真实姓名）
     */
    private String auditor;
    /**
     * 排序字段
     */
    private Integer sort;
    /**
     * 内容
     */
    private String context;
    /**
     * 附件名称
     */
    private String accessoryName;
    /**
     * 附件路径
     */
    private String accessoryPath;
    /**
     * 是否处理
     */
    private Short isDispose;

    /**
     * 审批状态 2.审核中 3.通过 4.拒绝 5.转交
     */
    private Short auditStatus;
    /**
     * 审核完成时间
     */
    private Date auditFinishTime;

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getAccessoryName() {
        return accessoryName;
    }

    public void setAccessoryName(String accessoryName) {
        this.accessoryName = accessoryName;
    }

    public String getAccessoryPath() {
        return accessoryPath;
    }

    public void setAccessoryPath(String accessoryPath) {
        this.accessoryPath = accessoryPath;
    }

    public Short getIsDispose() {
        return isDispose;
    }

    public void setIsDispose(Short isDispose) {
        this.isDispose = isDispose;
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

    @Override
    public String toString() {
        return "AuditTotalInfoVo{" +
                "auditor='" + auditor + '\'' +
                ", sort=" + sort +
                ", context='" + context + '\'' +
                ", accessoryName='" + accessoryName + '\'' +
                ", accessoryPath='" + accessoryPath + '\'' +
                ", isDispose=" + isDispose +
                ", auditStatus=" + auditStatus +
                ", auditFinishTime=" + auditFinishTime +
                '}';
    }
}
