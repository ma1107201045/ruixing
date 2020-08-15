package com.yintu.ruixing.zhishiguanli.impl;

import com.yintu.ruixing.zhishiguanli.ZhiShiGuanLiFileTypeDao;
import com.yintu.ruixing.zhishiguanli.ZhiShiGuanLiFileTypeFileDao;
import com.yintu.ruixing.zhishiguanli.ZhiShiGuanLiFileTypeEntity;
import com.yintu.ruixing.zhishiguanli.ZhiShiGuanLiFileTypeFileEntity;
import com.yintu.ruixing.zhishiguanli.ZhiShiGuanLiFileTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/8/11 17:22
 * @Version 1.0
 * 需求:知识管理  文件类型
 */
@Service
@Transactional
public class ZhiShiGuanLiFileTypeServiceImpl implements ZhiShiGuanLiFileTypeService {
    @Autowired
    private ZhiShiGuanLiFileTypeDao zhiShiGuanLiFileTypeDao;

    @Autowired
    private ZhiShiGuanLiFileTypeFileDao zhiShiGuanLiFileTypeFileDao;

    @Override
    public void deleteFileByIds(Integer id) {
        zhiShiGuanLiFileTypeFileDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<ZhiShiGuanLiFileTypeFileEntity> findFileByParentid(Integer id) {
        return zhiShiGuanLiFileTypeFileDao.findFileByParentid(id);
    }

    @Override
    public void deleteUpdataFileByIds(Integer id) {
        zhiShiGuanLiFileTypeFileDao.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteFileTypeByIds(Integer id) {
        zhiShiGuanLiFileTypeDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<ZhiShiGuanLiFileTypeFileEntity> findFiles(Integer id) {
        return zhiShiGuanLiFileTypeFileDao.findFiles(id);
    }

    @Override
    public ZhiShiGuanLiFileTypeFileEntity findById(Integer id) {
        return zhiShiGuanLiFileTypeFileDao.selectByPrimaryKey(id);
    }

    @Override
    public List<ZhiShiGuanLiFileTypeFileEntity> findFileById(Integer id, Integer page, Integer size, String fileName) {
        return zhiShiGuanLiFileTypeFileDao.findFileById(id,fileName);
    }

    @Override
    public List<ZhiShiGuanLiFileTypeFileEntity> findSomeFile(Integer page, Integer size, String fileName,Integer id) {
        return zhiShiGuanLiFileTypeFileDao.findSomeFile(fileName,id);
    }

    @Override
    public void updateFileById(ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity,Integer id) {
        zhiShiGuanLiFileTypeFileDao.updateByPrimaryKeySelective(zhiShiGuanLiFileTypeFileEntity);
    }

    @Override
    public void addOneFile(String fileName, Date createtime, String filePath, Integer id1) {
        zhiShiGuanLiFileTypeFileDao.addOneFile(fileName,createtime,filePath,id1);
    }

    @Override
    public ZhiShiGuanLiFileTypeFileEntity findFile(Integer id) {
        return zhiShiGuanLiFileTypeFileDao.selectByPrimaryKey(id);
    }

    @Override
    public void addFile(ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity) {
        zhiShiGuanLiFileTypeFileEntity.setParentid(0);
        zhiShiGuanLiFileTypeFileDao.insertSelective(zhiShiGuanLiFileTypeFileEntity);
    }

    @Override
    public void editFileTypeById(ZhiShiGuanLiFileTypeEntity zhiShiGuanLiFileTypeEntity) {
        zhiShiGuanLiFileTypeDao.updateByPrimaryKeySelective(zhiShiGuanLiFileTypeEntity);
    }

    @Override
    public void addFileType(ZhiShiGuanLiFileTypeEntity zhiShiGuanLiFileTypeEntity) {
        zhiShiGuanLiFileTypeDao.insertSelective(zhiShiGuanLiFileTypeEntity);
    }

    @Override
    public List<ZhiShiGuanLiFileTypeEntity> findSomeFileType(Integer page, Integer size, String fileTypeName) {
        return zhiShiGuanLiFileTypeDao.findSomeFileType(fileTypeName);
    }
}
