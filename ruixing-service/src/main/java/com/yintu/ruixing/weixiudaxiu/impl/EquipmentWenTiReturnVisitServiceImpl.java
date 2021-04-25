package com.yintu.ruixing.weixiudaxiu.impl;

import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.PhoneCode;
import com.yintu.ruixing.master.weixiudaxiu.EquipmentWenTiReturnVisitCommentDao;
import com.yintu.ruixing.master.weixiudaxiu.EquipmentWenTiReturnVisitDao;
import com.yintu.ruixing.master.weixiudaxiu.EquipmentWenTiReturnVisitFileDao;
import com.yintu.ruixing.master.weixiudaxiu.EquipmentWenTiReturnVisitPushRecordDao;
import com.yintu.ruixing.weixiudaxiu.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2021/4/21 17:23
 * @Version 1.0
 * 需求:
 */
@Service
@Transactional
public class EquipmentWenTiReturnVisitServiceImpl implements EquipmentWenTiReturnVisitService {
    @Autowired
    private EquipmentWenTiReturnVisitDao equipmentWenTiReturnVisitDao;
    @Autowired
    private EquipmentWenTiReturnVisitFileDao equipmentWenTiReturnVisitFileDao;
    @Autowired
    private EquipmentWenTiReturnVisitCommentDao equipmentWenTiReturnVisitCommentDao;
    @Autowired
    private EquipmentWenTiReturnVisitPushRecordDao equipmentWenTiReturnVisitPushRecordDao;


    @Override
    public List<EquipmentWenTiReturnVisitPushRecordEntity> findPushMessageRecordById(Integer vid) {
        return equipmentWenTiReturnVisitPushRecordDao.findPushMessageRecordById(vid);
    }

