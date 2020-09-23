package com.yintu.ruixing.danganguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineTechnologyStatusStationConfigurationEntity implements Serializable {
    private static final long serialVersionUID = -1925125258345265613L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private String docPath;

    private String docName;

}