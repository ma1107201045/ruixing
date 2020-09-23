package com.yintu.ruixing.danganguanli;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineTechnologyStatusXiangemutypeEntity implements Serializable {
    private static final long serialVersionUID = -7534561541999868501L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private Integer lineTechnologyStatusId;

    private Integer xiangmutypeId;


}