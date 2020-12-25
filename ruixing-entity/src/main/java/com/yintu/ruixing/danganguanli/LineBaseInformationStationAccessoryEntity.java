package com.yintu.ruixing.danganguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineBaseInformationStationAccessoryEntity implements Serializable {
    private static final long serialVersionUID = 7211271823209837608L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private String accessoryName;

    private String accessoryPath;
    @NotNull
    private Integer lineBaseInformationStationId;

    private Short hardwareMaterialCodeId;

    private Short softwareMaterialCodeId;

    private Short configurationFileId;

}