package com.yintu.ruixing.paigongguanli.impl;

import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.master.common.MessageDao;
import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiPaiGongDanDao;
import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiPaiGongDanRecordMessageDao;
import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiPaiGongDanShenQingDao;
import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiUserDao;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiPaiGongDanEntity;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiPaiGongDanRecordMessageEntity;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiPaiGongDanShenQingEntity;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiPaiGongDanShenQingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2021/4/12 14:15
 * @Version 1.0
 * 需求:
 */
@Service
@Transactional
public class PaiGongGuanLiPaiGongDanShenQingServiceImpl implements PaiGongGuanLiPaiGongDanShenQingService {
    @Autowired
    private PaiGongGuanLiPaiGongDanShenQingDao paiGongGuanLiPaiGongDanShenQingDao;
    @Autowired
    private PaiGongGuanLiPaiGongDanRecordMessageDao paiGongGuanLiPaiGongDanRecordMessageDao;
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private PaiGongGuanLiPaiGongDanDao paiGongGuanLiPaiGongDanDao;
    @Autowired
    private PaiGongGuanLiUserDao paiGongGuanLiUserDao;


    @Override
    public void editShenQingById(PaiGongGuanLiPaiGongDanShenQingEntity paiGongGuanLiPaiGongDanShenQingEntity, String username) {
        paiGongGuanLiPaiGongDanShenQingEntity.setIsnothandle(1);
        paiGongGuanLiPaiGongDanShenQingDao.updateByPrimaryKeySelective(paiGongGuanLiPaiGongDanShenQingEntity);
        //拒绝申请
        if (paiGongGuanLiPaiGongDanShenQingEntity.getShenqingState() == 0) {//申请状态0：拒绝 1：同意',
            //添加消息
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(username);//创建人
            messageEntity.setCreateTime(new Date());//创建时间
            messageEntity.setContext("您有派工单申请被拒绝,请查看！");
            messageEntity.setType((short) 5);
            messageEntity.setProjectId(paiGongGuanLiPaiGongDanShenQingEntity.getPgId());
            messageEntity.setMessageType((short) 3);
            messageEntity.setSenderId(paiGongGuanLiPaiGongDanShenQingEntity.getAcceptuserid());
            messageEntity.setReceiverId(paiGongGuanLiPaiGongDanShenQingEntity.getUserid());
            messageEntity.setStatus((short) 1);
            messageService.sendMessage(messageEntity);

            //添加记录
            PaiGongGuanLiPaiGongDanRecordMessageEntity recordMessageEntity = new PaiGongGuanLiPaiGongDanRecordMessageEntity();
            recordMessageEntity.setTypeid(paiGongGuanLiPaiGongDanShenQingEntity.getId());
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(new Date());
            if (paiGongGuanLiPaiGongDanShenQingEntity.getShenqingType() == 1) {//申请类型 1：申请完成 2：申请终止'
                recordMessageEntity.setContext(username + "拒绝了派工完成申请,原因是:"+paiGongGuanLiPaiGongDanShenQingEntity.getAcceptreason());
            }
            if (paiGongGuanLiPaiGongDanShenQingEntity.getShenqingType() == 2) {//申请类型 1：申请完成 2：申请终止'
                recordMessageEntity.setContext(username + "拒绝了派工终止申请,原因是:"+paiGongGuanLiPaiGongDanShenQingEntity.getAcceptreason());
            }
            recordMessageEntity.setTypenum(1);
            paiGongGuanLiPaiGongDanRecordMessageDao.insertSelective(recordMessageEntity);
        }
        //同意申请
        if (paiGongGuanLiPaiGongDanShenQingEntity.getShenqingState() == 1) {//申请状态0：拒绝 1：同意',
            //添加消息
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(username);//创建人
            messageEntity.setCreateTime(new Date());//创建时间
            messageEntity.setContext("您有派工单申请被拒绝,请查看！");
            messageEntity.setType((short) 5);
            messageEntity.setProjectId(paiGongGuanLiPaiGongDanShenQingEntity.getPgId());
            messageEntity.setMessageType((short) 3);
            messageEntity.setSenderId(paiGongGuanLiPaiGongDanShenQingEntity.getAcceptuserid());
            messageEntity.setReceiverId(paiGongGuanLiPaiGongDanShenQingEntity.getUserid());
            messageEntity.setStatus((short) 1);
            messageService.sendMessage(messageEntity);

            //添加记录
            PaiGongGuanLiPaiGongDanRecordMessageEntity recordMessageEntity = new PaiGongGuanLiPaiGongDanRecordMessageEntity();
            recordMessageEntity.setTypeid(paiGongGuanLiPaiGongDanShenQingEntity.getId());
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(new Date());
            if (paiGongGuanLiPaiGongDanShenQingEntity.getShenqingType() == 1) {//申请类型 1：申请完成 2：申请终止'
                recordMessageEntity.setContext(username + "同意了派工完成申请,原因是:"+paiGongGuanLiPaiGongDanShenQingEntity.getAcceptreason());
            }
            if (paiGongGuanLiPaiGongDanShenQingEntity.getShenqingType() == 2) {//申请类型 1：申请完成 2：申请终止'
                recordMessageEntity.setContext(username + "同意了派工终止申请,原因是:"+paiGongGuanLiPaiGongDanShenQingEntity.getAcceptreason());
            }
            recordMessageEntity.setTypenum(1);
            paiGongGuanLiPaiGongDanRecordMessageDao.insertSelective(recordMessageEntity);

            //更改派工状态
            PaiGongGuanLiPaiGongDanEntity paiGongDanEntity=new PaiGongGuanLiPaiGongDanEntity();
            paiGongDanEntity.setId(paiGongGuanLiPaiGongDanShenQingEntity.getPgId());
            if (paiGongGuanLiPaiGongDanShenQingEntity.getShenqingType() == 2) {//申请类型 1：申请完成 2：申请终止'
                paiGongDanEntity.setPaigongstate(5);
            }
            if (paiGongGuanLiPaiGongDanShenQingEntity.getShenqingType() == 1) {//申请类型 1：申请完成 2：申请终止'
                paiGongDanEntity.setPaigongstate(6);
            }
            paiGongGuanLiPaiGongDanDao.updateByPrimaryKeySelective(paiGongDanEntity);
        }
    }

