package com.yintu.ruixing.master.yuanchengzhichi;

import com.yintu.ruixing.yuanchengzhichi.AlarmTicketEntity;
import com.yintu.ruixing.yuanchengzhichi.AlarmTicketEntityWithBLOBs;

public interface AlarmTicketDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AlarmTicketEntityWithBLOBs record);

    int insertSelective(AlarmTicketEntityWithBLOBs record);

    AlarmTicketEntityWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AlarmTicketEntityWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(AlarmTicketEntityWithBLOBs record);

    int updateByPrimaryKey(AlarmTicketEntity record);
}