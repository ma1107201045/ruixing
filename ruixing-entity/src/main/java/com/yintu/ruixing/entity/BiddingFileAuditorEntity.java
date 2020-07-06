package com.yintu.ruixing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BiddingFileAuditorEntity implements Serializable {

    private static final long serialVersionUID = -5658902656832579108L;
    private Integer id;
    @NotNull
    private Integer biddingFileId;
    @NotNull
    private Integer auditorId;
    @NotNull
    private Short isPass;

}