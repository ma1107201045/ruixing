package com.yintu.ruixing.guzhangzhenduan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author Mr.liu
 * @Date 2020/8/5 18:44
 * @Version 1.0
 * 需求:
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class quduanEntity {

    private Integer createtime;

    private BigDecimal numValue;

}
