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

    void editWorkNameById(Integer id,AnZhuangTiaoShiWorkNameLibraryEntity anZhuangTiaoShiWorkNameLibraryEntity, String username, Integer receiverid);

    void deleteWorkNameByIds(Integer[] ids);

    List<AnZhuangTiaoShiWorkNameLibraryEntity> findAllWorkName();

    List<AnZhuangTiaoShiWorkNameLibraryEntity> findAllWorksById(Integer id);

    AnZhuangTiaoShiWorkNameLibraryEntity findWorkNameById(Integer id,Integer receiverid);

    List<AnZhuangTiaoShiWorksRecordMessageEntity> findRecordMessageById(Integer id);

    void editAuditorByWId(Integer id, AnZhuangTiaoShiWorksAuditorEntity anZhuangTiaoShiWorksAuditorEntity, String username, Integer receiverid, Integer senderId);

    List<AnZhuangTiaoShiWorksRecordMessageEntity> findWorkNameLibraryRecordMessageById(Integer id);

    List<AnZhuangTiaoShiCheZhanXiangMuTypeEntity> findAllXiangMuType();
}
