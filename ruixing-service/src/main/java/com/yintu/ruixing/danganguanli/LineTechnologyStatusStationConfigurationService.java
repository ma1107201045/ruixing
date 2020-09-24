package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/24 9:57
 */
public interface LineTechnologyStatusStationConfigurationService extends BaseService<LineTechnologyStatusStationConfigurationEntity, Integer> {
    long countByStationId(Integer stationId);

    List<LineTechnologyStatusStationConfigurationEntity> findAll();
}
