package com.yintu.ruixing.jiejuefangan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreSaleFileEntity implements Serializable {
    private static final long serialVersionUID = -3231686510759153555L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;
    @NotEmpty
    private String name;
    @NotEmpty
    private String path;

    private Date uploadDatetime;
    @NotNull
    private Short type;
    @NotNull
    private Short releaseStatus;

    private String remark;
    @NotNull
    private Integer preSaleId;

    private PreSaleEntity preSaleEntity;

    private List<PreSaleFileAuditorEntity> preSaleFileAuditorEntities;


}