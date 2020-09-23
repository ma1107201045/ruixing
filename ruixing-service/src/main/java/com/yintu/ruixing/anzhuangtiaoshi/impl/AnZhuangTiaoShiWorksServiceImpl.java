package com.yintu.ruixing.anzhuangtiaoshi.impl;

import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.anzhuangtiaoshi.*;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWorksService;
import com.yintu.ruixing.common.MessageDao;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/7/27 19:38
 * @Version 1.0
 * 需求:安装调试现场作业
 */
@Service
@Transactional
public class AnZhuangTiaoShiWorksServiceImpl implements AnZhuangTiaoShiWorksService {
    @Autowired
    private AnZhuangTiaoShiWorksCheZhanDao anZhuangTiaoShiWorksCheZhanDao;

    @Autowired
    private AnZhuangTiaoShiCheZhanDao anZhuangTiaoShiCheZhanDao;

    @Autowired
    private AnZhuangTiaoShiWorksDingDao anZhuangTiaoShiWorksDingDao;

    @Autowired
    private AnZhuangTiaoShiWorkNameTotalDao anZhuangTiaoShiWorkNameTotalDao;

    @Autowired
    private AnZhuangTiaoShiWorksFileDao anZhuangTiaoShiWorksFileDao;

    @Autowired
    private AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao;

    @Autowired
    private AnZhuangTiaoShiWorkNameLibraryDao anZhuangTiaoShiWorkNameLibraryDao;

    @Autowired
    private AnZhuangTiaoShiXiangMuServiceChooseDao anZhuangTiaoShiXiangMuServiceChooseDao;

    @Autowired
    private AnZhuangTiaoShiObjectAuditorDao anZhuangTiaoShiObjectAuditorDao;

    @Autowired
    private AnZhuangTiaoShiFileDao anZhuangTiaoShiFileDao;

    @Autowired
    private AnZhuangTiaoShiRecordMessageDao anZhuangTiaoShiRecordMessageDao;

    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageDao messageDao;

    @Override
    public List<AnZhuangTiaoShiFileEntity> findsomeFileByNmae(Integer page, Integer size, Integer xdid, Integer filetype, String filename, Integer uid) {
        return anZhuangTiaoShiFileDao.findsomeFileByNmae(xdid,filetype,filename,uid);
    }

    @Override
    public List<AnZhuangTiaoShiFileEntity> findsomeFileByNmaee(Integer page, Integer size, Integer xdid, Integer filetype, String filename, Integer uid) {
        return anZhuangTiaoShiFileDao.findsomeFileByNmaee(xdid,filetype,filename,uid);
    }

    @Override
    public List<AnZhuangTiaoShiFileEntity> findShuChuFileByid(Integer id, Integer page, Integer size, Integer uid) {
        return anZhuangTiaoShiFileDao.findShuChuFileByid(id,uid);
    }

    @Override
    public List<AnZhuangTiaoShiFileEntity> findShuChuFileeByidd(Integer id, Integer page, Integer size, Integer uid) {
        return anZhuangTiaoShiFileDao.findShuChuFileByidd(id,uid);
    }

    @Override
    public List<AnZhuangTiaoShiFileEntity> findShuRuFileByid(Integer id, Integer page, Integer size, Integer uid) {
        return anZhuangTiaoShiFileDao.findShuRuFileByid(id,uid);
    }

    @Override
    public List<AnZhuangTiaoShiFileEntity> findShuRuFileByidd(Integer id, Integer page, Integer size, Integer uid) {
        return anZhuangTiaoShiFileDao.findShuRuFileByidd(id,uid);
    }

    @Override
    public List<AnZhuangTiaoShiObjectAuditorEntity> findXMByid(Integer id) {
        return anZhuangTiaoShiObjectAuditorDao.findXMByid(id);
    }

    @Override
    public void deleteById(Integer id) {
        anZhuangTiaoShiWorksDingDao.deleteByPrimaryKey(id);
    }

