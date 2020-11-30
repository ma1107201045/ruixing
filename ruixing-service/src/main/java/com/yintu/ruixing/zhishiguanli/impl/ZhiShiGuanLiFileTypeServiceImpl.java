package com.yintu.ruixing.zhishiguanli.impl;

import com.yintu.ruixing.master.zhishiguanli.ZhiShiGuanLiFileTypeDao;
import com.yintu.ruixing.master.zhishiguanli.ZhiShiGuanLiFileTypeFileDao;
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
    public void updateFileById(ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity,Integer id,String username) {
        zhiShiGuanLiFileTypeFileEntity.setCreatetime(new Date());
        zhiShiGuanLiFileTypeFileEntity.setCreateName(username);
        zhiShiGuanLiFileTypeFileDao.updateByPrimaryKeySelective(zhiShiGuanLiFileTypeFileEntity);
    }

    @Override
    public void addOneFile(String fileName, Date createtime, String filePath, Integer id1,String username) {
        createtime=new Date();
        zhiShiGuanLiFileTypeFileDao.addOneFile(fileName,createtime,filePath,id1,username);
    }

    @Override
    public ZhiShiGuanLiFileTypeFileEntity findFile(Integer id) {
        return zhiShiGuanLiFileTypeFileDao.selectByPrimaryKey(id);
    }

    @Override
    public void addFile(ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity,String username) {
        zhiShiGuanLiFileTypeFileEntity.setParentid(0);
        zhiShiGuanLiFileTypeFileEntity.setCreatetime(new Date());
        zhiShiGuanLiFileTypeFileEntity.setCreateName(username);
        zhiShiGuanLiFileTypeFileDao.insertSelective(zhiShiGuanLiFileTypeFileEntity);
    }

    @Override
    public void editFileTypeById(ZhiShiGuanLiFileTypeEntity zhiShiGuanLiFileTypeEntity,String username) {
        zhiShiGuanLiFileTypeEntity.setUpdateName(username);
        zhiShiGuanLiFileTypeEntity.setUpdateTime(new Date());
        zhiShiGuanLiFileTypeDao.updateByPrimaryKeySelective(zhiShiGuanLiFileTypeEntity);
    }

    @Override
    public void addFileType(ZhiShiGuanLiFileTypeEntity zhiShiGuanLiFileTypeEntity,String username) {
        zhiShiGuanLiFileTypeEntity.setCreateTime(new Date());
        zhiShiGuanLiFileTypeEntity.setCreateName(username);
        zhiShiGuanLiFileTypeDao.insertSelective(zhiShiGuanLiFileTypeEntity);
    }

    @Override
    public List<ZhiShiGuanLiFileTypeEntity> findSomeFileType(Integer page, Integer size, String fileTypeName) {
        return zhiShiGuanLiFileTypeDao.findSomeFileType(fileTypeName);
    }
}
