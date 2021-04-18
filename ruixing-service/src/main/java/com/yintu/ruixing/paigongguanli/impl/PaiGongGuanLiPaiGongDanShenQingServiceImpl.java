package com.yintu.ruixing.paigongguanli.impl;

import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.master.common.MessageDao;
import com.yintu.ruixing.master.paigongguanli.*;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiPaiGongDanEntity;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiPaiGongDanRecordMessageEntity;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiPaiGongDanShenQingEntity;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiPaiGongDanShenQingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    @Autowired
    private PaiGongGuanLiUserDaystateDao paiGongGuanLiUserDaystateDao;


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
            messageEntity.setContext("您有派工单申请已经同意,请查看！");
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

            //更改派工人员日勤状态
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(new Date());
            Integer pgId = paiGongGuanLiPaiGongDanShenQingEntity.getPgId();
            PaiGongGuanLiPaiGongDanEntity guanLiPaiGongDanEntity=paiGongGuanLiPaiGongDanDao.selectByPrimaryKey(pgId);
            Date chuchaiendtime = guanLiPaiGongDanEntity.getChuchaiendtime();
            try {
                long oldChuChaiEndTime = chuchaiendtime.getTime();//计划出差结束时间
                long todayTime = sdf.parse(today).getTime();//今天时间
                if (todayTime<oldChuChaiEndTime){//提前结束派工
                    Long times=(oldChuChaiEndTime-todayTime)/86400000;
                    for (Integer i = 0; i <= times; i++) {
                        Date oneDate=new Date(todayTime+86400000*i);
                        Calendar calendar = Calendar.getInstance(); //得到日历
                        calendar.setTime(oneDate);
                        int week=calendar.get(Calendar.DAY_OF_WEEK)-1;
                        if (week==0 ||week==6){//周六日
                            paiGongGuanLiUserDaystateDao.updateUserDayStateRiQin(guanLiPaiGongDanEntity.getPaigongpeople(),sdf.format(oneDate),2);
                        }else {
                            paiGongGuanLiUserDaystateDao.updateUserDayStateRiQin(guanLiPaiGongDanEntity.getPaigongpeople(),sdf.format(oneDate),1);
                        }
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //更改派工状态
            PaiGongGuanLiPaiGongDanEntity paiGongDanEntity=new PaiGongGuanLiPaiGongDanEntity();
            paiGongDanEntity.setId(paiGongGuanLiPaiGongDanShenQingEntity.getPgId());
            if (paiGongGuanLiPaiGongDanShenQingEntity.getShenqingType() == 2) {//申请类型 1：申请完成 2：申请终止'
                paiGongDanEntity.setPaigongstate(5);
                paiGongDanEntity.setChuchaiendtime(new Date());
            }
            if (paiGongGuanLiPaiGongDanShenQingEntity.getShenqingType() == 1) {//申请类型 1：申请完成 2：申请终止'
                paiGongDanEntity.setPaigongstate(6);
                paiGongDanEntity.setChuchaiendtime(new Date());
            }
            paiGongGuanLiPaiGongDanDao.updateByPrimaryKeySelective(paiGongDanEntity);

            //更改派工人员日勤状态(出差结束的状态)
            Date chuchaistarttime = guanLiPaiGongDanEntity.getChuchaistarttime();
            String chuchaiKaiShiTime = sdf.format(chuchaistarttime);
            paiGongGuanLiUserDaystateDao.editUserotherStateOverChuChai(guanLiPaiGongDanEntity.getPaigongpeople(),chuchaiKaiShiTime,today);
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
