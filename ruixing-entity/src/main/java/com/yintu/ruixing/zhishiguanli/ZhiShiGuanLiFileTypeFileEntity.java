package com.yintu.ruixing.zhishiguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZhiShiGuanLiFileTypeFileEntity {
    private Integer id;

    private Integer parentid;

    private Integer tid;

    private Integer userid;

    private Integer auditstatus;

    private Integer filesize;

    private String fileName;

    private String filePath;

    private Date createtime;

    private Integer fabuType;

    private String yuliu;

    private String reason;

    private String remark;

    private String createName;
    private String updateName;
    private Date updateTime;


}