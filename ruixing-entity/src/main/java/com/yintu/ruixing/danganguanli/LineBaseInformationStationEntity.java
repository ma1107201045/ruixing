package com.yintu.ruixing.danganguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineBaseInformationStationEntity implements Serializable {
    private static final long serialVersionUID = 3510500225642091756L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private String name;

    private String workshop;

    private String workarea;

    private Integer quduanNum;

    private Integer dianmahuaNum;

    private Integer axlePoints;

    private Integer infoNum;

    private String hardwareMaterialCode;

    private String softwareMaterialCode;

    private Integer equipmentNumber;

    private String terminalVersion;

    private String configurationFile;

    private Date openTime;

    private Double length;

    private String version;

    private Integer lineBaseInformationId;


}