package com.yintu.ruixing.yunxingweihu;

public enum TaskEnum {

    MAINTENANCEPLAN("维护计划", "maintenancePlanTask", "用于周期性检查维护记录情况"),
    SPARETEST("备品实验", "spareTestTask", "用于周期性检查备品更换情况");

    private final String name;
    private final String value;
    private final String desc;

    TaskEnum(String name, String value, String desc) {
        this.name = name;
        this.value = value;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
