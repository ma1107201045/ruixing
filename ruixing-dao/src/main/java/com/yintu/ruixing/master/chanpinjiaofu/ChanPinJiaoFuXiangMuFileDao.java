package com.yintu.ruixing.master.chanpinjiaofu;

import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuFileEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ChanPinJiaoFuXiangMuFileDao {
    int insert(ChanPinJiaoFuXiangMuFileEntity record);

    int updateByPrimaryKey(ChanPinJiaoFuXiangMuFileEntity record);


    ////////////////////////////////////////////////////////////////////////
    int deleteByPrimaryKey(Integer id);

    ChanPinJiaoFuXiangMuFileEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChanPinJiaoFuXiangMuFileEntity record);

    int insertSelective(ChanPinJiaoFuXiangMuFileEntity record);

    List<ChanPinJiaoFuXiangMuFileEntity> findFileBySomething(@Param("xmid") Integer xmid, @Param("filetype") Integer filetype,
                                                             @Param("filename") String filename, @Param("uid") Integer uid);

    List<ChanPinJiaoFuXiangMuFileEntity> findFileBySomethingg(@Param("xmid") Integer xmid, @Param("filetype") Integer filetype,
                                                              @Param("filename") String filename, @Param("uid") Integer uid);

    List<ChanPinJiaoFuXiangMuFileEntity> findShuRuFile(@Param("xmid") Integer xmid, @Param("uid") Integer uid);

    List<ChanPinJiaoFuXiangMuFileEntity> findShuRuFilee(@Param("xmid") Integer xmid, @Param("uid") Integer uid);

    List<ChanPinJiaoFuXiangMuFileEntity> findShuChuFile(@Param("xmid") Integer xmid, @Param("uid") Integer uid);

    List<ChanPinJiaoFuXiangMuFileEntity> findShuChuFilee(@Param("xmid") Integer xmid, @Param("uid") Integer uid);

    List<ChanPinJiaoFuXiangMuFileEntity> findFile(Integer id);

    List<ChanPinJiaoFuXiangMuFileEntity> findFileById(Integer id);
}