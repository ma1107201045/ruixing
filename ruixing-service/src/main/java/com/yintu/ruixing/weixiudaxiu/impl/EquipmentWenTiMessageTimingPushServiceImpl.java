package com.yintu.ruixing.weixiudaxiu.impl;

import com.yintu.ruixing.master.weixiudaxiu.EquipmentWenTiMessageTimingPushDao;
import com.yintu.ruixing.master.weixiudaxiu.EquipmentWenTiMessageTimingPushRecordDao;
import com.yintu.ruixing.master.weixiudaxiu.EquipmentWenTiReturnVisitRecordmessageDao;
import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiMessageTimingPushEntity;
import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiMessageTimingPushService;
import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiReturnVisitRecordmessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2021/5/7 14:39
 * @Version 1.0
 * 需求:
 */
@Service
@Transactional
public class EquipmentWenTiMessageTimingPushServiceImpl implements EquipmentWenTiMessageTimingPushService {
    @Autowired
    private EquipmentWenTiMessageTimingPushDao equipmentWenTiMessageTimingPushDao;
    @Autowired
    private EquipmentWenTiReturnVisitRecordmessageDao equipmentWenTiReturnVisitRecordmessageDao;
    @Autowired
    private EquipmentWenTiMessageTimingPushRecordDao equipmentWenTiMessageTimingPushRecordDao;



    @Override
    public String findRecordNumberByPid(Integer pid) {
        String RecordNumber="";
        Calendar calendar = Calendar.getInstance();
        String months = "";//月
        Integer year = calendar.get(Calendar.YEAR);
        String years = year.toString().substring(2, 4);//年
        Integer month = calendar.get(Calendar.MONTH) + 1;
        Integer monthlength = month.toString().length();
        if (monthlength == 1) {
            months = "0" + month.toString();
        }
        if (monthlength == 2) {
            months = month.toString();
        }
        EquipmentWenTiMessageTimingPushEntity pushEntity=equipmentWenTiMessageTimingPushDao.selectByPrimaryKey(pid);
        String number = pushEntity.getNumber();
        String recordNumber=equipmentWenTiMessageTimingPushRecordDao.findRecordNum(number);
        if (recordNumber==null){
            RecordNumber=number+"-"+years+months+"01";
        }else {
            String[] recordNumber_split = recordNumber.split("-");
            String nowyear = recordNumber_split[1].substring(0, 2);
            String nowmonth = recordNumber_split[1].substring(2, 4);
            String nownum = recordNumber_split[1].substring(4, 6);
            if (years.equals(nowyear) && nowmonth.equals(months)) {
                Integer onenum = Integer.parseInt(nownum) + 1;
                int length = onenum.toString().length();
                if (length==1){
                    RecordNumber=number+"-"+years+months+"0"+ onenum;
                }else {
                    RecordNumber=number+"-"+years+months+onenum;
                }
            }else {
                RecordNumber=number+"-"+years+months+"01";
            }
        }

        return RecordNumber;
    }

    @Override
    public List<EquipmentWenTiMessageTimingPushEntity> findAllMessagePush(String tljName, String dwdName, String xdName,
                                                                          String startTime, String endTime, Integer implementState,
                                                                          Integer pushType) {
       return equipmentWenTiMessageTimingPushDao.findAllMessagePush(tljName,dwdName,xdName,startTime,endTime,implementState,pushType);
    }

    @Override
    public void deleteByIds(Integer[] ids) {
        for (Integer id : ids) {
            equipmentWenTiMessageTimingPushDao.deleteByPrimaryKey(id);
        }
    }