    @Override
    public void pushMessage(EquipmentWenTiReturnVisitPushRecordEntity equipmentWenTiReturnVisitPushRecordEntity) {
        String phone = equipmentWenTiReturnVisitPushRecordEntity.getPhone();
        String phonemsg = PhoneCode.getPhonemsg(phone);
        if ("true".equals(phonemsg)) {
            equipmentWenTiReturnVisitPushRecordEntity.setIsnotsuccess(1);
            //改变回访发送状态
            EquipmentWenTiReturnVisitEntity visitEntity=new EquipmentWenTiReturnVisitEntity();
            visitEntity.setPushstate(1);
            visitEntity.setId(equipmentWenTiReturnVisitPushRecordEntity.getVid());
            equipmentWenTiReturnVisitDao.updateByPrimaryKeySelective(visitEntity);
        } else {
            equipmentWenTiReturnVisitPushRecordEntity.setIsnotsuccess(0);
            //改变回访发送状态
            EquipmentWenTiReturnVisitEntity visitEntity=new EquipmentWenTiReturnVisitEntity();
            visitEntity.setPushstate(2);
            visitEntity.setId(equipmentWenTiReturnVisitPushRecordEntity.getVid());
            equipmentWenTiReturnVisitDao.updateByPrimaryKeySelective(visitEntity);
            throw new BaseRuntimeException("发送消息失败,请检查手机号准确性");
        }
        String pushNumber = equipmentWenTiReturnVisitPushRecordDao.findFristPushNumber();
        Calendar calendar = Calendar.getInstance();
        String months = "";
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH);
        Integer monthlength = month.toString().length();
        if (monthlength == 1) {
            months = "0" + month.toString();
        }
        if (monthlength == 2) {
            months = month.toString();
        }
        if (pushNumber == null) {
            if (monthlength == 1) {
                String onePushNumber = "GCFK-" + year + "-0" + months + "-001";
                equipmentWenTiReturnVisitPushRecordEntity.setPushnumber(onePushNumber);
            }
            if (monthlength == 2) {
                String onePushNumber = "GCFK-" + year + "-" + months + "-001";
                equipmentWenTiReturnVisitPushRecordEntity.setPushnumber(onePushNumber);
            }
        } else {//GCFK-2021-04-001
            String[] number_split = pushNumber.split("-");
            if (number_split[1].equals(year.toString()) && number_split[2].equals(months)) {
                Integer num = Integer.valueOf(number_split[3]) + 1;
                String onePushNumber = "GCFK-" + number_split[1] + "-" + number_split[2] + num;
                equipmentWenTiReturnVisitPushRecordEntity.setPushnumber(onePushNumber);
            }
            if (number_split[1].equals(year.toString())){
                String onePushNumber = "GCFK-" + number_split[1] + "-" + months + 001;
                equipmentWenTiReturnVisitPushRecordEntity.setPushnumber(onePushNumber);
            }else {
                String onePushNumber = "GCFK-" + year + "-" + months + 001;
                equipmentWenTiReturnVisitPushRecordEntity.setPushnumber(onePushNumber);
            }
        }
        equipmentWenTiReturnVisitPushRecordDao.insertSelective(equipmentWenTiReturnVisitPushRecordEntity);
    }

    @Override
    public EquipmentWenTiReturnVisitEntity findOneReturnVisitById(Integer id) {
        EquipmentWenTiReturnVisitEntity returnVisitEntity=equipmentWenTiReturnVisitDao.selectByPrimaryKey(id);
        List<EquipmentWenTiReturnVisitFileEntity> wenTiFile=equipmentWenTiReturnVisitFileDao.findWenTiFile(id);
        List<EquipmentWenTiReturnVisitFileEntity> returnFile=equipmentWenTiReturnVisitFileDao.findReturnFile(id);
        returnVisitEntity.setWenTiFileList(wenTiFile);
        returnVisitEntity.setReturnFileList(returnFile);
        //添加记录
        String pushNumber = equipmentWenTiReturnVisitPushRecordDao.findFristPushNumber();
        Calendar calendar = Calendar.getInstance();
        String months = "";
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH);
        Integer monthlength = month.toString().length();
        if (monthlength == 1) {
            months = "0" + month.toString();
        }
        if (monthlength == 2) {
            months = month.toString();
        }
        if (pushNumber == null) {
            if (monthlength == 1) {
                String onePushNumber = "GCFK-" + year + "-0" + months + "-001";
                returnVisitEntity.setPushNumber(onePushNumber);
            }
            if (monthlength == 2) {
                String onePushNumber = "GCFK-" + year + "-" + months + "-001";
                returnVisitEntity.setPushNumber(onePushNumber);
            }
        } else {//GCFK-2021-04-001
            String[] number_split = pushNumber.split("-");
            if (number_split[1].equals(year.toString()) && number_split[2].equals(months)) {
                Integer num = Integer.valueOf(number_split[3]) + 1;
                String onePushNumber = "GCFK-" + number_split[1] + "-" + number_split[2] + num;
                returnVisitEntity.setPushNumber(onePushNumber);
            }
            if (number_split[1].equals(year.toString())){
                String onePushNumber = "GCFK-" + number_split[1] + "-" + months + 001;
                returnVisitEntity.setPushNumber(onePushNumber);
            }else {
                String onePushNumber = "GCFK-" + year + "-" + months + 001;
                returnVisitEntity.setPushNumber(onePushNumber);
            }
        }
        return returnVisitEntity;
    }

    @Override
    public List<EquipmentWenTiReturnVisitCommentEntity> findCommentByVid(Integer vid) {
        return equipmentWenTiReturnVisitCommentDao.findCommentByVid(vid);
    }

    @Override
    public void addComment(EquipmentWenTiReturnVisitCommentEntity equipmentWenTiReturnVisitCommentEntity) {
        equipmentWenTiReturnVisitCommentDao.insertSelective(equipmentWenTiReturnVisitCommentEntity);
    }

    @Override
    public void deleteFileById(Integer id) {
        equipmentWenTiReturnVisitFileDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<EquipmentWenTiReturnVisitFileEntity> findFie(Integer filetype,Integer vid) {
        return equipmentWenTiReturnVisitFileDao.findFie(filetype,vid);
    }

    @Override
    public void addFile(EquipmentWenTiReturnVisitFileEntity equipmentWenTiReturnVisitFileEntity) {
        equipmentWenTiReturnVisitFileDao.insertSelective(equipmentWenTiReturnVisitFileEntity);
    }

    @Override
    public void deleteReturnVisitByIds(Integer[] ids) {
        for (Integer id : ids) {
            equipmentWenTiReturnVisitDao.deleteByPrimaryKey(id);
        }
    }

    @Override
    public List<EquipmentWenTiReturnVisitEntity> findAllReturnVisit(String renWuNumber, String recordNumber, String tljName,
                                                                    String dwdName, String xdName, Integer returnUserid,
                                                                    Integer renWuState, Integer pushState, String returnWenti,
                                                                    Integer wentiState, String startTime, String endTime,
                                                                    String years, Integer week,Integer longinuserid) {
        Date today=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String today_string = sdf.format(today);
        return equipmentWenTiReturnVisitDao.findAllReturnVisit(renWuNumber, recordNumber, tljName, dwdName, xdName,
                returnUserid, renWuState, pushState, returnWenti, wentiState, startTime, endTime, years, week,longinuserid,today_string);
    }

    @Override
    public void editReturnVisitById(EquipmentWenTiReturnVisitEntity equipmentWenTiReturnVisitEntity,
                                    String fristFileName, String firstFilePath, String secondFileName, String secondFilePath,Integer longinUserid) {
        if (equipmentWenTiReturnVisitEntity.getReplymessage() != null || equipmentWenTiReturnVisitEntity.getReturnwenti() != null || firstFilePath != null || secondFilePath != null) {
            Date today = new Date();
            equipmentWenTiReturnVisitEntity.setReturntime(today);
            String renwunumber = equipmentWenTiReturnVisitEntity.getRenwunumber();
            Integer returncycletype = equipmentWenTiReturnVisitEntity.getReturncycletype();//回访周期类型：1：每周，2：每月，3：每季度，4：每年',
            String recordNum = "";
            if (returncycletype == 1) {//周任务
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setFirstDayOfWeek(Calendar.MONDAY);
                calendar1.setTime(today);
                Integer week = calendar1.get(Calendar.WEEK_OF_YEAR);
                String week_string = week.toString();
                Integer year = calendar1.get(Calendar.YEAR);
                String years = year.toString().substring(2, 4);
                if (week_string.length() == 1) {
                    recordNum = renwunumber + "—" + years + "0" + week_string;
                    equipmentWenTiReturnVisitEntity.setRecordnumber(recordNum);
                }
                if (week_string.length() == 2) {
                    recordNum = renwunumber + "—" + years + week_string;
                    equipmentWenTiReturnVisitEntity.setRecordnumber(recordNum);
                }
            }
            if (returncycletype == 2) {//月任务
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setFirstDayOfWeek(Calendar.MONDAY);
                calendar1.setTime(today);
                Integer year = calendar1.get(Calendar.YEAR);
                String years = year.toString().substring(2, 4);
                Integer month = calendar1.get(Calendar.MONTH) + 1;
                String month_string = month.toString();
                if (month_string.length() == 1) {
                    recordNum = renwunumber + "—" + years + "0" + month_string;
                    equipmentWenTiReturnVisitEntity.setRecordnumber(recordNum);
                }
                if (month_string.length() == 2) {
                    recordNum = renwunumber + "—" + years + month_string;
                    equipmentWenTiReturnVisitEntity.setRecordnumber(recordNum);
                }
            }
            if (returncycletype == 3) {//季度任务
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setFirstDayOfWeek(Calendar.MONDAY);
                calendar1.setTime(today);
                Integer year = calendar1.get(Calendar.YEAR);
                String years = year.toString().substring(2, 4);
                Integer month = calendar1.get(Calendar.MONTH) + 1;
                Integer quarter = 0;
                if (1 <= month && month <= 3) {
                    quarter = 1;
                }
                if (4 <= month && month <= 6) {
                    quarter = 2;
                }
                if (7 <= month && month <= 9) {
                    quarter = 3;
                }
                if (10 <= month && month <= 12) {
                    quarter = 4;
                }
                recordNum = renwunumber + "—" + years + "0" + quarter.toString();
                equipmentWenTiReturnVisitEntity.setRecordnumber(recordNum);
            }
            if (returncycletype == 4) {//年任务
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setFirstDayOfWeek(Calendar.MONDAY);
                calendar1.setTime(today);
                Integer year = calendar1.get(Calendar.YEAR);
                String years = year.toString().substring(2, 4);
                recordNum = renwunumber + "—" + years + "01";
                equipmentWenTiReturnVisitEntity.setRecordnumber(recordNum);
            }
            equipmentWenTiReturnVisitDao.updateByPrimaryKeySelective(equipmentWenTiReturnVisitEntity);

            //添加文件
            if (!"".equals(firstFilePath)  ||!"".equals(fristFileName)) {
                String[] filePath = firstFilePath.split(",");
                String[] fileName = fristFileName.split(",");
                for (int i = 0; i < filePath.length; i++) {
                    EquipmentWenTiReturnVisitFileEntity fileEntity = new EquipmentWenTiReturnVisitFileEntity();
                    fileEntity.setFileName(fileName[i]);
                    fileEntity.setFilePath(filePath[i]);
                    fileEntity.setFileType(1);
                    fileEntity.setUid(longinUserid);
                    fileEntity.setVid(equipmentWenTiReturnVisitEntity.getId());
                    fileEntity.setCreatetime(today);
                    fileEntity.setCreatename(equipmentWenTiReturnVisitEntity.getUpdatename());
                    fileEntity.setUpdatetime(new Date());
                    fileEntity.setUpdatename(equipmentWenTiReturnVisitEntity.getUpdatename());
                    equipmentWenTiReturnVisitFileDao.insertSelective(fileEntity);
                }
            }
            if (!"".equals(secondFileName)  || !"".equals(secondFilePath)) {
                String[] filePath = secondFilePath.split(",");
                String[] fileName = secondFileName.split(",");
                for (int i = 0; i < filePath.length; i++) {
                    EquipmentWenTiReturnVisitFileEntity fileEntity = new EquipmentWenTiReturnVisitFileEntity();
                    fileEntity.setFileName(fileName[i]);
                    fileEntity.setFilePath(filePath[i]);
                    fileEntity.setFileType(2);
                    fileEntity.setUid(longinUserid);
                    fileEntity.setVid(equipmentWenTiReturnVisitEntity.getId());
                    fileEntity.setCreatetime(today);
                    fileEntity.setCreatename(equipmentWenTiReturnVisitEntity.getUpdatename());
                    fileEntity.setUpdatetime(new Date());
                    fileEntity.setUpdatename(equipmentWenTiReturnVisitEntity.getUpdatename());
                    equipmentWenTiReturnVisitFileDao.insertSelective(fileEntity);
                }
            }
        }
    }
}