    @Override
    public AnZhuangTiaoShiWorksDingEntity findOneWork(Integer cid, Integer wntid, Integer wnlId) {
        return anZhuangTiaoShiWorksDingDao.findOneWork(cid,wntid,wnlId);
    }

    @Override
    public JSONObject findAllCheZhanDatasByXDid(Integer id, Integer worksid) {
        JSONObject js = new JSONObject();
        //获取表头数据
        List<WorksDingTitleEntity> worksDingTitleEntityList = new ArrayList<>();
        List<Integer> wnlId = anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao.findAllwnlidByWorksid(worksid);
        Integer k = 0;
        if (wnlId.size() > 1) {
            for (Integer wnlid : wnlId) {
                k++;
                String workname = anZhuangTiaoShiWorkNameLibraryDao.findWorkNameByid(wnlid);
                WorksDingTitleEntity worksDingTitleEntity = new WorksDingTitleEntity();
                worksDingTitleEntity.setId(k);
                worksDingTitleEntity.setAworkName(workname);
                worksDingTitleEntity.setBworkDoerAndTime("作业人及标记时间");
                worksDingTitleEntity.setCworkBiaoShi("作业项标识");
                worksDingTitleEntity.setDworkState("作业项状态");
                worksDingTitleEntityList.add(worksDingTitleEntity);
            }
        }
        js.put("title", worksDingTitleEntityList);
        System.out.println(js);
        //获取数据
        List<WorksDingTitleDataEntity> worksDingTitleDataEntityList = new ArrayList<>();
        Integer a=-1;
        List<AnZhuangTiaoShiXiangMuServiceChooseEntity> xiangMuServiceChooseEntityList = anZhuangTiaoShiXiangMuServiceChooseDao.findCheZhanByXDid(id);
        for (AnZhuangTiaoShiXiangMuServiceChooseEntity xiangMuServiceChooseEntity : xiangMuServiceChooseEntityList) {
            a++;
            WorksDingTitleDataEntity dingTitleDataEntity=new WorksDingTitleDataEntity();
            JSONObject jss = new JSONObject();
            String chezhanname = xiangMuServiceChooseEntity.getChezhanname();
            Integer czid = xiangMuServiceChooseEntity.getCzid();
            Integer j = 0;
            if (wnlId.size() > 1) {
                for (Integer wnlid : wnlId) {
                    List dataList = new ArrayList<>();
                    j++;
                    String workstate = null;
                    String workerName = null;
                    Date workTime = null;
                    String workStatee = null;
                    Integer workState1 = null;
                    Integer workState = anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao.findWorkStateByIDS(worksid, wnlid);
                    if (workState == 1) {
                        workstate = "手动";
                    }
                    if (workState == 2) {
                        workstate = "自动";
                    }
                    AnZhuangTiaoShiWorksDingEntity worksDingEntity = anZhuangTiaoShiWorksDingDao.findDataByIDS(czid, worksid, wnlid);
                    if (worksDingEntity != null) {
                        workerName = worksDingEntity.getWorkerName();
                        workState1 = worksDingEntity.getWorkState();
                        if (workState1 == 1) {
                            workStatee = "已完成";
                        }
                        if (workState1 == 2) {
                            workStatee = "未完成";
                        }
                        workTime = worksDingEntity.getWorkTime();
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                        String format = sdf.format(workTime);
                        String bworkDoerAndTime = workerName +" "+ format;
                        dataList.add(workState1);
                        dataList.add(bworkDoerAndTime);
                        dataList.add(workstate);
                        dataList.add(workStatee);
                    }
                    if (worksDingEntity == null) {
                        dataList.add(2);
                        dataList.add("");
                        dataList.add(workstate);
                        dataList.add("未完成");
                    }
                    jss.put(j.toString(),dataList);
                    dingTitleDataEntity.setCzName(chezhanname);
                    dingTitleDataEntity.setCzid(czid);
                    dingTitleDataEntity.setList(jss);
                }
            }
            worksDingTitleDataEntityList.add(a,dingTitleDataEntity);
        }
        js.put("tableData",worksDingTitleDataEntityList);
        return js;
    }

