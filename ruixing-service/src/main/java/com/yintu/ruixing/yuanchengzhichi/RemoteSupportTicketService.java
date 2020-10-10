package com.yintu.ruixing.yuanchengzhichi;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/10/9 13:57
 */
public interface RemoteSupportTicketService extends BaseService<RemoteSupportTicketEntity, Integer> {

    void remove(Integer[] ids);

    RemoteSupportTicketEntity findLastByAlarmId(String alarmId);

    List<RemoteSupportTicketEntity> findByCondition(Integer[] ids, Short status, String alarmId);

}
