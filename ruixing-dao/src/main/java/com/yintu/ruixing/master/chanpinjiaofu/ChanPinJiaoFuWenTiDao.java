package com.yintu.ruixing.master.chanpinjiaofu;

import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuWenTiEntity;
import com.yintu.ruixing.xitongguanli.DepartmentEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface ChanPinJiaoFuWenTiDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ChanPinJiaoFuWenTiEntity record);

    int insertSelective(ChanPinJiaoFuWenTiEntity record);

    ChanPinJiaoFuWenTiEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChanPinJiaoFuWenTiEntity record);

    int updateByPrimaryKey(ChanPinJiaoFuWenTiEntity record);

    //////////////////////////////////////////////////////////////

    List<ChanPinJiaoFuWenTiEntity> findAllData();

    List<DepartmentEntity> findAllDepartment();

    void addWenTi(ChanPinJiaoFuWenTiEntity chanPinJiaoFuWenTiEntity);

    void editWenTiById(ChanPinJiaoFuWenTiEntity chanPinJiaoFuWenTiEntity);

    List<ChanPinJiaoFuWenTiEntity> findWenTiByName(String xiangMuName);

    void deletWenTiByIds(Integer[] ids);

    List<ChanPinJiaoFuWenTiEntity> findXiangMuMing();

    List<ChanPinJiaoFuWenTiEntity> downLoadByIds(Integer[] ids);

    List<ChanPinJiaoFuWenTiEntity> selectByCondition(Integer[] ids);
}