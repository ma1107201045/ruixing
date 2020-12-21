package com.yintu.ruixing.common.vo;

/**
 * @Author: mlf
 * @Date: 2020/12/21 16:01
 * @Version: 1.0
 */
public class AuditTotalInfoVo {

    /**
     * 审核人（用户真实姓名）
     */
    private int auditor;
    /**
     * 排序字段
     */
    private short sort;
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
    private short isDispose;

    /**
     * 审批状态 2.审核中 3.通过 4.拒绝 5.转交
     */
    private short auditStatus;

    public int getAuditor() {
        return auditor;
    }

    public void setAuditor(int auditor) {
        this.auditor = auditor;
    }

    public short getSort() {
        return sort;
    }

    public void setSort(short sort) {
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

    public short getIsDispose() {
        return isDispose;
    }

    public void setIsDispose(short isDispose) {
        this.isDispose = isDispose;
    }

    public short getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(short auditStatus) {
        this.auditStatus = auditStatus;
    }
}
