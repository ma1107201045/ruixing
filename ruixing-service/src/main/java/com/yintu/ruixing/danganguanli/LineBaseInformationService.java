package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;

import java.util.List;
import java.util.Map;

/**
 * @Author: mlf
 * @Date: 2020/12/8 15:42
 * @Version: 1.0
 */
public interface LineBaseInformationService extends BaseService<LineBaseInformationEntity, Integer> {

    List<Map<String, Object>> findDistinctTid();

    List<Map<String, Object>> findByTid(Integer tid);

    List<Map<String, Object>> findStationById(Integer id);

    List<TreeNodeUtil> findTree();


    List<DianWuDuanEntity> findDianWuDuanEntityById(Integer id);

    List<LineBaseInformationEntity> findByExample(Integer[] ids);


}
