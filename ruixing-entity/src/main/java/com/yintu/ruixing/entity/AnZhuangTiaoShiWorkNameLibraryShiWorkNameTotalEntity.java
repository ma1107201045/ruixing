package com.yintu.ruixing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity {
    private Integer id;

    private Integer wntid;//作业配置id

    private Integer wnlid;//作业项id

    private Integer workState;//作业项标识 1：手动， 2：自动',

    private String yuliu1;

    private String yuliu2;

    private AnZhuangTiaoShiWorkNameTotalEntity anZhuangTiaoShiWorkNameTotalEntity;

    private AnZhuangTiaoShiWorkNameLibraryEntity anZhuangTiaoShiWorkNameLibraryEntity;

}