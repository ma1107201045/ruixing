package com.yintu.ruixing.anzhuangtiaoshi.impl;

import com.yintu.ruixing.anzhuangtiaoshi.*;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.master.anzhuangtiaoshi.AnZhuangTiaoShiWenTiAuditorDao;
import com.yintu.ruixing.master.anzhuangtiaoshi.AnZhuangTiaoShiWenTiDao;
import com.yintu.ruixing.master.anzhuangtiaoshi.AnZhuangTiaoShiWenTiFileDao;
import com.yintu.ruixing.master.anzhuangtiaoshi.AnZhuangTiaoShiWenTiRecordMessageDao;
import com.yintu.ruixing.master.xitongguanli.DepartmentDao;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWenTiService;
import com.yintu.ruixing.xitongguanli.DepartmentEntity;
import com.yintu.ruixing.xitongguanli.DepartmentEntityExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/8/9 10:30
 * @Version 1.0
 * 需求:安装调试 问题跟踪
 */
@Service
@Transactional
public class AnZhuangTiaoShiWenTiServiceImpl implements AnZhuangTiaoShiWenTiService {
    @Autowired
    private AnZhuangTiaoShiWenTiDao anZhuangTiaoShiWenTiDao;

    @Autowired
    private AnZhuangTiaoShiWenTiFileDao anZhuangTiaoShiWenTiFileDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private AnZhuangTiaoShiWenTiRecordMessageDao anZhuangTiaoShiWenTiRecordMessageDao;

    @Autowired
    private MessageService messageService;

    @Autowired
    private AnZhuangTiaoShiWenTiAuditorDao anZhuangTiaoShiWenTiAuditorDao;

    @Override
    public List<AnZhuangTiaoShiRecordMessageEntity> findFileRecordMessageById(Integer id) {
        return anZhuangTiaoShiWenTiRecordMessageDao.findFileRecordMessageById(id);
    }

    @Override
    public AnZhuangTiaoShiWenTiFileEntity findWenTiFileById(Integer id) {
        return anZhuangTiaoShiWenTiFileDao.selectByPrimaryKey(id);
    }

