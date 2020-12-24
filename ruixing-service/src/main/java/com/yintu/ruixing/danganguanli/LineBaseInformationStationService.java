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
    void add(LineBaseInformationEntity lineBaseInformationEntity, Integer[] unitIds);

    void edit(LineBaseInformationEntity lineBaseInformationEntity, Integer[] unitIds);

    List<LineBaseInformationEntity> findHistoryByExample(Integer tid, Integer id, String name, Integer[] ids);


    List<DianWuDuanEntity> findDianWuDuanEntityById(Integer id);

}
