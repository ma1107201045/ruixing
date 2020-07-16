package com.yintu.ruixing.service;

import com.yintu.ruixing.entity.AnZhuangTiaoShiFileEntity;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/7/14 18:06
 * @Version 1.0
 * 需求:
 */
public interface AnZhuangTiaoShiFileService {
    List<AnZhuangTiaoShiFileEntity> findShuRuFileByXid(Integer id, Integer page, Integer size);

    List<AnZhuangTiaoShiFileEntity> findShuChuFileByXid(Integer id, Integer page, Integer size);

    void addFile(AnZhuangTiaoShiFileEntity anZhuangTiaoShiFileEntity);

    void editFileById(AnZhuangTiaoShiFileEntity anZhuangTiaoShiFileEntity);

    void deletFileByIds(Integer[] ids);

    AnZhuangTiaoShiFileEntity findById(Integer id);

    List<AnZhuangTiaoShiFileEntity> findFileByNmae(Integer page, Integer size, Integer xdid, Integer filetype, String filename);

}
