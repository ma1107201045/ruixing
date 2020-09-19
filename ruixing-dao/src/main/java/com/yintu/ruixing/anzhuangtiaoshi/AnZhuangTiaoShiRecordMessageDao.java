package com.yintu.ruixing.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiRecordMessageEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnZhuangTiaoShiRecordMessageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AnZhuangTiaoShiRecordMessageEntity record);

    AnZhuangTiaoShiRecordMessageEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiRecordMessageEntity record);

    int updateByPrimaryKey(AnZhuangTiaoShiRecordMessageEntity record);

    ////////////////////////////////////////////////////////////////////////////
    int insertSelective(AnZhuangTiaoShiRecordMessageEntity record);

    List<AnZhuangTiaoShiRecordMessageEntity> findReordById(Integer id);
}