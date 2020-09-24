package com.yintu.ruixing.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWorkNameLibraryEntity;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/7/16 16:51
 * @Version 1.0
 * 需求:
 */
public interface AnZhuangTiaoShiWorkNameLibraryService {
    void addWorkName(AnZhuangTiaoShiWorkNameLibraryEntity anZhuangTiaoShiWorkNameLibraryEntity, Integer[] uids, String username, Integer receiverid);

    List<AnZhuangTiaoShiWorkNameLibraryEntity> findWorkName(Integer page, Integer size, String workname);

    void editWorkNameById(Integer id,AnZhuangTiaoShiWorkNameLibraryEntity anZhuangTiaoShiWorkNameLibraryEntity, String username, Integer receiverid, Integer[] uids);

    void deleteWorkNameByIds(Integer[] ids);

    List<AnZhuangTiaoShiWorkNameLibraryEntity> findAllWorkName();

    List<AnZhuangTiaoShiWorkNameLibraryEntity> findAllWorksById(Integer id);

    AnZhuangTiaoShiWorkNameLibraryEntity findWorkNameById(Integer id);

    List<AnZhuangTiaoShiWorksRecordMessageEntity> findRecordMessageById(Integer id);

    void editAuditorByWId(Integer id, AnZhuangTiaoShiWorksAuditorEntity anZhuangTiaoShiWorksAuditorEntity, String username, Integer receiverid, Integer senderId);
}
