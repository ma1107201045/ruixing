package com.yintu.ruixing.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditTotalEntity implements Serializable {
    private static final long serialVersionUID = 6029448212998712680L;
    private Integer id;

    private String title;

    private Integer initiatorId;

    private Date initiatorTime;

    private Date finishTime;

    private Short status;

    private Integer moduleId;

    private Short auditType;

    private Integer auditTypeId;

}