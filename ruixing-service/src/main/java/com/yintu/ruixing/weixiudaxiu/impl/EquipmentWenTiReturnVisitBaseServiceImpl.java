package com.yintu.ruixing.weixiudaxiu.impl;

import com.yintu.ruixing.master.weixiudaxiu.EquipmentWenTiReturnVisitBaseDao;
import com.yintu.ruixing.master.weixiudaxiu.EquipmentWenTiReturnVisitDao;
import com.yintu.ruixing.master.weixiudaxiu.EquipmentWenTiReturnVisitRecordmessageDao;
import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiReturnVisitBaseEntity;
import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiReturnVisitBaseService;
import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiReturnVisitEntity;
import com.yintu.ruixing.weixiudaxiu.EquipmentWenTiReturnVisitRecordmessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2021/4/22 15:29
 * @Version 1.0
 * 需求:
 */
@Service
@Transactional
public class EquipmentWenTiReturnVisitBaseServiceImpl implements EquipmentWenTiReturnVisitBaseService {
    @Autowired
    private EquipmentWenTiReturnVisitBaseDao equipmentWenTiReturnVisitBaseDao;
    @Autowired
    private EquipmentWenTiReturnVisitRecordmessageDao equipmentWenTiReturnVisitRecordmessageDao;
    @Autowired
    private EquipmentWenTiReturnVisitDao equipmentWenTiReturnVisitDao;


    @Override
    public void findRenWuByNowYear(String today) {
        List<EquipmentWenTiReturnVisitBaseEntity> BaseEntityList = equipmentWenTiReturnVisitBaseDao.findRenWuByNowYear(today);
        if (BaseEntityList.size() != 0) {
            for (EquipmentWenTiReturnVisitBaseEntity baseEntity : BaseEntityList) {
                //新增一条回访任务（主页面）
                EquipmentWenTiReturnVisitEntity visitEntity = new EquipmentWenTiReturnVisitEntity();
                visitEntity.setRenwubaseid(baseEntity.getId());
                visitEntity.setRenwunumber(baseEntity.getRenwunumber());
                visitEntity.setTljname(baseEntity.getTljname());
                visitEntity.setDwdname(baseEntity.getDwdname());
                visitEntity.setXdname(baseEntity.getXdname());
                visitEntity.setReturnuserid(baseEntity.getReturnuserid());
                visitEntity.setReturnedusername(baseEntity.getReturnedusername());
                visitEntity.setReturnedusernamephone(baseEntity.getReturnedusernamephone());
                visitEntity.setRenwustate(0);
                visitEntity.setImplementstate(0);
                visitEntity.setWentistate(0);
                visitEntity.setPushstate(0);
                visitEntity.setEditState(1);
                visitEntity.setBegintime(new Date());
                visitEntity.setReturncycletype(baseEntity.getReturncycletype());
                visitEntity.setCreatetime(new Date());
                visitEntity.setCreatename("系统");
                visitEntity.setUpdatetime(new Date());
                visitEntity.setUpdatename("系统");
                Date renwustarttime = baseEntity.getRenwustarttime();
                Integer years = renwustarttime.getYear() + 1900;
                visitEntity.setYears(years.toString());
                if (baseEntity.getReturncycletype() == 4) {//年任务
                    visitEntity.setTypenumber(1);
                }
                equipmentWenTiReturnVisitDao.insertSelective(visitEntity);
            }
        }
        //改变所有上年的任务不可编辑
        Date nowday = new Date();
        Integer year = nowday.getYear() + 1900;
        Integer oneyear = year - 1;
        equipmentWenTiReturnVisitDao.editStateByYearAndDatas(oneyear.toString(), 1);
    }

