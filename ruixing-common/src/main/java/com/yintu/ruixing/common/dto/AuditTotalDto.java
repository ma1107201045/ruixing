package com.yintu.ruixing.common.dto;

/**
 * @Author: mlf
 * @Date: 2020/12/21 15:48
 * @Version: 1.0
 */
public class AuditTotalDto {

    /**
     * 1.待处理 2.已处理 3.我发起的
     */
    private int totalType;
    /**
     * 搜索内容
     */
    private String search;
    /**
     * 1.解决方案-售前技术支持 2.解决方案-招标投标技术支持 3.解决方案-设计联络及后续技术交流
     */
    private int moduleType;

    /**
     * 1.全部  2.审批完成 3.审批中 4.已撤销
     */
    private int smallType;

    public int getTotalType() {
        return totalType;
    }

    public void setTotalType(int totalType) {
        this.totalType = totalType;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public int getModuleType() {
        return moduleType;
    }

    public void setModuleType(int moduleType) {
        this.moduleType = moduleType;
    }

    public int getSmallType() {
        return smallType;
    }

    public void setSmallType(int smallType) {
        this.smallType = smallType;
    }
}
