package com.yintu.ruixing.service;

import com.yintu.ruixing.entity.TieLuJuEntity;

import java.util.List;

/**
 * @author:lcy
 * @date:2020-05-26 11
 * 铁路局
 */
public interface TieLuJuService {
    TieLuJuEntity findTieLuJuById(Long id);

    void addTieLuJU(TieLuJuEntity tieLuJuEntity);

    void editTieLuJuById(TieLuJuEntity tieLuJuEntity);

    void delTieLuJu(Long tid);

    List<Integer> findId(Long tid);
}
