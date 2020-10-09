package com.yintu.ruixing.yuanchengzhichi;

import com.yintu.ruixing.guzhangzhenduan.BaoJingYuJingBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoteSupportAlarmEntity implements Serializable {
    private static final long serialVersionUID = 410242960267070253L;
    private Integer id;

    private Integer msgId;

    private Integer sendNum;

    private Integer createtime;

    private Integer stationId;

    private Integer sectionId;

    private Integer alarmcode;

    private Integer reserved1;

    private Integer reserved2;

    private Integer alarmlevel;

    private Boolean status;

    private BaoJingYuJingBaseEntity baoJingYuJingBaseEntity;

    private Short alarmStatus;

}