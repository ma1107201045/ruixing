package com.yintu.ruixing.anzhuangtiaoshi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnZhuangTiaoShiWenTiEntity {
    private Integer id;
    private Integer userid;

    private String xdName;

    private String wentiMiaoshu;

    private String fankuiMode;

    private String shoulidanwei;

    private String shejifanwei;

    private Date askovertime;

    private String cuoshifangan;

    private String customerMessage;

    private String shishiplan;

    private Date actualovertime;

    private Integer wentiisover;

    private Integer wentistate;
    private Integer auditorId;

    private String yuliu;
    private String wentiType;

    private Integer auditorState;

    private Date createtime;

    private String createname;

    private Date updatetime;

    private String updatename;


}