package com.yintu.ruixing.common;

import javax.validation.constraints.NotNull;

/**
 * @Author: mlf
 * @Date: 2020/12/21 17:35
 * @Version: 1.0
 */
public class AuditDto {


    /**
     * 模块标识
     * 1.解决方案-售前技术支持 2.解决方案-招标投标技术支持 3.解决方案-设计联络及后续技术交流
     * 4.知识管理-文件审核 7.档案管理-顾客档案管理信息修改 5.产品交付-项目交付状态管理-项目状态变更
     * 6.产品交付-项目交付状态管理-输出文件发布
     */
    @NotNull
    private Short moduleType;
    /**
     * 1.项目 2.文件 3.顾客档案
     */
    @NotNull
    private Short type;

    /**
     * 主键
     */
    @NotNull
    private Integer id;


    /**
     * 2.审批完成 3.审批中 4.已撤销 5.转交
     */
    private Short isPass;
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
     * 转交人id
     */
    private Integer passUserId;
    /**
     * 登录id
     */
    private Integer loginUserId;
    /**
     * 登录用户名
     */
    private String userName;
    /**
     * 登录真实姓名
     */
    private String trueName;

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

    public Short getIsPass() {
        return isPass;
    }

    public void setIsPass(Short isPass) {
        this.isPass = isPass;
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

    public Integer getPassUserId() {
        return passUserId;
    }

    public void setPassUserId(Integer passUserId) {
        this.passUserId = passUserId;
    }

    public Integer getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(Integer loginUserId) {
        this.loginUserId = loginUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }
}
