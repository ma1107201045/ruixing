package com.yintu.ruixing.yuanchengzhichi;

import com.yintu.ruixing.xitongguanli.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoteSupportTicketPushEntity implements Serializable {
    private static final long serialVersionUID = -1920776281803706164L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private String operator;

    private Short type;

    private Integer ticketId;

    private RemoteSupportTicketEntity remoteSupportTicketEntity;

    private List<UserEntity> userEntities;


}