    @Override
    public void deletFileByIds(Integer[] ids) {
        anZhuangTiaoShiWorksFileDao.deletFileByIds(ids);
    }

    @Override
    public List<AnZhuangTiaoShiWorksFileEntity> findFileByNmae(Integer page, Integer size, Integer xdid, Integer filetype, String filename) {
        return anZhuangTiaoShiWorksFileDao.findFileByNmae(xdid, filename, filetype);
    }

    @Override
    public AnZhuangTiaoShiWorksFileEntity findById(Integer id) {
        return anZhuangTiaoShiWorksFileDao.selectByPrimaryKey(id);
    }

    /*@Override
    public void editFileById(AnZhuangTiaoShiWorksFileEntity anZhuangTiaoShiWorksFileEntity) {
        anZhuangTiaoShiWorksFileDao.updateByPrimaryKeySelective(anZhuangTiaoShiWorksFileEntity);
    }*/

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


    /*@Override
    public void addFile(AnZhuangTiaoShiWorksFileEntity anZhuangTiaoShiWorksFileEntity) {
        anZhuangTiaoShiWorksFileDao.insertSelective(anZhuangTiaoShiWorksFileEntity);
    }*/
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
        anZhuangTiaoShiFileEntity.setLeibie(1);
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
    public List<AnZhuangTiaoShiWorksFileEntity> findShuRuFileByXid(Integer id, Integer page, Integer size) {
        return anZhuangTiaoShiWorksFileDao.findShuRuFileByXid(id);
    }

    @Override
    public List<AnZhuangTiaoShiWorksFileEntity> findShuChuFileByXid(Integer id, Integer page, Integer size) {
        return anZhuangTiaoShiWorksFileDao.findShuChuFileByXid(id);
    }

    @Override
    public List<AnZhuangTiaoShiWorkNameTotalEntity> findAllWorks() {
        return anZhuangTiaoShiWorkNameTotalDao.findAllWorks();
    }

    @Override
    public void addWorksDatas(AnZhuangTiaoShiWorksDingEntity anZhuangTiaoShiWorksDingEntity) {
        anZhuangTiaoShiWorksDingDao.insertSelective(anZhuangTiaoShiWorksDingEntity);
    }

    @Override
    public List<AnZhuangTiaoShiWorkNameLibraryEntity> findWorksDatasByCid(Integer cid, Integer page, Integer size) {
        return anZhuangTiaoShiWorksDingDao.findWorksDatasByCid(cid);
    }

    @Override
    public List<AnZhuangTiaoShiWorksCheZhanEntity> findCheZhanDatasByXid(Integer xid, Integer page, Integer size) {
        return anZhuangTiaoShiWorksDingDao.findCheZhanDatasByXid(xid);
    }

    @Override
    public List<AnZhuangTiaoShiCheZhanEntity> findCheZhanByXid(Integer xid) {
        return anZhuangTiaoShiCheZhanDao.findCheZhanByXid(xid);
    }

    @Override
    public void editWorksCheZhanByXid(AnZhuangTiaoShiWorksCheZhanEntity anZhuangTiaoShiWorksCheZhanEntity) {
        anZhuangTiaoShiWorksCheZhanDao.updateByPrimaryKey(anZhuangTiaoShiWorksCheZhanEntity);
    }

    @Override
    public void addWorksCheZhan(AnZhuangTiaoShiWorksCheZhanEntity anZhuangTiaoShiWorksCheZhanEntity, String[] chezhanname) {
        for (int i = 0; i < chezhanname.length; i++) {
            anZhuangTiaoShiWorksCheZhanEntity.setCzName(chezhanname[i]);
            anZhuangTiaoShiWorksCheZhanDao.insertSelective(anZhuangTiaoShiWorksCheZhanEntity);
        }
    }
}
