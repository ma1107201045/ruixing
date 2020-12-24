package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

/**
 * @Author: mlf
 * @Date: 2020/12/24 9:26
 * @Version: 1.0
 */
public interface LineBaseInformationAccessoryService extends BaseService<LineBaseInformationAccessoryEntity, Integer> {

    List<LineBaseInformationAccessoryEntity> findByExample(LineBaseInformationAccessoryEntity query);
}
