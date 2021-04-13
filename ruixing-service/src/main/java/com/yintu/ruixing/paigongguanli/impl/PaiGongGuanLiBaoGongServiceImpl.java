package com.yintu.ruixing.paigongguanli.impl;

import com.alibaba.fastjson.JSONArray;
import com.yintu.ruixing.guzhangzhenduan.XianDuanEntity;
import com.yintu.ruixing.master.guzhangzhenduan.XianDuanDao;
import com.yintu.ruixing.master.paigongguanli.*;
import com.yintu.ruixing.paigongguanli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
            baoGongEntity.setUserid(senderid);
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
                    baoGongEntity.setUserid(senderid);
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
