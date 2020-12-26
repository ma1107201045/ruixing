package com.yintu.ruixing.common;

import javax.validation.constraints.NotNull;

/**
 * @Author: mlf
 * @Date: 2020/12/21 15:48
 * @Version: 1.0
 */
public class AuditTotalDto {

    /**
     * 1.待处理 2.已处理 3.我发起的
     */
    @NotNull
    private Short bigType;
    /**
     * 搜索内容
     */
    private String search;
    /**
     * 1.解决方案-售前技术支持 2.解决方案-招标投标技术支持 3.解决方案-设计联络及后续技术交流
     * 4.知识管理 7.档案管理-顾客档案管理
     */
    private Short moduleType;

    /**
     * 1.全部  3.审批完成 2.审批中 4.已撤销
     */
    private Short smallType;

    /**
     * 当前登录人id
     */
    private Integer loginUserId;

    public Short getBigType() {
        return bigType;
    }

    public void setBigType(Short bigType) {
        this.bigType = bigType;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Short getModuleType() {
        return moduleType;
    }

    public void setModuleType(Short moduleType) {
        this.moduleType = moduleType;
    }

    public Short getSmallType() {
        return smallType;
    }

    public void setSmallType(Short smallType) {
        this.smallType = smallType;
    }

    public Integer getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(Integer loginUserId) {
        this.loginUserId = loginUserId;
    }
}
