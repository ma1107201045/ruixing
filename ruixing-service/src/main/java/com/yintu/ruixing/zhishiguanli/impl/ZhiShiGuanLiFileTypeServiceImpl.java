package com.yintu.ruixing.zhishiguanli.impl;

import cn.hutool.core.bean.BeanUtil;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.master.common.MessageDao;
import com.yintu.ruixing.master.zhishiguanli.ZhiShiGuanLiFileTypeDao;
import com.yintu.ruixing.master.zhishiguanli.ZhiShiGuanLiFileTypeFileAuditorDao;
import com.yintu.ruixing.master.zhishiguanli.ZhiShiGuanLiFileTypeFileDao;
import com.yintu.ruixing.master.zhishiguanli.ZhiShiGuanLiFileTypeFileRecordmessageDao;
import com.yintu.ruixing.xitongguanli.*;
import com.yintu.ruixing.zhishiguanli.*;
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
    private AuditConfigurationUserService auditConfigurationUserService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private ZhiShiGuanLiFileTypeFileAuditorDao zhiShiGuanLiFileTypeFileAuditorDao;

    @Autowired
    private ZhiShiGuanLiFileTypeFileRecordmessageDao zhiShiGuanLiFileTypeFileRecordmessageDao;

    @Autowired
    private MessageDao messageDao;


    @Override
    public List<AuditConfigurationEntity> findAudit(short i, short i1, short i2) {
        return auditConfigurationService.findAudit(i, i1, i2);
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
    public List<ZhiShiGuanLiFileTypeFileEntity> findSomeFileBySize(Integer page, Integer size, String fileName, Integer id) {
        return zhiShiGuanLiFileTypeFileDao.findSomeFileBySize(fileName,id);
    }

    @Override
    public List<ZhiShiGuanLiFileTypeFileEntity> findSomeFileByTime(Integer page, Integer size, String fileName, Integer id) {
        return zhiShiGuanLiFileTypeFileDao.findSomeFileByTime(fileName,id);
    }

    @Override
    public void copyFileByParentid(Integer parentid, Integer[] fileid, ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity, String username) {
        for (int i = 0; i < fileid.length; i++) {
            ZhiShiGuanLiFileTypeFileEntity fileTypeFileEntity = zhiShiGuanLiFileTypeFileDao.selectByPrimaryKey(fileid[i]);
            String fileName = fileTypeFileEntity.getFileName();
            String filePath = fileTypeFileEntity.getFilePath();
            Integer fabuType = fileTypeFileEntity.getFabuType();
            Integer filesize = fileTypeFileEntity.getFilesize();
            String reason = fileTypeFileEntity.getReason();
            String remark = fileTypeFileEntity.getRemark();
            Integer auditstatus = fileTypeFileEntity.getAuditstatus();
            ZhiShiGuanLiFileTypeFileEntity fileEntity = new ZhiShiGuanLiFileTypeFileEntity();
            fileEntity.setTid(parentid);
            fileEntity.setFileName(fileName);
            fileEntity.setFilePath(filePath);
            fileEntity.setFabuType(fabuType);
            fileEntity.setCreateName(username);
            fileEntity.setCreatetime(new Date());
            fileEntity.setParentid(0);
            fileEntity.setFilesize(filesize);
            fileEntity.setReason(reason);
            fileEntity.setRemark(remark);
            fileEntity.setAuditstatus(auditstatus);
            zhiShiGuanLiFileTypeFileDao.insertSelective(fileEntity);
        }
    }

    @Override
    public void pasteFileByParentid(Integer parentid, Integer[] typeid, ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity) {
        for (int i = 0; i < typeid.length; i++) {
            zhiShiGuanLiFileTypeFileEntity.setId(typeid[i]);
            zhiShiGuanLiFileTypeFileEntity.setTid(parentid);
            zhiShiGuanLiFileTypeFileDao.updateByPrimaryKeySelective(zhiShiGuanLiFileTypeFileEntity);
        }
    }

    @Override
    public List<ZhiShiGuanLiFileTypeEntity> findFileTypeByParentid(Integer parentid) {
        return zhiShiGuanLiFileTypeDao.findFileTypeByParentid(parentid);
    }

    @Override
    public List<TreeNodeUtil> findShuXing(Integer id) {
        List<ZhiShiGuanLiFileTypeEntity> zhiShiGuanLiFileTypeEntityList = zhiShiGuanLiFileTypeDao.findFristType(id);
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
            if (shuXing.size() != 0) {
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
        return zhiShiGuanLiFileTypeFileDao.findFileById(id, fileName);
    }

    @Override
    public List<ZhiShiGuanLiFileTypeFileEntity> findSomeFile(Integer page, Integer size, String fileName, Integer id) {
        return zhiShiGuanLiFileTypeFileDao.findSomeFile(fileName, id);
    }

    @Override
    public void updateFileById(ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity, Integer id, String username,Integer uid,Integer[] auditorid,Integer[] sort) {
        zhiShiGuanLiFileTypeFileEntity.setCreatetime(new Date());
        zhiShiGuanLiFileTypeFileEntity.setCreateName(username);
        zhiShiGuanLiFileTypeFileEntity.setAuditstatus(1);
        zhiShiGuanLiFileTypeFileEntity.setUserid(uid);
        zhiShiGuanLiFileTypeFileDao.updateByPrimaryKeySelective(zhiShiGuanLiFileTypeFileEntity);
        String fileName = zhiShiGuanLiFileTypeFileEntity.getFileName();
        if (auditorid.length == 0 && sort.length == 0) {//使用审批流 来进行审批
            List<AuditConfigurationEntity> audit = auditConfigurationService.findAudit((short) 4, (short) 1, (short) 1);//查询审批流程
            List<AuditConfigurationUserEntity> userEntities = auditConfigurationUserService.findByauditConfigurationId(audit.get(0).getId());//查询审批流的人员
            for (AuditConfigurationUserEntity userEntity : userEntities) {
                if (userEntity.getUserId() != uid.longValue()) {
                    ZhiShiGuanLiFileTypeFileAuditorEntity fileAuditorEntity = new ZhiShiGuanLiFileTypeFileAuditorEntity();
                    fileAuditorEntity.setFileId(id);
                    fileAuditorEntity.setAuditorId(userEntity.getUserId().intValue());
                    fileAuditorEntity.setSort(userEntity.getSort());
                    if (userEntity.getSort() == 1) {
                        fileAuditorEntity.setActivate((short) 1);
                        //给第一批审核人 发消息
                        MessageEntity messageEntity=new MessageEntity();
                        messageEntity.setCreateBy(username);
                        messageEntity.setCreateTime(new Date());
                        messageEntity.setContext(fileName+"文件需要您审核,请查看！");
                        messageEntity.setType((short)6);
                        messageEntity.setMessageType((short)2);
                        messageEntity.setFileId(id);
                        messageEntity.setSenderId(uid);
                        messageEntity.setReceiverId(userEntity.getUserId().intValue());
                        messageEntity.setStatus((short)1);
                        messageDao.insertSelective(messageEntity);
                    } else {
                        fileAuditorEntity.setActivate((short) 0);
                    }
                    zhiShiGuanLiFileTypeFileAuditorDao.insertSelective(fileAuditorEntity);
                }
            }
        } else {//没有设置审批流  用选择的人进行审批
            if (auditorid.length != 0 && sort.length != 0 && auditorid.length == sort.length) {
                for (int i = 0; i < auditorid.length; i++) {
                    if (auditorid[i] != uid) {
                        ZhiShiGuanLiFileTypeFileAuditorEntity fileAuditorEntity = new ZhiShiGuanLiFileTypeFileAuditorEntity();
                        fileAuditorEntity.setFileId(id);
                        fileAuditorEntity.setAuditorId(auditorid[i]);
                        fileAuditorEntity.setSort(sort[i]);
                        if (sort[i] == 1) {
                            fileAuditorEntity.setActivate((short) 1);
                        } else {
                            fileAuditorEntity.setActivate((short) 0);
                        }
                        zhiShiGuanLiFileTypeFileAuditorDao.insertSelective(fileAuditorEntity);
                    }
                }
            }
        }
        //添加日志记录
        ZhiShiGuanLiFileTypeFileRecordmessageEntity recordmessageEntity=new ZhiShiGuanLiFileTypeFileRecordmessageEntity();
        recordmessageEntity.setTypeid(id);
        recordmessageEntity.setOperatorname(username);
        recordmessageEntity.setOperatortime(new Date());
        recordmessageEntity.setContext(username+"更新了此文件");
        recordmessageEntity.setTypenum(2);
        zhiShiGuanLiFileTypeFileRecordmessageDao.insertSelective(recordmessageEntity);
    }

    @Override
    public void addOneFile(String fileName, Date createtime, String filePath, Integer id1, String username,Integer filesize) {
        createtime = new Date();
        zhiShiGuanLiFileTypeFileDao.addOneFile(fileName, createtime, filePath, id1, username,filesize);
    }

    @Override
    public ZhiShiGuanLiFileTypeFileEntity findFile(Integer id) {
        return zhiShiGuanLiFileTypeFileDao.selectByPrimaryKey(id);
    }

    @Override
    public List<ZhiShiGuanLiFileTypeFileRecordmessageEntity> findRecordmessageByFileid(Integer id) {
        return zhiShiGuanLiFileTypeFileRecordmessageDao.findRecordmessageByFileid(id);
    }

    @Override
    public List<MessageEntity> findXiaoXi(Integer senderid) {
        Integer type = 6;
        return messageDao.findXiaoXi(senderid, type);
    }

    @Override
    public void addFile(ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity, String username, Integer uid, Integer[] auditorid, Integer[] sort) {
        zhiShiGuanLiFileTypeFileEntity.setParentid(0);
        zhiShiGuanLiFileTypeFileEntity.setUserid(uid);
        zhiShiGuanLiFileTypeFileEntity.setAuditstatus(1);
        zhiShiGuanLiFileTypeFileEntity.setCreatetime(new Date());
        zhiShiGuanLiFileTypeFileEntity.setCreateName(username);
        zhiShiGuanLiFileTypeFileDao.insertSelective(zhiShiGuanLiFileTypeFileEntity);
        Integer fileid = zhiShiGuanLiFileTypeFileEntity.getId();//新增文件的id
        String fileName = zhiShiGuanLiFileTypeFileEntity.getFileName();
        if (auditorid.length == 0 && sort.length == 0) {//使用审批流 来进行审批
            List<AuditConfigurationEntity> audit = auditConfigurationService.findAudit((short) 4, (short) 1, (short) 1);//查询审批流程
            List<AuditConfigurationUserEntity> userEntities = auditConfigurationUserService.findByauditConfigurationId(audit.get(0).getId());//查询审批流的人员
            for (AuditConfigurationUserEntity userEntity : userEntities) {
                if (userEntity.getUserId() != uid.longValue()) {
                    ZhiShiGuanLiFileTypeFileAuditorEntity fileAuditorEntity = new ZhiShiGuanLiFileTypeFileAuditorEntity();
                    fileAuditorEntity.setFileId(fileid);
                    fileAuditorEntity.setAuditorId(userEntity.getUserId().intValue());
                    fileAuditorEntity.setSort(userEntity.getSort());
                    if (userEntity.getSort() == 1) {
                        fileAuditorEntity.setActivate((short) 1);
                        //给第一批审核人 发消息
                        MessageEntity messageEntity=new MessageEntity();
                        messageEntity.setCreateBy(username);
                        messageEntity.setCreateTime(new Date());
                        messageEntity.setContext(fileName+"文件需要您审核,请查看！");
                        messageEntity.setType((short)6);
                        messageEntity.setMessageType((short)2);
                        messageEntity.setFileId(fileid);
                        messageEntity.setSenderId(uid);
                        messageEntity.setReceiverId(userEntity.getUserId().intValue());
                        messageEntity.setStatus((short)1);
                        messageDao.insertSelective(messageEntity);
                    } else {
                        fileAuditorEntity.setActivate((short) 0);
                    }
                    zhiShiGuanLiFileTypeFileAuditorDao.insertSelective(fileAuditorEntity);
                }
            }
        } else {//没有设置审批流  用选择的人进行审批
            if (auditorid.length != 0 && sort.length != 0 && auditorid.length == sort.length) {
                for (int i = 0; i < auditorid.length; i++) {
                    if (auditorid[i] != uid) {
                        ZhiShiGuanLiFileTypeFileAuditorEntity fileAuditorEntity = new ZhiShiGuanLiFileTypeFileAuditorEntity();
                        fileAuditorEntity.setFileId(fileid);
                        fileAuditorEntity.setAuditorId(auditorid[i]);
                        fileAuditorEntity.setSort(sort[i]);
                        if (sort[i] == 1) {
                            fileAuditorEntity.setActivate((short) 1);
                        } else {
                            fileAuditorEntity.setActivate((short) 0);
                        }
                        zhiShiGuanLiFileTypeFileAuditorDao.insertSelective(fileAuditorEntity);
                    }
                }
            }
        }
        //添加日志记录
        ZhiShiGuanLiFileTypeFileRecordmessageEntity recordmessageEntity=new ZhiShiGuanLiFileTypeFileRecordmessageEntity();
        recordmessageEntity.setTypeid(fileid);
        recordmessageEntity.setOperatorname(username);
        recordmessageEntity.setOperatortime(new Date());
        recordmessageEntity.setContext(username+"添加了此文件");
        recordmessageEntity.setTypenum(2);
        zhiShiGuanLiFileTypeFileRecordmessageDao.insertSelective(recordmessageEntity);
    }

    @Override
    public void editFileTypeById(ZhiShiGuanLiFileTypeEntity zhiShiGuanLiFileTypeEntity, String username) {
        zhiShiGuanLiFileTypeEntity.setUpdateName(username);
        zhiShiGuanLiFileTypeEntity.setUpdateTime(new Date());
        zhiShiGuanLiFileTypeDao.updateByPrimaryKeySelective(zhiShiGuanLiFileTypeEntity);
    }

    @Override
    public void addFileType(ZhiShiGuanLiFileTypeEntity zhiShiGuanLiFileTypeEntity, String username, Integer parentid) {
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