    @Override
    public void editAuditorByWJId(Integer id, AnZhuangTiaoShiWenTiAuditorEntity anZhuangTiaoShiWenTiAuditorEntity, String username, Integer receiverid, Integer senderId) {
        Date nowTime = new Date();
        AnZhuangTiaoShiWenTiFileEntity fileEntity = anZhuangTiaoShiWenTiFileDao.selectByPrimaryKey(id);
        if (anZhuangTiaoShiWenTiAuditorEntity.getIsPass() == 0) {//审核未通过
            AnZhuangTiaoShiWenTiFileEntity wenTiFileEntity = new AnZhuangTiaoShiWenTiFileEntity();
            wenTiFileEntity.setId(id);
            wenTiFileEntity.setAuditorState(3);
            anZhuangTiaoShiWenTiFileDao.updateByPrimaryKeySelective(wenTiFileEntity);
            //添加一条消息到消息表
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(username);//创建人
            messageEntity.setCreateTime(nowTime);//创建时间
            messageEntity.setContext("“" + fileEntity.getFileName() + "”文件审核未通过,请您查看！");
            messageEntity.setType((short) 3);
            //messageEntity.setMessageType((short) 2);
            messageEntity.setFileId(id);
            messageEntity.setSenderId(receiverid);
            messageEntity.setReceiverId(senderId);
            messageEntity.setStatus((short) 1);
            Integer fenlei = fileEntity.getFenlei();
            if (fenlei==1){
                messageEntity.setSmallType((short) 2);
            }
            if (fenlei==2){
                messageEntity.setSmallType((short) 3);
            }
            messageService.sendMessage(messageEntity);
            //新增查看消息
            AnZhuangTiaoShiWenTiRecordMessageEntity recordMessageEntity = new AnZhuangTiaoShiWenTiRecordMessageEntity();
            String reason = anZhuangTiaoShiWenTiAuditorEntity.getReason();
            recordMessageEntity.setTypeid(id);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(nowTime);
            recordMessageEntity.setContext("“" + fileEntity.getFileName() + "”问题审核未通过,原因是:" + reason);
            recordMessageEntity.setTypenum(2);
            anZhuangTiaoShiWenTiRecordMessageDao.insertSelective(recordMessageEntity);
        }
        if (anZhuangTiaoShiWenTiAuditorEntity.getIsPass() == 1) {//审核通过
            AnZhuangTiaoShiWenTiFileEntity wenTiFileEntity = new AnZhuangTiaoShiWenTiFileEntity();
            wenTiFileEntity.setId(id);
            wenTiFileEntity.setAuditorState(2);
            wenTiFileEntity.setUpdatetime(nowTime);
            wenTiFileEntity.setUpdatename(username);
            anZhuangTiaoShiWenTiFileDao.updateByPrimaryKeySelective(wenTiFileEntity);
            //新增查看消息
            AnZhuangTiaoShiWenTiRecordMessageEntity recordMessageEntity = new AnZhuangTiaoShiWenTiRecordMessageEntity();
            recordMessageEntity.setTypeid(id);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(nowTime);
            recordMessageEntity.setContext("“" + fileEntity.getFileName() + "”问题审核通过 ");
            recordMessageEntity.setTypenum(2);
            anZhuangTiaoShiWenTiRecordMessageDao.insertSelective(recordMessageEntity);
        }
        anZhuangTiaoShiWenTiAuditorEntity.setObjectId(id);
        anZhuangTiaoShiWenTiAuditorEntity.setUpdatename(username);
        anZhuangTiaoShiWenTiAuditorEntity.setUpdatetime(nowTime);
        anZhuangTiaoShiWenTiAuditorEntity.setAuditorId(receiverid);
        anZhuangTiaoShiWenTiAuditorDao.updateByPrimaryKeySelective(anZhuangTiaoShiWenTiAuditorEntity);
    }

    @Override
    public AnZhuangTiaoShiWenTiEntity findWenTiXiangQingById(Integer id) {
        return anZhuangTiaoShiWenTiDao.findWenTiXiangQingById(id);
    }

    @Override
    public AnZhuangTiaoShiWenTiEntity findWenTiById(Integer id, Integer receiverid) {
        return anZhuangTiaoShiWenTiDao.selectByPrimaryKey(id,receiverid);
    }

