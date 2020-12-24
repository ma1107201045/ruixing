package com.yintu.ruixing.danganguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineBaseInformationAccessoryEntity implements Serializable {
    private static final long serialVersionUID = -4276682899279197816L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private String accessoryName;

    private String accessoryPath;

    private Integer lineBaseInformationId;

    private Short technologyStateId;

    private Short modifySituationId;

    private Short auxiliaryProductInformationId;

    private Short manufacturerInformationId;


}