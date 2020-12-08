package com.yintu.ruixing.danganguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineBaseInformationEntity implements Serializable {
    private static final long serialVersionUID = 4000263142275847007L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private String shortName;

    private Double length;

    private Integer stationNum;

    private Integer quduanNum;

    private Integer dianmahuaNum;

    private Integer axlePoints;

    private Integer infoNum;

    private String technologyState;

    private Date openTime;

    private String guaranteePeriodInformation;

    private String auxiliaryProductInformation;

    private String manufacturerInformation;

    private String fileName;

    private String filePath;

    private String modifySituation;

}