package com.yintu.ruixing.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWorkNameTotalEntity;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/7/17 11:13
 * @Version 1.0
 * 需求:
 */
public interface AnZhuangTiaoShiWorkNameTotalService {
    void addWorkNameTotal(AnZhuangTiaoShiWorkNameTotalEntity anZhuangTiaoShiWorkNameTotalEntity, Integer[] uids, String username, Integer receiverid);

    List<AnZhuangTiaoShiWorkNameTotalEntity> findWorkNameTotal(Integer page, Integer size, String workname);

    void editWorkNameTotalById(Integer id,AnZhuangTiaoShiWorkNameTotalEntity anZhuangTiaoShiWorkNameTotalEntity, String username, Integer receiverid);

    void deleteWorkNameTotalByIds(Integer[] ids);

    void addWorkNameEdition(AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity, Integer[] wnlids, String username, Integer receiverid, Integer[] uids);

    List<AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity> findWorkNameById(Integer id, Integer page, Integer size);

    void editWorkNameById(Integer id,AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity, String username, Integer receiverid,Integer[] uids);

    void deleteWorkNameByIds(Integer[] ids);

    List<AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity> findWorkNameByWorkname(String workname, Integer page, Integer size);

    AnZhuangTiaoShiWorkNameTotalEntity findOneWorksById(Integer id, Integer receiverid);

    List<AnZhuangTiaoShiWorksRecordMessageEntity> findRecordMessageById(Integer id);

    void editAuditorByWId(Integer id, AnZhuangTiaoShiWorksAuditorEntity anZhuangTiaoShiWorksAuditorEntity, String username, Integer receiverid, Integer senderId);

    List<AnZhuangTiaoShiWorksRecordMessageEntity> findWorksRecordMessageById(Integer id);

    void editWorksAuditorByWId(Integer id, AnZhuangTiaoShiWorksAuditorEntity anZhuangTiaoShiWorksAuditorEntity, String username, Integer receiverid, Integer senderId);

    AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity findOneWorkNameById(Integer wntid,Integer wnlid,Integer receiverid);
}
