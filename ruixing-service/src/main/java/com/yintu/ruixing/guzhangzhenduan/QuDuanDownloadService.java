package com.yintu.ruixing.guzhangzhenduan;

import com.yintu.ruixing.common.util.BaseService;

import java.util.Date;
import java.util.List;

/**
 * @author:mlf
 * @date:2020/6/8 15:29
 */
public interface QuDuanDownloadService extends BaseService<QuDuanDownloadEntity, Integer> {


    void add(Integer czId, Short type, Date startDateTime, Date endDateTime);


    List<QuDuanDownloadEntity> findByDateTime(Integer czId, Date startDateTime, Date endDateTime);


//    QuDuanDownloadEntity findByCzIdAndUserId(Integer czId, Integer userId);
//
//    Integer changeDataStatus(Integer czId, Integer userId, Short dataStatus);
//
//    void changeSwitchStatus(Integer czId, Integer userId, Short switchStatus);
//
//    Short changeUpdateTime(Integer czId, Integer userId);
}
