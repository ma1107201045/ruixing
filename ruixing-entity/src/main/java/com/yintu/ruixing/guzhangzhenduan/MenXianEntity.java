package com.yintu.ruixing.guzhangzhenduan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenXianEntity implements Serializable {
    private static final long serialVersionUID = 837969792425718533L;
    private Integer id;

    private Integer czId;

    private Integer quduanId;

    private Integer propertyId;

    private String superiorLimitValue;

    private String superiorLimitValueUnit;

    private String lowerLimitValue;

    private String lowerLimitValueUnit;

    private String outburstValue;

    private String outburstValueUnit;

}