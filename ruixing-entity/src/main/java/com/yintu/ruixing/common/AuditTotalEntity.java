package com.yintu.ruixing.common;

import java.io.Serializable;
import java.util.Date;

public class AuditTotalEntity implements Serializable {
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