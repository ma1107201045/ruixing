package com.yintu.ruixing.paigongguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiGongGuanLiUserEntity {
    private Long id;

    private Integer userid;

    private String name;

    private String department;

    private Integer isdelete;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;


    private Date ccStatrTime;
    private Date ccEndTime;


}