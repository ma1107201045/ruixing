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
 * @Date 2020/7/17 11:13
 * @Version 1.0
 * 需求:现场作业的作业项配置
 */
@Service
@Transactional
public class AnZhuangTiaoShiWorkNameTotalServiceImpl implements AnZhuangTiaoShiWorkNameTotalService {

    @Autowired
    private AnZhuangTiaoShiWorkNameTotalDao anZhuangTiaoShiWorkNameTotalDao;

    @Autowired
    private AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao;

    @Autowired
    private AnZhuangTiaoShiWorksRecordMessageDao anZhuangTiaoShiWorksRecordMessageDao;

    @Autowired
    private AnZhuangTiaoShiWorksAuditorDao anZhuangTiaoShiWorksAuditorDao;

    @Autowired
    private MessageService messageService;

    @Autowired
    private AnZhuangTiaoShiWorkNameLibraryDao anZhuangTiaoShiWorkNameLibraryDao;

    @Override
    public void editWorksAuditorByWId(Integer id, AnZhuangTiaoShiWorksAuditorEntity anZhuangTiaoShiWorksAuditorEntity, String username, Integer receiverid, Integer senderId) {
        Date nowTime = new Date();
        if (anZhuangTiaoShiWorksAuditorEntity.getIsPass() == 0) {//审核未通过
            AnZhuangTiaoShiWorkNameTotalEntity workNameTotalEntity = anZhuangTiaoShiWorkNameTotalDao.findOneWorksById(id);
            //添加一条消息到消息表
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(username);//创建人
            messageEntity.setCreateTime(nowTime);//创建时间
            messageEntity.setContext("“" + workNameTotalEntity.getWorknamesall() + "”作业项版本审核未通过,请您查看！");
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
            recordMessageEntity.setContext("“" + workNameTotalEntity.getWorknamesall() + "”作业项版本审核未通过,原因是:" + reason);
            recordMessageEntity.setTypenum(1);
            anZhuangTiaoShiWorksRecordMessageDao.insertSelective(recordMessageEntity);
        }
        if (anZhuangTiaoShiWorksAuditorEntity.getIsPass() == 1) {//审核通过
            AnZhuangTiaoShiWorkNameTotalEntity workNameTotalEntity = anZhuangTiaoShiWorkNameTotalDao.findOneWorksById(id);
            //新增查看消息
            AnZhuangTiaoShiWorksRecordMessageEntity recordMessageEntity = new AnZhuangTiaoShiWorksRecordMessageEntity();
            recordMessageEntity.setTypeid(id);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(nowTime);
            recordMessageEntity.setContext("“" + workNameTotalEntity.getWorknamesall() + "”作业项版本审核通过 ");
            recordMessageEntity.setTypenum(1);
            anZhuangTiaoShiWorksRecordMessageDao.insertSelective(recordMessageEntity);
        }
        anZhuangTiaoShiWorksAuditorEntity.setUpdatename(username);
        anZhuangTiaoShiWorksAuditorEntity.setUpdatetime(nowTime);
        anZhuangTiaoShiWorksAuditorEntity.setAuditorId(receiverid);
        anZhuangTiaoShiWorksAuditorDao.updateByPrimaryKeySelective(anZhuangTiaoShiWorksAuditorEntity);
        List<Integer> ispass = anZhuangTiaoShiWorksAuditorDao.findIsPassByObjid(id);
        if (ispass.size() == 1 && ispass.get(0) == null) {
            AnZhuangTiaoShiWorkNameTotalEntity wenTiEntity = new AnZhuangTiaoShiWorkNameTotalEntity();
            wenTiEntity.setId(id);
            wenTiEntity.setAuditorState(1);
            anZhuangTiaoShiWorkNameTotalDao.updateByPrimaryKeySelective(wenTiEntity);
        }
        if (ispass.size() == 1 && ispass.get(0) != null && ispass.get(0) == 1) {
            AnZhuangTiaoShiWorkNameTotalEntity wenTiEntity = new AnZhuangTiaoShiWorkNameTotalEntity();
            wenTiEntity.setId(id);
            wenTiEntity.setAuditorState(2);
            anZhuangTiaoShiWorkNameTotalDao.updateByPrimaryKeySelective(wenTiEntity);
        }
        if (ispass.size() == 1 && ispass.get(0) != null && ispass.get(0) == 0) {
            AnZhuangTiaoShiWorkNameTotalEntity wenTiEntity = new AnZhuangTiaoShiWorkNameTotalEntity();
            wenTiEntity.setId(id);
            wenTiEntity.setAuditorState(1);
            anZhuangTiaoShiWorkNameTotalDao.updateByPrimaryKeySelective(wenTiEntity);
        }
        if (ispass.size() > 1) {
            AnZhuangTiaoShiWorkNameTotalEntity wenTiEntity = new AnZhuangTiaoShiWorkNameTotalEntity();
            wenTiEntity.setId(id);
            wenTiEntity.setAuditorState(3);
            anZhuangTiaoShiWorkNameTotalDao.updateByPrimaryKeySelective(wenTiEntity);
        }
    }

