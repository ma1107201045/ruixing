package com.yintu.ruixing.yuanchengzhichi;

import com.yintu.ruixing.xitongguanli.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmPushEntity implements Serializable {
    private static final long serialVersionUID = 2266958210981320338L;
    private Integer id;

    private String createBy;

    private Date createTime;

    private String modifiedBy;

    private Date modifiedTime;
    @NotNull
    private Integer alarmId;

    private String context;

    private AlarmEntity alarmEntity;

    private List<UserEntity> userEntities;

}