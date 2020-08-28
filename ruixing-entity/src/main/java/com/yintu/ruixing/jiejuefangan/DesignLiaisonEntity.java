package com.yintu.ruixing.jiejuefangan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
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

    private Date projectDate;

    private Short taskStatus;

    private Date taskFinishDate;

    private String remark;

    private Short meetingStatus;

    private Short changeStatus;

    private String bidder;

    private Integer railwayAdministrationId;

    private Integer biddingId;


}