package com.yintu.ruixing.master.zhishiguanli;

import com.yintu.ruixing.zhishiguanli.ZhiShiGuanLiFileTypeFileAuditorEntity;

import java.util.List;

public interface ZhiShiGuanLiFileTypeFileAuditorDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ZhiShiGuanLiFileTypeFileAuditorEntity record);

    int insertSelective(ZhiShiGuanLiFileTypeFileAuditorEntity record);

    ZhiShiGuanLiFileTypeFileAuditorEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ZhiShiGuanLiFileTypeFileAuditorEntity record);

    int updateByPrimaryKey(ZhiShiGuanLiFileTypeFileAuditorEntity record);

    List<ZhiShiGuanLiFileTypeFileAuditorEntity> findAuditorDatas(Integer id, Integer receiverid, short i);

    List<ZhiShiGuanLiFileTypeFileAuditorEntity> findAuditorDatasByIds(Integer id, short i);

    List<ZhiShiGuanLiFileTypeFileAuditorEntity> findAuditorDatasByFileidAndSort(Integer id, int i);

    List<ZhiShiGuanLiFileTypeFileAuditorEntity> findFileAuditorDatasByFileId(Integer id, Integer uid);

    List<Integer> findUserIdByFileid(Integer fileid);
}