package com.yintu.ruixing.jiejuefangan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreSaleFileAuditorEntity implements Serializable {
    private static final long serialVersionUID = -9174666833401218840L;
    private Integer id;

    private Integer preSaleFileId;

    private Integer auditorId;

    private Integer sort;

    private Short activate;


    private String accessoryName;

    private String accessoryPath;

    private String context;


}