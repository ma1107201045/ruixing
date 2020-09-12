package com.yintu.ruixing.yunxingweihu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SparePartsInfoEntity implements Serializable {
    private static final long serialVersionUID = 6490955410935947363L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private Short isFinish;

    private String documentNames;

    private String documentFiles;

    private Integer sparePartsId;

    private String context;

    private SparePartsEntity sparePartsEntity;

}