package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;

/**
 * @Author: mlf
 * @Date: 2020/12/24 18:14
 * @Version: 1.0
 */
public interface LineBaseInformationStationUnitService extends BaseService<LineBaseInformationStationUnitEntity, Integer> {

    void removeByLineBaseInformationStationId(Integer lineBaseInformationStationId);
}
