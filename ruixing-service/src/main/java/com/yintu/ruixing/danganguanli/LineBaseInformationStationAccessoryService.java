package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

/**
 * @Author: mlf
 * @Date: 2020/12/24 18:37
 * @Version: 1.0
 */
public interface LineBaseInformationStationAccessoryService extends BaseService<LineBaseInformationStationAccessoryEntity, Integer> {

    List<LineBaseInformationStationAccessoryEntity> findByExample(LineBaseInformationStationAccessoryEntity query);

}
