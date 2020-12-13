package com.yintu.ruixing.zhishiguanli;

import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.xitongguanli.AuditConfigurationEntity;
import com.yintu.ruixing.zhishiguanli.ZhiShiGuanLiFileTypeEntity;
import com.yintu.ruixing.zhishiguanli.ZhiShiGuanLiFileTypeFileEntity;

import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/8/11 17:22
 * @Version 1.0
 * 需求:知识管理 文件类型
 */
public interface ZhiShiGuanLiFileTypeService {
    List<ZhiShiGuanLiFileTypeEntity> findSomeFileType(Integer page, Integer size, String fileTypeName);

    void addFileType(ZhiShiGuanLiFileTypeEntity zhiShiGuanLiFileTypeEntity,String username,Integer parentid);

    void editFileTypeById(ZhiShiGuanLiFileTypeEntity zhiShiGuanLiFileTypeEntity,String username);

    void addFile(ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity,String username,Integer uid,Integer[] auditorid,Integer[] sort);

    ZhiShiGuanLiFileTypeFileEntity findFile(Integer id);

    void addOneFile(String fileName, Date createtime, String filePath, Integer id1,String username,Integer filesize);

    void updateFileById(ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity,Integer id,String username,Integer uid,Integer[] auditorid,Integer[] sort);

    List<ZhiShiGuanLiFileTypeFileEntity> findSomeFile(Integer page, Integer size, String fileName,Integer id);

    List<ZhiShiGuanLiFileTypeFileEntity> findFileById(Integer id, Integer page, Integer size, String fileName);

    ZhiShiGuanLiFileTypeFileEntity findById(Integer id);

    List<ZhiShiGuanLiFileTypeFileEntity> findFiles(Integer id);

    void deleteFileTypeByIds(Integer id);

    void deleteUpdataFileByIds(Integer id);

    List<ZhiShiGuanLiFileTypeFileEntity> findFileByParentid(Integer id);

    void deleteFileByIds(Integer id);

    List<TreeNodeUtil> findShuXing(Integer id);

    List<ZhiShiGuanLiFileTypeEntity> findFileTypeByParentid(Integer parentid);

    void pasteFileByParentid(Integer parentid, Integer[] typeid, ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity);

    void copyFileByParentid(Integer parentid, Integer[] fileid, ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity,String username);

    List<ZhiShiGuanLiFileTypeFileEntity> findSomeFileByTime(Integer page, Integer size, String fileName , Integer id);

    List<ZhiShiGuanLiFileTypeFileEntity> findSomeFileBySize(Integer page, Integer size, String fileName, Integer id);

    List<AuditConfigurationEntity> findAudit(short i, short i1, short i2);

    List<TreeNodeUtil> findTree();

    List<ZhiShiGuanLiFileTypeFileRecordmessageEntity> findRecordmessageByFileid(Integer id);

    List<MessageEntity> findXiaoXi(Integer senderid);
}