    @Override
    public void editAuditorByWTId(Integer id, AnZhuangTiaoShiWenTiAuditorEntity anZhuangTiaoShiWenTiAuditorEntity, String username, Integer receiverid, Integer senderId) {
        Date nowTime = new Date();
        if (anZhuangTiaoShiWenTiAuditorEntity.getIsPass() == 0) {//审核未通过
            AnZhuangTiaoShiWenTiEntity wenTiEntity = anZhuangTiaoShiWenTiDao.findOneWentById(id);
            //添加一条消息到消息表
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(username);//创建人
            messageEntity.setCreateTime(nowTime);//创建时间
            messageEntity.setContext("“" + wenTiEntity.getWentiMiaoshu() + "”问题审核未通过,请您查看！");
            messageEntity.setType((short) 3);
            //messageEntity.setMessageType((short) 2);
            messageEntity.setProjectId(id);
            messageEntity.setSenderId(receiverid);
            messageEntity.setReceiverId(senderId);
            messageEntity.setStatus((short) 1);
            messageEntity.setSmallType((short) 1);
            messageService.sendMessage(messageEntity);
            //新增查看消息
            AnZhuangTiaoShiWenTiRecordMessageEntity recordMessageEntity = new AnZhuangTiaoShiWenTiRecordMessageEntity();
            String reason = anZhuangTiaoShiWenTiAuditorEntity.getReason();
            recordMessageEntity.setTypeid(id);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(nowTime);
            recordMessageEntity.setContext("“" + wenTiEntity.getWentiMiaoshu() + "”问题审核未通过,原因是:" + reason);
            recordMessageEntity.setTypenum(1);
            anZhuangTiaoShiWenTiRecordMessageDao.insertSelective(recordMessageEntity);
        }
        if (anZhuangTiaoShiWenTiAuditorEntity.getIsPass() == 1) {//审核通过
            AnZhuangTiaoShiWenTiEntity wenTiEntity = anZhuangTiaoShiWenTiDao.findOneWentById(id);
            //新增查看消息
            AnZhuangTiaoShiWenTiRecordMessageEntity recordMessageEntity = new AnZhuangTiaoShiWenTiRecordMessageEntity();
            recordMessageEntity.setTypeid(id);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(nowTime);
            recordMessageEntity.setContext("“" + wenTiEntity.getWentiMiaoshu() + "”问题审核通过 ");
            recordMessageEntity.setTypenum(1);
            anZhuangTiaoShiWenTiRecordMessageDao.insertSelective(recordMessageEntity);
        }
        anZhuangTiaoShiWenTiAuditorEntity.setUpdatename(username);
        anZhuangTiaoShiWenTiAuditorEntity.setUpdatetime(nowTime);
        anZhuangTiaoShiWenTiAuditorEntity.setAuditorId(receiverid);
        anZhuangTiaoShiWenTiAuditorDao.updateByPrimaryKeySelective(anZhuangTiaoShiWenTiAuditorEntity);
        List<Integer> ispass = anZhuangTiaoShiWenTiAuditorDao.findIsPassByObjid(id);
        if (ispass.size() == 1 && ispass.get(0) == null) {
            AnZhuangTiaoShiWenTiEntity wenTiEntity = new AnZhuangTiaoShiWenTiEntity();
            wenTiEntity.setId(id);
            wenTiEntity.setAuditorState(1);
            anZhuangTiaoShiWenTiDao.updateByPrimaryKeySelective(wenTiEntity);
        }
        if (ispass.size() == 1 && ispass.get(0) != null && ispass.get(0) == 1) {
            AnZhuangTiaoShiWenTiEntity wenTiEntity = new AnZhuangTiaoShiWenTiEntity();
            wenTiEntity.setId(id);
            wenTiEntity.setAuditorState(2);
            anZhuangTiaoShiWenTiDao.updateByPrimaryKeySelective(wenTiEntity);
        }
        if (ispass.size() == 1 && ispass.get(0) != null && ispass.get(0) == 0) {
            AnZhuangTiaoShiWenTiEntity wenTiEntity = new AnZhuangTiaoShiWenTiEntity();
            wenTiEntity.setId(id);
            wenTiEntity.setAuditorState(1);
            anZhuangTiaoShiWenTiDao.updateByPrimaryKeySelective(wenTiEntity);
        }
        if (ispass.size() > 1) {
            AnZhuangTiaoShiWenTiEntity wenTiEntity = new AnZhuangTiaoShiWenTiEntity();
            wenTiEntity.setId(id);
            wenTiEntity.setAuditorState(3);
            anZhuangTiaoShiWenTiDao.updateByPrimaryKeySelective(wenTiEntity);
        }
    }

    @Override
    public List<AnZhuangTiaoShiRecordMessageEntity> findRecordMessageById(Integer id) {
        return anZhuangTiaoShiWenTiRecordMessageDao.findRecordMessageById(id);
    }

    @Override
    public List<AnZhuangTiaoShiWenTiEntity> findAllNotDoWellWenTi(Integer page, Integer size) {
        return anZhuangTiaoShiWenTiDao.findAllNotDoWellWenTi();
    }

    @Override
    public List<AnZhuangTiaoShiWorksFileEntity> findFileById(Integer id) {
        return anZhuangTiaoShiWenTiFileDao.findFileById(id);
    }

