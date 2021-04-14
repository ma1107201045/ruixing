package com.yintu.ruixing.paigongguanli.impl;

import com.alibaba.fastjson.JSONArray;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.guzhangzhenduan.XianDuanEntity;
import com.yintu.ruixing.master.guzhangzhenduan.XianDuanDao;
import com.yintu.ruixing.master.paigongguanli.*;
import com.yintu.ruixing.paigongguanli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Mr.liu
 * @Date 2020/10/9 11:07
 * @Version 1.0
 * 需求:报工
 */
@Service
@Transactional
public class PaiGongGuanLiBaoGongServiceImpl implements PaiGongGuanLiBaoGongService {
    @Autowired
    private PaiGongGuanLiBaoGongDao paiGongGuanLiBaoGongDao;
    @Autowired
    private PaiGongGuanLiRiQinDao paiGongGuanLiRiQinDao;
    @Autowired
    private PaiGongGuanLiPaiGongDanDao paiGongGuanLiPaiGongDanDao;
    @Autowired
    private PaiGongGuanLiBaoGongFileDao paiGongGuanLiBaoGongFileDao;
    @Autowired
    private PaiGongGuanLiBaoGongSecondaryDao paiGongGuanLiBaoGongSecondaryDao;
    @Autowired
    private XianDuanDao xianDuanDao;
    @Autowired
    private PaiGongGuanLiUserDao paiGongGuanLiUserDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private PaiGongGuanLiUserDaystateDao paiGongGuanLiUserDaystateDao;
    @Autowired
    private PaiGongGuanLiBaoGongCommentDao paiGongGuanLiBaoGongCommentDao;


    @Override
    public List<PaiGongGuanLiBaoGongEntity> findAllChuChaiPeopele(String lastDateTime, String nowDateTime) {
        List<PaiGongGuanLiBaoGongEntity> baoGongEntityList=paiGongGuanLiBaoGongDao.findAllChuChaiPeopele(lastDateTime,nowDateTime);
        for (PaiGongGuanLiBaoGongEntity baoGongEntity : baoGongEntityList) {
            Integer userid = baoGongEntity.getUserid();
            String name=paiGongGuanLiUserDao.findUserName(userid);
            baoGongEntity.setTruename(name);
        }
        return baoGongEntityList;
    }

    @Override
    public List<PaiGongGuanLiBaoGongEntity> findAllBaoGongAndAllComment(Integer userid, String riqi) {
        List<PaiGongGuanLiBaoGongEntity> baoGongEntityList=paiGongGuanLiBaoGongDao.findAllBaoGongAndAllComment(userid,riqi);
        for (PaiGongGuanLiBaoGongEntity baoGongEntity : baoGongEntityList) {
            Integer bid = baoGongEntity.getId();//报工id
            List<PaiGongGuanLiBaoGongCommentEntity>commentEntityList=paiGongGuanLiBaoGongCommentDao.findCommentByBid(bid);
            baoGongEntity.setCommentEntityList(commentEntityList);
        }
        return baoGongEntityList;
    }

    @Override
    public List<PaiGongGuanLiBaoGongFileEntity> findFileByBid(Integer bid, Integer baoGongType) {
        return paiGongGuanLiBaoGongFileDao.findFileByBid(bid,baoGongType);
    }

    @Override
    public List<PaiGongGuanLiUserEntity> findBaoGongUser(Integer baoGongType) {
        return paiGongGuanLiUserDao.findBaoGongUser(baoGongType);
    }

