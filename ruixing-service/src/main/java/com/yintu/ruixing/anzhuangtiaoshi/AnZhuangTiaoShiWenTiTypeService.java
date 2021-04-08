package com.yintu.ruixing.anzhuangtiaoshi;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2021/4/6 15:15
 * @Version 1.0
 * 需求:
 */
public interface AnZhuangTiaoShiWenTiTypeService {
    void addWenTiType(AnZhuangTiaoShiWenTiTypeEntity anZhuangTiaoShiWenTiTypeEntity);

    List<AnZhuangTiaoShiWenTiEntity> findWenTiType(String wenTiName, Integer typeId);

    void editById(AnZhuangTiaoShiWenTiTypeEntity anZhuangTiaoShiWenTiTypeEntity);

    void deleteByIds(Integer[] ids);

    List<AnZhuangTiaoShiWenTiEntity> findAllType(Integer typeId);
}
