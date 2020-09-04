package com.yintu.ruixing.chanpinjiaofu;

import com.yintu.ruixing.common.MessageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface ChanPinJiaoFuXiangMuDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ChanPinJiaoFuXiangMuEntity record);

    int insertSelective(ChanPinJiaoFuXiangMuEntity record);

    int updateByPrimaryKeySelective(ChanPinJiaoFuXiangMuEntity record);

    int updateByPrimaryKey(ChanPinJiaoFuXiangMuEntity record);


    // ////////////////////////////////////







    ChanPinJiaoFuXiangMuEntity selectByPrimaryKey(Integer id);

    List<ChanPinJiaoFuXiangMuEntity> findSanJiShu();

    List<ChanPinJiaoFuXiangMuEntity> findDiErJi(Integer xiangmuState);

    void addXiangMu(ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity);

    void editXiangMuById(ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity);

    void deletXiagMuById(Integer id);

    List<ChanPinJiaoFuXiangMuEntity> findAll();

    void addXiangMuFile(ChanPinJiaoFuXiangMuFileEntity chanPinJiaoFuXiangMuFileEntity);

    void editXiangMuFileById(ChanPinJiaoFuXiangMuFileEntity chanPinJiaoFuXiangMuFileEntity);

    ChanPinJiaoFuXiangMuFileEntity findById(Integer id);

    void deletXiangMuFileById(Integer id);

    void deletXiangMuFileByIds(Integer[] ids);

    List<ChanPinJiaoFuXiangMuEntity> findXiangMuData(@Param("xiangMuBianHao") String xiangMuBianHao,@Param("xiangMuName") String xiangMuName);

    List<ChanPinJiaoFuXiangMuEntity> findOneXiangMU(Integer id);

    List<ChanPinJiaoFuXiangMuEntity> findXiangMuByIds(@Param("stateid") Integer stateid );

    List<String> findZhengZaiZhiXing();

    List<String> findQianShou();

    List<String> findFaHuo();

    List<String> meiWanChengYanGong();

    List<String> zanBuFaHuo();

    List<String> luXuFaHuo();

    List<String> wuXuFaHuo();

    List<String> daiQianShu();

    List<String> daiYanGong();

    List<String> overQianShouMoney();

    List<String> overYanGongMoney();

    List<ChanPinJiaoFuXiangMuEntity> findJiaoFuQingKuangList(String choiceTing);

    List<ChanPinJiaoFuXiangMuEntity> findJiaoFuQingKuangLists(String choiceTing);

    void addAuditorName(ChanPinJiaoFuFileAuditorEntity chanPinJiaoFuFileAuditorEntity);

    void deletAuditor(Integer id);

    Integer[] findAllAuditorNameById(Integer id);


    void addXiaoXi(MessageEntity messageEntity);

    List<ChanPinJiaoFuXiangMuEntity> findAllXiangMu();

    List<String> findXiaoXi();

    List<ChanPinJiaoFuXiangMuFileEntity> findXiangMuAndBianHao();

    List<ChanPinJiaoFuXiangMuEntity> findXiangMuById(Integer id);
}