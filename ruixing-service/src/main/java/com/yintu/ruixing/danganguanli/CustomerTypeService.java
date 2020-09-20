package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/20 11:25
 */
public interface CustomerTypeService extends BaseService<CustomerTypeEntity, Integer> {


    List<CustomerTypeEntity> findAll();
}
