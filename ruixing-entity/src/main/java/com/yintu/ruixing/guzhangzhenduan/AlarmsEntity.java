package com.yintu.ruixing.guzhangzhenduan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author Mr.liu
 * @Date 2020/9/26 19:56
 * @Version 1.0
 * 需求:  报警预警统计
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmsEntity {

    private Integer bjyjType;

    private String bjyjcontext;

    private String quduan;

    private Date opentime;

    private Date endtime;

    private Integer isnotsky;

    private  Integer czid;

    private String czName;

    private Integer quid;


}
