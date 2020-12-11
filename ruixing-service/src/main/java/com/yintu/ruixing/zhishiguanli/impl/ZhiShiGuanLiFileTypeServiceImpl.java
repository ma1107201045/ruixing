package com.yintu.ruixing.zhishiguanli.impl;

import cn.hutool.core.bean.BeanUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.master.zhishiguanli.ZhiShiGuanLiFileTypeDao;
import com.yintu.ruixing.master.zhishiguanli.ZhiShiGuanLiFileTypeFileDao;
import com.yintu.ruixing.xitongguanli.*;
import com.yintu.ruixing.zhishiguanli.ZhiShiGuanLiFileTypeEntity;
import com.yintu.ruixing.zhishiguanli.ZhiShiGuanLiFileTypeFileEntity;
import com.yintu.ruixing.zhishiguanli.ZhiShiGuanLiFileTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Autowired
    private AuditConfigurationService auditConfigurationService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;


    @Override
    public List<AuditConfigurationEntity> findAudit(short i, short i1, short i2) {
        return auditConfigurationService.findAudit(i,i1,i2);
    }

    @Override
    public List<TreeNodeUtil> findTree() {
        List<RoleEntity> roleEntities = roleService.findAll();
        List<TreeNodeUtil> firstTreeNodeUtils = new ArrayList<>();
        for (RoleEntity roleEntity : roleEntities) {
            List<UserEntity> userEntities = roleService.findUsersByIds(roleEntity.getId());
            List<TreeNodeUtil> secondTreeNodeUtils = new ArrayList<>();
            TreeNodeUtil firstTreeNodeUtil = new TreeNodeUtil();
            firstTreeNodeUtil.setId(roleEntity.getId());
            firstTreeNodeUtil.setLabel(roleEntity.getName());
            firstTreeNodeUtil.setChildren(secondTreeNodeUtils);
            firstTreeNodeUtils.add(firstTreeNodeUtil);
            for (UserEntity userEntity : userEntities) {
                TreeNodeUtil secondTreeNodeUtil = new TreeNodeUtil();
                secondTreeNodeUtil.setId(userEntity.getId());
                secondTreeNodeUtil.setLabel(userEntity.getTrueName());
                secondTreeNodeUtil.setA_attr(BeanUtil.beanToMap(roleEntity));
                secondTreeNodeUtils.add(secondTreeNodeUtil);
            }
        }
        return firstTreeNodeUtils;
    }

    @Override
    public List<ZhiShiGuanLiFileTypeFileEntity> findSomeFileBySize(Integer page, Integer size, Integer id) {
        return zhiShiGuanLiFileTypeFileDao.findSomeFileBySize(id);
    }

    @Override
    public List<ZhiShiGuanLiFileTypeFileEntity> findSomeFileByTime(Integer page, Integer size,  Integer id) {
        return zhiShiGuanLiFileTypeFileDao.findSomeFileByTime(id);
    }

    @Override
    public void copyFileByParentid(Integer parentid, Integer[] fileid, ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity,String username) {
        for (int i = 0; i < fileid.length; i++) {
            ZhiShiGuanLiFileTypeFileEntity fileTypeFileEntity=zhiShiGuanLiFileTypeFileDao.selectByPrimaryKey(fileid[i]);
            String fileName = fileTypeFileEntity.getFileName();
            String filePath = fileTypeFileEntity.getFilePath();
            Integer fabuType = fileTypeFileEntity.getFabuType();
            ZhiShiGuanLiFileTypeFileEntity fileEntity=new ZhiShiGuanLiFileTypeFileEntity();
            fileEntity.setTid(parentid);
            fileEntity.setFileName(fileName);
            fileEntity.setFilePath(filePath);
            fileEntity.setFabuType(fabuType);
            fileEntity.setCreateName(username);
            fileEntity.setCreatetime(new Date());
            zhiShiGuanLiFileTypeFileDao.insertSelective(fileEntity);
        }
    }

    @Override
    public void pasteFileByParentid(Integer parentid, Integer[] typeid, ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity) {
        for (int i = 0; i < typeid.length; i++) {
            zhiShiGuanLiFileTypeFileEntity.setId(typeid[i]);
            zhiShiGuanLiFileTypeFileEntity.setParentid(parentid);
            zhiShiGuanLiFileTypeFileDao.updateByPrimaryKeySelective(zhiShiGuanLiFileTypeFileEntity);
        }
    }

    @Override
    public List<ZhiShiGuanLiFileTypeEntity> findFileTypeByParentid(Integer parentid) {
        return zhiShiGuanLiFileTypeDao.findFileTypeByParentid(parentid);
    }

    @Override
    public List<TreeNodeUtil> findShuXing(Integer id) {
        List<ZhiShiGuanLiFileTypeEntity> zhiShiGuanLiFileTypeEntityList=zhiShiGuanLiFileTypeDao.findFristType(id);
        List<TreeNodeUtil> treeNodeUtils = new ArrayList<>();
        for (ZhiShiGuanLiFileTypeEntity zhiShiGuanLiFileTypeEntity : zhiShiGuanLiFileTypeEntityList) {
            TreeNodeUtil treeNodeUtil = new TreeNodeUtil();
            Integer idd = zhiShiGuanLiFileTypeEntity.getId();
            Integer parentid = zhiShiGuanLiFileTypeEntity.getParentid();
            String filetype = zhiShiGuanLiFileTypeEntity.getFiletype();
            String filemiaoshu = zhiShiGuanLiFileTypeEntity.getFilemiaoshu();
            treeNodeUtil.setId((long) idd);
            treeNodeUtil.setLabel(filetype);
            treeNodeUtil.setValue(filemiaoshu);
            treeNodeUtil.setIcon(parentid.toString());
            List<TreeNodeUtil> shuXing = this.findShuXing(idd);
            if (shuXing.size()!=0) {
                treeNodeUtil.setChildren(shuXing);
            }
            treeNodeUtils.add(treeNodeUtil);
        }
        return treeNodeUtils;
    }

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
    public void addFileType(ZhiShiGuanLiFileTypeEntity zhiShiGuanLiFileTypeEntity,String username,Integer parentid) {
        zhiShiGuanLiFileTypeEntity.setCreateTime(new Date());
        zhiShiGuanLiFileTypeEntity.setCreateName(username);
        zhiShiGuanLiFileTypeEntity.setParentid(parentid);
        zhiShiGuanLiFileTypeDao.insertSelective(zhiShiGuanLiFileTypeEntity);
    }

    @Override
    public List<ZhiShiGuanLiFileTypeEntity> findSomeFileType(Integer page, Integer size, String fileTypeName) {
        return zhiShiGuanLiFileTypeDao.findSomeFileType(fileTypeName);
    }
}
