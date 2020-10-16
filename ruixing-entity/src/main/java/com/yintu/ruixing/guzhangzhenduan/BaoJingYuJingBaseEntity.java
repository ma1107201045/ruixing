package com.yintu.ruixing.guzhangzhenduan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaoJingYuJingBaseEntity {
    private Integer id;

    private Integer bjnumber;

    private String bjcontext;

    private Integer bjyjtype;

    private String userange;

    private Date createtime;

    private String createname;

    private Date updatetime;

    private String updatename;


}