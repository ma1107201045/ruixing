package com.yintu.ruixing.zhishiguanli;

import com.yintu.ruixing.common.util.TreeNodeUtil;
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

    void addFile(ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity,String username);

    ZhiShiGuanLiFileTypeFileEntity findFile(Integer id);

    void addOneFile(String fileName, Date createtime, String filePath, Integer id1,String username);

    void updateFileById(ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity,Integer id,String username);

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


}
