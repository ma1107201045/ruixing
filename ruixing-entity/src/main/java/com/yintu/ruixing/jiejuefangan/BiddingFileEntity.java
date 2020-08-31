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
public class BiddingFileEntity implements Serializable {
    private static final long serialVersionUID = 1980809989337498350L;
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
    @NotNull
    private Integer biddingId;

    private BiddingEntity biddingEntity;

    private List<BiddingFileAuditorEntity> biddingFileAuditorEntities;
}