    @Override
    public List<PaiGongGuanLiPaiGongDanShenQingEntity> findShenQing(Integer paiGongId, Integer userid) {
        List<PaiGongGuanLiPaiGongDanShenQingEntity> shenQingEntityList=paiGongGuanLiPaiGongDanShenQingDao.findShenQing(paiGongId, userid);
        for (PaiGongGuanLiPaiGongDanShenQingEntity shenQingEntity : shenQingEntityList) {
            String username=paiGongGuanLiUserDao.findUserName(shenQingEntity.getUserid());
            shenQingEntity.setUsername(username);
        }
        return shenQingEntityList;
    }

    @Override
    public void addShenQind(PaiGongGuanLiPaiGongDanShenQingEntity paiGongGuanLiPaiGongDanShenQingEntity, String username) {
        paiGongGuanLiPaiGongDanShenQingEntity.setIsnothandle(0);
        paiGongGuanLiPaiGongDanShenQingDao.insertSelective(paiGongGuanLiPaiGongDanShenQingEntity);

        //添加消息
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setCreateBy(username);//创建人
        messageEntity.setCreateTime(new Date());//创建时间
        if (paiGongGuanLiPaiGongDanShenQingEntity.getShenqingType() == 1) {//申请类型 1：申请完成 2：申请终止'
            messageEntity.setContext("有申请完成派工单消息,请查看！");
        }
        if (paiGongGuanLiPaiGongDanShenQingEntity.getShenqingType() == 2) {//申请类型 1：申请完成 2：申请终止'
            messageEntity.setContext("有申请终止派工单消息,请查看！");
        }
        messageEntity.setType((short) 5);
        messageEntity.setProjectId(paiGongGuanLiPaiGongDanShenQingEntity.getPgId());
        messageEntity.setMessageType((short) 3);
        messageEntity.setSenderId(paiGongGuanLiPaiGongDanShenQingEntity.getUserid());
        messageEntity.setReceiverId(paiGongGuanLiPaiGongDanShenQingEntity.getAcceptuserid());
        messageEntity.setStatus((short) 1);
        messageService.sendMessage(messageEntity);

        //添加记录
        PaiGongGuanLiPaiGongDanRecordMessageEntity recordMessageEntity = new PaiGongGuanLiPaiGongDanRecordMessageEntity();
        recordMessageEntity.setTypeid(paiGongGuanLiPaiGongDanShenQingEntity.getId());
        recordMessageEntity.setOperatorname(username);
        recordMessageEntity.setOperatortime(new Date());
        if (paiGongGuanLiPaiGongDanShenQingEntity.getShenqingType() == 1) {//申请类型 1：申请完成 2：申请终止'
            recordMessageEntity.setContext(username + "发起了派工完成申请");
        }
        if (paiGongGuanLiPaiGongDanShenQingEntity.getShenqingType() == 2) {//申请类型 1：申请完成 2：申请终止'
            recordMessageEntity.setContext(username + "发起了派工终止申请");
        }
        recordMessageEntity.setTypenum(1);
        paiGongGuanLiPaiGongDanRecordMessageDao.insertSelective(recordMessageEntity);

        //改变派工单的状态
        PaiGongGuanLiPaiGongDanEntity paiGongDanEntity=new PaiGongGuanLiPaiGongDanEntity();
        paiGongDanEntity.setState(4);
        paiGongDanEntity.setId(paiGongGuanLiPaiGongDanShenQingEntity.getPgId());
        paiGongGuanLiPaiGongDanDao.updateByPrimaryKeySelective(paiGongDanEntity);
    }


}
