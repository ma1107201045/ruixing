package com.yintu.ruixing.yuanchengzhichi;

import com.yintu.ruixing.guzhangzhenduan.AlarmEntity;

import java.util.List;

/**
 * @Author: mlf
 * @Date: 2020/12/9 21:02
 * @Version: 1.0
 */
public interface AlarmService {

    List<AlarmEntity> findByExample();
}
