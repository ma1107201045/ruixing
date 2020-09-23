package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/23 10:34
 */
public interface LineTechnologyStatusXiangemutypeService extends BaseService<LineTechnologyStatusXiangemutypeEntity, Integer> {

    List<LineTechnologyStatusXiangemutypeEntity> findByExample(Integer lineTechnologyStatusId);

}
