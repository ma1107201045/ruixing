package com.yintu.ruixing.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWenTiRecordMessageEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnZhuangTiaoShiWenTiRecordMessageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AnZhuangTiaoShiWenTiRecordMessageEntity record);

    int insertSelective(AnZhuangTiaoShiWenTiRecordMessageEntity record);

    AnZhuangTiaoShiWenTiRecordMessageEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiWenTiRecordMessageEntity record);

    int updateByPrimaryKey(AnZhuangTiaoShiWenTiRecordMessageEntity record);

    List<AnZhuangTiaoShiRecordMessageEntity> findRecordMessageById(Integer id);

    List<AnZhuangTiaoShiRecordMessageEntity> findFileRecordMessageById(Integer id);
}