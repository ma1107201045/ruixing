package com.yintu.ruixing.master.guzhangzhenduan;

import com.yintu.ruixing.guzhangzhenduan.QuDuanBaseEntity;
import com.yintu.ruixing.guzhangzhenduan.QuDuanShuXingEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @author:lcy
 * @date:2020-06-11 17
 * 曲线相关
 */

public interface QuXianDao {
    //List<SheBeiEntity> findSheBeiByCid(Integer id);

    List<String> findQuDuanById(Integer id);

    //List<QuDuanInfoEntity> findQuDuanDataByTime(Date time);

    List<QuDuanBaseEntity> findQuDuanDataByTime1(Date time);

    List<QuDuanShuXingEntity> shuXingMing();

    String findShuXingName(Integer shuxingId);

    String findShuXingHanZiName(Integer shuxingId);

    List<String> findDMHQuDuanById(Integer id);

    List<QuDuanShuXingEntity> kaiguanliang();

}