    @Override
    public List<PaiGongGuanLiBaoGongEntity> findBaoGongBySomethings(String startTime, String endTime, Integer userid, String xianDuan, Integer isNotClose,Integer baoGongType) {
        String startDayTime=null;
        String endDayTime=null;
        if (startTime==null || "".equals(startTime)){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            //获取当前月第一天：
            Calendar c = Calendar.getInstance();
            c.add(Calendar.MONTH, 0);
            c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
            String monthfirst = format.format(c.getTime());//本月第一天
            System.out.println("===============nowfirst:" + monthfirst);
            //获取当前月最后一天
            Calendar ca = Calendar.getInstance();
            ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
            String monthlast = format.format(ca.getTime());//本月最后一天
            System.out.println("===============last:" + monthlast);
            startDayTime=monthfirst;
            endDayTime=monthlast;
        }else {
            startDayTime=startTime;
            endDayTime=endTime;
        }
        List<PaiGongGuanLiBaoGongEntity> baoGongEntityList=paiGongGuanLiBaoGongDao.findBaoGongBySomethings(startDayTime,endDayTime,userid,xianDuan,isNotClose,baoGongType);
        for (PaiGongGuanLiBaoGongEntity baoGongEntity : baoGongEntityList) {
            Integer coordinationuserid = baoGongEntity.getCoordinationuserid();
            String coordinationuser=paiGongGuanLiUserDao.findUserNameById(coordinationuserid);
            baoGongEntity.setCoordinationuser(coordinationuser);
            baoGongEntity.setSignId(UUID.randomUUID().toString());
            Integer bid = baoGongEntity.getId();
            List<PaiGongGuanLiBaoGongSecondaryEntity> secondaryEntityList=paiGongGuanLiBaoGongSecondaryDao.findBaoGongByBid(bid);
            for (PaiGongGuanLiBaoGongSecondaryEntity secondaryEntity : secondaryEntityList) {
                secondaryEntity.setSignId(UUID.randomUUID().toString());
            }
            String uu= UUID.randomUUID().toString();
            baoGongEntity.setChildren(secondaryEntityList);
        }
        return baoGongEntityList;
    }

    @Override
    public List<PaiGongGuanLiBaoGongEntity> findAllBaoGong(Integer baoGongType) {
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.add(Calendar.DAY_OF_MONTH,-1); //设置为前一天
        Date dBefore = calendar.getTime(); //得到前一天的时间

        Calendar calendar1 = Calendar.getInstance(); //得到日历
        calendar1.add(Calendar.DAY_OF_MONTH,-1); //设置为后一天
        Date tommorow = calendar1.getTime(); //得到后一天的时间

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        String yesterdayDate = sdf.format(dBefore); //格式化前一天
        String tommorowDate = sdf.format(tommorow); //格式化后一天
        List<PaiGongGuanLiBaoGongEntity> baoGongEntityList=paiGongGuanLiBaoGongDao.findAllBaoGong(yesterdayDate,tommorowDate,baoGongType);
        for (PaiGongGuanLiBaoGongEntity baoGongEntity : baoGongEntityList) {
            Integer coordinationuserid = baoGongEntity.getCoordinationuserid();
            String coordinationuser=paiGongGuanLiUserDao.findUserNameById(coordinationuserid);
            baoGongEntity.setCoordinationuser(coordinationuser);
            baoGongEntity.setSignId(UUID.randomUUID().toString());
            Integer bid = baoGongEntity.getId();
            List<PaiGongGuanLiBaoGongSecondaryEntity> secondaryEntityList=paiGongGuanLiBaoGongSecondaryDao.findBaoGongByBid(bid);
            for (PaiGongGuanLiBaoGongSecondaryEntity secondaryEntity : secondaryEntityList) {
                secondaryEntity.setSignId(UUID.randomUUID().toString());
            }
            String uu= UUID.randomUUID().toString();
            baoGongEntity.setChildren(secondaryEntityList);
        }
        return baoGongEntityList;
    }

    @Override
    public void deleteFileByIds(Integer[] ids) {
        for (Integer id : ids) {
            paiGongGuanLiBaoGongFileDao.deleteByPrimaryKey(id);
        }
    }

    @Override
    public void addFile(PaiGongGuanLiBaoGongFileEntity paiGongGuanLiBaoGongFileEntity) {
        paiGongGuanLiBaoGongFileDao.insertSelective(paiGongGuanLiBaoGongFileEntity);
    }

    @Override
    public List<XianDuanEntity> findAllXianDuan() {
        return xianDuanDao.findAllXD();
    }

    @Override
    public List<PaiGongGuanLiPaiGongDanEntity> findAllNotOverPaiGong(Integer userid) {
        return paiGongGuanLiPaiGongDanDao.findAllNotOverPaiGong(userid);
    }

