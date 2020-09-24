package com.yintu.ruixing.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWorksRecordMessageEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnZhuangTiaoShiWorksRecordMessageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AnZhuangTiaoShiWorksRecordMessageEntity record);

    int insertSelective(AnZhuangTiaoShiWorksRecordMessageEntity record);

    AnZhuangTiaoShiWorksRecordMessageEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiWorksRecordMessageEntity record);

    int updateByPrimaryKey(AnZhuangTiaoShiWorksRecordMessageEntity record);

    List<AnZhuangTiaoShiWorksRecordMessageEntity> findRecordMessageById(Integer id);

    List<AnZhuangTiaoShiWorksRecordMessageEntity> findWorksRecordMessageById(Integer id);
}