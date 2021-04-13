package com.yintu.ruixing.paigongguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiGongGuanLiBaoGongFileEntity {
    private Integer id;

    private Integer bid;

    private String fileName;

    private String filePath;

    private String remark;

    private Double filesize;

    private Integer baogongtype;

    private Date createtime;

    private String createname;

    private Date updatetime;

    private String updatename;


}