    @Override
    public List<PaiGongGuanLiRiQinEntity> findPeopleAddressOnMap() {
        List<PaiGongGuanLiRiQinEntity> riQinEntityList = paiGongGuanLiRiQinDao.findPeopleAddressOnMap();
        for (PaiGongGuanLiRiQinEntity riQinEntity : riQinEntityList) {
            Integer uid = riQinEntity.getUid();
            PaiGongGuanLiBaoGongEntity JingWeiDu = paiGongGuanLiBaoGongDao.findJingWeiDuByUid(uid);
            Double latitude = JingWeiDu.getLatitude();
            Double longitude = JingWeiDu.getLongitude();
            riQinEntity.setLatitude(latitude);
            riQinEntity.setLongitude(longitude);
        }
        return riQinEntityList;
    }

    @Override
    public List<PaiGongGuanLiRiQinEntity> findPropleAddress(Integer page, Integer size) {
        List<PaiGongGuanLiRiQinEntity> riQinEntityList = paiGongGuanLiRiQinDao.findPropleAddress();
        for (PaiGongGuanLiRiQinEntity riQinEntity : riQinEntityList) {
            Integer uid = riQinEntity.getUid();
            String adress = paiGongGuanLiBaoGongDao.findAdressByUid(uid);
            riQinEntity.setAdress(adress);
        }
        return riQinEntityList;
    }

    @Override
    public void deleteBaoGongByIds(Integer[] ids) {
        for (Integer id : ids) {
            paiGongGuanLiBaoGongDao.deleteByPrimaryKey(id);
        }
    }

    @Override
    public void editBaoGongById(String username, Integer senderid, PaiGongGuanLiBaoGongEntity paiGongGuanLiBaoGongEntity) {
        paiGongGuanLiBaoGongEntity.setUpdatename(username);
        paiGongGuanLiBaoGongEntity.setUpdatetime(new Date());
        paiGongGuanLiBaoGongDao.updateByPrimaryKeySelective(paiGongGuanLiBaoGongEntity);
    }

    @Override
    public List<PaiGongGuanLiBaoGongEntity> findAllBaoGongByUid(Integer page, Integer size, Integer senderid, Date datetime) {
        return paiGongGuanLiBaoGongDao.findAllBaoGongByUid(senderid, datetime);
    }

