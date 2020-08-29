package com.yintu.ruixing.jiejuefangan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolutionLogEntity implements Serializable {
    private static final long serialVersionUID = -6402576063793057670L;
    private Integer id;

    private String operator;

    private Date operatorTime;

    private Short recordModule;

    private Short recordType;

    private Integer recordTypeId;

    private String context;
    
}