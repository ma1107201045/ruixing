package com.yintu.ruixing.yuanchengzhichi;

/**
 * @Author: mlf
 * @Date: 2020/12/10 15:36
 * @Version: 1.0
 */
public interface AlarmTicketService {

    void add(AlarmTicketEntityWithBLOBs alarmTicketEntityWithBLOBs);

    void edit(AlarmTicketEntityWithBLOBs alarmTicketEntityWithBLOBs);

    void add(AlarmTicketEntityWithBLOBs alarmTicketEntityWithBLOBs, Integer[] alarmIds, Integer[] faultStatus, Integer[] disposeStatus);

    void edit(AlarmTicketEntityWithBLOBs alarmTicketEntityWithBLOBs, Integer alarmId, Integer faultStatus, Integer disposeStatus);


    AlarmTicketEntityWithBLOBs findById(Integer id);
}
