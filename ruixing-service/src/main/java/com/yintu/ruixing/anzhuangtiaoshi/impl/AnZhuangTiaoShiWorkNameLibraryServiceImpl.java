package com.yintu.ruixing.anzhuangtiaoshi.impl;

import com.yintu.ruixing.anzhuangtiaoshi.*;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/7/16 16:49
 * @Version 1.0
 * 需求:安装调试现场作业的作业项
 */
@Service
@Transactional
public class AnZhuangTiaoShiWorkNameLibraryServiceImpl implements AnZhuangTiaoShiWorkNameLibraryService {

    @Autowired
    private AnZhuangTiaoShiWorkNameLibraryDao anZhuangTiaoShiWorkNameLibraryDao;

    @Autowired
    private AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao;

    @Autowired
    private AnZhuangTiaoShiWorksAuditorDao anZhuangTiaoShiWorksAuditorDao;

    @Autowired
    private MessageService messageService;

    @Autowired
    private AnZhuangTiaoShiWorksRecordMessageDao anZhuangTiaoShiWorksRecordMessageDao;

    @Override
    public List<AnZhuangTiaoShiWorksRecordMessageEntity> findWorkNameLibraryRecordMessageById(Integer id) {
        return anZhuangTiaoShiWorksRecordMessageDao.findWorkNameLibraryRecordMessageById(id);
    }

    @Override
    public void editAuditorByWId(Integer id, AnZhuangTiaoShiWorksAuditorEntity anZhuangTiaoShiWorksAuditorEntity, String username, Integer receiverid, Integer senderId) {
        Date nowTime = new Date();
        if (anZhuangTiaoShiWorksAuditorEntity.getIsPass() == 0) {//审核未通过
            AnZhuangTiaoShiWorkNameLibraryEntity workNameLibraryEntity = anZhuangTiaoShiWorkNameLibraryDao.findOneWorkNameByid(id, receiverid);
            //添加一条消息到消息表
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(username);//创建人
            messageEntity.setCreateTime(nowTime);//创建时间
            messageEntity.setContext("“" + workNameLibraryEntity.getWorkname() + "”作业项审核未通过,请您查看！");
            messageEntity.setType((short) 3);
            //messageEntity.setMessageType((short) 2);
            messageEntity.setProjectId(id);
            messageEntity.setSenderId(receiverid);
            messageEntity.setReceiverId(senderId);
            messageEntity.setStatus((short) 1);
            messageEntity.setSmallType((short) 5);
            messageService.sendMessage(messageEntity);
            //新增查看消息
            AnZhuangTiaoShiWorksRecordMessageEntity recordMessageEntity = new AnZhuangTiaoShiWorksRecordMessageEntity();
            String reason = anZhuangTiaoShiWorksAuditorEntity.getReason();
            recordMessageEntity.setTypeid(id);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(nowTime);
            recordMessageEntity.setContext("“" + workNameLibraryEntity.getWorkname() + "”作业项审核未通过,原因是:" + reason);
            recordMessageEntity.setTypenum(3);
            anZhuangTiaoShiWorksRecordMessageDao.insertSelective(recordMessageEntity);
        }
        if (anZhuangTiaoShiWorksAuditorEntity.getIsPass() == 1) {//审核通过
            AnZhuangTiaoShiWorkNameLibraryEntity workNameLibraryEntity = anZhuangTiaoShiWorkNameLibraryDao.findOneWorkNameByid(id, receiverid);
            //新增查看消息
            AnZhuangTiaoShiWorksRecordMessageEntity recordMessageEntity = new AnZhuangTiaoShiWorksRecordMessageEntity();
            recordMessageEntity.setTypeid(id);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(nowTime);
            recordMessageEntity.setContext("“" + workNameLibraryEntity.getWorkname() + "”作业项版本审核通过 ");
            recordMessageEntity.setTypenum(3);
            anZhuangTiaoShiWorksRecordMessageDao.insertSelective(recordMessageEntity);
        }
        anZhuangTiaoShiWorksAuditorEntity.setUpdatename(username);
        anZhuangTiaoShiWorksAuditorEntity.setUpdatetime(nowTime);
        anZhuangTiaoShiWorksAuditorEntity.setAuditorId(receiverid);
        anZhuangTiaoShiWorksAuditorDao.updateByPrimaryKeySelective(anZhuangTiaoShiWorksAuditorEntity);
        List<Integer> ispass = anZhuangTiaoShiWorksAuditorDao.findIsPassByObjidd(id);
        if (ispass.size() == 1 && ispass.get(0) == null) {
            AnZhuangTiaoShiWorkNameLibraryEntity workNameLibraryEntity = new AnZhuangTiaoShiWorkNameLibraryEntity();
            workNameLibraryEntity.setId(id);
            workNameLibraryEntity.setAuditorState(1);
            anZhuangTiaoShiWorkNameLibraryDao.updateByPrimaryKeySelective(workNameLibraryEntity);
        }
        if (ispass.size() == 1 && ispass.get(0) != null && ispass.get(0) == 1) {
            AnZhuangTiaoShiWorkNameLibraryEntity workNameLibraryEntity = new AnZhuangTiaoShiWorkNameLibraryEntity();
            workNameLibraryEntity.setId(id);
            workNameLibraryEntity.setAuditorState(2);
            anZhuangTiaoShiWorkNameLibraryDao.updateByPrimaryKeySelective(workNameLibraryEntity);
        }
        if (ispass.size() == 1 && ispass.get(0) != null && ispass.get(0) == 0) {
            AnZhuangTiaoShiWorkNameLibraryEntity workNameLibraryEntity = new AnZhuangTiaoShiWorkNameLibraryEntity();
            workNameLibraryEntity.setId(id);
            workNameLibraryEntity.setAuditorState(1);
            anZhuangTiaoShiWorkNameLibraryDao.updateByPrimaryKeySelective(workNameLibraryEntity);
        }
        if (ispass.size() > 1 && ispass.get(0) == null) {
            AnZhuangTiaoShiWorkNameLibraryEntity workNameLibraryEntity = new AnZhuangTiaoShiWorkNameLibraryEntity();
            workNameLibraryEntity.setId(id);
            workNameLibraryEntity.setAuditorState(1);
            anZhuangTiaoShiWorkNameLibraryDao.updateByPrimaryKeySelective(workNameLibraryEntity);
        }
        if (ispass.size() > 1 && ispass.get(0) != null ) {
            AnZhuangTiaoShiWorkNameLibraryEntity workNameLibraryEntity = new AnZhuangTiaoShiWorkNameLibraryEntity();
            workNameLibraryEntity.setId(id);
            workNameLibraryEntity.setAuditorState(3);
            anZhuangTiaoShiWorkNameLibraryDao.updateByPrimaryKeySelective(workNameLibraryEntity);
        }

    }


