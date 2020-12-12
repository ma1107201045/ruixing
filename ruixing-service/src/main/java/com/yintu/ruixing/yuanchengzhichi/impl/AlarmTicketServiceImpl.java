package com.yintu.ruixing.yuanchengzhichi.impl;

import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.master.yuanchengzhichi.AlarmTicketDao;
import com.yintu.ruixing.yuanchengzhichi.AlarmService;
import com.yintu.ruixing.yuanchengzhichi.AlarmTicketEntityWithBLOBs;
import com.yintu.ruixing.yuanchengzhichi.AlarmTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

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
    public void edit(AlarmTicketEntityWithBLOBs alarmTicketEntityWithBLOBs) {
        alarmTicketDao.updateByPrimaryKeySelective(alarmTicketEntityWithBLOBs);
    }

    @Override
    public void add(AlarmTicketEntityWithBLOBs alarmTicketEntityWithBLOBs, Integer[] alarmIds, Integer[] faultStatus, Integer[] disposeStatus) {
        if (disposeStatus.length > 0 && disposeStatus[0] == 1)
            throw new BaseRuntimeException("处置状态不能为未处理");
        this.add(alarmTicketEntityWithBLOBs);
        for (int i = 0; i < alarmIds.length; i++) {
            alarmService.edit(alarmIds[i], faultStatus[i], disposeStatus[i], alarmTicketEntityWithBLOBs.getId());
        }
    }

    @Override
    public void edit(AlarmTicketEntityWithBLOBs alarmTicketEntityWithBLOBs, Integer alarmId, Integer faultStatus, Integer disposeStatus) {
        if (alarmTicketEntityWithBLOBs.getId() == null && faultStatus == 2) {
            alarmTicketEntityWithBLOBs.setCreateBy(alarmTicketEntityWithBLOBs.getModifiedBy());
            alarmTicketEntityWithBLOBs.setCreateTime(alarmTicketEntityWithBLOBs.getModifiedTime());
            this.add(alarmTicketEntityWithBLOBs);
        } else {
            this.edit(alarmTicketEntityWithBLOBs);
        }
        alarmService.edit(alarmId, faultStatus, disposeStatus, alarmTicketEntityWithBLOBs.getId());
    }

    @Override
    public AlarmTicketEntityWithBLOBs findById(Integer id) {
        return alarmTicketDao.selectByPrimaryKey(id);
    }

}
