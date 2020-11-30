package com.yintu.ruixing.master.guzhangzhenduan;

import com.yintu.ruixing.guzhangzhenduan.TieLuJuEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:lcy
 * @date:2020-05-26 11
 * 铁路局相关dao
 */

public interface TieLuJuDao {
    TieLuJuEntity findTieLuJuById(Long tid);

    void addTieLuJU(TieLuJuEntity tieLuJuEntity);

    void editTieLuJuById(TieLuJuEntity tieLuJuEntity);

    void delTieLuJu(Long tid);

    List<Integer> findId(Long tid);

    List<TieLuJuEntity> findAllTieLuJu();

    Long findTLJid(long parseLong);

    List<TieLuJuEntity> findAllTieLuJuByName(String tljName);

    TieLuJuEntity selectByTljId(Long tljId);

}
