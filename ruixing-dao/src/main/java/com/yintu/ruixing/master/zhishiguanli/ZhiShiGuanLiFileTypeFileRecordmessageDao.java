package com.yintu.ruixing.master.zhishiguanli;

import com.yintu.ruixing.zhishiguanli.ZhiShiGuanLiFileTypeFileRecordmessageEntity;

import java.util.List;

public interface ZhiShiGuanLiFileTypeFileRecordmessageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ZhiShiGuanLiFileTypeFileRecordmessageEntity record);

    int insertSelective(ZhiShiGuanLiFileTypeFileRecordmessageEntity record);

    ZhiShiGuanLiFileTypeFileRecordmessageEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ZhiShiGuanLiFileTypeFileRecordmessageEntity record);

    int updateByPrimaryKey(ZhiShiGuanLiFileTypeFileRecordmessageEntity record);

    List<ZhiShiGuanLiFileTypeFileRecordmessageEntity> findRecordmessageByFileid(Integer id);
}