    @Override
    public void editById(EquipmentWenTiMessageTimingPushEntity equipmentWenTiMessageTimingPushEntity) {
        Integer id = equipmentWenTiMessageTimingPushEntity.getId();
        EquipmentWenTiMessageTimingPushEntity pushEntity=equipmentWenTiMessageTimingPushDao.selectByPrimaryKey(id);
        String tljname = pushEntity.getTljname();//铁路局
        String dwdname = pushEntity.getDwdname();//电务段
        String xdname = pushEntity.getXdname();//线段
        String pushusername = pushEntity.getPushusername();
        String pushusernamephone = pushEntity.getPushusernamephone();
        Integer pushtype = pushEntity.getPushtype();
        Date pushstarttime = pushEntity.getPushstarttime();
        Date pushendtime = pushEntity.getPushendtime();
        Date pushtime = pushEntity.getPushtime();
        StringBuilder sb=new StringBuilder();
        if (equipmentWenTiMessageTimingPushEntity.getTljname()!=null){
            if (!equipmentWenTiMessageTimingPushEntity.getTljname().equals(tljname)){
                sb.append(" 修改了铁路局 ");
            }
        }
        if (equipmentWenTiMessageTimingPushEntity.getDwdname()!=null){
            if (!equipmentWenTiMessageTimingPushEntity.getDwdname().equals(dwdname)){
                sb.append(" 修改了电务段 ");
            }
        }
        if (equipmentWenTiMessageTimingPushEntity.getXdname()!=null){
            if (!equipmentWenTiMessageTimingPushEntity.getXdname().equals(xdname)){
                sb.append(" 修改了线段 ");
            }
        }
        if (equipmentWenTiMessageTimingPushEntity.getPushusername()!=null){
            if (!equipmentWenTiMessageTimingPushEntity.getPushusername().equals(pushusername)){
                sb.append(" 修改了推送对象 ");
            }
        }
        if (equipmentWenTiMessageTimingPushEntity.getPushusernamephone()!=null){
            if (!equipmentWenTiMessageTimingPushEntity.getPushusernamephone().equals(pushusernamephone)){
                sb.append(" 修改了联系电话 ");
            }
        }
        if (equipmentWenTiMessageTimingPushEntity.getPushtype()!=pushtype){
            sb.append(" 修改了推送类型 ");
        }
        if (equipmentWenTiMessageTimingPushEntity.getPushstarttime()!=null){
            if (!equipmentWenTiMessageTimingPushEntity.getPushstarttime().equals(pushstarttime)){
                sb.append(" 修改了推送时间段 ");
            }
        }
        if (equipmentWenTiMessageTimingPushEntity.getPushendtime()!=null){
            if (!equipmentWenTiMessageTimingPushEntity.getPushendtime().equals(pushendtime)){
                sb.append(" 修改了推送时间段 ");
            }
        }
        if (equipmentWenTiMessageTimingPushEntity.getPushtime()!=null){
            if (!equipmentWenTiMessageTimingPushEntity.getPushtime().equals(pushtime)){
                sb.append(" 修改了指定日期 ");
            }
        }
        if (sb.length()==0){
            sb.append(" 暂未做任何修改 ");
        }

        //新增记录
        EquipmentWenTiReturnVisitRecordmessageEntity recordmessageEntity = new EquipmentWenTiReturnVisitRecordmessageEntity();
        recordmessageEntity.setTypeid(id);
        recordmessageEntity.setOperatorname(equipmentWenTiMessageTimingPushEntity.getUpdatename());
        recordmessageEntity.setOperatortime(new Date());
        recordmessageEntity.setTypenum(3);
        recordmessageEntity.setContext(equipmentWenTiMessageTimingPushEntity.getUpdatename() + sb);
        equipmentWenTiReturnVisitRecordmessageDao.insertSelective(recordmessageEntity);
    }

    @Override
    public void addMessagePush(EquipmentWenTiMessageTimingPushEntity equipmentWenTiMessageTimingPushEntity) {
        equipmentWenTiMessageTimingPushEntity.setImplementState(0);
        equipmentWenTiMessageTimingPushDao.insertSelective(equipmentWenTiMessageTimingPushEntity);
        Integer id = equipmentWenTiMessageTimingPushEntity.getId();
        //新增记录
        EquipmentWenTiReturnVisitRecordmessageEntity recordmessageEntity = new EquipmentWenTiReturnVisitRecordmessageEntity();
        recordmessageEntity.setTypeid(id);
        recordmessageEntity.setOperatorname(equipmentWenTiMessageTimingPushEntity.getCreatename());
        recordmessageEntity.setOperatortime(new Date());
        recordmessageEntity.setTypenum(3);
        recordmessageEntity.setContext(equipmentWenTiMessageTimingPushEntity.getCreatename() + "新增了一条信息推送");
        equipmentWenTiReturnVisitRecordmessageDao.insertSelective(recordmessageEntity);
    }

    @Override
    public String findNumber() {
        String number="";
        String fristNumber=equipmentWenTiMessageTimingPushDao.findFristNumber();
        if (fristNumber==null){
            number="SHTS000001";
        }else {
            String onenumber = fristNumber.substring(4, 10);
            Integer number_int = Integer.parseInt(onenumber);
            Integer number_total=number_int+1;
            int length = number_total.toString().length();
            if (length==1){
                number="SHTS00000"+number_total.toString();
            }
            if (length==2){
                number="SHTS0000"+number_total.toString();
            }
            if (length==3){
                number="SHTS000"+number_total.toString();
            }
            if (length==4){
                number="SHTS00"+number_total.toString();
            }
            if (length==5){
                number="SHTS0"+number_total.toString();
            }
            if (length==6){
                number="SHTS"+number_total.toString();
            }
        }
        return number;
    }
}
