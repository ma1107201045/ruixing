package com.yintu.ruixing.chanpinjiaofu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class ChanPinJiaoFuCostEntity {
    private Integer id;

    private String xiangmunumber;

    private String xiangmuname;

    private BigDecimal salesincome;

    private BigDecimal beipinsalesincome;

    private BigDecimal otherincome;

    private BigDecimal directoutcome;

    private BigDecimal yunzaoutcome;

    private BigDecimal testingoutcome;

    private BigDecimal otheroutcome;

    private Date createTime;

    private String createName;

    private String updateName;

    private Date updateTime;

}