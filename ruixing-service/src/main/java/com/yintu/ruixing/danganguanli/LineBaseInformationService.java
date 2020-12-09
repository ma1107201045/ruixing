package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;

import java.util.List;

/**
 * @Author: mlf
 * @Date: 2020/12/8 15:42
 * @Version: 1.0
 */
public interface LineBaseInformationService extends BaseService<LineBaseInformationEntity, Integer> {

    List<DianWuDuanEntity> findDianWuDuanEntityById(Integer id);

    List<LineBaseInformationEntity> findByExample(Integer[] ids);


}
