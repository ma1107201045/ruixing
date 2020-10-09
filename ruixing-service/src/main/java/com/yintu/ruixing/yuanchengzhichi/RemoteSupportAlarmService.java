package com.yintu.ruixing.yuanchengzhichi;

import java.util.Date;
import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/10/8 15:01
 */
public interface RemoteSupportAlarmService {

    boolean isTableExist(String tableName);

    void remove(String tableName, Integer id);

    List<RemoteSupportAlarmEntity> findByCondition(String tableName, Integer stationId, Date startTime, Date endTime);

    List<RemoteSupportAlarmEntity> findByCondition(Integer pageNumber, Integer pageSize, Integer stationId, Date startTime, Date endTime);
}
