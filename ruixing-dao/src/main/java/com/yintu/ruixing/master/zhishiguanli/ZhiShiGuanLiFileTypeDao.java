package com.yintu.ruixing.master.zhishiguanli;

import com.yintu.ruixing.zhishiguanli.ZhiShiGuanLiFileTypeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface ZhiShiGuanLiFileTypeDao {
    int insert(ZhiShiGuanLiFileTypeEntity record);

    ZhiShiGuanLiFileTypeEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(ZhiShiGuanLiFileTypeEntity record);



    //////////////////////////////////////////////////////////////////
    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ZhiShiGuanLiFileTypeEntity record);

    int insertSelective(ZhiShiGuanLiFileTypeEntity record);

    List<ZhiShiGuanLiFileTypeEntity> findSomeFileType(String fileTypeName);


}