    @Override
    public List<AnZhuangTiaoShiWorksRecordMessageEntity> findWorksRecordMessageById(Integer id) {
        return anZhuangTiaoShiWorksRecordMessageDao.findWorksRecordMessageById(id);
    }

    @Override
    public void editAuditorByWId(Integer id, AnZhuangTiaoShiWorksAuditorEntity anZhuangTiaoShiWorksAuditorEntity, String username, Integer receiverid, Integer senderId) {
        Date nowTime = new Date();
        if (anZhuangTiaoShiWorksAuditorEntity.getIsPass() == 0) {//审核未通过
            AnZhuangTiaoShiWorkNameTotalEntity workNameTotalEntity = anZhuangTiaoShiWorkNameTotalDao.findOneWorksById(id);
            //添加一条消息到消息表
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(username);//创建人
            messageEntity.setCreateTime(nowTime);//创建时间
            messageEntity.setContext("“" + workNameTotalEntity.getWorknamesall() + "”作业项版本审核未通过,请您查看！");
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
            recordMessageEntity.setContext("“" + workNameTotalEntity.getWorknamesall() + "”作业项版本审核未通过,原因是:" + reason);
            recordMessageEntity.setTypenum(1);
            anZhuangTiaoShiWorksRecordMessageDao.insertSelective(recordMessageEntity);
        }
        if (anZhuangTiaoShiWorksAuditorEntity.getIsPass() == 1) {//审核通过
            AnZhuangTiaoShiWorkNameTotalEntity workNameTotalEntity = anZhuangTiaoShiWorkNameTotalDao.findOneWorksById(id);
            //新增查看消息
            AnZhuangTiaoShiWorksRecordMessageEntity recordMessageEntity = new AnZhuangTiaoShiWorksRecordMessageEntity();
            recordMessageEntity.setTypeid(id);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(nowTime);
            recordMessageEntity.setContext("“" + workNameTotalEntity.getWorknamesall() + "”作业项版本审核通过 ");
            recordMessageEntity.setTypenum(1);
            anZhuangTiaoShiWorksRecordMessageDao.insertSelective(recordMessageEntity);
        }
        anZhuangTiaoShiWorksAuditorEntity.setUpdatename(username);
        anZhuangTiaoShiWorksAuditorEntity.setUpdatetime(nowTime);
        anZhuangTiaoShiWorksAuditorEntity.setAuditorId(receiverid);
        anZhuangTiaoShiWorksAuditorDao.updateByPrimaryKeySelective(anZhuangTiaoShiWorksAuditorEntity);
        List<Integer> ispass = anZhuangTiaoShiWorksAuditorDao.findIsPassByObjid(id);
        if (ispass.size() == 1 && ispass.get(0) == null) {
            AnZhuangTiaoShiWorkNameTotalEntity wenTiEntity = new AnZhuangTiaoShiWorkNameTotalEntity();
            wenTiEntity.setId(id);
            wenTiEntity.setAuditorState(1);
            anZhuangTiaoShiWorkNameTotalDao.updateByPrimaryKeySelective(wenTiEntity);
        }
        if (ispass.size() == 1 && ispass.get(0) != null && ispass.get(0) == 1) {
            AnZhuangTiaoShiWorkNameTotalEntity wenTiEntity = new AnZhuangTiaoShiWorkNameTotalEntity();
            wenTiEntity.setId(id);
            wenTiEntity.setAuditorState(2);
            anZhuangTiaoShiWorkNameTotalDao.updateByPrimaryKeySelective(wenTiEntity);
        }
        if (ispass.size() == 1 && ispass.get(0) != null && ispass.get(0) == 0) {
            AnZhuangTiaoShiWorkNameTotalEntity wenTiEntity = new AnZhuangTiaoShiWorkNameTotalEntity();
            wenTiEntity.setId(id);
            wenTiEntity.setAuditorState(1);
            anZhuangTiaoShiWorkNameTotalDao.updateByPrimaryKeySelective(wenTiEntity);
        }
        if (ispass.size() > 1) {
            AnZhuangTiaoShiWorkNameTotalEntity wenTiEntity = new AnZhuangTiaoShiWorkNameTotalEntity();
            wenTiEntity.setId(id);
            wenTiEntity.setAuditorState(3);
            anZhuangTiaoShiWorkNameTotalDao.updateByPrimaryKeySelective(wenTiEntity);
        }
    }


