package com.yintu.ruixing.master.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWenTiEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AnZhuangTiaoShiWenTiDao {
    int insert(AnZhuangTiaoShiWenTiEntity record);

    int updateByPrimaryKey(AnZhuangTiaoShiWenTiEntity record);


    ///////////////////////////////////////////////////////
    AnZhuangTiaoShiWenTiEntity selectByPrimaryKey(@Param("id")Integer id,@Param("receiverid") Integer receiverid);

    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiWenTiEntity record);

    int insertSelective(AnZhuangTiaoShiWenTiEntity record);

    List<AnZhuangTiaoShiWenTiEntity> findSomeWenTi( String xdname,
                                                    Integer receiverid, String startTime, String endTime, String wenTiType,
                                                   String fankuiMode, String shouliDanwei, Integer isNotOver );

    List<AnZhuangTiaoShiWenTiEntity> findAllNotDoWellWenTi();

    AnZhuangTiaoShiWenTiEntity findOneWentById(Integer id);

    AnZhuangTiaoShiWenTiEntity findWenTiXiangQingById(Integer id);
}