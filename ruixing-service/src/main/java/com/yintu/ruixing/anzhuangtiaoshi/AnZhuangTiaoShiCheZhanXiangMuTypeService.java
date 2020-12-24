package com.yintu.ruixing.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiCheZhanXiangMuTypeEntity;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/7/11 17:39
 * @Version 1.0
 * 需求:安装调试项目类型
 */
public interface AnZhuangTiaoShiCheZhanXiangMuTypeService {
    void addXiangMuType(AnZhuangTiaoShiCheZhanXiangMuTypeEntity anZhuangTiaoShiCheZhanXiangMuTypeEntity);

    List<AnZhuangTiaoShiCheZhanXiangMuTypeEntity> findAllXiangMuType(Integer page, Integer size);

    void editXiangMuTypeById(AnZhuangTiaoShiCheZhanXiangMuTypeEntity anZhuangTiaoShiCheZhanXiangMuTypeEntity);

    void deletXiangMuTypeByIds(Integer[] ids);

    List<AnZhuangTiaoShiCheZhanXiangMuTypeEntity> findXiangMuTypeByName(String xmname, Integer page, Integer size);

    List<AnZhuangTiaoShiCheZhanXiangMuTypeEntity> findAll();


}
