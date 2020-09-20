package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/8/10 14:28
 */
public interface CustomerDutyService extends BaseService<CustomerDutyEntity, Integer> {

    void remove(Integer[] ids);

    List<CustomerDutyEntity> findByExample(String name);
}
