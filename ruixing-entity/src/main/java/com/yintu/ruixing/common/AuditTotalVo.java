package com.yintu.ruixing.common;

import java.util.Date;
import java.util.List;

/**
 * @Author: mlf
 * @Date: 2020/12/21 15:49
 * @Version: 1.0
 */
public class AuditTotalVo {


    /**
     * 模块标识
     * 1.解决方案-售前技术支持 2.解决方案-招标投标技术支持 3.解决方案-设计联络及后续技术交流
     * 4.知识管理 5.产品交付-项目交付状态管理-项目状态变更  6.产品交付-项目交付状态管理-输出文件发布
     */
    private Short moduleType;
    /**
     * 1.项目 2.文件
     */
    private Short type;
    /**
     * 主键
     */
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 备注
     */
    private String remark;

    /**
     * 发起人
     */
    private String initiator;

    /**
     * 发起时间
     */
    private Date initiateTime;

    /**
     * 完成时间
     */
    private Date auditFinishTime;

    /**
     * 2.审批完成 3.审批中 4.已撤销
     */
    private Short auditStatus;

    /**
     * 审批详情
     */
    private List<AuditTotalInfoVo> auditTotalInfoVos;


    public Short getModuleType() {
        return moduleType;
    }

    public void setModuleType(Short moduleType) {
        this.moduleType = moduleType;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public Date getInitiateTime() {
        return initiateTime;
    }

    public void setInitiateTime(Date initiateTime) {
        this.initiateTime = initiateTime;
    }

    public Date getAuditFinishTime() {
        return auditFinishTime;
    }

    public void setAuditFinishTime(Date auditFinishTime) {
        this.auditFinishTime = auditFinishTime;
    }

    public Short getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Short auditStatus) {
        this.auditStatus = auditStatus;
    }

    public List<AuditTotalInfoVo> getAuditTotalInfoVos() {
        return auditTotalInfoVos;
    }

    public void setAuditTotalInfoVos(List<AuditTotalInfoVo> auditTotalInfoVos) {
        this.auditTotalInfoVos = auditTotalInfoVos;
    }

    @Override
    public String toString() {
        return "AuditTotalVo{" +
                "moduleType=" + moduleType +
                ", type=" + type +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", remark='" + remark + '\'' +
                ", initiator='" + initiator + '\'' +
                ", initiateTime=" + initiateTime +
                ", auditFinishTime=" + auditFinishTime +
                ", auditStatus=" + auditStatus +
                ", auditTotalInfoVos=" + auditTotalInfoVos +
                '}';
    }
}
