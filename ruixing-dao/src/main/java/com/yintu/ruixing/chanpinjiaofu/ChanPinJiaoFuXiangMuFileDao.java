package com.yintu.ruixing.chanpinjiaofu;

import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuFileEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChanPinJiaoFuXiangMuFileDao {
    int insert(ChanPinJiaoFuXiangMuFileEntity record);

    int updateByPrimaryKey(ChanPinJiaoFuXiangMuFileEntity record);


    ////////////////////////////////////////////////////////////////////////
    int deleteByPrimaryKey(Integer id);

    ChanPinJiaoFuXiangMuFileEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChanPinJiaoFuXiangMuFileEntity record);

    int insertSelective(ChanPinJiaoFuXiangMuFileEntity record);

    List<ChanPinJiaoFuXiangMuFileEntity> findFileBySomething(@Param("xmid") Integer xmid,@Param("filetype") Integer filetype,@Param("filename") String filename);

    List<ChanPinJiaoFuXiangMuFileEntity> findShuRuFile(Integer xmid);

    List<ChanPinJiaoFuXiangMuFileEntity> findShuChuFile(Integer xmid);

    List<ChanPinJiaoFuXiangMuFileEntity> findFile(Integer id);

    List<ChanPinJiaoFuXiangMuFileEntity> findFileById(Integer id);
}