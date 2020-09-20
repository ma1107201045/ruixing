package com.yintu.ruixing.anzhuangtiaoshi;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnZhuangTiaoShiWenTiFileEntity {
    private Integer id;

    private Integer uid;

    private Integer wid;

    private Integer fenlei;

    private String fileName;

    private String filePath;

    private Integer fileState;

    private String yuliu;

    private Integer auditorState;

    private Date createtime;

    private String createname;

    private Date updatetime;

    private String updatename;
}