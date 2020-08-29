package com.yintu.ruixing.jiejuefangan;

import com.yintu.ruixing.common.util.BaseService;

import java.util.List;

/**
 * @author:mlf
 * @date:2020/8/29 10:21
 */
public interface SolutionLogService extends BaseService<SolutionLogEntity, Integer> {

    List<SolutionLogEntity> findByExample(SolutionLogEntity entity);
}