    @Override
    public void findRenWuByNowQuarter(String today) {
        List<EquipmentWenTiReturnVisitBaseEntity> BaseEntityList = equipmentWenTiReturnVisitBaseDao.findRenWuByNowQuarter(today);
        if (BaseEntityList.size() != 0) {
            for (EquipmentWenTiReturnVisitBaseEntity baseEntity : BaseEntityList) {
                //新增一条回访任务（主页面）
                EquipmentWenTiReturnVisitEntity visitEntity = new EquipmentWenTiReturnVisitEntity();
                visitEntity.setRenwubaseid(baseEntity.getId());
                visitEntity.setRenwunumber(baseEntity.getRenwunumber());
                visitEntity.setTljname(baseEntity.getTljname());
                visitEntity.setDwdname(baseEntity.getDwdname());
                visitEntity.setXdname(baseEntity.getXdname());
                visitEntity.setReturnuserid(baseEntity.getReturnuserid());
                visitEntity.setReturnedusername(baseEntity.getReturnedusername());
                visitEntity.setReturnedusernamephone(baseEntity.getReturnedusernamephone());
                visitEntity.setRenwustate(0);
                visitEntity.setImplementstate(0);
                visitEntity.setWentistate(0);
                visitEntity.setPushstate(0);
                visitEntity.setEditState(1);
                visitEntity.setBegintime(new Date());
                visitEntity.setReturncycletype(baseEntity.getReturncycletype());
                visitEntity.setCreatetime(new Date());
                visitEntity.setCreatename("系统");
                visitEntity.setUpdatetime(new Date());
                visitEntity.setUpdatename("系统");
                Date renwustarttime = baseEntity.getRenwustarttime();
                Integer years = renwustarttime.getYear() + 1900;
                visitEntity.setYears(years.toString());
                if (baseEntity.getReturncycletype() == 3) {//季度任务
                    Integer quarter = 0;
                    Integer month = renwustarttime.getMonth() + 1;
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
                    visitEntity.setTypenumber(quarter);
                }
                equipmentWenTiReturnVisitDao.insertSelective(visitEntity);
            }
        }
        //改变所有上季度的任务不可编辑
        Date nowday = new Date();
        Integer year = nowday.getYear() + 1900;
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(new Date());
        Integer month = calendar.get(Calendar.MONTH) + 1;
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
        if (quarter == 1) {
            Integer oneyear = year - 1;
            equipmentWenTiReturnVisitDao.editStateByYearAndDatas(oneyear.toString(), 4);
        } else {
            equipmentWenTiReturnVisitDao.editStateByYearAndDatas(year.toString(), quarter);
        }
    }

    @Override
    public void findRenWuByNowMonth(String today) {
        List<EquipmentWenTiReturnVisitBaseEntity> BaseEntityList = equipmentWenTiReturnVisitBaseDao.findRenWuByNowMonth(today);
        if (BaseEntityList.size() != 0) {
            for (EquipmentWenTiReturnVisitBaseEntity baseEntity : BaseEntityList) {
                //新增一条回访任务（主页面）
                EquipmentWenTiReturnVisitEntity visitEntity = new EquipmentWenTiReturnVisitEntity();
                visitEntity.setRenwubaseid(baseEntity.getId());
                visitEntity.setRenwunumber(baseEntity.getRenwunumber());
                visitEntity.setTljname(baseEntity.getTljname());
                visitEntity.setDwdname(baseEntity.getDwdname());
                visitEntity.setXdname(baseEntity.getXdname());
                visitEntity.setReturnuserid(baseEntity.getReturnuserid());
                visitEntity.setReturnedusername(baseEntity.getReturnedusername());
                visitEntity.setReturnedusernamephone(baseEntity.getReturnedusernamephone());
                visitEntity.setRenwustate(0);
                visitEntity.setImplementstate(0);
                visitEntity.setWentistate(0);
                visitEntity.setPushstate(0);
                visitEntity.setEditState(1);
                visitEntity.setBegintime(new Date());
                visitEntity.setReturncycletype(baseEntity.getReturncycletype());
                visitEntity.setCreatetime(new Date());
                visitEntity.setCreatename("系统");
                visitEntity.setUpdatetime(new Date());
                visitEntity.setUpdatename("系统");
                Date renwustarttime = baseEntity.getRenwustarttime();
                Integer years = renwustarttime.getYear() + 1900;
                visitEntity.setYears(years.toString());

                if (baseEntity.getReturncycletype() == 2) {//月任务
                    Integer month = renwustarttime.getMonth() + 1;
                    visitEntity.setTypenumber(month);
                }
                equipmentWenTiReturnVisitDao.insertSelective(visitEntity);
            }
        }
        //改变所有上月的任务不可编辑
        Date nowday = new Date();
        Integer year = nowday.getYear() + 1900;
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(new Date());
        Integer month = calendar.get(Calendar.MONTH) + 1;
        if (month == 1) {
            Integer oneyear = year - 1;
            equipmentWenTiReturnVisitDao.editStateByYearAndDatas(oneyear.toString(), 12);
        } else {
            equipmentWenTiReturnVisitDao.editStateByYearAndDatas(year.toString(), month);
        }
    }

