package com.yintu.ruixing.yuanchengzhichi;

import java.util.Date;
import java.util.List;

/**
 * @Author: mlf
 * @Date: 2020/12/9 21:02
 * @Version: 1.0
 */
public interface AlarmService {

    void edit(Integer id, Integer faultStatus, Integer idea, String remark);

    AlarmEntity findById(Integer id);

    List<AlarmEntity> findByExample(Integer tid, Integer did, Integer xid, Date beginTime, Date endTime, AlarmEntity alarmEntityQuery, Integer xtType);
}
