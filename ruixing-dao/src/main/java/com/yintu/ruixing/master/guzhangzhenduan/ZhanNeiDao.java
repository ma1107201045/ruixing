package com.yintu.ruixing.master.guzhangzhenduan;

import com.yintu.ruixing.guzhangzhenduan.CheZhanEntity;
import com.yintu.ruixing.guzhangzhenduan.QuDuanBaseEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:lcy
 * @date:2020-06-06 19
 * 站内相关
 */

public interface ZhanNeiDao {
    List<QuDuanBaseEntity> findAllDianMaHua(Long id);

    List<CheZhanEntity> findAllWangLuoLianJie();

    void editWangLuoLianJieById(CheZhanEntity cheZhanEntity);
}
