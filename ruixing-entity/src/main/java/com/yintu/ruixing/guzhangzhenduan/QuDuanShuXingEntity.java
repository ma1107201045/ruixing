package com.yintu.ruixing.guzhangzhenduan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author:lcy
 * @date:2020-06-17 18
 * 区段属性  电压电流温度
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuDuanShuXingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String sqlname;

    private Integer type;

    private Integer mid;
}
