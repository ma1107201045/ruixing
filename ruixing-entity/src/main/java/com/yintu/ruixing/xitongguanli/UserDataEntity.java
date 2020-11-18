package com.yintu.ruixing.xitongguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDataEntity implements Serializable {
    private static final long serialVersionUID = -6733097471283268803L;
    private Long id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private Long userId;

    private Long tId;

    private Long dId;

    private Long xId;

    private Long cId;

}