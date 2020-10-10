package com.yintu.ruixing.paigongguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiGongGuanLiCostListEntity {
    private Integer id;

    private String xiangmunumber;

    private String xiangmuname;

    private String paigongnumber;

    private String createname;

    private Date createtime;

    private String updatename;

    private Date updatetime;

    private Integer userid;

    private Date time;

    private BigDecimal costTotal;


}