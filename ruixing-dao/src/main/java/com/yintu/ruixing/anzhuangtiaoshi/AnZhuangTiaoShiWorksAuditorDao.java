package com.yintu.ruixing.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWorksAuditorEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AnZhuangTiaoShiWorksAuditorDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AnZhuangTiaoShiWorksAuditorEntity record);

    int insertSelective(AnZhuangTiaoShiWorksAuditorEntity record);

    AnZhuangTiaoShiWorksAuditorEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiWorksAuditorEntity record);

    int updateByPrimaryKey(AnZhuangTiaoShiWorksAuditorEntity record);

    List<Integer> findIsPassByObjid(Integer id);

    List<Integer> findIsPassByObjidd(Integer id);

    Integer findidByIds(@Param("id") Integer id,@Param("receiverid") Integer receiverid);
}