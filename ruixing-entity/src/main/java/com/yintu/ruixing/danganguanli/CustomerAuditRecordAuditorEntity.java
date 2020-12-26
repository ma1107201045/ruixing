package com.yintu.ruixing.danganguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAuditRecordAuditorEntity implements Serializable {
    private static final long serialVersionUID = 8691008472271372446L;
    private Integer id;

    private Integer customerAuditRecordId;

    private Integer auditorId;

    private Integer sort;

    private Short activate;

    private String accessoryName;

    private String accessoryPath;

    private Short isDispose;

    private Short auditStatus;

    private Date auditFinishTime;

    private String context;

}