package com.yintu.ruixing.common.vo;

import java.util.Date;
import java.util.List;

/**
 * @Author: mlf
 * @Date: 2020/12/21 15:49
 * @Version: 1.0
 */
public class AuditTotalVo {

    /**
     * 主键
     */
    private int id;

    /**
     * 模块标识
     * <p>
     * 1.解决方案-售前技术支持 2.解决方案-招标投标技术支持 3.解决方案-设计联络及后续技术交流
     */
    private int moduleType;

    /**
     * 1.项目 2.文件
     */
    private int type;

    /**
     * 标题
     */
    private String title;

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
    private Date finishTime;

    /**
     * 2.审批完成 3.审批中 4.已撤销
     */
    private Short auditStatus;

    /**
     * 审批详情
     */
    private List<AuditTotalInfoVo> auditTotalInfoVos;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getModuleType() {
        return moduleType;
    }

    public void setModuleType(int moduleType) {
        this.moduleType = moduleType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
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
}
