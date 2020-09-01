package com.yintu.ruixing.jiejuefangan;

import com.yintu.ruixing.guzhangzhenduan.TieLuJuEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BiddingEntity implements Serializable {
    private static final long serialVersionUID = 6496546618138184802L;
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

    private String bidder;

    private Integer railwayAdministrationId;

    private TieLuJuEntity tieLuJuEntity;

    private Integer preSaleId;


}