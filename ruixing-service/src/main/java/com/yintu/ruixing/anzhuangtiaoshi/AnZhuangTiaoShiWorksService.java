package com.yintu.ruixing.anzhuangtiaoshi;

import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.anzhuangtiaoshi.*;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/7/27 19:38
 * @Version 1.0
 * 需求:安装调试现场作业
 */
public interface AnZhuangTiaoShiWorksService {
    void addWorksCheZhan(AnZhuangTiaoShiWorksCheZhanEntity anZhuangTiaoShiWorksCheZhanEntity, String[] chezhanname);

    void editWorksCheZhanByXid(AnZhuangTiaoShiWorksCheZhanEntity anZhuangTiaoShiWorksCheZhanEntity);

    List<AnZhuangTiaoShiCheZhanEntity> findCheZhanByXid(Integer xid);

    List<AnZhuangTiaoShiWorksCheZhanEntity> findCheZhanDatasByXid(Integer xid, Integer page, Integer size);

    List<AnZhuangTiaoShiWorkNameLibraryEntity> findWorksDatasByCid(Integer cid, Integer page, Integer size);

    void addWorksDatas(AnZhuangTiaoShiWorksDingEntity anZhuangTiaoShiWorksDingEntity);

    List<AnZhuangTiaoShiWorkNameTotalEntity> findAllWorks();

    List<AnZhuangTiaoShiWorksFileEntity> findShuRuFileByXid(Integer id, Integer page, Integer size);

    List<AnZhuangTiaoShiWorksFileEntity> findShuChuFileByXid(Integer id, Integer page, Integer size);

    void addFile(AnZhuangTiaoShiFileEntity anZhuangTiaoShiFileEntity, String username,Integer senderid,Integer[] uids);

    void editFileById(AnZhuangTiaoShiFileEntity anZhuangTiaoShiFileEntity, Integer id, Integer[] uids, Integer userid, String username);

    AnZhuangTiaoShiWorksFileEntity findById(Integer id);

    List<AnZhuangTiaoShiWorksFileEntity> findFileByNmae(Integer page, Integer size, Integer xdid, Integer filetype, String filename);

    void deletFileByIds(Integer[] ids);

    JSONObject findAllCheZhanDatasByXDid(Integer id, Integer worksid);

    AnZhuangTiaoShiWorksDingEntity findOneWork(Integer cid, Integer wntid, Integer wnlId);

    void deleteById(Integer id);

    List<AnZhuangTiaoShiObjectAuditorEntity> findXMByid(Integer id);


    List<AnZhuangTiaoShiFileEntity> findShuRuFileByid(Integer id, Integer page, Integer size, Integer uid);

    List<AnZhuangTiaoShiFileEntity> findShuRuFileByidd(Integer id, Integer page, Integer size, Integer uid);

    List<AnZhuangTiaoShiFileEntity> findShuChuFileByid(Integer id, Integer page, Integer size, Integer uid);

    List<AnZhuangTiaoShiFileEntity> findShuChuFileeByidd(Integer id, Integer page, Integer size, Integer uid);

    List<AnZhuangTiaoShiFileEntity> findsomeFileByNmae(Integer page, Integer size, Integer xdid, Integer filetype, String filename, Integer uid);

    List<AnZhuangTiaoShiFileEntity> findsomeFileByNmaee(Integer page, Integer size, Integer xdid, Integer filetype, String filename, Integer uid);

    void editAuditorByWJId(AnZhuangTiaoShiObjectAuditorEntity anZhuangTiaoShiObjectAuditorEntity, Integer id, String username, Integer receiverid, Integer senderId);

}
