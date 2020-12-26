package com.yintu.ruixing.chanpinjiaofu;

import com.yintu.ruixing.common.AuditTotalVo;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.xitongguanli.AuditConfigurationEntity;
import com.yintu.ruixing.xitongguanli.UserEntity;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
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

    void editXiangMuById(ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity, String username, Integer id,  Integer senderid, Integer[] auditorid, Integer[] sort);

    void deletXiagMuById(Integer id);

    List<ChanPinJiaoFuXiangMuEntity> findAll(Integer page, Integer size, Integer uid);

    void addXiangMuFile(ChanPinJiaoFuXiangMuFileEntity chanPinJiaoFuXiangMuFileEntity,  String username, Integer uid, Integer[] auditorid, Integer[] sort);

    void editXiangMuFileById(ChanPinJiaoFuXiangMuFileEntity chanPinJiaoFuXiangMuFileEntity, Integer id,  Integer uid, String username, Integer[] auditorid, Integer[] sort);

    ChanPinJiaoFuXiangMuFileEntity findById(Integer id);

    void deletXiangMuFileById(Integer id);

    void deletXiangMuFileByIds(Integer[] ids);

    List<ChanPinJiaoFuXiangMuEntity> findXiangMuData(String xiangMuBianHao, String xiangMuName, Integer page, Integer size,Integer uid);

    List<ChanPinJiaoFuXiangMuEntity> findXiangMuByIds(Integer stateid, Integer page, Integer size,Integer uid);

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

    List<ChanPinJiaoFuXiangMuEntity> findXiangMuById(Integer id,Integer uid);

    List<ChanPinJiaoFuXiangMuFileEntity> findFileById(Integer id,Integer uid);

    void editAuditorByXMId(Integer id, Short isPass,Integer passUserId,
                           String username, Integer receiverid,
                           String accessoryName,String accessoryPath,String context);

    void editAuditorByWJId(Integer id, Short isPass, Integer passUserId,
                           String username, Integer receiverid,
                           String accessoryName, String accessoryPath, String context);

    List<ChanPinJiaoFuFileAuditorEntity> findXMByXmid(Integer xmid);


    long findProjectSum();

    void exportFile(ServletOutputStream outputStream, Integer[] ids)throws IOException;

    List<ChanPinJiaoFuFileAuditorEntity>findAuditorDatas(Integer objectid,Integer objectType,Integer auditorid,Integer sort,Short activate);

    List<UserEntity> findZhuanJiaoAuditorName(Integer objectid, Integer objectType);

    List<AuditConfigurationEntity> findXMAudit(short i, short i1, short i2);

    List<TreeNodeUtil> findTree();

    List<AuditConfigurationEntity> findFileAudit(short i, short i1, short i2);

    List<AuditTotalVo> findByCPJFXiangMuExample(String search, Integer userId, Short auditStatus, Integer auditorId, Short activate, Short isDispose);

    List<AuditTotalVo> findByCPJFFileExample(String search, Integer userId, Short auditStatus, Integer auditorId, Short activate, Short isDispose);
}