    @Override
    public void addBaoGongDan(JSONArray baoGongDatas, String username, Integer senderid) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        if (baoGongDatas.size()==1){
            Integer qita =0;
            Object baoGongData = baoGongDatas.get(0);
            Map<String, Object> baoGongDatamap = (Map<String, Object>) baoGongData;
            Map<String, Object> onebaoGongDatamap = (Map<String, Object>) baoGongDatamap.get("addForm");
            String address = (String) onebaoGongDatamap.get("address");//地址
            Double longitude = (Double) onebaoGongDatamap.get("longitude");//经度
            Double latitude = (Double) onebaoGongDatamap.get("latitude");//纬度
            String filename = (String) onebaoGongDatamap.get("filename");//文件名
            String filepath = (String) onebaoGongDatamap.get("filepath");//文件路径
            String xianduan = (String) onebaoGongDatamap.get("xianduan");//线段名
            Integer coordinationuserid = (Integer) onebaoGongDatamap.get("coordinationuserid");//需要配合人id
            Integer daiUserid = (Integer) onebaoGongDatamap.get("userid");//代报工人id
            String department = (String) onebaoGongDatamap.get("department");//需要配合部门
            String workcontent = (String) onebaoGongDatamap.get("workcontent");//工作内容
            qita = (Integer) onebaoGongDatamap.get("qita");//qita=1:报工的出差报工，2：报工的其他报工，3：代报工的出差报工，4：代报工的其他报工',5：补充报工的出差报工，6：补充报工的其他报工
            //新增报工
            PaiGongGuanLiBaoGongEntity baoGongEntity=new PaiGongGuanLiBaoGongEntity();
            baoGongEntity.setAddress(address);
            baoGongEntity.setLatitude(latitude);
            baoGongEntity.setLongitude(longitude);
            baoGongEntity.setXianduan(xianduan);
            baoGongEntity.setCoordinationuserid(coordinationuserid);
            baoGongEntity.setDepartment(department);
            baoGongEntity.setWorkcontent(workcontent);
            baoGongEntity.setQita(qita);
            baoGongEntity.setUid(senderid);
            if (qita==3 ||qita==4){
                baoGongEntity.setUserid(daiUserid);
                paiGongGuanLiUserDaystateDao.editUserBaoGongState(daiUserid,today,1);
            }else {
                baoGongEntity.setUserid(senderid);
                paiGongGuanLiUserDaystateDao.editUserBaoGongState(senderid,today,1);
            }
            baoGongEntity.setIsnotover(1);
            baoGongEntity.setDatetime(new Date());
            baoGongEntity.setCreatename(username);
            baoGongEntity.setCreatetime(new Date());
            baoGongEntity.setUpdatename(username);
            baoGongEntity.setUpdatetime(new Date());
            paiGongGuanLiBaoGongDao.insertSelective(baoGongEntity);
            //新增文件
            if (filename!=null && filepath!=null) {
                Integer bid = baoGongEntity.getId();//新增报工的id
                String[] filenames = filename.split(",");
                String[] filepaths = filepath.split(",");
                for (int i = 0; i < filenames.length; i++) {
                    PaiGongGuanLiBaoGongFileEntity fileEntity = new PaiGongGuanLiBaoGongFileEntity();
                    fileEntity.setBid(bid);
                    fileEntity.setFileName(filenames[i]);
                    fileEntity.setFilePath(filepaths[i]);
                    fileEntity.setBaogongtype(qita);
                    fileEntity.setCreatename(username);
                    fileEntity.setCreatetime(new Date());
                    fileEntity.setUpdatename(username);
                    fileEntity.setUpdatetime(new Date());
                    paiGongGuanLiBaoGongFileDao.insertSelective(fileEntity);
                }
            }
            //给需要配合的人发送消息
            if (coordinationuserid!=null){
                Integer userid=paiGongGuanLiUserDao.findUseridByid(coordinationuserid);
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setCreateBy(username);//创建人
                messageEntity.setCreateTime(new Date());//创建时间
                messageEntity.setContext("有报工任务需要您配合处理,请查看！");
                messageEntity.setType((short) 5);
                messageEntity.setProjectId(baoGongEntity.getId());
                messageEntity.setMessageType((short) 3);
                messageEntity.setSenderId(senderid);
                messageEntity.setReceiverId(userid);
                messageEntity.setStatus((short) 1);
                messageService.sendMessage(messageEntity);
            }

        }
        if (baoGongDatas.size()>1){
            Integer bid =0;
            Integer qita =0;
            for (int i = 0; i < baoGongDatas.size(); i++) {
                if (i==0) {
                    Object baoGongData = baoGongDatas.get(0);
                    Map<String, Object> baoGongDatamap = (Map<String, Object>) baoGongData;
                    Map<String, Object> onebaoGongDatamap = (Map<String, Object>) baoGongDatamap.get("addForm");
                    String address = (String) onebaoGongDatamap.get("address");//地址
                    Double longitude = (Double) onebaoGongDatamap.get("longitude");//经度
                    Double latitude = (Double) onebaoGongDatamap.get("latitude");//纬度
                    String filename = (String) onebaoGongDatamap.get("filename");//文件名
                    String filepath = (String) onebaoGongDatamap.get("filepath");//文件路径
                    String xianduan = (String) onebaoGongDatamap.get("xianduan");//线段名
                    Integer coordinationuserid = (Integer) onebaoGongDatamap.get("coordinationuserid");//需要配合人id
                    Integer daiUserid = (Integer) onebaoGongDatamap.get("userid");//代报工人id
                    String department = (String) onebaoGongDatamap.get("department");//需要配合部门
                    String workcontent = (String) onebaoGongDatamap.get("workcontent");//工作内容
                     qita = (Integer) onebaoGongDatamap.get("qita");//1:出差报工，2：其他报工，3：代报工，4：补充报工',
                    //新增报工
                    PaiGongGuanLiBaoGongEntity baoGongEntity = new PaiGongGuanLiBaoGongEntity();
                    baoGongEntity.setAddress(address);
                    baoGongEntity.setLatitude(latitude);
                    baoGongEntity.setLongitude(longitude);
                    baoGongEntity.setXianduan(xianduan);
                    baoGongEntity.setCoordinationuserid(coordinationuserid);
                    baoGongEntity.setDepartment(department);
                    baoGongEntity.setWorkcontent(workcontent);
                    baoGongEntity.setQita(qita);
                    baoGongEntity.setUid(senderid);
                    if (qita==3 ||qita==4){
                        baoGongEntity.setUserid(daiUserid);
                        paiGongGuanLiUserDaystateDao.editUserBaoGongState(daiUserid,today,1);
                    }else {
                        baoGongEntity.setUserid(senderid);
                        paiGongGuanLiUserDaystateDao.editUserBaoGongState(senderid,today,1);
                    }
                    baoGongEntity.setIsnotover(1);
                    baoGongEntity.setDatetime(new Date());
                    baoGongEntity.setCreatename(username);
                    baoGongEntity.setCreatetime(new Date());
                    baoGongEntity.setUpdatename(username);
                    baoGongEntity.setUpdatetime(new Date());
                    paiGongGuanLiBaoGongDao.insertSelective(baoGongEntity);
                    //新增文件
                    if (filename != null && filepath != null) {
                         bid = baoGongEntity.getId();//新增报工的id
                        String[] filenames = filename.split(",");
                        String[] filepaths = filepath.split(",");
                        for (int ii = 0; ii < filenames.length; ii++) {
                            PaiGongGuanLiBaoGongFileEntity fileEntity = new PaiGongGuanLiBaoGongFileEntity();
                            fileEntity.setBid(bid);
                            fileEntity.setFileName(filenames[ii]);
                            fileEntity.setFilePath(filepaths[ii]);
                            fileEntity.setBaogongtype(qita);
                            fileEntity.setCreatename(username);
                            fileEntity.setCreatetime(new Date());
                            fileEntity.setUpdatename(username);
                            fileEntity.setUpdatetime(new Date());
                            paiGongGuanLiBaoGongFileDao.insertSelective(fileEntity);
                        }
                    }
                    //给需要配合的人发送消息
                    if (coordinationuserid!=null){
                        Integer userid=paiGongGuanLiUserDao.findUseridByid(coordinationuserid);
                        MessageEntity messageEntity = new MessageEntity();
                        messageEntity.setCreateBy(username);//创建人
                        messageEntity.setCreateTime(new Date());//创建时间
                        messageEntity.setContext("有报工任务需要您配合处理,请查看！");
                        messageEntity.setType((short) 5);
                        messageEntity.setProjectId(baoGongEntity.getId());
                        messageEntity.setMessageType((short) 3);
                        messageEntity.setSenderId(senderid);
                        messageEntity.setReceiverId(userid);
                        messageEntity.setStatus((short) 1);
                        messageService.sendMessage(messageEntity);
                    }
                }else {
                    Object baoGongData = baoGongDatas.get(i);
                    Map<String, Object> baoGongDatamap = (Map<String, Object>) baoGongData;
                    Map<String, Object> onebaoGongDatamap = (Map<String, Object>) baoGongDatamap.get("addForm");
                    String xianduan = (String) onebaoGongDatamap.get("xianduan");//线段名
                    String workcontent = (String) onebaoGongDatamap.get("workcontent");//工作内容
                    //添加次报工
                    PaiGongGuanLiBaoGongSecondaryEntity secondaryEntity=new PaiGongGuanLiBaoGongSecondaryEntity();
                    secondaryEntity.setBid(bid);
                    secondaryEntity.setXianduan(xianduan);
                    secondaryEntity.setContent(workcontent);
                    secondaryEntity.setBaogongtype(qita);
                    secondaryEntity.setCreatename(username);
                    secondaryEntity.setCreatetime(new Date());
                    secondaryEntity.setUpdatename(username);
                    secondaryEntity.setUpdatetime(new Date());
                    paiGongGuanLiBaoGongSecondaryDao.insertSelective(secondaryEntity);
                }
            }
        }
    }


    @Override
    public void addBaoGong(List<PaiGongGuanLiBaoGongEntity> paiGongGuanLiBaoGongEntity, String username, Integer senderid) {
        Date nowDay = new Date();
        if (paiGongGuanLiBaoGongEntity.size() == 1) {
            for (PaiGongGuanLiBaoGongEntity gongGuanLiBaoGongEntity : paiGongGuanLiBaoGongEntity) {
                if (gongGuanLiBaoGongEntity.getUserid() == null) {
                    gongGuanLiBaoGongEntity.setUserid(senderid);
                }
                if (gongGuanLiBaoGongEntity.getDatetime() == null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date parse = null;
                    try {
                        parse = sdf.parse(sdf.format(nowDay));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    gongGuanLiBaoGongEntity.setDatetime(parse);
                }
                gongGuanLiBaoGongEntity.setUid(senderid);
                gongGuanLiBaoGongEntity.setCreatename(username);
                gongGuanLiBaoGongEntity.setCreatetime(nowDay);
                gongGuanLiBaoGongEntity.setUpdatename(username);
                gongGuanLiBaoGongEntity.setUpdatetime(nowDay);
                paiGongGuanLiBaoGongDao.insertSelective(gongGuanLiBaoGongEntity);
                //添加文件
                Integer bid = gongGuanLiBaoGongEntity.getId();
                String filename = gongGuanLiBaoGongEntity.getFilename();
                String filepath = gongGuanLiBaoGongEntity.getFilepath();
                Integer qita = gongGuanLiBaoGongEntity.getQita();
                if (filename!=null && filepath!=null){
                    PaiGongGuanLiBaoGongFileEntity fileEntity=new PaiGongGuanLiBaoGongFileEntity();
                    fileEntity.setBid(bid);
                    fileEntity.setFileName(filename);
                    fileEntity.setFilePath(filepath);
                    fileEntity.setCreatename(username);
                    fileEntity.setCreatetime(new Date());
                    fileEntity.setUpdatename(username);
                    fileEntity.setUpdatetime(new Date());
                    paiGongGuanLiBaoGongFileDao.insertSelective(fileEntity);
                }
            }
        }else {
            for (int i = 0; i < paiGongGuanLiBaoGongEntity.size(); i++) {
                Integer bid =0;
                if (i==0) {
                    //新增主报工
                    PaiGongGuanLiBaoGongEntity gongGuanLiBaoGongEntity = paiGongGuanLiBaoGongEntity.get(0);
                    gongGuanLiBaoGongEntity.setDatetime(nowDay);
                    gongGuanLiBaoGongEntity.setUid(senderid);
                    gongGuanLiBaoGongEntity.setCreatename(username);
                    gongGuanLiBaoGongEntity.setCreatetime(nowDay);
                    gongGuanLiBaoGongEntity.setUpdatename(username);
                    gongGuanLiBaoGongEntity.setUpdatetime(nowDay);
                    paiGongGuanLiBaoGongDao.insertSelective(gongGuanLiBaoGongEntity);
                    bid= gongGuanLiBaoGongEntity.getId();
                }else {
                    //添加次报工
                    PaiGongGuanLiBaoGongEntity gongGuanLiBaoGongEntity = paiGongGuanLiBaoGongEntity.get(i);
                    String xianduan = gongGuanLiBaoGongEntity.getXianduan();
                    String workcontent = gongGuanLiBaoGongEntity.getWorkcontent();
                    PaiGongGuanLiBaoGongSecondaryEntity secondaryEntity=new PaiGongGuanLiBaoGongSecondaryEntity();
                    secondaryEntity.setBid(bid);
                    secondaryEntity.setXianduan(xianduan);
                    secondaryEntity.setContent(workcontent);
                    secondaryEntity.setCreatename(username);
                    secondaryEntity.setCreatetime(nowDay);
                    secondaryEntity.setUpdatename(username);
                    secondaryEntity.setUpdatetime(nowDay);
                    paiGongGuanLiBaoGongSecondaryDao.insertSelective(secondaryEntity);
                }
            }
        }


    }


}
