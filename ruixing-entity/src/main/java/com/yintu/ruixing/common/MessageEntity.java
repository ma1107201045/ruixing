package com.yintu.ruixing.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity implements Serializable {
    private static final long serialVersionUID = -8394870858990881942L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private String title;

    private Short type;

    private Short smallType;

    private Short messageType;

    private Integer projectId;

    private Integer fileId;

    private Integer senderId;

    private Integer receiverId;

    private Short status;

    private String context;

}