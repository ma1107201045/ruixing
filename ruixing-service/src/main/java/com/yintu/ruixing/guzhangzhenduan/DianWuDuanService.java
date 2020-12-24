package com.yintu.ruixing.guzhangzhenduan;

import java.util.List;

/**
 * @author:lcy
 * @date:2020-05-26 11
 * 电务段
 */
public interface DianWuDuanService {
    DianWuDuanEntity findDianWuDuanById(Long did);

    void addDianWuDuan(DianWuDuanEntity dianWuDuanEntity);

    void editDianWuDuan(DianWuDuanEntity dianWuDuanEntity);

    void delDianWuDuan(Long did);

    List<Integer> findId(Long did);

    List<DianWuDuanEntity> findAll();
}
