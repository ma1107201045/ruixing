package com.yintu.ruixing.jiejuefangan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DesignLiaisonFileAuditorEntity implements Serializable {
    private static final long serialVersionUID = 441858108475262315L;
    private Integer id;

    private Integer designLiaisonFileId;

    private Integer auditorId;

    private Integer sort;

    private Short activate;


}