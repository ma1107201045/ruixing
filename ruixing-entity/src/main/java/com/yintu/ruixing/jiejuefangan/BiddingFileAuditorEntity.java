package com.yintu.ruixing.jiejuefangan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BiddingFileAuditorEntity implements Serializable {
    private static final long serialVersionUID = -2006822035386161184L;
    private Integer id;

    private Integer biddingFileId;

    private Integer auditorId;

    private Integer sort;

    private Short activate;


}