package com.yintu.ruixing.master.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiObjectAuditorEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface AnZhuangTiaoShiObjectAuditorDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AnZhuangTiaoShiObjectAuditorEntity record);

    AnZhuangTiaoShiObjectAuditorEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiObjectAuditorEntity record);

    int updateByPrimaryKey(AnZhuangTiaoShiObjectAuditorEntity record);
    ///////////////////////////////////////////////////////////////////
    int insertSelective(AnZhuangTiaoShiObjectAuditorEntity record);

    void deleteAuditorByObjectid(Integer id);

    void editAuditorByXMId(AnZhuangTiaoShiObjectAuditorEntity anZhuangTiaoShiObjectAuditorEntity);

    List<AnZhuangTiaoShiObjectAuditorEntity> findXMByid(Integer id);
}