    @Override
    public List<DepartmentEntity> findAllDepartment(DepartmentEntityExample departmentEntityExample) {
        return departmentDao.selectByExample(departmentEntityExample);
    }

    @Override
    public void deleteFileByIds(Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            anZhuangTiaoShiWenTiFileDao.deleteByPrimaryKey(ids[i]);
        }
    }

    @Override
    public AnZhuangTiaoShiWenTiFileEntity findById(Integer id) {
        return anZhuangTiaoShiWenTiFileDao.selectByPrimaryKey(id);
    }

    @Override
    public void addFanKuiFile(AnZhuangTiaoShiWenTiFileEntity anZhuangTiaoShiWenTiFileEntity, Integer[] uids, String username, Integer senderid) {
        Date nowTime = new Date();
        anZhuangTiaoShiWenTiFileEntity.setFenlei(1);
        anZhuangTiaoShiWenTiFileEntity.setUid(senderid);
        anZhuangTiaoShiWenTiFileEntity.setCreatename(username);
        anZhuangTiaoShiWenTiFileEntity.setCreatetime(nowTime);
        anZhuangTiaoShiWenTiFileEntity.setAuditorState(1);
        anZhuangTiaoShiWenTiFileDao.insertSelective(anZhuangTiaoShiWenTiFileEntity);
        Integer fileid = anZhuangTiaoShiWenTiFileEntity.getId();
        Integer wid = anZhuangTiaoShiWenTiFileEntity.getWid();
        for (Integer uid : uids) {
            AnZhuangTiaoShiWenTiAuditorEntity wenTiAuditorEntity = new AnZhuangTiaoShiWenTiAuditorEntity();
            wenTiAuditorEntity.setObjectId(fileid);
            wenTiAuditorEntity.setAuditorId(uid);
            wenTiAuditorEntity.setObjecttype(2);
            wenTiAuditorEntity.setDoname(username);
            wenTiAuditorEntity.setDotime(nowTime);
            anZhuangTiaoShiWenTiAuditorDao.insertSelective(wenTiAuditorEntity);
            //添加一条消息到消息表
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(username);//创建人
            messageEntity.setCreateTime(nowTime);//创建时间
            messageEntity.setContext("“" + anZhuangTiaoShiWenTiFileEntity.getFileName() + "”反馈文件需要您审核,请查看！");
            messageEntity.setType((short) 3);
            //messageEntity.setMessageType((short) 2);
            messageEntity.setProjectId(wid);
            messageEntity.setFileId(fileid);
            messageEntity.setSenderId(senderid);
            messageEntity.setReceiverId(uid);
            messageEntity.setStatus((short) 1);
            messageEntity.setSmallType((short) 2);
            messageService.sendMessage(messageEntity);
        }
        AnZhuangTiaoShiWenTiRecordMessageEntity recordMessageEntity = new AnZhuangTiaoShiWenTiRecordMessageEntity();
        recordMessageEntity.setTypeid(fileid);
        recordMessageEntity.setOperatorname(username);
        recordMessageEntity.setOperatortime(nowTime);
        recordMessageEntity.setContext("新增" + anZhuangTiaoShiWenTiFileEntity.getFileName() + "反馈文件");
        recordMessageEntity.setTypenum(2);
        anZhuangTiaoShiWenTiRecordMessageDao.insertSelective(recordMessageEntity);
    }

    @Override
    public void addShuRuFile(AnZhuangTiaoShiWenTiFileEntity anZhuangTiaoShiWenTiFileEntity, Integer[] uids, String username, Integer senderid) {
        Date nowTime = new Date();
        anZhuangTiaoShiWenTiFileEntity.setFenlei(2);
        anZhuangTiaoShiWenTiFileEntity.setUid(senderid);
        anZhuangTiaoShiWenTiFileEntity.setCreatename(username);
        anZhuangTiaoShiWenTiFileEntity.setCreatetime(nowTime);
        anZhuangTiaoShiWenTiFileEntity.setAuditorState(1);
        anZhuangTiaoShiWenTiFileDao.insertSelective(anZhuangTiaoShiWenTiFileEntity);
        Integer fileid = anZhuangTiaoShiWenTiFileEntity.getId();
        Integer wid = anZhuangTiaoShiWenTiFileEntity.getWid();
        for (Integer uid : uids) {
            AnZhuangTiaoShiWenTiAuditorEntity wenTiAuditorEntity = new AnZhuangTiaoShiWenTiAuditorEntity();
            wenTiAuditorEntity.setObjectId(fileid);
            wenTiAuditorEntity.setAuditorId(uid);
            wenTiAuditorEntity.setObjecttype(3);
            wenTiAuditorEntity.setDoname(username);
            wenTiAuditorEntity.setDotime(nowTime);
            anZhuangTiaoShiWenTiAuditorDao.insertSelective(wenTiAuditorEntity);
            //添加一条消息到消息表
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(username);//创建人
            messageEntity.setCreateTime(nowTime);//创建时间
            messageEntity.setContext("“" + anZhuangTiaoShiWenTiFileEntity.getFileName() + "”输出文件需要您审核,请查看！");
            messageEntity.setType((short) 3);
            //messageEntity.setMessageType((short) 2);
            messageEntity.setProjectId(wid);
            messageEntity.setFileId(fileid);
            messageEntity.setSenderId(senderid);
            messageEntity.setReceiverId(uid);
            messageEntity.setStatus((short) 1);
            messageEntity.setSmallType((short) 3);
            messageService.sendMessage(messageEntity);
        }
        AnZhuangTiaoShiWenTiRecordMessageEntity recordMessageEntity = new AnZhuangTiaoShiWenTiRecordMessageEntity();
        recordMessageEntity.setTypeid(fileid);
        recordMessageEntity.setOperatorname(username);
        recordMessageEntity.setOperatortime(nowTime);
        recordMessageEntity.setContext("新增" + anZhuangTiaoShiWenTiFileEntity.getFileName() + "输出文件");
        recordMessageEntity.setTypenum(2);
        anZhuangTiaoShiWenTiRecordMessageDao.insertSelective(recordMessageEntity);
    }

    @Override
    public List<AnZhuangTiaoShiWenTiFileEntity> findAllShuChuFileById(Integer wid, Integer page, Integer size, String fileName) {
        return anZhuangTiaoShiWenTiFileDao.findAllShuChuFileById(wid, fileName);
    }

    @Override
    public List<AnZhuangTiaoShiWenTiFileEntity> findAllFanKuiFileById(Integer wid, Integer page, Integer size, String fileName) {
        return anZhuangTiaoShiWenTiFileDao.findAllFanKuiFileById(wid, fileName);
    }

    @Override
    public void deleteWenTiByIds(Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            anZhuangTiaoShiWenTiDao.deleteByPrimaryKey(ids[i]);
        }
    }

    @Override
    public List<AnZhuangTiaoShiWenTiEntity> findSomeWenTi(Integer page, Integer size, String xdname, Integer receiverid ,String startTime, String endTime,String wenTiType,
                                                          String fankuiMode, String shouliDanwei,Integer isNotOver) {
        return anZhuangTiaoShiWenTiDao.findSomeWenTi(xdname, receiverid,startTime,endTime,wenTiType,fankuiMode,shouliDanwei,isNotOver);
    }

    @Override
    public void editWenTiById(AnZhuangTiaoShiWenTiEntity anZhuangTiaoShiWenTiEntity, Integer id, Integer senderid, String username) {
        StringBuilder sb = new StringBuilder();
        Date nowTime = new Date();
        anZhuangTiaoShiWenTiEntity.setUpdatename(username);
        anZhuangTiaoShiWenTiEntity.setUpdatetime(nowTime);
        anZhuangTiaoShiWenTiDao.updateByPrimaryKeySelective(anZhuangTiaoShiWenTiEntity);
        AnZhuangTiaoShiWenTiEntity wenTiEntity = anZhuangTiaoShiWenTiDao.findOneWentById(id);
        Integer aa = 0;
        if (!wenTiEntity.getXdName().equals(anZhuangTiaoShiWenTiEntity.getXdName())) {
            sb.append(" 线段名改为" + anZhuangTiaoShiWenTiEntity.getXdName() + ",");
            aa++;
        }
        if (!wenTiEntity.getWentiMiaoshu().equals(anZhuangTiaoShiWenTiEntity.getWentiMiaoshu())) {
            sb.append(" 线问题描述改为" + anZhuangTiaoShiWenTiEntity.getWentiMiaoshu() + ",");
            aa++;
        }
        if (!wenTiEntity.getWentiType().equals(anZhuangTiaoShiWenTiEntity.getWentiType())) {
            sb.append(" 类别改为" + anZhuangTiaoShiWenTiEntity.getWentiType() + ",");
            aa++;
        }
        if (!wenTiEntity.getFankuiMode().equals(anZhuangTiaoShiWenTiEntity.getFankuiMode())) {
            sb.append(" 反馈方式改为" + anZhuangTiaoShiWenTiEntity.getFankuiMode() + ",");
            aa++;
        }
        if (!wenTiEntity.getShoulidanwei().equals(anZhuangTiaoShiWenTiEntity.getShoulidanwei())) {
            sb.append(" 受理单位改为" + anZhuangTiaoShiWenTiEntity.getShoulidanwei() + ",");
            aa++;
        }
        if (!wenTiEntity.getShejifanwei().equals(anZhuangTiaoShiWenTiEntity.getShejifanwei())) {
            sb.append(" 涉及范围改为" + anZhuangTiaoShiWenTiEntity.getShoulidanwei() + ",");
            aa++;
        }
        if (wenTiEntity.getAskovertime().getTime() != anZhuangTiaoShiWenTiEntity.getAskovertime().getTime()) {
            sb.append(" 要求完成时间改为" + anZhuangTiaoShiWenTiEntity.getAskovertime() + ",");
            aa++;
        }
        if (!wenTiEntity.getShishiplan().equals(anZhuangTiaoShiWenTiEntity.getShishiplan())) {
            sb.append(" 现场实施计划改为" + anZhuangTiaoShiWenTiEntity.getShishiplan() + ",");
            aa++;
        }
        if (!wenTiEntity.getCuoshifangan().equals(anZhuangTiaoShiWenTiEntity.getCuoshifangan())) {
            sb.append(" 措施方案改为" + anZhuangTiaoShiWenTiEntity.getShishiplan() + ",");
            aa++;
        }
        if (wenTiEntity.getCustomerMessage() == null) {
            sb.append(" 新增顾客确认意见为" + anZhuangTiaoShiWenTiEntity.getCustomerMessage() + ",");
            aa++;
        }
        if (wenTiEntity.getCustomerMessage() != null && !wenTiEntity.getCustomerMessage().equals(anZhuangTiaoShiWenTiEntity.getCustomerMessage())) {
            sb.append(" 顾客确认意见改为" + anZhuangTiaoShiWenTiEntity.getCustomerMessage() + ",");
            aa++;
        }
        if (wenTiEntity.getActualovertime() == null) {
            sb.append(" 新增实际完成时间为" + anZhuangTiaoShiWenTiEntity.getActualovertime() + ",");
            aa++;
        }
        if (wenTiEntity.getActualovertime() != null && wenTiEntity.getActualovertime().getTime() != anZhuangTiaoShiWenTiEntity.getActualovertime().getTime()) {
            sb.append(" 实际完成时间改为" + anZhuangTiaoShiWenTiEntity.getActualovertime() + ",");
            aa++;
        }
        if (wenTiEntity.getWentiisover() != anZhuangTiaoShiWenTiEntity.getWentiisover()) {
            if (wenTiEntity.getWentiisover() == 1) {
                sb.append(" 问题改为未关闭 ,");
                aa++;
            }
            if (wenTiEntity.getWentiisover() == 0) {
                sb.append(" 问题改为已关闭 ,");
                aa++;
            }
        }
        if (aa == 0) {
            sb.append("没有修改任何状态");
        }
        AnZhuangTiaoShiWenTiRecordMessageEntity recordMessageEntity = new AnZhuangTiaoShiWenTiRecordMessageEntity();
        recordMessageEntity.setTypeid(id);
        recordMessageEntity.setOperatorname(username);
        recordMessageEntity.setOperatortime(nowTime);
        recordMessageEntity.setContext(sb.toString());
        recordMessageEntity.setTypenum(1);
        anZhuangTiaoShiWenTiRecordMessageDao.insertSelective(recordMessageEntity);
    }

    @Override
    public void addWenTi(AnZhuangTiaoShiWenTiEntity anZhuangTiaoShiWenTiEntity, Integer[] uids, String username, Integer senderid) {
        Date nowTime = new Date();
        anZhuangTiaoShiWenTiEntity.setWentiisover(0);
        anZhuangTiaoShiWenTiEntity.setCreatename(username);
        anZhuangTiaoShiWenTiEntity.setCreatetime(nowTime);
        anZhuangTiaoShiWenTiEntity.setAuditorState(1);
        anZhuangTiaoShiWenTiEntity.setUserid(senderid);
        Date actualovertime = anZhuangTiaoShiWenTiEntity.getAskovertime();
        System.out.println("111111111"+actualovertime);
        anZhuangTiaoShiWenTiDao.insertSelective(anZhuangTiaoShiWenTiEntity);
        Integer wtid = anZhuangTiaoShiWenTiEntity.getId();
        AnZhuangTiaoShiWenTiRecordMessageEntity recordMessageEntity = new AnZhuangTiaoShiWenTiRecordMessageEntity();
        recordMessageEntity.setTypeid(wtid);
        recordMessageEntity.setOperatorname(username);
        recordMessageEntity.setOperatortime(nowTime);
        recordMessageEntity.setContext("新增一条" + anZhuangTiaoShiWenTiEntity.getWentiMiaoshu() + "的问题");
        recordMessageEntity.setTypenum(1);
        anZhuangTiaoShiWenTiRecordMessageDao.insertSelective(recordMessageEntity);
        for (Integer uid : uids) {
            AnZhuangTiaoShiWenTiAuditorEntity wenTiAuditorEntity = new AnZhuangTiaoShiWenTiAuditorEntity();
            wenTiAuditorEntity.setObjectId(wtid);
            wenTiAuditorEntity.setAuditorId(uid);
            wenTiAuditorEntity.setObjecttype(1);
            wenTiAuditorEntity.setDoname(username);
            wenTiAuditorEntity.setDotime(nowTime);
            anZhuangTiaoShiWenTiAuditorDao.insertSelective(wenTiAuditorEntity);
            //添加一条消息到消息表
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(username);//创建人
            messageEntity.setCreateTime(nowTime);//创建时间
            messageEntity.setContext("“" + anZhuangTiaoShiWenTiEntity.getWentiMiaoshu() + "”问题需要您审核,请查看！");
            messageEntity.setType((short) 3);
            //messageEntity.setMessageType((short) 2);
            messageEntity.setProjectId(wtid);
            messageEntity.setSenderId(senderid);
            messageEntity.setReceiverId(uid);
            messageEntity.setStatus((short) 1);
            messageEntity.setSmallType((short)1);
            messageService.sendMessage(messageEntity);
        }

    }
}
