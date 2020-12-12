package com.yintu.ruixing.yuanchengzhichi;

import java.util.Date;
import java.util.List;

/**
 * @Author: mlf
 * @Date: 2020/12/9 21:02
 * @Version: 1.0
 */
public interface AlarmService {
    void remove(Integer[] ids);

    void edit(AlarmEntity alarmEntity);

    AlarmEntity findById(Integer id);

    void edit(Integer id, Integer idea, String remark);

    void edit(Integer id, Integer faultStatus, Integer disposeStatus, Integer alarmTicketId);

    List<AlarmEntity> findByExample(Integer tid, Integer did, Integer xid, Date beginTime, Date endTime, AlarmEntity alarmEntityQuery, String xtType);

    List<AlarmEntity> findByDisposeStatus();

    long count();
}
