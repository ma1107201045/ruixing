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
    private short bigType;
    /**
     * 搜索内容
     */
    private String search;
    /**
     * 1.解决方案-售前技术支持 2.解决方案-招标投标技术支持 3.解决方案-设计联络及后续技术交流
     */
    private short moduleType;

    /**
     * 1.全部  2.审批完成 3.审批中 4.已撤销
     */
    private short smallType;

    /**
     * 当前登录人id
     */
    private int loginUserId;

    public short getBigType() {
        return bigType;
    }

    public void setBigType(short bigType) {
        this.bigType = bigType;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public short getModuleType() {
        return moduleType;
    }

    public void setModuleType(short moduleType) {
        this.moduleType = moduleType;
    }

    public short getSmallType() {
        return smallType;
    }

    public void setSmallType(short smallType) {
        this.smallType = smallType;
    }

    public int getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(int loginUserId) {
        this.loginUserId = loginUserId;
    }
}
