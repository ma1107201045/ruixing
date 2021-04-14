package com.yintu.ruixing.paigongguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiGongGuanLiUserDaystateEntity {
    private Integer id;

    private Integer userid;

    private String username;

    private String riqi;

    private Integer daystate;

    private Integer otherState;

    private Integer baogongState;

    private Date createtime;

    private String createname;

    private Date updatetime;

    private String updatename;

    private List<PaiGongGuanLiUserDaystateEntity> userlist;



}