package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/24 9:47
 */
public interface LineTechnologyStatusStationDeviceService extends BaseService<LineTechnologyStatusStationDeviceEntity, Integer> {

    long countByStationId(Integer stationId);

    List<LineTechnologyStatusStationDeviceEntity> findByExample(Integer stationId);

}
