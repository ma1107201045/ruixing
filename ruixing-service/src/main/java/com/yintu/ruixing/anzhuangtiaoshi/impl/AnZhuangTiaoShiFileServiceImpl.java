package com.yintu.ruixing.anzhuangtiaoshi.impl;

import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiFileDao;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiFileEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/7/14 18:06
 * @Version 1.0
 * 需求:
 */
@Service
@Transactional
public class AnZhuangTiaoShiFileServiceImpl implements AnZhuangTiaoShiFileService {
    @Autowired
    private AnZhuangTiaoShiFileDao anZhuangTiaoShiFileDao;

    @Override
    public List<AnZhuangTiaoShiFileEntity> findFileByNmae(Integer page, Integer size, Integer xdid, Integer filetype, String filename) {
        return anZhuangTiaoShiFileDao.findFileByNmae(xdid,filetype,filename);
    }

    @Override
    public AnZhuangTiaoShiFileEntity findById(Integer id) {
        return anZhuangTiaoShiFileDao.selectByPrimaryKey(id);
    }

    @Override
    public void deletFileByIds(Integer[] ids) {
        anZhuangTiaoShiFileDao.deletFileByIds(ids);
    }

    @Override
    public void editFileById(AnZhuangTiaoShiFileEntity anZhuangTiaoShiFileEntity) {
        anZhuangTiaoShiFileDao.editFileById(anZhuangTiaoShiFileEntity);
    }

    @Override
    public void addFile(AnZhuangTiaoShiFileEntity anZhuangTiaoShiFileEntity, String username,Integer senderid) {
        anZhuangTiaoShiFileEntity.setCreateName(username);
        anZhuangTiaoShiFileEntity.setUid(senderid);
        anZhuangTiaoShiFileEntity.setCreatetime(new Date());
        anZhuangTiaoShiFileDao.addFile(anZhuangTiaoShiFileEntity);
    }

    @Override
    public List<AnZhuangTiaoShiFileEntity> findShuChuFileByXid(Integer id, Integer page, Integer size) {
        return anZhuangTiaoShiFileDao.findShuChuFileByXid(id);
    }

    @Override
    public List<AnZhuangTiaoShiFileEntity> findShuRuFileByXid(Integer id, Integer page, Integer size) {
        return anZhuangTiaoShiFileDao.findShuRuFileByXid(id);
    }
}
