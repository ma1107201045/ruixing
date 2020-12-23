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

    void add(LineBaseInformationEntity lineBaseInformationEntity, Integer[] unitIds);

    void edit(LineBaseInformationEntity lineBaseInformationEntity, Integer[] unitIds);

    List<Map<String, Object>> findRailwaysBureauTid();

    List<Map<String, Object>> findByTid(Integer tid);

    List<Map<String, Object>> findStationById(Integer id);

    List<TreeNodeUtil> findTree();

    LineBaseInformationEntity findNewVersionByTid(Integer tid);

    List<LineBaseInformationEntity> findByExample(Integer[] ids);


    List<DianWuDuanEntity> findDianWuDuanEntityById(Integer id);


}
