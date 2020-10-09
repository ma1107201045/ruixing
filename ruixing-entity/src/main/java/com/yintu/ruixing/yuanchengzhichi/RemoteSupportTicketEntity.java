package com.yintu.ruixing.yuanchengzhichi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoteSupportTicketEntity implements Serializable {
    private static final long serialVersionUID = 8272235721946921132L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;
    @NotNull
    private Short status;

    private Long alarmId;

    private String opinion;

}