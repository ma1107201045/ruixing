package com.yintu.ruixing.yuanchengzhichi;

import com.yintu.ruixing.xitongguanli.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmPushUserEntity implements Serializable {
    private static final long serialVersionUID = 1027533637652961642L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;

    private Integer alarmPushId;

    private Integer userId;


}