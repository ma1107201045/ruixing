package com.yintu.ruixing.master.chanpinjiaofu;

import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuEntity;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuPropertyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/6/23 16:19
 * @Version 1.0
 * 需求:产品交付模块
 */

public interface ChanPinJiaoFuDao {

    Integer findAllDataShu();

    void fristData(ChanPinJiaoFuPropertyEntity chanPinJiaoFuPropertyEntity);

    void addDataShu(ChanPinJiaoFuPropertyEntity chanPinJiaoFuPropertyEntity);

    List<ChanPinJiaoFuPropertyEntity> findChanPinJiaoFuShuXing(int i);

    void editDataShuById(ChanPinJiaoFuPropertyEntity chanPinJiaoFuPropertyEntity);

    List<Integer> findParedId(Integer id);

    void deleteDataShuById(Integer id);

    List<ChanPinJiaoFuEntity> findAllXiangMuState();

    List<ChanPinJiaoFuEntity> findXiangMuData(@Param("bianhao") String bianhao,@Param("name") String name);

    List<Integer> findIdS(Integer id);

    List<ChanPinJiaoFuEntity> findXiangMuDataById(Integer idd);

    List<ChanPinJiaoFuEntity> findXiangMuDataByIds(@Param("firstid") Integer firstid,@Param("secondid") Integer secondid,@Param("fileid") Integer fileid);

    ChanPinJiaoFuEntity findById(Integer id);

    void addXiangMuData(ChanPinJiaoFuEntity chanPinJiaoFuEntity);

    void editXiangMuDataById(ChanPinJiaoFuEntity chanPinJiaoFuEntity);

    void deletXiangMuDataById(Integer id);

    void deletXiangMuDataByIds(Integer[] ids);

    List<ChanPinJiaoFuEntity> findXiangMuDataByIdFirst(Integer id);

    List<ChanPinJiaoFuEntity> findXiangMuDataByIdSecond(Integer id);

    List<ChanPinJiaoFuEntity> findXiangMuDataByIdThird(Integer id);
}
