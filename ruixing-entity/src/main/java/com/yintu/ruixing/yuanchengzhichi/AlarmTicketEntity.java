package com.yintu.ruixing.yuanchengzhichi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmTicketEntity implements Serializable {
    private static final long serialVersionUID = -1900221034734089765L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private Integer alarmId;

}