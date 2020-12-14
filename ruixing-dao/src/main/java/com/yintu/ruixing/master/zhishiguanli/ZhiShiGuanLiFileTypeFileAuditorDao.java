package com.yintu.ruixing.master.zhishiguanli;

import com.yintu.ruixing.zhishiguanli.ZhiShiGuanLiFileTypeFileAuditorEntity;

public interface ZhiShiGuanLiFileTypeFileAuditorDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ZhiShiGuanLiFileTypeFileAuditorEntity record);

    int insertSelective(ZhiShiGuanLiFileTypeFileAuditorEntity record);

    ZhiShiGuanLiFileTypeFileAuditorEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ZhiShiGuanLiFileTypeFileAuditorEntity record);

    int updateByPrimaryKey(ZhiShiGuanLiFileTypeFileAuditorEntity record);
}