package com.yintu.ruixing.paigongguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiGongGuanLiCostEntity {
    private Integer id;

    private Integer cid;

    private String name;

    private BigDecimal cost;


}