    @Override
    public void findRenWuByNowTime(String today) {
        List<EquipmentWenTiReturnVisitBaseEntity> BaseEntityList = equipmentWenTiReturnVisitBaseDao.findRenWuByNowTime(today);
        if (BaseEntityList.size() != 0) {
            for (EquipmentWenTiReturnVisitBaseEntity baseEntity : BaseEntityList) {
                //新增一条回访任务（主页面）
                EquipmentWenTiReturnVisitEntity visitEntity = new EquipmentWenTiReturnVisitEntity();
                visitEntity.setRenwubaseid(baseEntity.getId());
                visitEntity.setRenwunumber(baseEntity.getRenwunumber());
                visitEntity.setTljname(baseEntity.getTljname());
                visitEntity.setDwdname(baseEntity.getDwdname());
                visitEntity.setXdname(baseEntity.getXdname());
                visitEntity.setReturnuserid(baseEntity.getReturnuserid());
                visitEntity.setReturnedusername(baseEntity.getReturnedusername());
                visitEntity.setReturnedusernamephone(baseEntity.getReturnedusernamephone());
                visitEntity.setRenwustate(0);
                visitEntity.setImplementstate(0);
                visitEntity.setWentistate(0);
                visitEntity.setPushstate(0);
                visitEntity.setEditState(1);
                visitEntity.setBegintime(new Date());
                visitEntity.setReturncycletype(baseEntity.getReturncycletype());
                visitEntity.setCreatetime(new Date());
                visitEntity.setCreatename("系统");
                visitEntity.setUpdatetime(new Date());
                visitEntity.setUpdatename("系统");
                Date renwustarttime = baseEntity.getRenwustarttime();
                Integer years = renwustarttime.getYear() + 1900;
                visitEntity.setYears(years.toString());
                if (baseEntity.getReturncycletype() == 1) {//周任务
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setFirstDayOfWeek(Calendar.MONDAY);
                    calendar1.setTime(new Date());
                    visitEntity.setTypenumber(calendar1.get(Calendar.WEEK_OF_YEAR));
                }
                equipmentWenTiReturnVisitDao.insertSelective(visitEntity);
            }
        }
        //改变所有上周的任务不可编辑
        Date nowday = new Date();
        Integer year = nowday.getYear() + 1900;
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(new Date());
        Integer week = calendar.get(Calendar.WEEK_OF_YEAR);
        if (week == 1) {
            Integer oneyear = year - 1;
            equipmentWenTiReturnVisitDao.editStateByYearAndDatas(oneyear.toString(), 52);
        } else {
            equipmentWenTiReturnVisitDao.editStateByYearAndDatas(year.toString(), week);
        }
    }

