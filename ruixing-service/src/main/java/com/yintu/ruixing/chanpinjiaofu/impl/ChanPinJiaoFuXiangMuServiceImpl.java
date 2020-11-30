package com.yintu.ruixing.chanpinjiaofu.impl;

import com.yintu.ruixing.chanpinjiaofu.*;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.master.chanpinjiaofu.ChanPinJiaoFuRecordMessageDao;
import com.yintu.ruixing.master.chanpinjiaofu.ChanPinJiaoFuXiangMuDao;
import com.yintu.ruixing.master.chanpinjiaofu.ChanPinJiaoFuXiangMuFileDao;
import com.yintu.ruixing.master.common.MessageDao;
import com.yintu.ruixing.master.xitongguanli.UserDao;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.xitongguanli.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Mr.liu
 * @Date 2020/7/1 13:44
 * @Version 1.0
 * 需求:产品交付
 */
@Service
@Transactional
public class ChanPinJiaoFuXiangMuServiceImpl implements ChanPinJiaoFuXiangMuService {
    @Autowired
    private ChanPinJiaoFuXiangMuDao chanPinJiaoFuXiangMuDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private ChanPinJiaoFuRecordMessageDao chanPinJiaoFuRecordMessageDao;

    @Autowired
    private ChanPinJiaoFuXiangMuFileDao chanPinJiaoFuXiangMuFileDao;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Override
    public List<ChanPinJiaoFuXiangMuEntity> findJiaoFuQingKuangList(String choiceTing, Integer page, Integer size) {
        return chanPinJiaoFuXiangMuDao.findJiaoFuQingKuangList(choiceTing);
    }


    @Override
    public void editXiaoXiById(MessageEntity messageEntity) {
        messageEntity.setStatus((short) 2);
        messageDao.updateByPrimaryKeySelective(messageEntity);
    }

    @Override
    public List<MessageEntity> findXiaoXi(Integer senderid) {
        Integer type = 2;
        return messageDao.findXiaoXi(senderid, type);
    }

    @Override
    public List<ChanPinJiaoFuXiangMuEntity> findAllXiangMu() {
        return chanPinJiaoFuXiangMuDao.findAllXiangMu();
    }

    @Override
    public void addXiaoXi(MessageEntity messageEntity) {
        /*messageEntity.setContext("项目待发货，请及时联系顾客确认供货计划！");
        messageEntity.setType((short)2);
        messageEntity.setStatus((short)1);
        messageEntity.setCreatedDate(new Date());*/
        messageDao.insertSelective(messageEntity);
    }