    @Override
    public List<AnZhuangTiaoShiWorksRecordMessageEntity> findRecordMessageById(Integer id) {
        return anZhuangTiaoShiWorksRecordMessageDao.findRecordMessageById(id);
    }

    @Override
    public AnZhuangTiaoShiWorkNameLibraryEntity findWorkNameById(Integer id, Integer receiverid) {
        return anZhuangTiaoShiWorkNameLibraryDao.findOneWorkNameByid(id, receiverid);
    }

    @Override
    public List<AnZhuangTiaoShiWorkNameLibraryEntity> findAllWorksById(Integer id) {
        return anZhuangTiaoShiWorkNameLibraryDao.findAllWorksById(id);
    }

    @Override
    public List<AnZhuangTiaoShiWorkNameLibraryEntity> findAllWorkName() {
        return anZhuangTiaoShiWorkNameLibraryDao.findAllWorkName();
    }

    @Override
    public void deleteWorkNameByIds(Integer[] ids) {
        anZhuangTiaoShiWorkNameLibraryDao.deleteWorkNameByIds(ids);
        anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao.deleteWorkNameByIdss(ids);
    }

    @Override
    public void editWorkNameById(Integer id, AnZhuangTiaoShiWorkNameLibraryEntity anZhuangTiaoShiWorkNameLibraryEntity, String username, Integer receiverid, Integer[] uids) {
        Date nowTime = new Date();
        StringBuilder sb = new StringBuilder();
        AnZhuangTiaoShiWorkNameLibraryEntity workNameLibraryEntity = anZhuangTiaoShiWorkNameLibraryDao.findOneWorkNameByid(id, receiverid);
        anZhuangTiaoShiWorkNameLibraryEntity.setUpdatename(username);
        anZhuangTiaoShiWorkNameLibraryEntity.setUpdatetime(nowTime);
        anZhuangTiaoShiWorkNameLibraryEntity.setAuditorState(1);
        anZhuangTiaoShiWorkNameLibraryDao.updateByPrimaryKeySelective(anZhuangTiaoShiWorkNameLibraryEntity);
        if (!workNameLibraryEntity.getWorkname().equals(anZhuangTiaoShiWorkNameLibraryEntity.getWorkname())) {
            sb.append(" 作业项名改为" + anZhuangTiaoShiWorkNameLibraryEntity.getWorkname() + ",");
        } else {
            sb.append("没有修改任何状态");
        }
        AnZhuangTiaoShiWorksRecordMessageEntity recordMessageEntity = new AnZhuangTiaoShiWorksRecordMessageEntity();
        recordMessageEntity.setTypeid(id);
        recordMessageEntity.setOperatorname(username);
        recordMessageEntity.setOperatortime(nowTime);
        recordMessageEntity.setContext(sb.toString());
        recordMessageEntity.setTypenum(3);
        anZhuangTiaoShiWorksRecordMessageDao.insertSelective(recordMessageEntity);
        for (Integer uid : uids) {
            AnZhuangTiaoShiWorksAuditorEntity worksAuditorEntity = new AnZhuangTiaoShiWorksAuditorEntity();
            worksAuditorEntity.setObjectId(id);
            worksAuditorEntity.setAuditorId(uid);
            worksAuditorEntity.setObjecttype(2);
            worksAuditorEntity.setDoname(username);
            worksAuditorEntity.setDotime(nowTime);
            anZhuangTiaoShiWorksAuditorDao.insertSelective(worksAuditorEntity);
            //添加一条消息到消息表
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(username);//创建人
            messageEntity.setCreateTime(nowTime);//创建时间
            messageEntity.setContext("“" + anZhuangTiaoShiWorkNameLibraryEntity.getWorkname() + "”作业项需要您审核,请查看！");
            messageEntity.setType((short) 3);
            //messageEntity.setMessageType((short) 2);
            messageEntity.setProjectId(id);
            //messageEntity.setFileId(fileid);
            messageEntity.setSenderId(receiverid);
            messageEntity.setReceiverId(uid);
            messageEntity.setStatus((short) 1);
            messageEntity.setSmallType((short) 7);
            messageService.sendMessage(messageEntity);
        }
    }

