package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/24 9:30
 */
public interface LineTechnologyStatusStationSafetyInformationService extends
        BaseService<LineTechnologyStatusStationSafetyInformationEntity, Integer> {
    long countByStationId(Integer stationId);

    List<LineTechnologyStatusStationSafetyInformationEntity> findByExample(Integer stationId);

}
