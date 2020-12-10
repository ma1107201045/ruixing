package com.yintu.ruixing.yuanchengzhichi;

import com.yintu.ruixing.guzhangzhenduan.BaoJingYuJingBaseEntity;
import com.yintu.ruixing.guzhangzhenduan.CheZhanEntity;
import com.yintu.ruixing.guzhangzhenduan.QuDuanBaseEntity;
import com.yintu.ruixing.guzhangzhenduan.XianDuanEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Alias("alarm")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmEntity implements Serializable {
    private static final long serialVersionUID = -1532247728711658070L;
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

    private Integer platformTime;

    private Integer recoverTime;

    private Boolean status;

    private Integer faultStatus;

    private Integer disposeStatus;

    private Integer idea;

    private String remark;

    private String bjcontext;

    private QuDuanBaseEntity quDuanBaseEntity;

    private CheZhanEntity cheZhanEntity;

    private XianDuanEntity xianDuanEntity;


}