package com.yintu.ruixing.common.enumobject;

public enum EnumModule {


    JJFA("解决方案", (short) 1, "解决方案消息模块标识"),
    CPJF("产品交付", (short) 1, "解决方案消息模块标识");

    private final String name;
    private final Short value;
    private final String description;


    EnumModule(String name, Short value, String description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public Short getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

}
