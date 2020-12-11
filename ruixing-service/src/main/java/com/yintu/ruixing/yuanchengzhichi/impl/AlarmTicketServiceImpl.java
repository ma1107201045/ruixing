package com.yintu.ruixing.yuanchengzhichi.impl;

import com.yintu.ruixing.master.yuanchengzhichi.AlarmTicketDao;
import com.yintu.ruixing.yuanchengzhichi.AlarmEntity;
import com.yintu.ruixing.yuanchengzhichi.AlarmService;
import com.yintu.ruixing.yuanchengzhichi.AlarmTicketEntityWithBLOBs;
import com.yintu.ruixing.yuanchengzhichi.AlarmTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: mlf
 * @Date: 2020/12/10 17:27
 * @Version: 1.0
 */
@Service
@Transactional
public class AlarmTicketServiceImpl implements AlarmTicketService {
    @Autowired
    private AlarmTicketDao alarmTicketDao;
    @Autowired
    private AlarmService alarmService;

    @Override
    public void add(AlarmTicketEntityWithBLOBs alarmTicketEntityWithBLOBs) {
        alarmTicketDao.insertSelective(alarmTicketEntityWithBLOBs);
    }

    @Override
    public void add(Integer alarmId, Integer faultStatus, Integer disposeStatus, AlarmTicketEntityWithBLOBs alarmTicketEntityWithBLOBs) {

    }
}
