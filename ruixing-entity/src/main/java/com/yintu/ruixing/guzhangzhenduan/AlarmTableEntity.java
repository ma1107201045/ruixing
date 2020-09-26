package com.yintu.ruixing.guzhangzhenduan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmTableEntity {
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

    private Integer status;

}