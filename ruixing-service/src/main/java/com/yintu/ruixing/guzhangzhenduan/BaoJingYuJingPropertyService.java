package com.yintu.ruixing.guzhangzhenduan;

import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.guzhangzhenduan.BaoJingYuJingEntity;
import com.yintu.ruixing.guzhangzhenduan.QuDuanBaseEntity;

import java.util.Date;
import java.util.List;

/**
 * @author:lcy
 * @date:2020-06-17 10
 */
public interface BaoJingYuJingPropertyService {
    List<TreeNodeUtil> findBaoJingYuJingTree(Integer parentId);

    List<BaoJingYuJingEntity> findAllYuJingBaoJing(Integer page, Integer size);

    List<BaoJingYuJingEntity> findYuJingBaoJingBySouSuo(Integer[] ids, Integer sid, Integer qid,
                                                        Date startTime, Date huifuTime, Integer tianChuang,
                                                        Integer lvChuHuiFu, Integer lvChuKaiTong, Integer page, Integer size);

    List<QuDuanBaseEntity> findAllQuDuan();

    Integer findAlarmNumber(String tableName);

    void editAlarmState(AlarmTableEntity alarmTableEntity,String tableName);

    List<AlarmEntity> findAllNotReadAlarmDatas(Integer page, Integer size,String tableName);
}
