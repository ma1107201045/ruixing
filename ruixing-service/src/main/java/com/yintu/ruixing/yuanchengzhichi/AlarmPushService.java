package com.yintu.ruixing.yuanchengzhichi;

import com.yintu.ruixing.common.util.BaseService;

/**
 * @Author: mlf
 * @Date: 2020/12/23 10:50
 * @Version: 1.0
 */
public interface AlarmPushService extends BaseService<AlarmPushEntity, Integer> {


    void add(AlarmPushEntity entity, Integer[] userIds);

}
