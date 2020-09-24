package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/24 9:26
 */
public interface LineTechnologyStatusStationUnitService extends BaseService<LineTechnologyStatusStationUnitEntity, Integer> {

    long countByStationId(Integer stationId);

    List<LineTechnologyStatusStationUnitEntity> findAll();
}
