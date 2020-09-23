package com.yintu.ruixing.danganguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineTechnologyStatusStationEntity implements Serializable {
    private static final long serialVersionUID = -3410948774067488898L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private String skillShaftCount;

    private String maintenanceTerminal;

    private Date openDate;

    private String lenght;

    private Integer cid;

}