package com.yintu.ruixing.yuanchengzhichi;

import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: mlf
 * @Date: 2020/12/10 15:36
 * @Version: 1.0
 */
public interface AlarmTicketService {

    void add(AlarmTicketEntityWithBLOBs alarmTicketEntityWithBLOBs);

    void add(Integer alarmId, Integer faultStatus, Integer disposeStatus, AlarmTicketEntityWithBLOBs alarmTicketEntityWithBLOBs);
}
