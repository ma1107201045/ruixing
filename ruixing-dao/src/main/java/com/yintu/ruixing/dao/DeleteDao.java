package com.yintu.ruixing.dao;

/**
 * @author:lcy
 * @date:2020-06-02 20
 * 分页
 */
public interface DeleteDao {
/*
    List<DataStatsEntity> tljPage();

    List<DataStatsEntity> dwdPage();

    List<DataStatsEntity> czPage();

    List<DataStatsEntity> xdPage();*/

    int delTieLuJU(int[] ids);

    int delDianDuDuan(int[] ids);

    int delXianDuan(int[] ids);

    int delCheZhan(int[] ids);
}
