package com.yintu.ruixing.anzhuangtiaoshi.impl;

import com.yintu.ruixing.anzhuangtiaoshi.*;
import com.yintu.ruixing.common.MessageDao;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
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
    @Autowired
    private AnZhuangTiaoShiRecordMessageDao anZhuangTiaoShiRecordMessageDao;
    @Autowired
    private AnZhuangTiaoShiObjectAuditorDao anZhuangTiaoShiObjectAuditorDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageDao messageDao;

    @Override
    public List<AnZhuangTiaoShiFileEntity> findFileByNmaee(Integer page, Integer size, Integer xdid, Integer filetype, String filename,Integer uid) {
        return anZhuangTiaoShiFileDao.findFileByNmaee(xdid,filetype,filename,uid);
    }

    @Override
    public List<AnZhuangTiaoShiFileEntity> findFileByNmae(Integer page, Integer size, Integer xdid, Integer filetype, String filename,Integer uid) {
        return anZhuangTiaoShiFileDao.findFileByNmae(xdid,filetype,filename,uid);
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
    public List<AnZhuangTiaoShiFileEntity> findShuChuFile(Integer id, Integer page, Integer size, Integer uid) {
        return anZhuangTiaoShiFileDao.findShuChuFile(id,uid);
    }

    @Override
    public List<AnZhuangTiaoShiFileEntity> findFileById(Integer id) {
        return anZhuangTiaoShiFileDao.findFileById(id);
    }

    @Override
    public List<AnZhuangTiaoShiFileEntity> findShuChuFilee(Integer id, Integer page, Integer size, Integer uid) {
        return anZhuangTiaoShiFileDao.findShuChuFilee(id,uid);
    }

    @Override
    public List<AnZhuangTiaoShiFileEntity> findShuRuFile(Integer id, Integer page, Integer size, Integer uid) {
        return anZhuangTiaoShiFileDao.findShuRuFile(id,uid);
    }

    @Override
    public List<AnZhuangTiaoShiFileEntity> findShuRuFilee(Integer id, Integer page, Integer size, Integer uid) {
        return anZhuangTiaoShiFileDao.findShuRuFilee(id,uid);
    }

    @Override
    public List<AnZhuangTiaoShiObjectAuditorEntity> findXMByid(Integer id) {
        return anZhuangTiaoShiObjectAuditorDao.findXMByid(id);
    }

    @Override
    public List<AnZhuangTiaoShiRecordMessageEntity> findReordById(Integer id) {
        return anZhuangTiaoShiRecordMessageDao.findReordById(id);
    }

    @Override
    public void editAuditorByWJId(AnZhuangTiaoShiObjectAuditorEntity anZhuangTiaoShiObjectAuditorEntity, Integer id, String username, Integer receiverid, Integer senderId) {
        Date nowTime = new Date();
        if (anZhuangTiaoShiObjectAuditorEntity.getIsPass() == 0) {//审核未通过
            AnZhuangTiaoShiFileEntity onefileEntity = anZhuangTiaoShiFileDao.selectByPrimaryKey(id);
            AnZhuangTiaoShiFileEntity fileEntity=new AnZhuangTiaoShiFileEntity();
            fileEntity.setId(id);
            fileEntity.setAuditorState(3);
            anZhuangTiaoShiFileDao.editFileById(fileEntity);
            //新增消息  提醒发送者查看审核未通过的文件
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setTitle("文件");
            messageEntity.setCreateBy(username);
            messageEntity.setCreateTime(nowTime);
            messageEntity.setContext("“" + onefileEntity.getFileName() + "”文件审核未通过,请您查看！");
            messageEntity.setType((short) 2);
            messageEntity.setMessageType((short) 2);
            messageEntity.setFileId(id);
            messageEntity.setReceiverId(senderId);
            messageEntity.setSenderId(receiverid);
            messageEntity.setStatus((short) 1);
            messageDao.insertSelective(messageEntity);
            //新增查看消息
            AnZhuangTiaoShiRecordMessageEntity recordMessageEntity=new AnZhuangTiaoShiRecordMessageEntity();
            String reason = anZhuangTiaoShiObjectAuditorEntity.getReason();
            recordMessageEntity.setTypeid(id);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(nowTime);
            recordMessageEntity.setTypenum(2);
            recordMessageEntity.setContext("“" + onefileEntity.getFileName() + "”文件审核未通过,原因是:" + reason);
            anZhuangTiaoShiRecordMessageDao.insertSelective(recordMessageEntity);
        }
        if (anZhuangTiaoShiObjectAuditorEntity.getIsPass() == 1) {//审核通过
            AnZhuangTiaoShiFileEntity fileEntity=new AnZhuangTiaoShiFileEntity();
            fileEntity.setId(id);
            fileEntity.setAuditorState(2);
            anZhuangTiaoShiFileDao.editFileById(fileEntity);
            //新增查看消息
            AnZhuangTiaoShiFileEntity onefileEntity = anZhuangTiaoShiFileDao.selectByPrimaryKey(id);
            AnZhuangTiaoShiRecordMessageEntity recordMessageEntity=new AnZhuangTiaoShiRecordMessageEntity();
            recordMessageEntity.setTypeid(id);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(nowTime);
            recordMessageEntity.setTypenum(2);
            recordMessageEntity.setContext("“" + onefileEntity.getFileName() + "”文件审核已通过！");
            anZhuangTiaoShiRecordMessageDao.insertSelective(recordMessageEntity);
        }
        anZhuangTiaoShiObjectAuditorEntity.setCreatetime(nowTime);
        anZhuangTiaoShiObjectAuditorEntity.setCreatename(username);
        anZhuangTiaoShiObjectAuditorDao.editAuditorByXMId(anZhuangTiaoShiObjectAuditorEntity);
    }

    @Override
    public void editFileById(AnZhuangTiaoShiFileEntity anZhuangTiaoShiFileEntity, Integer id, Integer[] uids, Integer userid, String username) {
        AnZhuangTiaoShiFileEntity fileEntity=anZhuangTiaoShiFileDao.selectByPrimaryKey(id);
        Integer fileid = anZhuangTiaoShiFileEntity.getId();//文件id
        StringBuilder sb = new StringBuilder();
        Date nowTime = new Date();
        String fileName = anZhuangTiaoShiFileEntity.getFileName();
        Integer xdid = anZhuangTiaoShiFileEntity.getXdid();
        //删除中间表的审查人id
        anZhuangTiaoShiObjectAuditorDao.deleteAuditorByObjectid(id);
        //添加审查人
        if (uids != null) {
            for (Integer uid : uids) {
                AnZhuangTiaoShiObjectAuditorEntity objectAuditorEntity=new AnZhuangTiaoShiObjectAuditorEntity();
                objectAuditorEntity.setObjectId(id);
                objectAuditorEntity.setAuditorId(uid);
                objectAuditorEntity.setObjecttype(2);
                objectAuditorEntity.setCreatename(username);
                objectAuditorEntity.setCreatetime(nowTime);
                anZhuangTiaoShiObjectAuditorDao.insertSelective(objectAuditorEntity);
                //添加一条消息到消息表
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setCreateBy(username);//创建人
                messageEntity.setCreateTime(nowTime);//创建时间
                messageEntity.setContext("“" + fileName + "”文件需要您审核,请查看！");
                messageEntity.setType((short) 3);
                messageEntity.setMessageType((short) 2);
                messageEntity.setProjectId(xdid);
                messageEntity.setFileId(fileid);
                messageEntity.setSenderId(userid);
                messageEntity.setReceiverId(uid);
                messageEntity.setStatus((short) 1);
                messageService.sendMessage(messageEntity);
            }
        }
        anZhuangTiaoShiFileEntity.setUid(userid);
        anZhuangTiaoShiFileEntity.setUpdateTime(nowTime);
        anZhuangTiaoShiFileEntity.setUpdateName(username);
        if (anZhuangTiaoShiFileEntity.getFileType() == 2 && anZhuangTiaoShiFileEntity.getFabuType() == 2) {
            anZhuangTiaoShiFileEntity.setAuditorState(1);
        }
        anZhuangTiaoShiFileDao.editFileById(anZhuangTiaoShiFileEntity);
        Integer aa = 0;
        if (!fileEntity.getFileName().equals(anZhuangTiaoShiFileEntity.getFileName())) {
            sb.append("文件名改为" + anZhuangTiaoShiFileEntity.getFileName());
            aa++;
        }
        if (fileEntity.getAuditorState() == null || fileEntity.getAuditorState() != anZhuangTiaoShiFileEntity.getAuditorState()) {
            if (anZhuangTiaoShiFileEntity.getAuditorState() == null) {
                sb.append(" ");
            } else {
                if (anZhuangTiaoShiFileEntity.getAuditorState() == 1) {
                    sb.append("文件审核状态更改为  待审核,  ");
                    aa++;
                }
                if (anZhuangTiaoShiFileEntity.getAuditorState() == 2) {
                    sb.append("文件审核状态更改为  已审核未通过, ");
                    aa++;
                }
                if (anZhuangTiaoShiFileEntity.getAuditorState() == 3) {
                    sb.append("文件审核状态更改为  已审核已通过,");
                    aa++;
                }
            }
        }
        if (fileEntity.getFileType() != anZhuangTiaoShiFileEntity.getFileType()) {
            if (anZhuangTiaoShiFileEntity.getFileType() == 1) {
                sb.append("文件类型更改为  输入文件, ");
                aa++;
            }
            if (anZhuangTiaoShiFileEntity.getFileType() == 2) {
                sb.append("文件类型更改为  输出文件, ");
                aa++;
            }
        }
        if (fileEntity.getFabuType() != anZhuangTiaoShiFileEntity.getFabuType()) {
            if (anZhuangTiaoShiFileEntity.getFabuType() == 1) {
                sb.append("发布状态更改为  录入, ");
                aa++;
            }
            if (anZhuangTiaoShiFileEntity.getFabuType() == 2) {
                sb.append("发布状态更改为  发布, ");
                aa++;
            }
        }
        if (aa == 0) {
            sb.append("没有修改任何状态");
        }

        String context = sb.toString();
        AnZhuangTiaoShiRecordMessageEntity recordMessageEntity=new AnZhuangTiaoShiRecordMessageEntity();
        recordMessageEntity.setTypeid(id);
        recordMessageEntity.setOperatorname(username);
        recordMessageEntity.setOperatortime(nowTime);
        recordMessageEntity.setContext(context);
        recordMessageEntity.setTypenum(2);
        anZhuangTiaoShiRecordMessageDao.insertSelective(recordMessageEntity);

    }

    @Override
    public void addFile(AnZhuangTiaoShiFileEntity anZhuangTiaoShiFileEntity, String username,Integer senderid,Integer[] uids) {
        Date nowTime = new Date();
        anZhuangTiaoShiFileEntity.setCreateName(username);
        anZhuangTiaoShiFileEntity.setUid(senderid);
        anZhuangTiaoShiFileEntity.setCreatetime(nowTime);
        if (anZhuangTiaoShiFileEntity.getFabuType() == 2 && anZhuangTiaoShiFileEntity.getFileType() == 2) {
            anZhuangTiaoShiFileEntity.setAuditorState(1);
        } else {
            anZhuangTiaoShiFileEntity.setAuditorState(null);
        }
        if (anZhuangTiaoShiFileEntity.getRemarks().equals("")) {
            anZhuangTiaoShiFileEntity.setRemarks("无");
        }
        String fileName = anZhuangTiaoShiFileEntity.getFileName();
        Integer xdid = anZhuangTiaoShiFileEntity.getXdid();
        anZhuangTiaoShiFileDao.addFile(anZhuangTiaoShiFileEntity);
        Integer fileid = anZhuangTiaoShiFileEntity.getId();
        AnZhuangTiaoShiRecordMessageEntity recordMessageEntity=new AnZhuangTiaoShiRecordMessageEntity();
        recordMessageEntity.setTypeid(fileid);
        recordMessageEntity.setOperatorname(username);
        recordMessageEntity.setOperatortime(nowTime);
        recordMessageEntity.setContext("新增“" + fileName + "”文件");
        recordMessageEntity.setTypenum(2);
        anZhuangTiaoShiRecordMessageDao.insertSelective(recordMessageEntity);
        if (uids.length!=0) {
            for (Integer uid : uids) {
                AnZhuangTiaoShiObjectAuditorEntity objectAuditorEntity = new AnZhuangTiaoShiObjectAuditorEntity();
                objectAuditorEntity.setAuditorId(uid);
                objectAuditorEntity.setObjectId(fileid);
                objectAuditorEntity.setObjecttype(2);
                objectAuditorEntity.setCreatename(username);
                objectAuditorEntity.setCreatetime(nowTime);
                anZhuangTiaoShiObjectAuditorDao.insertSelective(objectAuditorEntity);
                //添加一条消息到消息表
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setCreateBy(username);//创建人
                messageEntity.setCreateTime(nowTime);//创建时间
                messageEntity.setContext("“" + fileName + "”文件需要您审核,请查看！");
                messageEntity.setType((short) 3);
                messageEntity.setMessageType((short) 2);
                messageEntity.setProjectId(xdid);
                messageEntity.setFileId(fileid);
                messageEntity.setSenderId(senderid);
                messageEntity.setReceiverId(uid);
                messageEntity.setStatus((short) 1);
                messageService.sendMessage(messageEntity);
            }
        }

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
