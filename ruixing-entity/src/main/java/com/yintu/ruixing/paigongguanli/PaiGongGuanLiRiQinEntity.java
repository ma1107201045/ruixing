package com.yintu.ruixing.paigongguanli;

import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiGongGuanLiRiQinEntity {
    private Integer id;

    private Integer uid;

    private Integer userdongtai;

    private Date starttime;

    private Date endtime;

    private Date creattime;

    private Date updatetime;

    private String username;

    private String danwei;

    private String bumen;

    private String zhiwei;


}