    @Override
    public void editImplementstateById(EquipmentWenTiReturnVisitBaseEntity equipmentWenTiReturnVisitBaseEntity) {
        equipmentWenTiReturnVisitBaseDao.updateByPrimaryKeySelective(equipmentWenTiReturnVisitBaseEntity);
    }


    @Override
    public List<EquipmentWenTiReturnVisitBaseEntity> findAllReturnVisitRenWu(String tljName, String dwdName, String xdName,
                                                                             Integer returnuserid, Integer returncycletype,
                                                                             String statrTime, String endTime, Integer implementstate) {
        List<EquipmentWenTiReturnVisitBaseEntity> baseEntityList = equipmentWenTiReturnVisitBaseDao.findAllReturnVisitRenWu(tljName, dwdName, xdName,
                returnuserid, returncycletype,
                statrTime, endTime, implementstate);
        return baseEntityList;
    }

    @Override
    public void editReturnVisitRenWuById(EquipmentWenTiReturnVisitBaseEntity equipmentWenTiReturnVisitBaseEntity) {
        Integer id = equipmentWenTiReturnVisitBaseEntity.getId();
        EquipmentWenTiReturnVisitBaseEntity baseEntity = equipmentWenTiReturnVisitBaseDao.selectByPrimaryKey(id);
        equipmentWenTiReturnVisitBaseDao.updateByPrimaryKeySelective(equipmentWenTiReturnVisitBaseEntity);
        StringBuilder sb = new StringBuilder();
        String tljname = baseEntity.getTljname();
        String dwdname = baseEntity.getDwdname();
        String xdname = baseEntity.getXdname();
        Integer returnuserid = baseEntity.getReturnuserid();
        String returnedusername = baseEntity.getReturnedusername();
        String returnedusernamephone = baseEntity.getReturnedusernamephone();
        Integer returncycletype = baseEntity.getReturncycletype();
        Date renwustarttime = baseEntity.getRenwustarttime();
        Date rewuendtime = baseEntity.getRewuendtime();
        if (!tljname.equals(equipmentWenTiReturnVisitBaseEntity.getTljname())) {
            sb.append(" 修改了铁路局 ");
        }
        if (!dwdname.equals(equipmentWenTiReturnVisitBaseEntity.getDwdname())) {
            sb.append(" 修改了电务段 ");
        }
        if (!xdname.equals(equipmentWenTiReturnVisitBaseEntity.getXdname())) {
            sb.append(" 修改了电务段 ");
        }
        if (returnuserid != equipmentWenTiReturnVisitBaseEntity.getReturnuserid()) {
            sb.append(" 修改了回访人 ");
        }
        if (!returnedusername.equals(equipmentWenTiReturnVisitBaseEntity.getReturnedusername())) {
            sb.append(" 修改了被回访人 ");
        }
        if (!returnedusernamephone.equals(equipmentWenTiReturnVisitBaseEntity.getReturnedusernamephone())) {
            sb.append(" 修改了被回访人的电话 ");
        }
        if (returncycletype != equipmentWenTiReturnVisitBaseEntity.getReturncycletype()) {
            sb.append(" 修改了回访周期类型 ");
        }
        if (renwustarttime.getTime() != equipmentWenTiReturnVisitBaseEntity.getRenwustarttime().getTime()) {
            sb.append(" 修改了回访开始时间 ");
        }
        if (rewuendtime.getTime() != equipmentWenTiReturnVisitBaseEntity.getRewuendtime().getTime()) {
            sb.append(" 修改了回访结束时间");
        }
        if (sb.length() == 0) {
            sb.append("没有做任何修改");
        }
        //新增记录
        EquipmentWenTiReturnVisitRecordmessageEntity recordmessageEntity = new EquipmentWenTiReturnVisitRecordmessageEntity();
        recordmessageEntity.setTypeid(equipmentWenTiReturnVisitBaseEntity.getId());
        recordmessageEntity.setOperatorname(equipmentWenTiReturnVisitBaseEntity.getUpdatename());
        recordmessageEntity.setOperatortime(new Date());
        recordmessageEntity.setTypenum(1);
        recordmessageEntity.setContext(equipmentWenTiReturnVisitBaseEntity.getUpdatename() + sb);
        equipmentWenTiReturnVisitRecordmessageDao.insertSelective(recordmessageEntity);

    }

