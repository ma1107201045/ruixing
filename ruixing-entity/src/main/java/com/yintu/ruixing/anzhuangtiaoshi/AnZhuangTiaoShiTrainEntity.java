package com.yintu.ruixing.anzhuangtiaoshi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnZhuangTiaoShiTrainEntity {
    private Integer id;

    private String xdName;

    private String customer;

    private Integer traintype;

    private String traincontent;

    private String trainmode;

    private String trainaddress;

    private Date trainstarttime;

    private Date trainendtime;

    private String yuliu;

    private Date createtime;

    private String createname;

    private Date updatetime;

    private String updatename;

}