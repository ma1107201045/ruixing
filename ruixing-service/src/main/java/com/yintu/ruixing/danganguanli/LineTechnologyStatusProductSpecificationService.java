package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/24 18:12
 */
public interface LineTechnologyStatusProductSpecificationService extends BaseService<LineTechnologyStatusProductSpecificationEntity, Integer> {

    List<LineTechnologyStatusProductSpecificationEntity> findAll();
}
