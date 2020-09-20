package com.yintu.ruixing.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWenTiAuditorEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnZhuangTiaoShiWenTiAuditorDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AnZhuangTiaoShiWenTiAuditorEntity record);

    int insertSelective(AnZhuangTiaoShiWenTiAuditorEntity record);

    AnZhuangTiaoShiWenTiAuditorEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiWenTiAuditorEntity record);

    int updateByPrimaryKey(AnZhuangTiaoShiWenTiAuditorEntity record);

    List<Integer> findIsPassByObjid(Integer id);
}