    @Override
    public List<AnZhuangTiaoShiWorkNameLibraryEntity> findWorkName(Integer page, Integer size, String workname) {
        return anZhuangTiaoShiWorkNameLibraryDao.findWorkName(workname);
    }

    @Override
    public void addWorkName(AnZhuangTiaoShiWorkNameLibraryEntity anZhuangTiaoShiWorkNameLibraryEntity, Integer[] uids, String username, Integer receiverid) {
        Date nowTime = new Date();
        anZhuangTiaoShiWorkNameLibraryEntity.setCreatename(username);
        anZhuangTiaoShiWorkNameLibraryEntity.setCreatetime(nowTime);
        anZhuangTiaoShiWorkNameLibraryEntity.setAuditorState(1);
        anZhuangTiaoShiWorkNameLibraryEntity.setUserid(receiverid);
        anZhuangTiaoShiWorkNameLibraryDao.insertSelective(anZhuangTiaoShiWorkNameLibraryEntity);
        Integer id = anZhuangTiaoShiWorkNameLibraryEntity.getId();
        for (Integer uid : uids) {
            AnZhuangTiaoShiWorksAuditorEntity worksAuditorEntity = new AnZhuangTiaoShiWorksAuditorEntity();
            worksAuditorEntity.setObjectId(id);
            worksAuditorEntity.setAuditorId(uid);
            worksAuditorEntity.setObjecttype(2);
            worksAuditorEntity.setDoname(username);
            worksAuditorEntity.setDotime(nowTime);
            anZhuangTiaoShiWorksAuditorDao.insertSelective(worksAuditorEntity);
            //添加一条消息到消息表
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(username);//创建人
            messageEntity.setCreateTime(nowTime);//创建时间
            messageEntity.setContext("“" + anZhuangTiaoShiWorkNameLibraryEntity.getWorkname() + "”作业项需要您审核,请查看！");
            messageEntity.setType((short) 3);
            //messageEntity.setMessageType((short) 2);
            messageEntity.setProjectId(id);
            //messageEntity.setFileId(fileid);
            messageEntity.setSenderId(receiverid);
            messageEntity.setReceiverId(uid);
            messageEntity.setStatus((short) 1);
            messageEntity.setSmallType((short) 7);
            messageService.sendMessage(messageEntity);
        }
        AnZhuangTiaoShiWorksRecordMessageEntity recordMessageEntity = new AnZhuangTiaoShiWorksRecordMessageEntity();
        recordMessageEntity.setTypeid(id);
        recordMessageEntity.setOperatorname(username);
        recordMessageEntity.setOperatortime(nowTime);
        recordMessageEntity.setContext("新增" + anZhuangTiaoShiWorkNameLibraryEntity.getWorkname() + "作业项");
        recordMessageEntity.setTypenum(3);
        anZhuangTiaoShiWorksRecordMessageDao.insertSelective(recordMessageEntity);
    }
}
