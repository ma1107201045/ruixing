package com.yintu.ruixing.guzhangzhenduan;


import com.yintu.ruixing.xitongguanli.UserEntity;

import java.util.List;

/**
 * @author Qiao
 * @date 2020/5/21 17:06
 * @return 以列表的形式展示铁路局、电务段、线段、车站名称service
 */
public interface ListService {


    Object getErJi();

    Object getSanJi();

    /**
     * 故障诊断
     *
     * @param userEntity
     * @return
     */
    List<TieLuJuEntity> findOneTwoDatas(UserEntity userEntity);

    /**
     * 故障诊断
     *
     * @param userEntity
     * @return
     */
    List<DianWuDuanEntity> findXDAndCZByDWDId(Integer dwdid, UserEntity userEntity);

    /**
     * 数据配置左边树
     *
     * @return
     */
    Object getMenuList();
}
