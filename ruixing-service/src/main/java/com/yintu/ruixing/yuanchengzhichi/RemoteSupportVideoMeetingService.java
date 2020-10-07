package com.yintu.ruixing.yuanchengzhichi;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/10/7 15:15
 */
public interface RemoteSupportVideoMeetingService extends BaseService<RemoteSupportVideoMeetingEntityWithBLOBs, Integer> {


    void addJoinPerson(Integer id, String joinPerson);

    void addDuration(Integer id, Integer duration, String loginUserName);

    void addOpinion(Integer id, String opinion, String loginUserName);

    void remove(Integer[] ids);

    RemoteSupportVideoMeetingEntityWithBLOBs findByCondition(Integer id);

    List<RemoteSupportVideoMeetingEntityWithBLOBs> findByCondition(Integer[] ids, String context);

}