    @Override
    public List<AnZhuangTiaoShiWorksRecordMessageEntity> findRecordMessageById(Integer id) {
        return anZhuangTiaoShiWorksRecordMessageDao.findRecordMessageById(id);
    }

    @Override
    public AnZhuangTiaoShiWorkNameTotalEntity findOneWorksById(Integer id) {
        return anZhuangTiaoShiWorkNameTotalDao.findOneWorksById(id);
    }

    @Override
    public List<AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity> findWorkNameByWorkname(String workname, Integer page, Integer size) {
        return anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao.findWorkNameByWorkname(workname);
    }

    @Override
    public void deleteWorkNameByIds(Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao.deleteByPrimaryKey(ids[i]);
        }
    }

    @Override
    public void editWorkNameById(Integer id, AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity, String username, Integer receiverid, Integer[] uids) {
        StringBuilder sb = new StringBuilder();
        Date nowTime = new Date();
        Integer wnlid = anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity.getWnlid();
        Integer wntid = anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity.getWntid();
        anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity.setUpdatename(username);
        anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity.setUpdatetime(nowTime);
        anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao.updateByPrimaryKeySelective(anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity);
        AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity workNameTotalEntity=anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao.selectByPrimaryKey(id);
        Integer aa = 0;
        if (anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity.getWnlid()!=workNameTotalEntity.getWnlid()) {
            String workName=anZhuangTiaoShiWorkNameLibraryDao.findWorkNameById(anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity.getWnlid());
            sb.append(" 作业项改为" + workName+ ",");
            aa++;
        }
        if (anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity.getWorkState()!=workNameTotalEntity.getWorkState()&&anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity.getWorkState()==1) {
            sb.append(" 作业项标识改为 手动");
            aa++;
        }
        if (anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity.getWorkState()!=workNameTotalEntity.getWorkState()&&anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity.getWorkState()==2) {
            sb.append(" 作业项标识改为 自动");
            aa++;
        }
        if (aa == 0) {
            sb.append("没有修改任何状态");
        }
        AnZhuangTiaoShiWorksRecordMessageEntity recordMessageEntity = new AnZhuangTiaoShiWorksRecordMessageEntity();
        recordMessageEntity.setTypeid(id);
        recordMessageEntity.setOperatorname(username);
        recordMessageEntity.setOperatortime(nowTime);
        recordMessageEntity.setContext(sb.toString());
        recordMessageEntity.setTypenum(1);
        anZhuangTiaoShiWorksRecordMessageDao.insertSelective(recordMessageEntity);
        String Worknamesall = anZhuangTiaoShiWorkNameTotalDao.findWorkNameTotalById(wntid);
        String Workname = anZhuangTiaoShiWorkNameLibraryDao.findWorkNameById(wnlid);
        for (Integer uid : uids) {
            AnZhuangTiaoShiWorksAuditorEntity worksAuditorEntity = new AnZhuangTiaoShiWorksAuditorEntity();
            worksAuditorEntity.setObjectId(id);
            worksAuditorEntity.setAuditorId(uid);
            worksAuditorEntity.setObjecttype(1);
            worksAuditorEntity.setDoname(username);
            worksAuditorEntity.setDotime(nowTime);
            anZhuangTiaoShiWorksAuditorDao.insertSelective(worksAuditorEntity);
            //添加一条消息到消息表
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(username);//创建人
            messageEntity.setCreateTime(nowTime);//创建时间
            messageEntity.setContext("“" + Worknamesall + "”版本下的" +   "作业项变更为“"+Workname+"”需要您审核,请查看！");
            messageEntity.setType((short) 3);
            //messageEntity.setMessageType((short) 2);
            messageEntity.setProjectId(wntid);
            messageEntity.setFileId(wnlid);
            messageEntity.setSenderId(receiverid);
            messageEntity.setReceiverId(uid);
            messageEntity.setStatus((short) 1);
            messageEntity.setSmallType((short) 5);
            messageService.sendMessage(messageEntity);
        }

    }

    @Override
    public List<AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity> findWorkNameById(Integer id, Integer page, Integer size) {
        return anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao.findWorkNameById(id);
    }

    @Override
    public void addWorkNameEdition(AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity, Integer[] wnlids, String username, Integer receiverid, Integer[] uids) {
        for (int i = 0; i < wnlids.length; i++) {
            Date nowTime = new Date();
            anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity.setCreatetime(nowTime);
            anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity.setCreatename(username);
            anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity.setWnlid(wnlids[i]);
            anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity.setAuditorState(1);
            Integer wntid = anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity.getWntid();
            anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao.insertSelective(anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity);
            String Worknamesall = anZhuangTiaoShiWorkNameTotalDao.findWorkNameTotalById(wntid);
            String Workname = anZhuangTiaoShiWorkNameLibraryDao.findWorkNameById(wnlids[i]);
            Integer id = anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalEntity.getId();
            for (Integer uid : uids) {
                AnZhuangTiaoShiWorksAuditorEntity worksAuditorEntity = new AnZhuangTiaoShiWorksAuditorEntity();
                worksAuditorEntity.setObjectId(id);
                worksAuditorEntity.setAuditorId(uid);
                worksAuditorEntity.setObjecttype(1);
                worksAuditorEntity.setDoname(username);
                worksAuditorEntity.setDotime(nowTime);
                anZhuangTiaoShiWorksAuditorDao.insertSelective(worksAuditorEntity);
                //添加一条消息到消息表
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setCreateBy(username);//创建人
                messageEntity.setCreateTime(nowTime);//创建时间
                messageEntity.setContext("“" + Worknamesall + "版本下的" + Workname + "”作业项需要您审核,请查看！");
                messageEntity.setType((short) 3);
                //messageEntity.setMessageType((short) 2);
                messageEntity.setProjectId(wntid);
                messageEntity.setFileId(wnlids[i]);
                messageEntity.setSenderId(receiverid);
                messageEntity.setReceiverId(uid);
                messageEntity.setStatus((short) 1);
                messageEntity.setSmallType((short) 5);
                messageService.sendMessage(messageEntity);
            }
            AnZhuangTiaoShiWorksRecordMessageEntity recordMessageEntity = new AnZhuangTiaoShiWorksRecordMessageEntity();
            recordMessageEntity.setTypeid(wnlids[i]);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(nowTime);
            recordMessageEntity.setContext("在" + Worknamesall + "版本下新增" + Workname + "作业项");
            recordMessageEntity.setTypenum(1);
            anZhuangTiaoShiWorksRecordMessageDao.insertSelective(recordMessageEntity);

        }
    }

    @Override
    public void deleteWorkNameTotalByIds(Integer[] ids) {
        anZhuangTiaoShiWorkNameTotalDao.deleteWorkNameTotalByIds(ids);
    }

    @Override
    public void editWorkNameTotalById(Integer id, AnZhuangTiaoShiWorkNameTotalEntity anZhuangTiaoShiWorkNameTotalEntity, String username, Integer receiverid) {
        StringBuilder sb = new StringBuilder();
        Date nowTime = new Date();
        anZhuangTiaoShiWorkNameTotalEntity.setUpdatetime(nowTime);
        anZhuangTiaoShiWorkNameTotalEntity.setUpdatename(username);
        anZhuangTiaoShiWorkNameTotalDao.updateByPrimaryKeySelective(anZhuangTiaoShiWorkNameTotalEntity);
        AnZhuangTiaoShiWorkNameTotalEntity workNameTotalEntity = anZhuangTiaoShiWorkNameTotalDao.selectByPrimaryKey(id);
        Integer aa = 0;
        if (!workNameTotalEntity.getWorknamesall().equals(anZhuangTiaoShiWorkNameTotalEntity.getWorknamesall())) {
            sb.append(" 作业项配置版本" + anZhuangTiaoShiWorkNameTotalEntity.getWorknamesall() + ",");
            aa++;
        }
        if (workNameTotalEntity.getStarttime() != null && workNameTotalEntity.getStarttime().getTime() != anZhuangTiaoShiWorkNameTotalEntity.getStarttime().getTime()) {
            sb.append(" 实施开始时间改为" + anZhuangTiaoShiWorkNameTotalEntity.getStarttime() + ",");
            aa++;
        }
        if (workNameTotalEntity.getEndtime() != null && workNameTotalEntity.getEndtime().getTime() != anZhuangTiaoShiWorkNameTotalEntity.getEndtime().getTime()) {
            sb.append(" 实施结束时间改为" + anZhuangTiaoShiWorkNameTotalEntity.getEndtime() + ",");
            aa++;
        }
        if (aa == 0) {
            sb.append("没有修改任何状态");
        }
        AnZhuangTiaoShiWorksRecordMessageEntity recordMessageEntity = new AnZhuangTiaoShiWorksRecordMessageEntity();
        recordMessageEntity.setTypeid(id);
        recordMessageEntity.setOperatorname(username);
        recordMessageEntity.setOperatortime(nowTime);
        recordMessageEntity.setContext(sb.toString());
        recordMessageEntity.setTypenum(1);
        anZhuangTiaoShiWorksRecordMessageDao.insertSelective(recordMessageEntity);

    }

    @Override
    public List<AnZhuangTiaoShiWorkNameTotalEntity> findWorkNameTotal(Integer page, Integer size, String workname) {
        List<AnZhuangTiaoShiWorkNameTotalEntity> workNameTotalEntities = anZhuangTiaoShiWorkNameTotalDao.findWorkNameTotal(workname);
        for (AnZhuangTiaoShiWorkNameTotalEntity workNameTotalEntity : workNameTotalEntities) {
            Integer id = workNameTotalEntity.getId();
            //查找此作业项配置版本有多少个作业项
            Integer worknametotal = anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao.findWorkNameTatol(id);
            workNameTotalEntity.setWorknametotal(worknametotal);
        }
        return workNameTotalEntities;
    }

    @Override
    public void addWorkNameTotal(AnZhuangTiaoShiWorkNameTotalEntity anZhuangTiaoShiWorkNameTotalEntity, Integer[] uids, String username, Integer receiverid) {
        Date nowTime = new Date();
        anZhuangTiaoShiWorkNameTotalEntity.setCreatename(username);
        anZhuangTiaoShiWorkNameTotalEntity.setCreatetime(nowTime);
        anZhuangTiaoShiWorkNameTotalEntity.setAuditorState(1);
        anZhuangTiaoShiWorkNameTotalDao.insertSelective(anZhuangTiaoShiWorkNameTotalEntity);
        Integer wid = anZhuangTiaoShiWorkNameTotalEntity.getId();
        for (Integer uid : uids) {
            AnZhuangTiaoShiWorksAuditorEntity worksAuditorEntity = new AnZhuangTiaoShiWorksAuditorEntity();
            worksAuditorEntity.setObjectId(wid);
            worksAuditorEntity.setAuditorId(uid);
            worksAuditorEntity.setObjecttype(1);
            worksAuditorEntity.setDoname(username);
            worksAuditorEntity.setDotime(nowTime);
            anZhuangTiaoShiWorksAuditorDao.insertSelective(worksAuditorEntity);
            //添加一条消息到消息表
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(username);//创建人
            messageEntity.setCreateTime(nowTime);//创建时间
            messageEntity.setContext("“" + anZhuangTiaoShiWorkNameTotalEntity.getWorknamesall() + "”作业项版本需要您审核,请查看！");
            messageEntity.setType((short) 3);
            //messageEntity.setMessageType((short) 2);
            messageEntity.setProjectId(wid);
            //messageEntity.setFileId(fileid);
            messageEntity.setSenderId(receiverid);
            messageEntity.setReceiverId(uid);
            messageEntity.setStatus((short) 1);
            messageEntity.setSmallType((short) 5);
            messageService.sendMessage(messageEntity);
        }
        AnZhuangTiaoShiWorksRecordMessageEntity recordMessageEntity = new AnZhuangTiaoShiWorksRecordMessageEntity();
        recordMessageEntity.setTypeid(wid);
        recordMessageEntity.setOperatorname(username);
        recordMessageEntity.setOperatortime(nowTime);
        recordMessageEntity.setContext("新增" + anZhuangTiaoShiWorkNameTotalEntity.getWorknamesall() + "作业项版本");
        recordMessageEntity.setTypenum(1);
        anZhuangTiaoShiWorksRecordMessageDao.insertSelective(recordMessageEntity);
    }
}
