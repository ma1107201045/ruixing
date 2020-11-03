package com.yintu.ruixing.chanpinjiaofu;

import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.xitongguanli.UserEntity;

import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/7/1 13:45
 * @Version 1.0
 * 需求:产品交付相关
 */
public interface ChanPinJiaoFuXiangMuService {
    List<TreeNodeUtil> findSanJiShu();

    void addXiangMu(ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity, String username, Integer senderid);

    void editXiangMuById(ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity, String username, Integer id, Integer[] uids, Integer senderid);

    void deletXiagMuById(Integer id);

    List<ChanPinJiaoFuXiangMuEntity> findAll(Integer page, Integer size);

    void addXiangMuFile(ChanPinJiaoFuXiangMuFileEntity chanPinJiaoFuXiangMuFileEntity, Integer[] uids, String username, Integer uid);

    void editXiangMuFileById(ChanPinJiaoFuXiangMuFileEntity chanPinJiaoFuXiangMuFileEntity, Integer id, Integer[] uids, Integer uid, String username);

    ChanPinJiaoFuXiangMuFileEntity findById(Integer id);

    void deletXiangMuFileById(Integer id);

    void deletXiangMuFileByIds(Integer[] ids);

    List<ChanPinJiaoFuXiangMuEntity> findXiangMuData(String xiangMuBianHao, String xiangMuName, Integer page, Integer size);

    List<ChanPinJiaoFuXiangMuEntity> findXiangMuByIds(Integer stateid, Integer page, Integer size);

    Map<String, Object> findJiaoFuQingKuangNumberAll();

    List<ChanPinJiaoFuXiangMuEntity> findJiaoFuQingKuangList(String choiceTing, Integer page, Integer size);

    List<ChanPinJiaoFuXiangMuEntity> findJiaoFuQingKuangLists(String choiceTing, Integer page, Integer size);

    List<UserEntity> findAllAuditorNameById(Integer id);

    void addXiaoXi(MessageEntity messageEntity);

    List<ChanPinJiaoFuXiangMuEntity> findAllXiangMu();

    List<MessageEntity> findXiaoXi(Integer senderid);

    void editXiaoXiById(MessageEntity messageEntity);

    List<ChanPinJiaoFuRecordMessageEntity> findReordById(Integer id);

    List<ChanPinJiaoFuXiangMuFileEntity> findFileBySomething(Integer xmid, Integer page, Integer size, Integer filetype, String filename, Integer uid);

    List<ChanPinJiaoFuXiangMuFileEntity> findFileBySomethingg(Integer xmid, Integer page, Integer size, Integer filetype, String filename, Integer uid);

    List<ChanPinJiaoFuXiangMuFileEntity> findShuRuFile(Integer xmid, Integer page, Integer size, Integer uid);

    List<ChanPinJiaoFuXiangMuFileEntity> findShuRuFilee(Integer xmid, Integer page, Integer size, Integer uid);

    List<ChanPinJiaoFuXiangMuFileEntity> findShuChuFile(Integer xmid, Integer page, Integer size, Integer uid);

    List<ChanPinJiaoFuXiangMuFileEntity> findShuChuFilee(Integer xmid, Integer page, Integer size, Integer uid);

    List<ChanPinJiaoFuXiangMuFileEntity> findFile(Integer id);

    List<ChanPinJiaoFuRecordMessageEntity> findFileReordById(Integer id);

    List<ChanPinJiaoFuXiangMuEntity> findXiangMuById(Integer id);

    List<ChanPinJiaoFuXiangMuFileEntity> findFileById(Integer id);

    void editAuditorByXMId(ChanPinJiaoFuFileAuditorEntity chanPinJiaoFuFileAuditorEntity, Integer id, String username, Integer receiverid, Integer senderId);

    void editAuditorByWJId(ChanPinJiaoFuFileAuditorEntity chanPinJiaoFuFileAuditorEntity, Integer id, String username, Integer receiverid, Integer senderId);

    List<ChanPinJiaoFuFileAuditorEntity> findXMByXmid(Integer xmid);


    long findProjectSum();
}
