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

    void editAlarmState(AlarmTableEntity alarmTableEntity);

    List<AlarmEntity> findAllNotReadAlarmDatas(Integer page, Integer size);

    List<AlarmEntity> findAllHistoryAlarmDatas(Integer page, Integer size, String tableName);

    List<AlarmEntity> findSomeAlarmDatasByChoose(Date starTime, Date endTime, Integer dwdid, Integer xdid, Integer czid, Integer page, Integer size);

    Integer findAllAlarmNumber();

    Integer findAllAlarmNumberByDWDid(Integer dwdid);

    Integer findAllAlarmNumberByXDid(Integer dwdid,Integer xdid);

    List<AlarmEntity> findAllNotReadAlarmDatasByCZid(Integer page, Integer size, Integer czid);
}