    @Override
    public void deleteReturnVisitRenWuByIds(Integer[] ids) {
        for (Integer id : ids) {
            equipmentWenTiReturnVisitBaseDao.deleteByPrimaryKey(id);
        }
    }

    @Override
    public List<EquipmentWenTiReturnVisitRecordmessageEntity> findRecordById(Integer id) {
        return equipmentWenTiReturnVisitRecordmessageDao.findRecordById(id);
    }

    @Override
    public void addReturnVisitRenWu(EquipmentWenTiReturnVisitBaseEntity equipmentWenTiReturnVisitBaseEntity) {
        //新增回访任务
        equipmentWenTiReturnVisitBaseEntity.setImplementstate(0);
        equipmentWenTiReturnVisitBaseDao.insertSelective(equipmentWenTiReturnVisitBaseEntity);
        Integer id = equipmentWenTiReturnVisitBaseEntity.getId();
        //新增记录
        EquipmentWenTiReturnVisitRecordmessageEntity recordmessageEntity = new EquipmentWenTiReturnVisitRecordmessageEntity();
        recordmessageEntity.setTypeid(id);
        recordmessageEntity.setOperatorname(equipmentWenTiReturnVisitBaseEntity.getCreatename());
        recordmessageEntity.setOperatortime(new Date());
        recordmessageEntity.setTypenum(1);
        recordmessageEntity.setContext(equipmentWenTiReturnVisitBaseEntity.getCreatename() + "新增了一条回访任务");
        equipmentWenTiReturnVisitRecordmessageDao.insertSelective(recordmessageEntity);

        //新增一条回访任务（主页面）
        EquipmentWenTiReturnVisitEntity visitEntity = new EquipmentWenTiReturnVisitEntity();
        visitEntity.setRenwubaseid(id);
        visitEntity.setRenwunumber(equipmentWenTiReturnVisitBaseEntity.getRenwunumber());
        visitEntity.setTljname(equipmentWenTiReturnVisitBaseEntity.getTljname());
        visitEntity.setDwdname(equipmentWenTiReturnVisitBaseEntity.getDwdname());
        visitEntity.setXdname(equipmentWenTiReturnVisitBaseEntity.getXdname());
        visitEntity.setReturnuserid(equipmentWenTiReturnVisitBaseEntity.getReturnuserid());
        visitEntity.setReturnedusername(equipmentWenTiReturnVisitBaseEntity.getReturnedusername());
        visitEntity.setReturnedusernamephone(equipmentWenTiReturnVisitBaseEntity.getReturnedusernamephone());
        visitEntity.setRenwustate(0);
        visitEntity.setImplementstate(0);
        visitEntity.setWentistate(0);
        visitEntity.setPushstate(0);
        visitEntity.setEditState(1);
        visitEntity.setBegintime(equipmentWenTiReturnVisitBaseEntity.getRenwustarttime());
        visitEntity.setReturncycletype(equipmentWenTiReturnVisitBaseEntity.getReturncycletype());
        visitEntity.setCreatetime(new Date());
        visitEntity.setCreatename(equipmentWenTiReturnVisitBaseEntity.getCreatename());
        visitEntity.setUpdatetime(new Date());
        visitEntity.setUpdatename(equipmentWenTiReturnVisitBaseEntity.getCreatename());
        Date renwustarttime = equipmentWenTiReturnVisitBaseEntity.getRenwustarttime();
        Integer years = renwustarttime.getYear() + 1900;
        visitEntity.setYears(years.toString());
        if (equipmentWenTiReturnVisitBaseEntity.getReturncycletype() == 1) {//周任务
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setFirstDayOfWeek(Calendar.MONDAY); //美国是以周日为每周的第一天 现把周一设成第一天
            calendar1.setTime(renwustarttime);
            visitEntity.setTypenumber(calendar1.get(Calendar.WEEK_OF_YEAR));
        }
        if (equipmentWenTiReturnVisitBaseEntity.getReturncycletype() == 2) {//月任务
            Integer month = renwustarttime.getMonth() + 1;
            visitEntity.setTypenumber(month);
        }
        if (equipmentWenTiReturnVisitBaseEntity.getReturncycletype() == 3) {//季度任务
            Integer quarter = 0;
            Integer month = renwustarttime.getMonth() + 1;
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
            visitEntity.setTypenumber(quarter);
        }
        if (equipmentWenTiReturnVisitBaseEntity.getReturncycletype() == 4) {//年任务
            visitEntity.setTypenumber(1);
        }
        equipmentWenTiReturnVisitDao.insertSelective(visitEntity);

    }

