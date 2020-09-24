package com.yintu.ruixing.danganguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineTechnologyStatusProductEntityWithBLOBs extends LineTechnologyStatusProductEntity {
    private static final long serialVersionUID = -1163438924349541646L;
    private String factoryInfo;

    private String remark;

}