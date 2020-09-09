package com.yintu.ruixing.yunxingweihu;

import java.util.Date;

public class RunningAttentionLogEntity {
    private Integer id;

    private String operator;

    private Date operatorTime;

    private Short recordModule;

    private Integer recordTypeId;

    private String context;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Date getOperatorTime() {
        return operatorTime;
    }

    public void setOperatorTime(Date operatorTime) {
        this.operatorTime = operatorTime;
    }

    public Short getRecordModule() {
        return recordModule;
    }

    public void setRecordModule(Short recordModule) {
        this.recordModule = recordModule;
    }

    public Integer getRecordTypeId() {
        return recordTypeId;
    }

    public void setRecordTypeId(Integer recordTypeId) {
        this.recordTypeId = recordTypeId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context == null ? null : context.trim();
    }
}