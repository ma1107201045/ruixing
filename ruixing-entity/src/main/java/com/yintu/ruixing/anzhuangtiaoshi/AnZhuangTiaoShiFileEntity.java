package com.yintu.ruixing.anzhuangtiaoshi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnZhuangTiaoShiFileEntity {
    private Integer id;

    private Integer xdid;

    private Integer uid;

    private String xdName;

    private Date createtime;

    private Integer fileType;

    private String fileName;

    private String filePath;

    private Integer fabuType;

    private Integer auditorState;

    private String yuliu;

    private String remarks;

    private Integer auditorId;

    private String createName;

    private Date updateTime;

    private String updateName;

}