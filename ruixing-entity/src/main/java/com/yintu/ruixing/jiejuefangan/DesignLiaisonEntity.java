package com.yintu.ruixing.jiejuefangan;

import com.yintu.ruixing.guzhangzhenduan.TieLuJuEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DesignLiaisonEntity implements Serializable {
    private static final long serialVersionUID = -1651432067056769490L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;
    @NotEmpty
    private String projectName;

    private Short projectStatus;
    @NotNull
    private Date projectDate;

    private Short taskStatus;

    private Date taskFinishDate;

    private String remark;

    private Short meetingStatus;

    private Short changeStatus;

    private String bidder;

    private Integer railwayAdministrationId;

    private TieLuJuEntity tieLuJuEntity;

    private Integer biddingId;


}