    @Override
    public List<UserEntity> findAllAuditorNameById(Integer id) {
        Integer[] ids = chanPinJiaoFuXiangMuDao.findAllAuditorNameById(id);
        List<UserEntity> userEntity = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            UserEntity userEntities = userDao.selectByPrimaryKey((long) ids[i]);
            System.out.println(userEntities);
            userEntity.add(userEntities);
        }
        return userEntity;
    }

    @Override
    public List<ChanPinJiaoFuXiangMuEntity> findJiaoFuQingKuangLists(String choiceTing, Integer page, Integer size) {
        return chanPinJiaoFuXiangMuDao.findJiaoFuQingKuangLists(choiceTing);
    }

    @Override
    public Map<String, Object> findJiaoFuQingKuangNumberAll() {
        Map<String, Object> map = new HashMap<>();
        List<String> zhengZaiZhiXing = chanPinJiaoFuXiangMuDao.findZhengZaiZhiXing();
        zhengZaiZhiXing.add("zhengZaiZhiXing");
        List<String> meiWanChengFaHuo = chanPinJiaoFuXiangMuDao.findFaHuo();
        meiWanChengFaHuo.add("meiWanChengFaHuo");
        List<String> meiWanChengQianShou = chanPinJiaoFuXiangMuDao.findQianShou();
        meiWanChengQianShou.add("meiWanChengQianShou");
        List<String> meiWanChengYanGong = chanPinJiaoFuXiangMuDao.meiWanChengYanGong();
        meiWanChengYanGong.add("meiWanChengYanGong");
        List<String> zanBuFaHuo = chanPinJiaoFuXiangMuDao.zanBuFaHuo();
        zanBuFaHuo.add("zanBuFaHuo");
        List<String> luXuFaHuo = chanPinJiaoFuXiangMuDao.luXuFaHuo();
        luXuFaHuo.add("luXuFaHuo");
        List<String> wuXuFaHuo = chanPinJiaoFuXiangMuDao.wuXuFaHuo();
        wuXuFaHuo.add("wuXuFaHuo");
        List<String> daiQianShu = chanPinJiaoFuXiangMuDao.daiQianShu();
        daiQianShu.add("daiQianShu");
        List<String> daiYanGong = chanPinJiaoFuXiangMuDao.daiYanGong();
        daiYanGong.add("daiYanGong");
        List<String> overQianShouMoney = chanPinJiaoFuXiangMuDao.overQianShouMoney();
        overQianShouMoney.add("overQianShouMoney");
        List<String> overYanGongMoney = chanPinJiaoFuXiangMuDao.overYanGongMoney();
        overYanGongMoney.add("overYanGongMoney");

        map.put("zhengZaiZhiXing", zhengZaiZhiXing);
        map.put("meiWanChengQianShou", meiWanChengQianShou);
        map.put("meiWanChengFaHuo", meiWanChengFaHuo);
        map.put("meiWanChengYanGong", meiWanChengYanGong);
        map.put("zanBuFaHuo", zanBuFaHuo);
        map.put("luXuFaHuo", luXuFaHuo);
        map.put("wuXuFaHuo", wuXuFaHuo);
        map.put("daiQianShu", daiQianShu);
        map.put("daiYanGong", daiYanGong);
        map.put("overQianShouMoney", overQianShouMoney);
        map.put("overYanGongMoney", overYanGongMoney);
        return map;
    }


    /////////////////////////项目交付状态小模块/////////////////////////////////////
    @Override
    public List<ChanPinJiaoFuXiangMuEntity> findXiangMuByIds(Integer stateid, Integer page, Integer size) {
        return chanPinJiaoFuXiangMuDao.findXiangMuByIds(stateid);
    }

    @Override
    public List<ChanPinJiaoFuXiangMuEntity> findXiangMuData(String xiangMuBianHao, String xiangMuName, Integer page, Integer size) {
        return chanPinJiaoFuXiangMuDao.findXiangMuData(xiangMuBianHao, xiangMuName);
    }

    @Override
    public void deletXiangMuFileByIds(Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            chanPinJiaoFuXiangMuFileDao.deleteByPrimaryKey(ids[i]);
        }
    }

    @Override
    public void deletXiangMuFileById(Integer id) {
        chanPinJiaoFuXiangMuFileDao.deleteByPrimaryKey(id);
    }

    @Override
    public ChanPinJiaoFuXiangMuFileEntity findById(Integer id) {
        return chanPinJiaoFuXiangMuFileDao.selectByPrimaryKey(id);
    }

    @Override
    public void editXiangMuFileById(ChanPinJiaoFuXiangMuFileEntity chanPinJiaoFuXiangMuFileEntity, Integer id, Integer[] uids, Integer uId, String username) {
        ChanPinJiaoFuXiangMuFileEntity fileEntity = chanPinJiaoFuXiangMuFileDao.selectByPrimaryKey(id);
        Integer xmId = chanPinJiaoFuXiangMuFileEntity.getXmId();//新增文件的项目id
        StringBuilder sb = new StringBuilder();
        Date nowTime = new Date();
        //删除中间表的审查人id
        chanPinJiaoFuXiangMuDao.deletAuditor(id);
        //添加审查人
        if (uids != null) {
            for (Integer uid : uids) {
                ChanPinJiaoFuFileAuditorEntity chanPinJiaoFuFileAuditorEntity = new ChanPinJiaoFuFileAuditorEntity();
                chanPinJiaoFuFileAuditorEntity.setChanPinJiaoFuFileId(id);
                chanPinJiaoFuFileAuditorEntity.setAuditorId(uid);
                chanPinJiaoFuFileAuditorEntity.setObjectType(2);
                chanPinJiaoFuFileAuditorEntity.setDoName(username);
                chanPinJiaoFuFileAuditorEntity.setDoTime(new Date());
                chanPinJiaoFuXiangMuDao.addAuditorName(chanPinJiaoFuFileAuditorEntity);
                //添加一条消息到消息表
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setCreateBy(username);//创建人
                messageEntity.setCreateTime(nowTime);//创建时间
                messageEntity.setContext("“" + chanPinJiaoFuXiangMuFileEntity.getFileName() + "”文件需要您审核,请查看！");
                messageEntity.setType((short) 2);
                messageEntity.setProjectId(xmId);
                messageEntity.setMessageType((short) 2);
                messageEntity.setFileId(id);
                messageEntity.setSenderId(uId);
                messageEntity.setReceiverId(uid);
                messageEntity.setStatus((short) 1);
                messageService.sendMessage(messageEntity);
            }
        }
        chanPinJiaoFuXiangMuFileEntity.setUid(uId);
        chanPinJiaoFuXiangMuFileEntity.setUpdatetime(nowTime);
        chanPinJiaoFuXiangMuFileEntity.setUpdatename(username);
        if (chanPinJiaoFuXiangMuFileEntity.getFileType() == 2 && chanPinJiaoFuXiangMuFileEntity.getFabuType() == 2) {
            chanPinJiaoFuXiangMuFileEntity.setAuditorState(1);
        }
        chanPinJiaoFuXiangMuFileDao.updateByPrimaryKeySelective(chanPinJiaoFuXiangMuFileEntity);
        Integer aa = 0;
        if (!fileEntity.getFileName().equals(chanPinJiaoFuXiangMuFileEntity.getFileName())) {
            sb.append("文件名改为" + chanPinJiaoFuXiangMuFileEntity.getFileName());
            aa++;
        }
        if (fileEntity.getAuditorState() == null || fileEntity.getAuditorState() != chanPinJiaoFuXiangMuFileEntity.getAuditorState()) {
            if (chanPinJiaoFuXiangMuFileEntity.getAuditorState() == null) {
                sb.append(" ");
            } else {
                if (chanPinJiaoFuXiangMuFileEntity.getAuditorState() == 1) {
                    sb.append("文件审核状态更改为  待审核,  ");
                    aa++;
                }
                if (chanPinJiaoFuXiangMuFileEntity.getAuditorState() == 2) {
                    sb.append("文件审核状态更改为  已审核未通过, ");
                    aa++;
                }
                if (chanPinJiaoFuXiangMuFileEntity.getAuditorState() == 3) {
                    sb.append("文件审核状态更改为  已审核已通过,");
                    aa++;
                }
            }
        }
        if (fileEntity.getFileType() != chanPinJiaoFuXiangMuFileEntity.getFileType()) {
            if (chanPinJiaoFuXiangMuFileEntity.getFileType() == 1) {
                sb.append("文件类型更改为  输入文件, ");
                aa++;
            }
            if (chanPinJiaoFuXiangMuFileEntity.getFileType() == 2) {
                sb.append("文件类型更改为  输出文件, ");
                aa++;
            }
        }
        if (fileEntity.getFabuType() != chanPinJiaoFuXiangMuFileEntity.getFabuType()) {
            if (chanPinJiaoFuXiangMuFileEntity.getFabuType() == 1) {
                sb.append("发布状态更改为  录入, ");
                aa++;
            }
            if (chanPinJiaoFuXiangMuFileEntity.getFabuType() == 2) {
                sb.append("发布状态更改为  发布, ");
                aa++;
            }
        }
        if (aa == 0) {
            sb.append("没有修改任何状态");
        }
        Integer typenum = 2;
        Integer mid = id;
        String context = sb.toString();
        chanPinJiaoFuRecordMessageDao.addRecordMessage(mid, typenum, nowTime, username, context);
    }

    @Override
    public void addXiangMuFile(ChanPinJiaoFuXiangMuFileEntity chanPinJiaoFuXiangMuFileEntity, Integer[] uids, String username, Integer uId) {
        Date nowTime = new Date();
        chanPinJiaoFuXiangMuFileEntity.setCreatetime(nowTime);
        chanPinJiaoFuXiangMuFileEntity.setCreatename(username);
        chanPinJiaoFuXiangMuFileEntity.setUid(uId);
        if (chanPinJiaoFuXiangMuFileEntity.getFabuType() == 2 && chanPinJiaoFuXiangMuFileEntity.getFileType() == 2) {
            chanPinJiaoFuXiangMuFileEntity.setAuditorState(1);
        } else {
            chanPinJiaoFuXiangMuFileEntity.setAuditorState(null);
        }
        if (chanPinJiaoFuXiangMuFileEntity.getRemarks().equals("")) {
            chanPinJiaoFuXiangMuFileEntity.setRemarks("无");
        }
        String fileName = chanPinJiaoFuXiangMuFileEntity.getFileName();
        chanPinJiaoFuXiangMuFileDao.insertSelective(chanPinJiaoFuXiangMuFileEntity);
        Integer fid = chanPinJiaoFuXiangMuFileEntity.getId();//新增文件id
        Integer typenum = 2;
        String context = "新增“" + fileName + "”文件";
        chanPinJiaoFuRecordMessageDao.addRecordMessage(fid, typenum, nowTime, username, context);
        Integer xmId = chanPinJiaoFuXiangMuFileEntity.getXmId();//新增文件的项目id
        for (Integer uid : uids) {
            ChanPinJiaoFuFileAuditorEntity chanPinJiaoFuFileAuditorEntity = new ChanPinJiaoFuFileAuditorEntity();
            chanPinJiaoFuFileAuditorEntity.setChanPinJiaoFuFileId(fid);
            chanPinJiaoFuFileAuditorEntity.setAuditorId(uid);
            chanPinJiaoFuFileAuditorEntity.setObjectType(2);
            chanPinJiaoFuXiangMuDao.addAuditorName(chanPinJiaoFuFileAuditorEntity);
            //添加一条消息到消息表
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(username);//创建人
            messageEntity.setCreateTime(nowTime);//创建时间
            messageEntity.setContext("“" + fileName + "”文件需要您审核,请查看！");
            messageEntity.setType((short) 2);
            messageEntity.setMessageType((short) 2);
            messageEntity.setProjectId(xmId);
            messageEntity.setFileId(fid);
            messageEntity.setSenderId(uId);
            messageEntity.setReceiverId(uid);
            messageEntity.setStatus((short) 1);
            messageService.sendMessage(messageEntity);
        }
    }

    @Override
    public List<ChanPinJiaoFuXiangMuEntity> findXiangMuById(Integer id) {
        return chanPinJiaoFuXiangMuDao.findXiangMuById(id);
    }

    @Override
    public List<ChanPinJiaoFuXiangMuEntity> findAll(Integer page, Integer size) {
        return chanPinJiaoFuXiangMuDao.findAll();
    }

    @Override
    public void deletXiagMuById(Integer id) {
        chanPinJiaoFuXiangMuDao.deletXiagMuById(id);
    }

    @Override
    public List<ChanPinJiaoFuXiangMuFileEntity> findFile(Integer id) {
        return chanPinJiaoFuXiangMuFileDao.findFile(id);
    }

    @Override
    public List<ChanPinJiaoFuRecordMessageEntity> findFileReordById(Integer id) {
        return chanPinJiaoFuRecordMessageDao.findFileReordById(id);
    }

    @Override
    public List<ChanPinJiaoFuRecordMessageEntity> findReordById(Integer id) {
        return chanPinJiaoFuRecordMessageDao.findReordById(id);
    }

    @Override
    public List<ChanPinJiaoFuXiangMuFileEntity> findShuChuFilee(Integer xmid, Integer page, Integer size, Integer uid) {
        return chanPinJiaoFuXiangMuFileDao.findShuChuFilee(xmid, uid);//需要审核的
    }

    @Override
    public List<ChanPinJiaoFuXiangMuFileEntity> findShuChuFile(Integer xmid, Integer page, Integer size, Integer uid) {
        return chanPinJiaoFuXiangMuFileDao.findShuChuFile(xmid, uid);//不用审核的
    }

    @Override
    public List<ChanPinJiaoFuXiangMuFileEntity> findShuRuFilee(Integer xmid, Integer page, Integer size, Integer uid) {
        return chanPinJiaoFuXiangMuFileDao.findShuRuFilee(xmid, uid);//需要审核的
    }

    @Override
    public List<ChanPinJiaoFuXiangMuFileEntity> findShuRuFile(Integer xmid, Integer page, Integer size, Integer uid) {
        return chanPinJiaoFuXiangMuFileDao.findShuRuFile(xmid, uid);//不用审核的
    }

    @Override
    public List<ChanPinJiaoFuXiangMuFileEntity> findFileBySomethingg(Integer xmid, Integer page, Integer size, Integer filetype, String filename, Integer uid) {
        return chanPinJiaoFuXiangMuFileDao.findFileBySomethingg(xmid, filetype, filename, uid);//需要审核的
    }

    @Override
    public List<ChanPinJiaoFuXiangMuFileEntity> findFileBySomething(Integer xmid, Integer page, Integer size, Integer filetype, String filename, Integer uid) {
        return chanPinJiaoFuXiangMuFileDao.findFileBySomething(xmid, filetype, filename, uid);//不用审核的
    }

    @Override
    public List<ChanPinJiaoFuFileAuditorEntity> findXMByXmid(Integer xmid) {
        return chanPinJiaoFuXiangMuDao.findXMByXmid(xmid);
    }

    @Override
    public long findProjectSum() {
        return chanPinJiaoFuXiangMuDao.selectProjectSum();
    }

    @Override
    public void editAuditorByWJId(ChanPinJiaoFuFileAuditorEntity chanPinJiaoFuFileAuditorEntity, Integer id, String username, Integer receiverid, Integer senderId) {
        Date nowTime = new Date();
        if (chanPinJiaoFuFileAuditorEntity.getIsPass() == 0) {//审核未通过
            ChanPinJiaoFuXiangMuFileEntity onefileEntity = chanPinJiaoFuXiangMuFileDao.selectByPrimaryKey(id);
            ChanPinJiaoFuXiangMuFileEntity fileEntity = new ChanPinJiaoFuXiangMuFileEntity();
            fileEntity.setId(id);
            fileEntity.setAuditorState(3);
            chanPinJiaoFuXiangMuFileDao.updateByPrimaryKeySelective(fileEntity);
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
            ChanPinJiaoFuRecordMessageEntity recordMessageEntity = new ChanPinJiaoFuRecordMessageEntity();
            String reason = chanPinJiaoFuFileAuditorEntity.getReason();
            recordMessageEntity.setTypeid(id);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(nowTime);
            recordMessageEntity.setTypenum(2);
            recordMessageEntity.setContext("“" + onefileEntity.getFileName() + "”文件审核未通过,原因是:" + reason);
            chanPinJiaoFuRecordMessageDao.insertSelective(recordMessageEntity);
        }
        if (chanPinJiaoFuFileAuditorEntity.getIsPass() == 1) {//审核通过
            ChanPinJiaoFuXiangMuFileEntity fileEntity = new ChanPinJiaoFuXiangMuFileEntity();
            fileEntity.setId(id);
            fileEntity.setAuditorState(2);
            chanPinJiaoFuXiangMuFileDao.updateByPrimaryKeySelective(fileEntity);
            //新增查看消息
            ChanPinJiaoFuXiangMuFileEntity onefileEntity = chanPinJiaoFuXiangMuFileDao.selectByPrimaryKey(id);
            ChanPinJiaoFuRecordMessageEntity recordMessageEntity = new ChanPinJiaoFuRecordMessageEntity();
            recordMessageEntity.setTypeid(id);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(nowTime);
            recordMessageEntity.setTypenum(2);
            recordMessageEntity.setContext("“" + onefileEntity.getFileName() + "”文件审核已通过！");
            chanPinJiaoFuRecordMessageDao.insertSelective(recordMessageEntity);
        }
        chanPinJiaoFuFileAuditorEntity.setDoTime(nowTime);
        chanPinJiaoFuFileAuditorEntity.setDoName(username);
        chanPinJiaoFuXiangMuDao.editAuditorByXMId(chanPinJiaoFuFileAuditorEntity);
    }

    @Override
    public void editAuditorByXMId(ChanPinJiaoFuFileAuditorEntity chanPinJiaoFuFileAuditorEntity, Integer id, String username, Integer receiverid, Integer senderId) {
        Date nowTime = new Date();
        if (chanPinJiaoFuFileAuditorEntity.getIsPass() == 0) {//审核未通过
            ChanPinJiaoFuXiangMuEntity xiangMuEntity = chanPinJiaoFuXiangMuDao.selectByPrimaryKey(id);
            ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity = new ChanPinJiaoFuXiangMuEntity();
            chanPinJiaoFuXiangMuEntity.setId(id);
            chanPinJiaoFuXiangMuEntity.setAuditorstate(3);
            chanPinJiaoFuXiangMuDao.editXiangMuById(chanPinJiaoFuXiangMuEntity);
            //新增消息  提醒发送者查看审核未通过的项目
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setTitle("项目");
            messageEntity.setCreateBy(username);
            messageEntity.setCreateTime(nowTime);
            messageEntity.setContext("“" + xiangMuEntity.getXiangmuName() + "”项目审核未通过,请您查看！");
            messageEntity.setType((short) 2);
            messageEntity.setMessageType((short) 3);
            messageEntity.setProjectId(id);
            messageEntity.setReceiverId(senderId);
            messageEntity.setSenderId(receiverid);
            messageEntity.setStatus((short) 1);
            messageDao.insertSelective(messageEntity);
            //新增查看消息
            ChanPinJiaoFuRecordMessageEntity recordMessageEntity = new ChanPinJiaoFuRecordMessageEntity();
            String reason = chanPinJiaoFuFileAuditorEntity.getReason();
            recordMessageEntity.setTypeid(id);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(nowTime);
            recordMessageEntity.setTypenum(1);
            recordMessageEntity.setContext("“" + xiangMuEntity.getXiangmuName() + "”项目审核未通过,原因是:" + reason);
            chanPinJiaoFuRecordMessageDao.insertSelective(recordMessageEntity);
        }
        if (chanPinJiaoFuFileAuditorEntity.getIsPass() == 1) {//审核通过
            ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity = new ChanPinJiaoFuXiangMuEntity();
            chanPinJiaoFuXiangMuEntity.setId(id);
            chanPinJiaoFuXiangMuEntity.setAuditorstate(2);
            chanPinJiaoFuXiangMuDao.editXiangMuById(chanPinJiaoFuXiangMuEntity);
            //新增查看消息
            ChanPinJiaoFuXiangMuEntity xiangMuEntity = chanPinJiaoFuXiangMuDao.selectByPrimaryKey(id);
            ChanPinJiaoFuRecordMessageEntity recordMessageEntity = new ChanPinJiaoFuRecordMessageEntity();
            recordMessageEntity.setTypeid(id);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(nowTime);
            recordMessageEntity.setTypenum(1);
            recordMessageEntity.setContext("“" + xiangMuEntity.getXiangmuName() + "”项目审核已通过！");
            chanPinJiaoFuRecordMessageDao.insertSelective(recordMessageEntity);
        }
        chanPinJiaoFuFileAuditorEntity.setDoTime(nowTime);
        chanPinJiaoFuFileAuditorEntity.setDoName(username);
        chanPinJiaoFuXiangMuDao.editAuditorByXMId(chanPinJiaoFuFileAuditorEntity);
    }

    @Override
    public List<ChanPinJiaoFuXiangMuFileEntity> findFileById(Integer id) {
        return chanPinJiaoFuXiangMuFileDao.findFileById(id);
    }

    @Override
    public void editXiangMuById(ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity, String username, Integer id, Integer[] uids, Integer senderid) {
        StringBuilder sb = new StringBuilder();
        Date nowTime = new Date();
        //添加审查人
        if (uids.length != 0) {
            for (Integer uid : uids) {
                ChanPinJiaoFuFileAuditorEntity chanPinJiaoFuFileAuditorEntity = new ChanPinJiaoFuFileAuditorEntity();
                chanPinJiaoFuFileAuditorEntity.setChanPinJiaoFuFileId(id);
                chanPinJiaoFuFileAuditorEntity.setAuditorId(uid);
                chanPinJiaoFuFileAuditorEntity.setObjectType(1);
                chanPinJiaoFuXiangMuDao.addAuditorName(chanPinJiaoFuFileAuditorEntity);
                //添加消息
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setCreateBy(username);//创建人
                messageEntity.setCreateTime(nowTime);//创建时间
                messageEntity.setType((short) 2);
                messageEntity.setMessageType((short) 3);
                messageEntity.setProjectId(id);
                messageEntity.setSenderId(senderid);
                messageEntity.setReceiverId(uid);
                messageEntity.setStatus((short) 1);
                messageEntity.setTitle("项目");
                messageEntity.setContext("“" + chanPinJiaoFuXiangMuEntity.getXiangmuName() + "”项目需要您审核,请查看");
                messageService.sendMessage(messageEntity);
            }
            chanPinJiaoFuXiangMuEntity.setAuditorstate(1);
        }
        chanPinJiaoFuXiangMuEntity.setUpdateTime(nowTime);
        chanPinJiaoFuXiangMuEntity.setOperatorName(username);
        Integer typenum = 1;
        Integer mid = id;
        ChanPinJiaoFuXiangMuEntity xiangMuEntity = chanPinJiaoFuXiangMuDao.selectByPrimaryKey(id);
        chanPinJiaoFuXiangMuDao.editXiangMuById(chanPinJiaoFuXiangMuEntity);
        Integer aa = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (xiangMuEntity.getXiangmuState() != chanPinJiaoFuXiangMuEntity.getXiangmuState()) {
            if (chanPinJiaoFuXiangMuEntity.getXiangmuState() == 1) {
                sb.append(" 项目状态改为“正在执行”,");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getXiangmuState() == 2) {
                sb.append(" 项目状态改为“仅剩尾款”,");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getXiangmuState() == 3) {
                sb.append(" 项目状态改为“项目关闭”,");
                aa++;
            }
        }
        if (!xiangMuEntity.getXiangmuName().equals(chanPinJiaoFuXiangMuEntity.getXiangmuName())) {
            String xiangmuName = chanPinJiaoFuXiangMuEntity.getXiangmuName();
            sb.append(" 项目名改为" + xiangmuName + ",");
            aa++;
        }
        if (xiangMuEntity.getXiaoshouState() != chanPinJiaoFuXiangMuEntity.getXiaoshouState()) {
            if (chanPinJiaoFuXiangMuEntity.getXiaoshouState() == 1) {
                sb.append(" 销售需求状态改为 前续未办理,");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getXiaoshouState() == 2) {
                sb.append(" 销售需求状态改为 全部品类/数量已下需求,");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getXiaoshouState() == 3) {
                sb.append(" 销售需求状态改为 部分品类/数量已下需求,");
                aa++;
            }
        }
        if (xiangMuEntity.getFahuoState() != chanPinJiaoFuXiangMuEntity.getFahuoState()) {
            if (chanPinJiaoFuXiangMuEntity.getFahuoState() == 1) {
                sb.append(" 发货状态改为 项目停滞暂不发货,");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getFahuoState() == 2) {
                sb.append(" 发货状态改为 暂未开始发货,");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getFahuoState() == 3) {
                sb.append(" 发货状态改为 陆续发货中,");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getFahuoState() == 4) {
                sb.append(" 发货状态改为 工程结束不需发货,");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getFahuoState() == 5) {
                sb.append(" 发货状态改为 当前订单完成,");
                aa++;
            }
        }
        if (xiangMuEntity.getYangongState() != chanPinJiaoFuXiangMuEntity.getYangongState()) {
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 1) {
                sb.append(" 验工状态改为  是否需要验工, ");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 2) {
                sb.append(" 验工状态改为  前续未办理, ");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 3) {
                sb.append(" 验工状态改为  待验工, ");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 4) {
                sb.append(" 验工状态改为  顾客要求暂缓验工, ");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 5) {
                sb.append(" 验工状态改为  完成部分验工, ");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 6) {
                sb.append(" 验工状态改为  完成验工, ");
                aa++;
            }
        }
        if (xiangMuEntity.getQianshouState() != chanPinJiaoFuXiangMuEntity.getQianshouState()) {
            if (chanPinJiaoFuXiangMuEntity.getQianshouState() == 1) {
                sb.append(" 签收状态改为  前续未办理, ");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getQianshouState() == 2) {
                sb.append(" 签收状态改为  公司暂存不签收, ");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getQianshouState() == 3) {
                sb.append(" 签收状态改为  已转储待签收, ");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getQianshouState() == 4) {
                sb.append(" 签收状态改为  完成签收, ");
                aa++;
            }
        }
        if (xiangMuEntity.getXianchangfuwu() != chanPinJiaoFuXiangMuEntity.getXianchangfuwu()) {
            if (chanPinJiaoFuXiangMuEntity.getXianchangfuwu() == 1) {
                sb.append(" 是否需要现场服务改为 是, ");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getXianchangfuwu() == 0) {
                sb.append(" 是否需要现场服务改为 否, ");
                aa++;
            }
        }
        if (xiangMuEntity.getRemarks() == null) {
            if (chanPinJiaoFuXiangMuEntity.getRemarks() != null) {
                sb.append("新增备注" + chanPinJiaoFuXiangMuEntity.getRemarks() + ",");
                aa++;
            }
        }
        if (xiangMuEntity.getRemarks() != null && xiangMuEntity.getRemarks() != chanPinJiaoFuXiangMuEntity.getRemarks()) {
            sb.append("备注改为" + chanPinJiaoFuXiangMuEntity.getRemarks() + ",");
            aa++;
        }
        if (xiangMuEntity.getFahuoTixingTime() == null) {
            if (chanPinJiaoFuXiangMuEntity.getFahuoTixingTime() != null) {
                Date fahuoTixingTime = chanPinJiaoFuXiangMuEntity.getFahuoTixingTime();
                String format = sdf.format(fahuoTixingTime);
                sb.append("新增发货时间" + format + ",");
                aa++;
            }
        }
        if (xiangMuEntity.getFahuoTixingTime() != null && xiangMuEntity.getFahuoTixingTime().getTime() != chanPinJiaoFuXiangMuEntity.getFahuoTixingTime().getTime()) {
            Date fahuoTixingTime = chanPinJiaoFuXiangMuEntity.getFahuoTixingTime();
            String format = sdf.format(fahuoTixingTime);
            sb.append("发货时间改为" + format + ",");
            aa++;
        }
        if (aa == 0) {
            sb.append("没有修改任何状态");
        }
        String context = sb.toString();
        chanPinJiaoFuRecordMessageDao.addRecordMessage(mid, typenum, nowTime, username, context);
    }

    @Override
    public void addXiangMu(ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity, String username, Integer senderid) {
        Date nowTime = new Date();
        chanPinJiaoFuXiangMuEntity.setCreateTime(nowTime);
        chanPinJiaoFuXiangMuEntity.setOperatorName(username);
        String xiangmuName = chanPinJiaoFuXiangMuEntity.getXiangmuName();
        Integer typenum = 1;
        String context = "新增“" + xiangmuName + "”项目 , 项目状态为“正在执行”";
        chanPinJiaoFuXiangMuDao.addXiangMu(chanPinJiaoFuXiangMuEntity);
        Integer mid = chanPinJiaoFuXiangMuEntity.getId();
        chanPinJiaoFuRecordMessageDao.addRecordMessage(mid, typenum, nowTime, username, context);
        List<Integer> uids = new ArrayList<>();
        if (chanPinJiaoFuXiangMuEntity.getXianchangfuwu() == 1) {//“是否需要现场服务”为真
            //获取所有的人员id  发消息给所有人
            String truename = null;
            List<UserEntity> userEntitiess = userService.findByTruename(truename);
            for (UserEntity entitiess : userEntitiess) {
                Integer id = entitiess.getId().intValue();
                uids.add(id);
            }
            for (Integer uid : uids) {
                //添加一条消息到消息表
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setContext("“" + xiangmuName + "”项目需现场服务，请关注项目进展情况，及时安排现场服务计划！");
                messageEntity.setType((short) 2);
                messageEntity.setStatus((short) 1);
                messageEntity.setCreateBy(username);//创建人
                messageEntity.setCreateTime(nowTime);//创建时间
                messageEntity.setMessageType((short) 1);
                messageEntity.setProjectId(mid);
                messageEntity.setSenderId(senderid);
                messageEntity.setReceiverId(uid);
                messageService.sendMessage(messageEntity);
            }
        }
    }

    //展示树形结构
    @Override
    public List<TreeNodeUtil> findSanJiShu() {
        List<ChanPinJiaoFuXiangMuEntity> chanPinJiaoFuXiangMuEntities = chanPinJiaoFuXiangMuDao.findSanJiShu();
        List<TreeNodeUtil> treeNodeUtils = new ArrayList<>();
        for (ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity : chanPinJiaoFuXiangMuEntities) {
            TreeNodeUtil treeNodeUtil = new TreeNodeUtil();

            if (chanPinJiaoFuXiangMuEntity.getXiangmuState() == 1 || chanPinJiaoFuXiangMuEntity.getXiangmuState() == 2 || chanPinJiaoFuXiangMuEntity.getXiangmuState() == 3) {
                //第一级
                Integer xmid = chanPinJiaoFuXiangMuEntity.getId();
                treeNodeUtil.setValue(xmid.toString());
                treeNodeUtil.setId((long) chanPinJiaoFuXiangMuEntity.getId());
                treeNodeUtil.setLabel(chanPinJiaoFuXiangMuEntity.getXiangmuState().toString());
                List<ChanPinJiaoFuXiangMuEntity> chanPinJiaoFuXiangMuEntities1 = chanPinJiaoFuXiangMuDao.findDiErJi(chanPinJiaoFuXiangMuEntity.getXiangmuState());
                List<TreeNodeUtil> treeNodeUtilss = new ArrayList<>();
                for (ChanPinJiaoFuXiangMuEntity pinJiaoFuXiangMuEntity : chanPinJiaoFuXiangMuEntities1) {
                    //第二级
                    TreeNodeUtil treeNodeUtil1 = new TreeNodeUtil();
                    //Map<String,Object> map=new HashMap();
                    Integer id = pinJiaoFuXiangMuEntity.getId() + 1111;
                    treeNodeUtil1.setValue(id.toString());
                    treeNodeUtil1.setId((long) pinJiaoFuXiangMuEntity.getId());
                    treeNodeUtil1.setLabel(pinJiaoFuXiangMuEntity.getXiangmuBianhao());
                    // map.put("xiangmu",chanPinJiaoFuXiangMuDao.findOneXiangMU(pinJiaoFuXiangMuEntity.getId()));
                    //treeNodeUtil1.setLi_attr(map);
                    treeNodeUtilss.add(treeNodeUtil1);
                    treeNodeUtil.setChildren(treeNodeUtilss);
                    //第三级
                    Integer aa = id + 2222;
                    Integer aaa = id + 3333;
                    List<TreeNodeUtil> treeNodeUtilss2 = new ArrayList<>();
                    List<TreeNodeUtil> treeNodeUtilss3 = new ArrayList<>();
                    TreeNodeUtil treeNodeUtil2 = new TreeNodeUtil();
                    TreeNodeUtil treeNodeUtil3 = new TreeNodeUtil();
                    treeNodeUtil2.setId((long) 1);
                    treeNodeUtil2.setLabel("输入文件");
                    treeNodeUtil2.setValue(aa.toString());
                    treeNodeUtil2.setChildren(treeNodeUtilss3);
                    treeNodeUtil3.setId((long) 2);
                    treeNodeUtil3.setLabel("输出文件");
                    treeNodeUtil3.setValue(aaa.toString());
                    treeNodeUtil3.setChildren(treeNodeUtilss3);
                    treeNodeUtilss2.add(treeNodeUtil2);
                    treeNodeUtilss2.add(treeNodeUtil3);
                    treeNodeUtil1.setChildren(treeNodeUtilss2);
                }
            }
            treeNodeUtils.add(treeNodeUtil);
        }
        Integer a = 0;
        Integer b = 0;
        Integer c = 0;
        for (TreeNodeUtil nodeUtil : treeNodeUtils) {
            if (nodeUtil.getLabel().equals("1")) {
                a++;
            }
            if (nodeUtil.getLabel().equals("2")) {
                b++;
            }
            if (nodeUtil.getLabel().equals("3")) {
                c++;
            }
        }

        if (a == 0) {
            TreeNodeUtil treeNodeUtil1 = new TreeNodeUtil();
            treeNodeUtil1.setLabel("正在执行");
            treeNodeUtils.add(0, treeNodeUtil1);
        }
        if (b == 0) {
            TreeNodeUtil treeNodeUtil2 = new TreeNodeUtil();
            treeNodeUtil2.setLabel("仅剩尾款");
            treeNodeUtils.add(1, treeNodeUtil2);
        }
        if (c == 0) {
            TreeNodeUtil treeNodeUtil3 = new TreeNodeUtil();
            treeNodeUtil3.setLabel("项目关闭");
            treeNodeUtils.add(2, treeNodeUtil3);
        }
        return treeNodeUtils;
    }

}




