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
    private static final long serialVersionUID = 7286806262496745258L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private String title;

    private Short type;

    private Short smallType;

    private Short status;

    private String context;

}