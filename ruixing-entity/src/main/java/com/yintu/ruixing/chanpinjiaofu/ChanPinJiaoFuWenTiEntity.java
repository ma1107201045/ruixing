package com.yintu.ruixing.chanpinjiaofu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChanPinJiaoFuWenTiEntity {
    private Integer id;

    private String xiangmuBianhao;//项目编号

    private String xiangmuName;//项目名

    private String wentihuanjie;//问题环节

    private String cunzaiwenti;//存在问题

    private String peihebumen;//配合部门

    private String waibudanwei;//需要配合的外部单位

    private String jihua;//工作计划

    private Integer state;//状态更新 1：已更新， 2：未更新',

    private Integer wentiState;//问题状态 1：打开，2：关闭',

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getXiangmuName() {
        return xiangmuName;
    }

    public void setXiangmuName(String xiangmuName) {
        this.xiangmuName = xiangmuName == null ? null : xiangmuName.trim();
    }

    public String getWentihuanjie() {
        return wentihuanjie;
    }

    public void setWentihuanjie(String wentihuanjie) {
        this.wentihuanjie = wentihuanjie == null ? null : wentihuanjie.trim();
    }

    public String getCunzaiwenti() {
        return cunzaiwenti;
    }

    public void setCunzaiwenti(String cunzaiwenti) {
        this.cunzaiwenti = cunzaiwenti == null ? null : cunzaiwenti.trim();
    }

    public String getPeihebumen() {
        return peihebumen;
    }

    public void setPeihebumen(String peihebumen) {
        this.peihebumen = peihebumen == null ? null : peihebumen.trim();
    }

    public String getWaibudanwei() {
        return waibudanwei;
    }

    public void setWaibudanwei(String waibudanwei) {
        this.waibudanwei = waibudanwei == null ? null : waibudanwei.trim();
    }

    public String getJihua() {
        return jihua;
    }

    public void setJihua(String jihua) {
        this.jihua = jihua == null ? null : jihua.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getWentiState() {
        return wentiState;
    }

    public void setWentiState(Integer wentiState) {
        this.wentiState = wentiState;
    }
}