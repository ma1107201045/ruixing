package com.yintu.ruixing.master.zhishiguanli;

import com.yintu.ruixing.common.AuditTotalVo;
import com.yintu.ruixing.zhishiguanli.ZhiShiGuanLiFileTypeFileEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface ZhiShiGuanLiFileTypeFileDao {
    int insert(ZhiShiGuanLiFileTypeFileEntity record);

    int updateByPrimaryKey(ZhiShiGuanLiFileTypeFileEntity record);


    ////////////////////////////////////////////////////////////

    int deleteByPrimaryKey(Integer id);

    ZhiShiGuanLiFileTypeFileEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ZhiShiGuanLiFileTypeFileEntity record);

    int insertSelective(ZhiShiGuanLiFileTypeFileEntity record);

    void addOneFile(@Param("fileName") String fileName, @Param("createtime") Date createtime,
                    @Param("filePath") String filePath, @Param("id1") Integer id1, @Param("username") String username,Integer filesize);

    List<ZhiShiGuanLiFileTypeFileEntity> findSomeFile(@Param("fileName") String fileName, @Param("id") Integer id);

    List<ZhiShiGuanLiFileTypeFileEntity> findFileById(@Param("id") Integer id, @Param("fileName") String fileName);

    List<ZhiShiGuanLiFileTypeFileEntity> findFiles(Integer id);

    List<ZhiShiGuanLiFileTypeFileEntity> findFileByParentid(Integer id);

    List<ZhiShiGuanLiFileTypeFileEntity> findSomeFileByTime( String fileName,Integer id);

    List<ZhiShiGuanLiFileTypeFileEntity> findSomeFileBySize( String fileName,Integer id);

    ZhiShiGuanLiFileTypeFileEntity findFileDatasById(Integer id);

    List<AuditTotalVo> findByZSGLExample(String search, Integer userId, Short auditStatus, Integer auditorId, Short activate, Short isDispose);
}