package com.yintu.ruixing.weixiudaxiu.impl;

import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.PhoneCode;
import com.yintu.ruixing.danganguanli.CustomerEntity;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuEntity;
import com.yintu.ruixing.guzhangzhenduan.XianDuanEntity;
import com.yintu.ruixing.master.danganguanli.CustomerDao;
import com.yintu.ruixing.master.guzhangzhenduan.DianWuDuanDao;
import com.yintu.ruixing.master.guzhangzhenduan.TieLuJuDao;
import com.yintu.ruixing.master.guzhangzhenduan.XianDuanDao;
import com.yintu.ruixing.master.weixiudaxiu.*;
import com.yintu.ruixing.weixiudaxiu.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2021/4/27 15:51
 * @Version 1.0
 * 需求:
 */
@Service
@Transactional
public class EquipmentWenTiOnlineAcceptServiceImpl implements EquipmentWenTiOnlineAcceptService {
    @Autowired
    private EquipmentWenTiCustomerWenTiTypeDao equipmentWenTiCustomerWenTiTypeDao;
    @Autowired
    private EquipmentWenTiOnlineAcceptFeedbackDao equipmentWenTiOnlineAcceptFeedbackDao;
    @Autowired
    private DianWuDuanDao dianWuDuanDao;
    @Autowired
    private XianDuanDao xianDuanDao;
    @Autowired
    private TieLuJuDao tieLuJuDao;
    @Autowired
    private EquipmentWenTiReturnVisitRecordmessageDao equipmentWenTiReturnVisitRecordmessageDao;
    @Autowired
    private EquipmentWenTiOnlineAcceptFeedbackFileDao equipmentWenTiOnlineAcceptFeedbackFileDao;
    @Autowired
    private EquipmentWenTiReturnVisitCommentDao equipmentWenTiReturnVisitCommentDao;
    @Autowired
    private EquipmentWenTiOnlineAcceptFeedbackPushRecordDao equipmentWenTiOnlineAcceptFeedbackPushRecordDao;
    @Autowired
    private CustomerDao customerDao;


    @Override
    public List<CustomerEntity> findAllCustomer() {
        return customerDao.findAllCustomer();
    }

    @Override
    public List<EquipmentWenTiOnlineAcceptFeedbackPushRecordEntity> findPushMessageRecordById(Integer fid) {
        return equipmentWenTiOnlineAcceptFeedbackPushRecordDao.findPushMessageRecordById(fid);
    }

