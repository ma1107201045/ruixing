package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;

import java.util.List;

/**
 * @Author: mlf
 * @Date: 2020/12/24 18:07
 * @Version: 1.0
 */
public interface LineBaseInformationStationService extends BaseService<LineBaseInformationStationEntity, Integer> {
    void add(LineBaseInformationStationEntity lineBaseInformationStationEntity, Integer[] unitIds);

    void edit(LineBaseInformationStationEntity lineBaseInformationStationEntity, Integer[] unitIds);


    List<LineBaseInformationStationEntity> findHistoryByExample(Integer lineBaseInformationId, Integer id, String name, Integer[] ids);

    List<DianWuDuanEntity> findDianWuDuanEntityById(Integer id);

}
