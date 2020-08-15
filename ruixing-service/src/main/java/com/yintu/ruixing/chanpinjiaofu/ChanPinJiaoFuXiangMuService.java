package com.yintu.ruixing.chanpinjiaofu;

import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuEntity;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuFileEntity;
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

    void addXiangMu(ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity);

    void editXiangMuById(ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity);

    void deletXiagMuById(Integer id);

    List<ChanPinJiaoFuXiangMuEntity> findAll(Integer page,Integer size);

    void addXiangMuFile(ChanPinJiaoFuXiangMuFileEntity chanPinJiaoFuXiangMuFileEntity,Integer[] uids);

    void editXiangMuFileById(ChanPinJiaoFuXiangMuFileEntity chanPinJiaoFuXiangMuFileEntity,Integer id,Integer[] uids);

    ChanPinJiaoFuXiangMuFileEntity findById(Integer id);

    void deletXiangMuFileById(Integer id);

    void deletXiangMuFileByIds(Integer[] ids);

    List<ChanPinJiaoFuXiangMuEntity> findXiangMuData(String xiangMuBianHao, String xiangMuName, Integer page, Integer size);

    List<ChanPinJiaoFuXiangMuEntity> findXiangMuByIds(Integer stateid, Integer id, Integer typeid, Integer page, Integer size);

    Map<String, Object> findJiaoFuQingKuangNumberAll();

    List<ChanPinJiaoFuXiangMuEntity> findJiaoFuQingKuangList(String choiceTing, Integer page, Integer size);

    List<ChanPinJiaoFuXiangMuEntity> findJiaoFuQingKuangLists(String choiceTing, Integer page, Integer size);

    List<UserEntity> findAllAuditorNameById(Integer id);

    void addXiaoXi(MessageEntity messageEntity);

    List<ChanPinJiaoFuXiangMuEntity> findAllXiangMu();

    List<MessageEntity> findXiaoXi();

    void editXiaoXiById(MessageEntity messageEntity);
}