    @Override
    public void pushMessage(EquipmentWenTiOnlineAcceptFeedbackPushRecordEntity equipmentWenTiOnlineAcceptFeedbackPushRecordEntity) {
        String phone = equipmentWenTiOnlineAcceptFeedbackPushRecordEntity.getPhone();
        String phonemsg = PhoneCode.getPhonemsg(phone);
        if ("true".equals(phonemsg)) {
            equipmentWenTiOnlineAcceptFeedbackPushRecordEntity.setIsnotsuccess(1);
            //改变回访发送状态
            EquipmentWenTiOnlineAcceptFeedbackEntity feedbackEntity = new EquipmentWenTiOnlineAcceptFeedbackEntity();
            feedbackEntity.setPushstate(1);
            feedbackEntity.setId(equipmentWenTiOnlineAcceptFeedbackPushRecordEntity.getFid());
            equipmentWenTiOnlineAcceptFeedbackDao.updateByPrimaryKeySelective(feedbackEntity);
        } else {
            equipmentWenTiOnlineAcceptFeedbackPushRecordEntity.setIsnotsuccess(0);
            //改变回访发送状态
            EquipmentWenTiOnlineAcceptFeedbackEntity feedbackEntity = new EquipmentWenTiOnlineAcceptFeedbackEntity();
            feedbackEntity.setPushstate(2);
            feedbackEntity.setId(equipmentWenTiOnlineAcceptFeedbackPushRecordEntity.getFid());
            equipmentWenTiOnlineAcceptFeedbackDao.updateByPrimaryKeySelective(feedbackEntity);
            throw new BaseRuntimeException("发送消息失败,请检查手机号准确性");
        }
        String pushNumber = equipmentWenTiOnlineAcceptFeedbackPushRecordDao.findFristPushNumber();
        Calendar calendar = Calendar.getInstance();
        String months = "";
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH) + 1;
        Integer monthlength = month.toString().length();
        if (monthlength == 1) {
            months = "0" + month.toString();
        }
        if (monthlength == 2) {
            months = month.toString();
        }
        if (pushNumber == null) {
            String onePushNumber = "GCFK-" + year + "-" + months + "-001";
            equipmentWenTiOnlineAcceptFeedbackPushRecordEntity.setPushnumber(onePushNumber);
        } else {//GCFK-2021-04-001
            String[] number_split = pushNumber.split("-");
            if (number_split[1].equals(year.toString()) && number_split[2].equals(months)) {
                Integer num = Integer.valueOf(number_split[3]) + 1;
                String onePushNumber = "GCFK-" + number_split[1] + "-" + number_split[2] + num;
                equipmentWenTiOnlineAcceptFeedbackPushRecordEntity.setPushnumber(onePushNumber);
            }
            if (number_split[1].equals(year.toString())) {
                String onePushNumber = "GCFK-" + number_split[1] + "-" + months + 001;
                equipmentWenTiOnlineAcceptFeedbackPushRecordEntity.setPushnumber(onePushNumber);
            } else {
                String onePushNumber = "GCFK-" + year + "-" + months + 001;
                equipmentWenTiOnlineAcceptFeedbackPushRecordEntity.setPushnumber(onePushNumber);
            }
        }
        equipmentWenTiOnlineAcceptFeedbackPushRecordDao.insertSelective(equipmentWenTiOnlineAcceptFeedbackPushRecordEntity);
    }

    @Override
    public EquipmentWenTiOnlineAcceptFeedbackPushRecordEntity findOneAcceptFeedbackById(Integer id) {
        return equipmentWenTiOnlineAcceptFeedbackPushRecordDao.findOneAcceptFeedbackById(id);
    }

    @Override
    public List<EquipmentWenTiReturnVisitCommentEntity> findCommentByFid(Integer fid) {
        return equipmentWenTiReturnVisitCommentDao.findCommentByFid(fid);
    }

    @Override
    public void addComment(EquipmentWenTiReturnVisitCommentEntity equipmentWenTiReturnVisitCommentEntity) {
        equipmentWenTiReturnVisitCommentDao.insertSelective(equipmentWenTiReturnVisitCommentEntity);
    }



    @Override
    public List<EquipmentWenTiOnlineAcceptFeedbackFileEntity> findFileByfid(Integer fid) {
        return equipmentWenTiOnlineAcceptFeedbackFileDao.findFileByfid(fid);
    }

    @Override
    public void editAcceptFeedbackByIdByUser(EquipmentWenTiOnlineAcceptFeedbackEntity equipmentWenTiOnlineAcceptFeedbackEntity,
                                             String fileName, String filePath, Integer longinUserid) {
        EquipmentWenTiOnlineAcceptFeedbackEntity feedbackEntity=equipmentWenTiOnlineAcceptFeedbackDao.selectByPrimaryKey(equipmentWenTiOnlineAcceptFeedbackEntity.getId());
        equipmentWenTiOnlineAcceptFeedbackDao.updateByPrimaryKeySelective(equipmentWenTiOnlineAcceptFeedbackEntity);
        String tljname = equipmentWenTiOnlineAcceptFeedbackEntity.getTljname();
        String dwdname = equipmentWenTiOnlineAcceptFeedbackEntity.getDwdname();
        String xdname = equipmentWenTiOnlineAcceptFeedbackEntity.getXdname();
        String feedbackusernamephone = equipmentWenTiOnlineAcceptFeedbackEntity.getFeedbackusernamephone();
        String wentitype = equipmentWenTiOnlineAcceptFeedbackEntity.getWentitype();
        String wentimiaoshu = equipmentWenTiOnlineAcceptFeedbackEntity.getWentimiaoshu();
        String replymessage = equipmentWenTiOnlineAcceptFeedbackEntity.getReplymessage();
        StringBuilder sb=new StringBuilder();
        if (!tljname.equals(feedbackEntity.getTljname())){
            sb.append(" 修改了铁路局 ");
        }
        if (!feedbackusernamephone.equals(feedbackEntity.getFeedbackusernamephone())){
            sb.append(" 修改了电话号码 ");
        }
        if (feedbackEntity.getDwdname()!=null){
            if (!dwdname.equals(feedbackEntity.getDwdname())){
                sb.append(" 修改了电务段 ");
            }
        }
        if (feedbackEntity.getXdname()!=null){
            if (!xdname.equals(feedbackEntity.getXdname())){
                sb.append(" 修改了线段 ");
            }
        }
        if (feedbackEntity.getWentitype()!=null){
            if (!wentitype.equals(feedbackEntity.getWentitype())){
                sb.append(" 修改了问题类型 ");
            }
        }
        if (feedbackEntity.getWentimiaoshu()!=null){
            if (!wentimiaoshu.equals(feedbackEntity.getWentimiaoshu())){
                sb.append(" 修改了问题描述 ");
            }
        }
        if (feedbackEntity.getReplymessage()!=null){
            if (!replymessage.equals(feedbackEntity.getReplymessage())){
                sb.append(" 修改了问题回复 ");
            }
        }
        if (sb.length()==0){
            sb.append("没做任何修改");
        }

        //新增记录
        EquipmentWenTiReturnVisitRecordmessageEntity recordmessageEntity = new EquipmentWenTiReturnVisitRecordmessageEntity();
        recordmessageEntity.setTypeid(equipmentWenTiOnlineAcceptFeedbackEntity.getId());
        recordmessageEntity.setOperatorname(equipmentWenTiOnlineAcceptFeedbackEntity.getUpdatename());
        recordmessageEntity.setOperatortime(new Date());
        recordmessageEntity.setTypenum(2);
        recordmessageEntity.setContext(equipmentWenTiOnlineAcceptFeedbackEntity.getUpdatename() + sb);
        equipmentWenTiReturnVisitRecordmessageDao.insertSelective(recordmessageEntity);

        //新增文件
        if (!"".equals(filePath) || !"".equals(fileName)) {
            String[] onefilePath = filePath.split(",");
            String[] onefileName = fileName.split(",");
            for (int i = 0; i < onefilePath.length; i++) {
                EquipmentWenTiOnlineAcceptFeedbackFileEntity fileEntity = new EquipmentWenTiOnlineAcceptFeedbackFileEntity();
                fileEntity.setFileName(onefileName[i]);
                fileEntity.setFilePath(onefilePath[i]);
                fileEntity.setFileType(1);
                fileEntity.setUid(longinUserid);
                fileEntity.setFid(equipmentWenTiOnlineAcceptFeedbackEntity.getId());
                fileEntity.setCreatetime(new Date());
                fileEntity.setCreatename(equipmentWenTiOnlineAcceptFeedbackEntity.getUpdatename());
                fileEntity.setUpdatetime(new Date());
                fileEntity.setUpdatename(equipmentWenTiOnlineAcceptFeedbackEntity.getUpdatename());
                equipmentWenTiOnlineAcceptFeedbackFileDao.insertSelective(fileEntity);
            }
        }
    }

    @Override
    public List<EquipmentWenTiOnlineAcceptFeedbackEntity> findAllAcceptFeedback(String number, String statrTime, String endTime,
                                                                                String tljName, String dwdName, String xdName,
                                                                                String feedbackName, String wentiType,
                                                                                String wentiMiaoshu, Integer feedbackState,
                                                                                Integer acceptUserid, Integer wentiState,
                                                                                Integer pushState) {
        return equipmentWenTiOnlineAcceptFeedbackDao.findAllAcceptFeedback(number,statrTime,endTime,
                tljName,dwdName,xdName,feedbackName,wentiType,wentiMiaoshu,feedbackState,acceptUserid,wentiState,pushState);
    }

    @Override
    public void acceptById(EquipmentWenTiOnlineAcceptFeedbackEntity equipmentWenTiOnlineAcceptFeedbackEntity) {
        equipmentWenTiOnlineAcceptFeedbackDao.updateByPrimaryKeySelective(equipmentWenTiOnlineAcceptFeedbackEntity);
        //新增记录
        EquipmentWenTiReturnVisitRecordmessageEntity recordmessageEntity = new EquipmentWenTiReturnVisitRecordmessageEntity();
        recordmessageEntity.setTypeid(equipmentWenTiOnlineAcceptFeedbackEntity.getId());
        recordmessageEntity.setOperatorname(equipmentWenTiOnlineAcceptFeedbackEntity.getCreatename());
        recordmessageEntity.setOperatortime(new Date());
        recordmessageEntity.setTypenum(2);
        recordmessageEntity.setContext(equipmentWenTiOnlineAcceptFeedbackEntity.getCreatename() + "受理了此问题反馈");
        equipmentWenTiReturnVisitRecordmessageDao.insertSelective(recordmessageEntity);
    }

    @Override
    public List<EquipmentWenTiOnlineAcceptFeedbackEntity> findAcceptFeedbackByphone(String phone) {
        return equipmentWenTiOnlineAcceptFeedbackDao.findAcceptFeedbackByphone(phone);
    }

    @Override
    public void deleteByIds(Integer[] ids) {
        for (Integer id : ids) {
            equipmentWenTiOnlineAcceptFeedbackDao.deleteByPrimaryKey(id);
        }
    }

    @Override
    public void editAcceptFeedbackById(EquipmentWenTiOnlineAcceptFeedbackEntity equipmentWenTiOnlineAcceptFeedbackEntity) {
        EquipmentWenTiOnlineAcceptFeedbackEntity feedbackEntity=equipmentWenTiOnlineAcceptFeedbackDao.selectByPrimaryKey(equipmentWenTiOnlineAcceptFeedbackEntity.getId());
        equipmentWenTiOnlineAcceptFeedbackDao.updateByPrimaryKeySelective(equipmentWenTiOnlineAcceptFeedbackEntity);
        String tljname = equipmentWenTiOnlineAcceptFeedbackEntity.getTljname();
        String dwdname = equipmentWenTiOnlineAcceptFeedbackEntity.getDwdname();
        String xdname = equipmentWenTiOnlineAcceptFeedbackEntity.getXdname();
        String feedbackusernamephone = equipmentWenTiOnlineAcceptFeedbackEntity.getFeedbackusernamephone();
        String wentitype = equipmentWenTiOnlineAcceptFeedbackEntity.getWentitype();
        String wentimiaoshu = equipmentWenTiOnlineAcceptFeedbackEntity.getWentimiaoshu();
        StringBuilder sb=new StringBuilder();
        if (!tljname.equals(feedbackEntity.getTljname())){
            sb.append(" 修改了铁路局 ");
        }
        if (!feedbackusernamephone.equals(feedbackEntity.getFeedbackusernamephone())){
            sb.append(" 修改了电话号码 ");
        }
        if (feedbackEntity.getDwdname()!=null){
            if (!dwdname.equals(feedbackEntity.getDwdname())){
                sb.append(" 修改了电务段 ");
            }
        }
        if (feedbackEntity.getXdname()!=null){
            if (!xdname.equals(feedbackEntity.getXdname())){
                sb.append(" 修改了线段 ");
            }
        }
        if (feedbackEntity.getWentitype()!=null){
            if (!wentitype.equals(feedbackEntity.getWentitype())){
                sb.append(" 修改了问题类型 ");
            }
        }
        if (feedbackEntity.getWentimiaoshu()!=null){
            if (!wentimiaoshu.equals(feedbackEntity.getWentimiaoshu())){
                sb.append(" 修改了问题描述 ");
            }
        }
        if (sb.length()==0){
            sb.append("没做任何修改");
        }

        //新增记录
        EquipmentWenTiReturnVisitRecordmessageEntity recordmessageEntity = new EquipmentWenTiReturnVisitRecordmessageEntity();
        recordmessageEntity.setTypeid(equipmentWenTiOnlineAcceptFeedbackEntity.getId());
        recordmessageEntity.setOperatorname(equipmentWenTiOnlineAcceptFeedbackEntity.getUpdatename());
        recordmessageEntity.setOperatortime(new Date());
        recordmessageEntity.setTypenum(2);
        recordmessageEntity.setContext(equipmentWenTiOnlineAcceptFeedbackEntity.getUpdatename() + sb);
        equipmentWenTiReturnVisitRecordmessageDao.insertSelective(recordmessageEntity);
    }

    @Override
    public String findNumber(String tljName) {
        String number = equipmentWenTiOnlineAcceptFeedbackDao.findNumber();
        String codeNumber = "";
        String name = "";
        if (number == null) {
            Calendar calendar = Calendar.getInstance();
            String months = "";
            Integer year = calendar.get(Calendar.YEAR);
            String years = year.toString().substring(2, 4);
            Integer month = calendar.get(Calendar.MONTH) + 1;
            Integer monthlength = month.toString().length();
            if (monthlength == 1) {
                months = "0" + month.toString();
            }
            if (monthlength == 2) {
                months = month.toString();
            }
            codeNumber = years + months + "01";
        } else {
            Calendar calendar = Calendar.getInstance();
            String months = "";
            Integer year = calendar.get(Calendar.YEAR);
            String years = year.toString().substring(2, 4);
            Integer month = calendar.get(Calendar.MONTH) + 1;
            Integer monthlength = month.toString().length();
            if (monthlength == 1) {
                months = "0" + month.toString();
            }
            if (monthlength == 2) {
                months = month.toString();
            }
            String lastNumber = number.substring(number.length() - 6);//编号的后六位210406
            String oneyear = lastNumber.substring(0, 2);//年
            String onemonth = lastNumber.substring(2, 4);//月
            String num = lastNumber.substring(4, 6);//序号
            if (years.equals(oneyear) && onemonth.equals(months)) {
                Integer onenum = Integer.parseInt(num) + 1;
                int length = onenum.toString().length();
                if (length==1){
                    codeNumber = years + months +"0"+ onenum;
                }else {
                    codeNumber = years + months + onenum;
                }
            }
            else {
                codeNumber = years + months + "01";
            }
        }
        if ("哈尔滨局".equals(tljName)){
            name="HEB";
        }
        if ("沈阳局".equals(tljName)){
            name="SY";
        }
        if ("北京局".equals(tljName)){
            name="BJ";
        }
        if ("呼和局".equals(tljName)){
            name="HH";
        }
        if ("西安局".equals(tljName)){
            name="XA";
        }
        if ("兰州局".equals(tljName)){
            name="LZ";
        }
        if ("成都局".equals(tljName)){
            name="CD";
        }
        if ("太原局".equals(tljName)){
            name="TY";
        }
        if ("郑州局".equals(tljName)){
            name="ZZ";
        }
        if ("武汉局".equals(tljName)){
            name="WH";
        }
        if ("上海局".equals(tljName)){
            name="SH";
        }
        if ("南昌局".equals(tljName)){
            name="NC";
        }
        if ("广州局".equals(tljName)){
            name="GZ";
        }
        if ("济南局".equals(tljName)){
            name="JN";
        }
        if ("南宁局".equals(tljName)){
            name="NN";
        }
        if ("昆明局".equals(tljName)){
            name="KM";
        }
        if ("乌鲁木齐局".equals(tljName)){
            name="MLMQ";
        }
        if ("青藏公司".equals(tljName)){
            name="QZ";
        }
        String Number=name+codeNumber;
        return Number;
    }

    @Override
    public List<TieLuJuEntity> findAllTieLuJu() {
        return tieLuJuDao.findAllTieLuJu();
    }

    @Override
    public List<XianDuanEntity> findAllXianDuan() {
        return xianDuanDao.findAllXD();
    }

    @Override
    public List<DianWuDuanEntity> findAllDianWuDuan() {
        return dianWuDuanDao.findDianWuDuan();
    }

    @Override
    public void addAcceptFeedback(EquipmentWenTiOnlineAcceptFeedbackEntity equipmentWenTiOnlineAcceptFeedbackEntity) {
        equipmentWenTiOnlineAcceptFeedbackEntity.setFeedbacktime(new Date());
        equipmentWenTiOnlineAcceptFeedbackEntity.setFeedbackstate(0);
        equipmentWenTiOnlineAcceptFeedbackEntity.setWentistate(0);
        equipmentWenTiOnlineAcceptFeedbackEntity.setPushstate(0);
        equipmentWenTiOnlineAcceptFeedbackDao.insertSelective(equipmentWenTiOnlineAcceptFeedbackEntity);
        Integer id = equipmentWenTiOnlineAcceptFeedbackEntity.getId();
        //新增记录
        EquipmentWenTiReturnVisitRecordmessageEntity recordmessageEntity = new EquipmentWenTiReturnVisitRecordmessageEntity();
        recordmessageEntity.setTypeid(id);
        recordmessageEntity.setOperatorname(equipmentWenTiOnlineAcceptFeedbackEntity.getCreatename());
        recordmessageEntity.setOperatortime(new Date());
        recordmessageEntity.setTypenum(2);
        recordmessageEntity.setContext(equipmentWenTiOnlineAcceptFeedbackEntity.getCreatename() + "新增了一条回访任务");
        equipmentWenTiReturnVisitRecordmessageDao.insertSelective(recordmessageEntity);
    }

    @Override
    public void deleteWenTiTypeByIds(Integer[] ids) {
        for (Integer id : ids) {
            equipmentWenTiCustomerWenTiTypeDao.deleteByPrimaryKey(id);
        }
    }

    @Override
    public List<EquipmentWenTiCustomerWenTiTypeEntity> findAllWenTiType() {
        return equipmentWenTiCustomerWenTiTypeDao.findAllWenTiType();
    }

    @Override
    public void editWenTiTypeById(EquipmentWenTiCustomerWenTiTypeEntity equipmentWenTiCustomerWenTiTypeEntity) {
        equipmentWenTiCustomerWenTiTypeDao.updateByPrimaryKeySelective(equipmentWenTiCustomerWenTiTypeEntity);
    }

    @Override
    public void addWenTiType(EquipmentWenTiCustomerWenTiTypeEntity equipmentWenTiCustomerWenTiTypeEntity) {
        equipmentWenTiCustomerWenTiTypeDao.insertSelective(equipmentWenTiCustomerWenTiTypeEntity);
    }
}
