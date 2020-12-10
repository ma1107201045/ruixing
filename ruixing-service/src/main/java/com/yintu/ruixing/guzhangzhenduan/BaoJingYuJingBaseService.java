package com.yintu.ruixing.guzhangzhenduan;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/9/26 16:40
 * @Version 1.0
 * 需求:
 */
public interface BaoJingYuJingBaseService {
    void addBaoJing(BaoJingYuJingBaseEntity baoJingYuJingBaseEntity, String username);

    List<BaoJingYuJingBaseEntity> findBJYJData(Integer page, Integer size, String context, Integer bjyjType);

    void editBJYJDataByid(BaoJingYuJingBaseEntity baoJingYuJingBaseEntity, String username);

    void deleteBJYJdDataByids(Integer[] ids);

    List<BaoJingYuJingBaseEntity> findAllBaoJing(Integer page, Integer size);

    List<BaoJingYuJingBaseEntity> findBJDataBySomething(Integer page, Integer size, String context);

    String findAlarmContext(Integer alarmcode, Integer bjyjType);
}
