package com.yintu.ruixing.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleJobEntity implements Serializable {
    private static final long serialVersionUID = 3522786559329804896L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private String jobName;

    private String cronExpression;

    private String beanName;

    private String methodName;

    private Integer status;

    private Boolean deleteFlag;

}