package com.yintu.ruixing.anzhuangtiaoshi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnZhuangTiaoShiWorkNameLibraryEntity {
    private Integer id;

    private String workname;

    private String yuliu1;

    private String yuliu2;



    private Integer userid;
    private Integer auditorState;
    private Integer auditorId;

    private Date createtime;

    private String createname;

    private Date updatetime;

    private String updatename;
}