package com.yintu.ruixing.master.guzhangzhenduan;

import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author:lcy
 * @date:2020-05-26 11
 * 电务段相关dao
 */

public interface DianWuDuanDao {
    DianWuDuanEntity findDianWuDuanById(Long did);

    void addDianWuDuan(DianWuDuanEntity dianWuDuanEntity);

    void editDianWuDuan(DianWuDuanEntity dianWuDuanEntity);

    void delDianWuDuan(Long did);

    List<Integer> findId(Long did);

    List<DianWuDuanEntity> findDianWuDuan();

    Long dwdid(@Param("parseLong") long parseLong, @Param("parseLong1") long parseLong1);

    List<DianWuDuanEntity> findDianWuDuanByName(String dwdname);

    List<DianWuDuanEntity> findDianWuDuanBydid(@Param("parseLong") long parseLong, @Param("parseLong1") long parseLong1);

    String findDWDJsonByDid(@Param("did") Integer did);

    List<DianWuDuanEntity> selectAll();
}
