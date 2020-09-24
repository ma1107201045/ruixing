package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/23 14:55
 */
public interface LineTechnologyStatusStationService extends BaseService<LineTechnologyStatusStationEntity, Integer> {

    List<LineTechnologyStatusStationEntity> findByExample(Integer cid);

    Map<String, Object> findStationStatistics(Integer cid);

    Map<String, Object> findStationInfoAndStatistics(Integer cid, String username);
}
