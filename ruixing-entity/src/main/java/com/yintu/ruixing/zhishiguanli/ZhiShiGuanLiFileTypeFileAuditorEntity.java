package com.yintu.ruixing.zhishiguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZhiShiGuanLiFileTypeFileAuditorEntity {
    private Integer id;

    private Integer fileId;

    private Integer auditorId;

    private Integer sort;

    private Short activate;

    private String accessoryName;

    private String accessoryPath;

    private Short isDispose;

    private Short auditStatus;

    private Date auditFinishTime;

    private String context;


}