    @Override
    public String findRenWuNumber(Integer returncycletype) {
        String renWuNumber = "";
        if (returncycletype == 1) {
            String oneRenwuNumber = equipmentWenTiReturnVisitBaseDao.findRenWuNumberByType(returncycletype);
            if (oneRenwuNumber == null) {
                renWuNumber = "W001";
            } else {
                String substring = oneRenwuNumber.substring(1, 4);
                Integer i = Integer.parseInt(substring) + 1;
                int length = i.toString().length();
                if (length == 1) {
                    renWuNumber = "W00" + i.toString();
                }
                if (length == 2) {
                    renWuNumber = "W0" + i.toString();
                }
                if (length == 3) {
                    renWuNumber = "W" + i.toString();
                }
            }
        }
        if (returncycletype == 2) {
            String oneRenwuNumber = equipmentWenTiReturnVisitBaseDao.findRenWuNumberByType(returncycletype);
            if (oneRenwuNumber == null) {
                renWuNumber = "M001";
            } else {
                String substring = oneRenwuNumber.substring(1, 4);
                Integer i = Integer.parseInt(substring) + 1;
                int length = i.toString().length();
                if (length == 1) {
                    renWuNumber = "M00" + i.toString();
                }
                if (length == 2) {
                    renWuNumber = "M0" + i.toString();
                }
                if (length == 2) {
                    renWuNumber = "M" + i.toString();
                }
            }
        }
        if (returncycletype == 3) {
            String oneRenwuNumber = equipmentWenTiReturnVisitBaseDao.findRenWuNumberByType(returncycletype);
            if (oneRenwuNumber == null) {
                renWuNumber = "Q001";
            } else {
                String substring = oneRenwuNumber.substring(1, 4);
                Integer i = Integer.parseInt(substring) + 1;
                int length = i.toString().length();
                if (length == 1) {
                    renWuNumber = "Q00" + i.toString();
                }
                if (length == 2) {
                    renWuNumber = "Q0" + i.toString();
                }
                if (length == 3) {
                    renWuNumber = "Q" + i.toString();
                }
            }
        }
        if (returncycletype == 4) {
            String oneRenwuNumber = equipmentWenTiReturnVisitBaseDao.findRenWuNumberByType(returncycletype);
            if (oneRenwuNumber == null) {
                renWuNumber = "Y001";
            } else {
                String substring = oneRenwuNumber.substring(1, 4);
                Integer i = Integer.parseInt(substring) + 1;
                int length = i.toString().length();
                if (length == 1) {
                    renWuNumber = "Y00" + i.toString();
                }
                if (length == 2) {
                    renWuNumber = "Y0" + i.toString();
                }
                if (length == 3) {
                    renWuNumber = "Y" + i.toString();
                }
            }
        }
        return renWuNumber;
    }
}
