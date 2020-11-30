package com.yintu.ruixing.master.anzhuangtiaoshi;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiFileEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AnZhuangTiaoShiFileDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AnZhuangTiaoShiFileEntity record);

    int insertSelective(AnZhuangTiaoShiFileEntity record);

    int updateByPrimaryKey(AnZhuangTiaoShiFileEntity record);

    int updateByPrimaryKeySelective(AnZhuangTiaoShiFileEntity record);

    //////////////////////////////////////////////////////

    List<AnZhuangTiaoShiFileEntity> findShuRuFileByXid(Integer id);

    List<AnZhuangTiaoShiFileEntity> findShuChuFileByXid(Integer id);

    void addFile(AnZhuangTiaoShiFileEntity anZhuangTiaoShiFileEntity);

    void editFileById(AnZhuangTiaoShiFileEntity anZhuangTiaoShiFileEntity);

    void deletFileByIds(Integer[] ids);

    AnZhuangTiaoShiFileEntity selectByPrimaryKey(Integer id);

    List<AnZhuangTiaoShiFileEntity> findFileByNmae(@Param("xdid") Integer xdid,@Param("filetype") Integer filetype,@Param("filename") String filename,@Param("uid")Integer uid);

    List<AnZhuangTiaoShiFileEntity> findFileByNmaee(@Param("xdid") Integer xdid,@Param("filetype") Integer filetype,@Param("filename") String filename,@Param("uid")Integer uid);

    List<AnZhuangTiaoShiFileEntity> findShuRuFile(@Param("id")Integer id,@Param("uid") Integer uid);

    List<AnZhuangTiaoShiFileEntity> findShuRuFilee(@Param("id")Integer id,@Param("uid") Integer uid);

    List<AnZhuangTiaoShiFileEntity> findShuChuFile(@Param("id")Integer id,@Param("uid") Integer uid);

    List<AnZhuangTiaoShiFileEntity> findShuChuFilee(@Param("id")Integer id,@Param("uid") Integer uid);

    List<AnZhuangTiaoShiFileEntity> findFileById(Integer id);

    List<AnZhuangTiaoShiFileEntity> findShuRuFileByid(@Param("id")Integer id,@Param("uid") Integer uid);

    List<AnZhuangTiaoShiFileEntity> findShuRuFileByidd(@Param("id")Integer id,@Param("uid") Integer uid);

    List<AnZhuangTiaoShiFileEntity> findShuChuFileByid(@Param("id")Integer id,@Param("uid") Integer uid);

    List<AnZhuangTiaoShiFileEntity> findShuChuFileByidd(@Param("id")Integer id,@Param("uid") Integer uid);

    List<AnZhuangTiaoShiFileEntity> findsomeFileByNmae(@Param("xdid") Integer xdid,@Param("filetype") Integer filetype,@Param("filename") String filename,@Param("uid")Integer uid);

    List<AnZhuangTiaoShiFileEntity> findsomeFileByNmaee(@Param("xdid") Integer xdid,@Param("filetype") Integer filetype,@Param("filename") String filename,@Param("uid")Integer uid);
}