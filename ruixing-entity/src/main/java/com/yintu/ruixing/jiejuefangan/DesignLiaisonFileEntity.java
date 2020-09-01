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
public class DesignLiaisonFileEntity implements Serializable {
    private static final long serialVersionUID = -9142910497691464517L;
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

    private Short releaseStatus;

    private Integer userId;

    private String remark;

    @NotNull
    private Integer designLiaisonId;

    private DesignLiaisonEntity designLiaisonEntity;

    private List<DesignLiaisonFileAuditorEntity> designLiaisonFileAuditorEntities;

}