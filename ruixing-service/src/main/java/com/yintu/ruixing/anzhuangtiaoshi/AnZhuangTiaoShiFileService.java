package com.yintu.ruixing.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiFileEntity;

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

    void addFile(AnZhuangTiaoShiFileEntity anZhuangTiaoShiFileEntity, String username,Integer senderid);

    void editFileById(AnZhuangTiaoShiFileEntity anZhuangTiaoShiFileEntity);

    void deletFileByIds(Integer[] ids);

    AnZhuangTiaoShiFileEntity findById(Integer id);

    List<AnZhuangTiaoShiFileEntity> findFileByNmae(Integer page, Integer size, Integer xdid, Integer filetype, String filename);

}
