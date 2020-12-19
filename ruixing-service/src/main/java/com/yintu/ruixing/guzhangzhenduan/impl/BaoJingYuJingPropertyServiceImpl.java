package com.yintu.ruixing.guzhangzhenduan.impl;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.StringUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
import com.yintu.ruixing.master.guzhangzhenduan.*;
import com.yintu.ruixing.slave.guzhangzhenduan.QuDuanInfoDaoV2;
import com.yintu.ruixing.yuanchengzhichi.AlarmEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author:lcy
 * @date:2020-06-17 10
 */
@Service
@Transactional
public class BaoJingYuJingPropertyServiceImpl implements BaoJingYuJingPropertyService {
    @Autowired
    private BaoJingYuJingPropertyDao baoJingYuJingPropertyDao;
    @Autowired
    private BaoJingYuJingDao baoJingYuJingDao;
    @Autowired
    private QuDuanBaseDao quDuanBaseDao;
    @Autowired
    private AlarmTableDao alarmTableDao;
    @Autowired
    private CheZhanDao cheZhanDao;
    @Autowired
    private BaoJingYuJingBaseDao baoJingYuJingBaseDao;
    @Autowired
    private SkylightTimeDao skylightTimeDao;
    @Autowired
    private QuDuanInfoDaoV2 quDuanInfoDaoV2;

    @Override
    public List<AlarmsEntity> findSomeAlarmDatasByChoose(Date starTime, Date endTime, Integer dwdid, Integer xdid, Integer czid, Integer page, Integer size) {
        List<String> times = new ArrayList<>();
        Page<AlarmsEntity> alarmsEntityList = new Page<>();
        if (dwdid != null && xdid == null && czid == null) {
            Long startimee = null;
            Long endtimee = null;
            if (starTime != null && endTime != null) {
                startimee = starTime.getTime() / 1000;
                endtimee = endTime.getTime() / 1000;
            }
            PageHelper.startPage(page, size);
            Page<AlarmEntity> alarmTableEntityList = (Page<AlarmEntity>) alarmTableDao.findAllAlarmDatasBySomethings(dwdid, startimee, endtimee);
            //List<AlarmEntity> alarmTableEntityList =  alarmTableDao.findAllAlarmDatasBySomethings(dwdid, startimee, endtimee);
            if (alarmTableEntityList != null) {
                alarmsEntityList.setTotal(alarmTableEntityList.getTotal());
                for (AlarmEntity alarmTableEntity : alarmTableEntityList) {
                    AlarmsEntity alarmsEntity = new AlarmsEntity();
                    Integer stationId = alarmTableEntity.getStationId();//车站id
                    Integer sectionId = alarmTableEntity.getSectionId();//区段id
                    Integer alarmcode = alarmTableEntity.getAlarmcode();//报警预警
                    Integer alarmlevel = alarmTableEntity.getAlarmlevel();//报警预警类别
                    Integer createtime = alarmTableEntity.getCreatetime();//开始时间
                    Integer recoverTime = alarmTableEntity.getRecoverTime();//结束时间
                    Integer czNumber = cheZhanDao.findCzNumber(stationId);
                    String czname = cheZhanDao.findczName(stationId);

                    String quduanNmae = null;
                    String alarmContext = null;
                    Integer isnotsky = null;
                    Integer bjyjType = null;
                    if (czNumber > 0) {//通信编码电路
                        //查询区段名
                        quduanNmae = quDuanBaseDao.findQuduanName(stationId, sectionId);
                        if (alarmlevel == 1) {//报警信息
                            bjyjType = 2;
                            alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//报警内容
                        } else if (alarmlevel == 2) {//预警信息
                            bjyjType = 3;
                            alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//预警内容
                        }
                        SkylightTimeEntity skylightTimeEntity = skylightTimeDao.findSkyLight(stationId, sectionId);
                        if (skylightTimeEntity == null) {//没有查询到天窗
                            isnotsky = 0;
                        } else if (skylightTimeEntity != null) {//查询到天窗
                            Integer endtime = new Long(skylightTimeEntity.getEndTime().getTime()).intValue();
                            if (endtime < createtime) {//天窗结束时间小于报警开始时间  天窗已经关闭
                                isnotsky = 0;
                            } else {//天窗开启
                                isnotsky = 1;
                            }
                        }
                    } else if (czNumber == 0) {//继电编码电路
                        //查询区段名
                        quduanNmae = quDuanBaseDao.findQuduanName(stationId, sectionId);
                        if (alarmlevel == 1) {//报警信息
                            bjyjType = 1;
                            alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//报警内容
                        } else if (alarmlevel == 2) {//预警信息
                            bjyjType = 3;
                            alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//预警内容
                        }
                        SkylightTimeEntity skylightTimeEntity = skylightTimeDao.findSkyLight(stationId, sectionId);
                        if (skylightTimeEntity == null) {//没有查询到天窗
                            isnotsky = 0;
                        } else if (skylightTimeEntity != null) {//查询到天窗
                            Integer endtime = new Long(skylightTimeEntity.getEndTime().getTime()).intValue();
                            if (endtime < createtime) {//天窗结束时间小于报警开始时间  天窗已经关闭
                                isnotsky = 0;
                            } else {//天窗开启
                                isnotsky = 1;
                            }
                        }
                    }
                    Date parse = null;
                    Date recoverparse = null;
                    Date date = new Date(createtime * 1000L);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    try {
                        parse = sdf.parse(sdf.format(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (recoverTime != 0) {
                        Date recoverdate = new Date(recoverTime * 1000L);
                        try {
                            recoverparse = sdf.parse(sdf.format(recoverdate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    alarmsEntity.setBjyjType(bjyjType);
                    alarmsEntity.setBjyjcontext(alarmContext);
                    alarmsEntity.setQuduan(quduanNmae);
                    alarmsEntity.setIsnotsky(isnotsky);
                    alarmsEntity.setOpentime(parse);
                    alarmsEntity.setEndtime(recoverparse);
                    alarmsEntity.setCzid(stationId);
                    alarmsEntity.setQuid(sectionId);
                    alarmsEntity.setCzName(czname);
                    alarmsEntityList.add(alarmsEntity);
                }
            } else {
                return new ArrayList<>();
            }
        }

        if (dwdid != null && xdid != null && czid == null) {
            Long startimee = null;
            Long endtimee = null;
            if (starTime != null && endTime != null) {
                startimee = starTime.getTime() / 1000;
                endtimee = endTime.getTime() / 1000;
            }
            PageHelper.startPage(page, size);
            Page<AlarmEntity> alarmTableEntityList = (Page<AlarmEntity>) alarmTableDao.findAllAlarmDatasByDwdidAndXdid(dwdid, xdid, startimee, endtimee);
            // List<AlarmEntity> alarmTableEntityList =  alarmTableDao.findAllAlarmDatasByDwdidAndXdid(dwdid, xdid, startimee, endtimee);
            if (!alarmTableEntityList.isEmpty()) {
                alarmsEntityList.setTotal(alarmTableEntityList.getTotal());
                for (AlarmEntity alarmTableEntity : alarmTableEntityList) {
                    AlarmsEntity alarmsEntity = new AlarmsEntity();
                    Integer stationId = alarmTableEntity.getStationId();//车站id
                    Integer sectionId = alarmTableEntity.getSectionId();//区段id
                    Integer alarmcode = alarmTableEntity.getAlarmcode();//报警预警
                    Integer alarmlevel = alarmTableEntity.getAlarmlevel();//报警预警类别
                    Integer createtime = alarmTableEntity.getCreatetime();//开始时间
                    Integer recoverTime = alarmTableEntity.getRecoverTime();
                    Integer czNumber = cheZhanDao.findCzNumber(stationId);
                    String czname = cheZhanDao.findczName(stationId);

                    String quduanNmae = null;
                    String alarmContext = null;
                    Integer isnotsky = null;
                    Integer bjyjType = null;
                    if (czNumber > 0) {//通信编码电路
                        //查询区段名
                        quduanNmae = quDuanBaseDao.findQuduanName(stationId, sectionId);
                        if (alarmlevel == 1) {//报警信息
                            bjyjType = 2;
                            alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//报警内容
                        } else if (alarmlevel == 2) {//预警信息
                            bjyjType = 3;
                            alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//预警内容
                        }
                        SkylightTimeEntity skylightTimeEntity = skylightTimeDao.findSkyLight(stationId, sectionId);
                        if (skylightTimeEntity == null) {//没有查询到天窗
                            isnotsky = 0;
                        } else if (skylightTimeEntity != null) {//查询到天窗
                            Integer endtime = new Long(skylightTimeEntity.getEndTime().getTime()).intValue();
                            if (endtime < createtime) {//天窗结束时间小于报警开始时间  天窗已经关闭
                                isnotsky = 0;
                            } else {//天窗开启
                                isnotsky = 1;
                            }
                        }

                    } else if (czNumber == 0) {//继电编码电路
                        //查询区段名
                        quduanNmae = quDuanBaseDao.findQuduanName(stationId, sectionId);
                        if (alarmlevel == 1) {//报警信息
                            bjyjType = 1;
                            alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//报警内容
                        } else if (alarmlevel == 2) {//预警信息
                            bjyjType = 3;
                            alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//预警内容
                        }
                        SkylightTimeEntity skylightTimeEntity = skylightTimeDao.findSkyLight(stationId, sectionId);
                        if (skylightTimeEntity == null) {//没有查询到天窗
                            isnotsky = 0;
                        } else if (skylightTimeEntity != null) {//查询到天窗
                            Integer endtime = new Long(skylightTimeEntity.getEndTime().getTime()).intValue();
                            if (endtime < createtime) {//天窗结束时间小于报警开始时间  天窗已经关闭
                                isnotsky = 0;
                            } else {//天窗开启
                                isnotsky = 1;
                            }
                        }
                    }
                    Date parse = null;
                    Date recoverparse = null;
                    Date date = new Date(createtime * 1000L);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    try {
                        parse = sdf.parse(sdf.format(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (recoverTime != 0) {
                        Date recoverdate = new Date(recoverTime * 1000L);
                        try {
                            recoverparse = sdf.parse(sdf.format(recoverdate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    alarmsEntity.setBjyjType(bjyjType);
                    alarmsEntity.setBjyjcontext(alarmContext);
                    alarmsEntity.setQuduan(quduanNmae);
                    alarmsEntity.setIsnotsky(isnotsky);
                    alarmsEntity.setOpentime(parse);
                    alarmsEntity.setEndtime(recoverparse);
                    alarmsEntity.setCzid(stationId);
                    alarmsEntity.setQuid(sectionId);
                    alarmsEntity.setCzName(czname);
                    alarmsEntityList.add(alarmsEntity);
                }
            } else {
                return new ArrayList<>();
            }

        }


        if (dwdid != null && xdid != null && czid != null) {
            Long startimee = null;
            Long endtimee = null;
            if (starTime != null && endTime != null) {
                startimee = starTime.getTime() / 1000;
                endtimee = endTime.getTime() / 1000;
            }
            PageHelper.startPage(page, size);
            Page<AlarmEntity> alarmTableEntityList = (Page<AlarmEntity>) alarmTableDao.findAllAlarmDatasByczid(czid, startimee, endtimee);
            // List<AlarmEntity> alarmTableEntityList =  alarmTableDao.findAllAlarmDatasByczid(czid, startimee, endtimee);
            if (!alarmTableEntityList.isEmpty()) {
                alarmsEntityList.setTotal(alarmTableEntityList.getTotal());
                for (AlarmEntity alarmTableEntity : alarmTableEntityList) {
                    AlarmsEntity alarmsEntity = new AlarmsEntity();

                    Integer stationId = alarmTableEntity.getStationId();//车站id
                    Integer sectionId = alarmTableEntity.getSectionId();//区段id
                    Integer alarmcode = alarmTableEntity.getAlarmcode();//报警预警
                    Integer alarmlevel = alarmTableEntity.getAlarmlevel();//报警预警类别
                    Integer createtime = alarmTableEntity.getCreatetime();//开始时间
                    Integer recoverTime = alarmTableEntity.getRecoverTime();//结束时间
                    Integer czNumber = cheZhanDao.findCzNumber(stationId);
                    String czname = cheZhanDao.findczName(stationId);

                    String quduanNmae = null;
                    String alarmContext = null;
                    Integer isnotsky = null;
                    Integer bjyjType = null;
                    if (czNumber > 0) {//通信编码电路
                        //查询区段名
                        quduanNmae = quDuanBaseDao.findQuduanName(stationId, sectionId);
                        if (alarmlevel == 1) {//报警信息
                            bjyjType = 2;
                            alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//报警内容
                        } else if (alarmlevel == 2) {//预警信息
                            bjyjType = 3;
                            alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//预警内容
                        }
                        SkylightTimeEntity skylightTimeEntity = skylightTimeDao.findSkyLight(stationId, sectionId);
                        if (skylightTimeEntity == null) {//没有查询到天窗
                            isnotsky = 0;
                        } else if (skylightTimeEntity != null) {//查询到天窗
                            Integer endtime = new Long(skylightTimeEntity.getEndTime().getTime()).intValue();
                            if (endtime < createtime) {//天窗结束时间小于报警开始时间  天窗已经关闭
                                isnotsky = 0;
                            } else {//天窗开启
                                isnotsky = 1;
                            }
                        }
                    } else if (czNumber == 0) {//继电编码电路
                        //查询区段名
                        quduanNmae = quDuanBaseDao.findQuduanName(stationId, sectionId);
                        if (alarmlevel == 1) {//报警信息
                            bjyjType = 1;
                            alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//报警内容
                        } else if (alarmlevel == 2) {//预警信息
                            bjyjType = 3;
                            alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//预警内容
                        }
                        SkylightTimeEntity skylightTimeEntity = skylightTimeDao.findSkyLight(stationId, sectionId);
                        if (skylightTimeEntity == null) {//没有查询到天窗
                            isnotsky = 0;
                        } else if (skylightTimeEntity != null) {//查询到天窗
                            Integer endtime = new Long(skylightTimeEntity.getEndTime().getTime()).intValue();
                            if (endtime < createtime) {//天窗结束时间小于报警开始时间  天窗已经关闭
                                isnotsky = 0;
                            } else {//天窗开启
                                isnotsky = 1;
                            }
                        }
                    }
                    Date parse = null;
                    Date recoverparse = null;
                    Date date = new Date(createtime * 1000L);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    try {
                        parse = sdf.parse(sdf.format(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (recoverTime != 0) {
                        Date recoverdate = new Date(recoverTime * 1000L);
                        try {
                            recoverparse = sdf.parse(sdf.format(recoverdate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    alarmsEntity.setBjyjType(bjyjType);
                    alarmsEntity.setBjyjcontext(alarmContext);
                    alarmsEntity.setQuduan(quduanNmae);
                    alarmsEntity.setIsnotsky(isnotsky);
                    alarmsEntity.setOpentime(parse);
                    alarmsEntity.setEndtime(recoverparse);
                    alarmsEntity.setCzid(stationId);
                    alarmsEntity.setQuid(sectionId);
                    alarmsEntity.setCzName(czname);
                    alarmsEntityList.add(alarmsEntity);
                }
            } else {
                return new ArrayList<>();
            }

        }
        return alarmsEntityList;
    }


    @Override
    public List<AlarmsEntity> findAllHistoryAlarmDatas(Integer page, Integer size) {
        Page<AlarmsEntity> alarmsEntityList = new Page<>();
        Page<AlarmEntity> alarmTableEntityList = (Page<AlarmEntity>) alarmTableDao.findAllAlarmDatas();
        alarmsEntityList.setTotal(alarmTableEntityList.getTotal());
        for (AlarmEntity alarmTableEntity : alarmTableEntityList) {
            AlarmsEntity alarmsEntity = new AlarmsEntity();

            Integer stationId = alarmTableEntity.getStationId();//车站id
            Integer sectionId = alarmTableEntity.getSectionId();//区段id
            Integer alarmcode = alarmTableEntity.getAlarmcode();//报警预警
            Integer alarmlevel = alarmTableEntity.getAlarmlevel();//报警预警类别
            Integer createtime = alarmTableEntity.getCreatetime();//开始时间
            Integer recoverTime = alarmTableEntity.getRecoverTime();//结束时间
            Integer czNumber = cheZhanDao.findCzNumber(stationId);
            String czname = cheZhanDao.findczName(stationId);

            String quduanNmae = null;
            String alarmContext = null;
            Integer isnotsky = null;
            Integer bjyjType = null;
            if (czNumber > 0) {//通信编码电路
                //查询区段名
                quduanNmae = quDuanBaseDao.findQuduanName(stationId, sectionId);
                if (alarmlevel == 1) {//报警信息
                    bjyjType = 2;
                    alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//报警内容
                } else if (alarmlevel == 2) {//预警信息
                    bjyjType = 3;
                    alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//预警内容
                }
                SkylightTimeEntity skylightTimeEntity = skylightTimeDao.findSkyLight(stationId, sectionId);
                if (skylightTimeEntity == null) {//没有查询到天窗
                    isnotsky = 0;
                } else if (skylightTimeEntity != null) {//查询到天窗
                    Integer endtime = new Long(skylightTimeEntity.getEndTime().getTime()).intValue();
                    if (endtime < createtime) {//天窗结束时间小于报警开始时间  天窗已经关闭
                        isnotsky = 0;
                    } else {//天窗开启
                        isnotsky = 1;
                    }
                }

            } else if (czNumber == 0) {//继电编码电路
                //查询区段名
                quduanNmae = quDuanBaseDao.findQuduanName(stationId, sectionId);
                if (alarmlevel == 1) {//报警信息
                    bjyjType = 1;
                    alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//报警内容
                } else if (alarmlevel == 2) {//预警信息
                    bjyjType = 3;
                    alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//预警内容
                }
                SkylightTimeEntity skylightTimeEntity = skylightTimeDao.findSkyLight(stationId, sectionId);
                if (skylightTimeEntity == null) {//没有查询到天窗
                    isnotsky = 0;
                } else if (skylightTimeEntity != null) {//查询到天窗
                    Integer endtime = new Long(skylightTimeEntity.getEndTime().getTime()).intValue();
                    if (endtime < createtime) {//天窗结束时间小于报警开始时间  天窗已经关闭
                        isnotsky = 0;
                    } else {//天窗开启
                        isnotsky = 1;
                    }
                }
            }
            Date parse = null;
            Date recoverparse = null;
            Date date = new Date(createtime * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            try {
                parse = sdf.parse(sdf.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (recoverTime != 0) {
                Date recoverdate = new Date(recoverTime * 1000L);
                try {
                    recoverparse = sdf.parse(sdf.format(recoverdate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            alarmsEntity.setBjyjType(bjyjType);
            alarmsEntity.setBjyjcontext(alarmContext);
            alarmsEntity.setQuduan(quduanNmae);
            alarmsEntity.setIsnotsky(isnotsky);
            alarmsEntity.setOpentime(parse);
            alarmsEntity.setEndtime(recoverparse);
            alarmsEntity.setCzid(stationId);
            alarmsEntity.setQuid(sectionId);
            alarmsEntity.setCzName(czname);
            alarmsEntityList.add(alarmsEntity);
        }
        return alarmsEntityList;
    }


    @Override
    public List<AlarmsEntity> findAllNotReadAlarmDatasByCZid(Integer page, Integer size, Integer czid) {
        Date dayTime = new Date();
        String tableName = StringUtil.getBaoJingYuJingTableName(czid, dayTime);
        PageHelper.startPage(page, size);
        Page<AlarmsEntity> alarmsEntityList = new Page<>();
        Page<AlarmEntity> alarmTableEntityList = (Page<AlarmEntity>) alarmTableDao.findAllNotReadAlarmDatasByCZid(czid);
        alarmsEntityList.setTotal(alarmTableEntityList.getTotal());
        for (AlarmEntity alarmTableEntity : alarmTableEntityList) {
            AlarmsEntity alarmsEntity = new AlarmsEntity();

            Integer stationId = alarmTableEntity.getStationId();//车站id
            Integer sectionId = alarmTableEntity.getSectionId();//区段id
            Integer alarmcode = alarmTableEntity.getAlarmcode();//报警预警
            Integer alarmlevel = alarmTableEntity.getAlarmlevel();//报警预警类别
            Integer createtime = alarmTableEntity.getCreatetime();//开始时间
            Integer recoverTime = alarmTableEntity.getRecoverTime();//结束时间
            Integer czNumber = cheZhanDao.findCzNumber(stationId);
            String czname = cheZhanDao.findczName(stationId);

            String quduanNmae = null;
            String alarmContext = null;
            Integer isnotsky = null;
            Integer bjyjType = null;
            if (czNumber > 0) {//通信编码电路
                //查询区段名
                quduanNmae = quDuanBaseDao.findQuduanName(stationId, sectionId);
                if (alarmlevel == 1) {//报警信息
                    bjyjType = 2;
                    alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//报警内容
                } else if (alarmlevel == 2) {//预警信息
                    bjyjType = 3;
                    alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//预警内容
                }
                SkylightTimeEntity skylightTimeEntity = skylightTimeDao.findSkyLight(stationId, sectionId);
                if (skylightTimeEntity == null) {//没有查询到天窗
                    isnotsky = 0;
                } else if (skylightTimeEntity != null) {//查询到天窗
                    Integer endtime = new Long(skylightTimeEntity.getEndTime().getTime()).intValue();
                    if (endtime < createtime) {//天窗结束时间小于报警开始时间  天窗已经关闭
                        isnotsky = 0;
                    } else {//天窗开启
                        isnotsky = 1;
                    }
                }

            } else if (czNumber == 0) {//继电编码电路
                //查询区段名
                quduanNmae = quDuanBaseDao.findQuduanName(stationId, sectionId);
                if (alarmlevel == 1) {//报警信息
                    bjyjType = 1;
                    alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//报警内容
                } else if (alarmlevel == 2) {//预警信息
                    bjyjType = 3;
                    alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//预警内容
                }
                SkylightTimeEntity skylightTimeEntity = skylightTimeDao.findSkyLight(stationId, sectionId);
                if (skylightTimeEntity == null) {//没有查询到天窗
                    isnotsky = 0;
                } else if (skylightTimeEntity != null) {//查询到天窗
                    Integer endtime = new Long(skylightTimeEntity.getEndTime().getTime()).intValue();
                    if (endtime < createtime) {//天窗结束时间小于报警开始时间  天窗已经关闭
                        isnotsky = 0;
                    } else {//天窗开启
                        isnotsky = 1;
                    }
                }
            }
            Date parse = null;
            Date recoverparse = null;
            Date date = new Date(createtime * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            try {
                parse = sdf.parse(sdf.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (recoverTime != 0) {
                Date recoverdate = new Date(recoverTime * 1000L);
                try {
                    recoverparse = sdf.parse(sdf.format(recoverdate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            alarmsEntity.setBjyjType(bjyjType);
            alarmsEntity.setBjyjcontext(alarmContext);
            alarmsEntity.setQuduan(quduanNmae);
            alarmsEntity.setIsnotsky(isnotsky);
            alarmsEntity.setOpentime(parse);
            alarmsEntity.setEndtime(recoverparse);
            alarmsEntity.setCzid(stationId);
            alarmsEntity.setQuid(sectionId);
            alarmsEntity.setCzName(czname);
            alarmsEntityList.add(alarmsEntity);
        }
        return alarmsEntityList;
    }

    @Override
    public List<AlarmsEntity> findNotRecoveryAlarmDatas(Integer page, Integer size) {
        List<AlarmsEntity> alarmsEntityList=new ArrayList<>();
        List<AlarmEntity> alarmEntityList=alarmTableDao.findNotRecoveryAlarmDatas();
        for (AlarmEntity alarmTableEntity : alarmEntityList) {
            AlarmsEntity alarmsEntity = new AlarmsEntity();

            Integer stationId = alarmTableEntity.getStationId();//车站id
            Integer sectionId = alarmTableEntity.getSectionId();//区段id
            Integer alarmcode = alarmTableEntity.getAlarmcode();//报警预警
            Integer alarmlevel = alarmTableEntity.getAlarmlevel();//报警预警类别
            Integer createtime = alarmTableEntity.getCreatetime();//开始时间
            Integer recoverTime = alarmTableEntity.getRecoverTime();//恢复时间
            Integer czNumber = cheZhanDao.findCzNumber(stationId);
            String czname = cheZhanDao.findczName(stationId);

            String quduanNmae = null;
            String alarmContext = null;
            Integer isnotsky = null;
            Integer bjyjType = null;
            if (czNumber > 0) {//通信编码电路
                //查询区段名
                quduanNmae = quDuanBaseDao.findQuduanName(stationId, sectionId);
                if (alarmlevel == 1) {//报警信息
                    bjyjType = 2;
                    alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//报警内容
                } else if (alarmlevel == 2) {//预警信息
                    bjyjType = 3;
                    alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//预警内容
                }
                SkylightTimeEntity skylightTimeEntity = skylightTimeDao.findSkyLight(stationId, sectionId);
                if (skylightTimeEntity == null) {//没有查询到天窗
                    isnotsky = 0;
                } else if (skylightTimeEntity != null) {//查询到天窗
                    Integer endtime = new Long(skylightTimeEntity.getEndTime().getTime()).intValue();
                    if (endtime < createtime) {//天窗结束时间小于报警开始时间  天窗已经关闭
                        isnotsky = 0;
                    } else {//天窗开启
                        isnotsky = 1;
                    }
                }

            } else if (czNumber == 0) {//继电编码电路
                //查询区段名
                quduanNmae = quDuanBaseDao.findQuduanName(stationId, sectionId);
                if (alarmlevel == 1) {//报警信息
                    bjyjType = 1;
                    alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//报警内容
                } else if (alarmlevel == 2) {//预警信息
                    bjyjType = 3;
                    alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//预警内容
                }
                SkylightTimeEntity skylightTimeEntity = skylightTimeDao.findSkyLight(stationId, sectionId);
                if (skylightTimeEntity == null) {//没有查询到天窗
                    isnotsky = 0;
                } else if (skylightTimeEntity != null) {//查询到天窗
                    Integer endtime = new Long(skylightTimeEntity.getEndTime().getTime()).intValue();
                    if (endtime < createtime) {//天窗结束时间小于报警开始时间  天窗已经关闭
                        isnotsky = 0;
                    } else {//天窗开启
                        isnotsky = 1;
                    }
                }
            }
            Date parse = null;
            Date recoverparse = null;
            Date date = new Date(createtime * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            try {
                parse = sdf.parse(sdf.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (recoverTime != 0) {
                Date recoverdate = new Date(recoverTime * 1000L);
                try {
                    recoverparse = sdf.parse(sdf.format(recoverdate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            alarmsEntity.setBjyjType(bjyjType);
            alarmsEntity.setBjyjcontext(alarmContext);
            alarmsEntity.setQuduan(quduanNmae);
            alarmsEntity.setIsnotsky(isnotsky);
            alarmsEntity.setOpentime(parse);
            alarmsEntity.setEndtime(recoverparse);
            alarmsEntity.setCzid(stationId);
            alarmsEntity.setQuid(sectionId);
            alarmsEntity.setCzName(czname);
            alarmsEntityList.add(alarmsEntity);
        }
        return alarmsEntityList;
    }

    @Override
    public List<AlarmsEntity> findAllNotReadAlarmDatas(Integer page, Integer size) {
       /* String databaseName = "ruixing";
        String tableName = "alarm" + "_%" ;
        List<String> cztables = new ArrayList<>();
        List<String> tables = alarmDao.selectLikeTable(databaseName, tableName);
        for (String table : tables) {
            cztables.add(table);
        }
        StringBuilder sb = new StringBuilder();
        for (String table : cztables) {
            sb.append("SELECT * FROM (SELECT * FROM ").append(table).append(" ORDER BY id DESC) AS ").append(table).append(" UNION ALL ");
        }*/
        PageHelper.startPage(page, size);
        Page<AlarmsEntity> alarmsEntityList = new Page<>();
        Page<AlarmEntity> alarmTableEntityList = (Page<AlarmEntity>) alarmTableDao.findAllNotReadAlarmDatas();
        alarmsEntityList.setTotal(alarmTableEntityList.getTotal());
        for (AlarmEntity alarmTableEntity : alarmTableEntityList) {
            AlarmsEntity alarmsEntity = new AlarmsEntity();

            Integer stationId = alarmTableEntity.getStationId();//车站id
            Integer sectionId = alarmTableEntity.getSectionId();//区段id
            Integer alarmcode = alarmTableEntity.getAlarmcode();//报警预警
            Integer alarmlevel = alarmTableEntity.getAlarmlevel();//报警预警类别
            Integer createtime = alarmTableEntity.getCreatetime();//开始时间
            Integer recoverTime = alarmTableEntity.getRecoverTime();//恢复时间
            Integer czNumber = cheZhanDao.findCzNumber(stationId);
            String czname = cheZhanDao.findczName(stationId);

            String quduanNmae = null;
            String alarmContext = null;
            Integer isnotsky = null;
            Integer bjyjType = null;
            if (czNumber > 0) {//通信编码电路
                //查询区段名
                quduanNmae = quDuanBaseDao.findQuduanName(stationId, sectionId);
                if (alarmlevel == 1) {//报警信息
                    bjyjType = 2;
                    alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//报警内容
                } else if (alarmlevel == 2) {//预警信息
                    bjyjType = 3;
                    alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//预警内容
                }
                SkylightTimeEntity skylightTimeEntity = skylightTimeDao.findSkyLight(stationId, sectionId);
                if (skylightTimeEntity == null) {//没有查询到天窗
                    isnotsky = 0;
                } else if (skylightTimeEntity != null) {//查询到天窗
                    Integer endtime = new Long(skylightTimeEntity.getEndTime().getTime()).intValue();
                    if (endtime < createtime) {//天窗结束时间小于报警开始时间  天窗已经关闭
                        isnotsky = 0;
                    } else {//天窗开启
                        isnotsky = 1;
                    }
                }

            } else if (czNumber == 0) {//继电编码电路
                //查询区段名
                quduanNmae = quDuanBaseDao.findQuduanName(stationId, sectionId);
                if (alarmlevel == 1) {//报警信息
                    bjyjType = 1;
                    alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//报警内容
                } else if (alarmlevel == 2) {//预警信息
                    bjyjType = 3;
                    alarmContext = baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);//预警内容
                }
                SkylightTimeEntity skylightTimeEntity = skylightTimeDao.findSkyLight(stationId, sectionId);
                if (skylightTimeEntity == null) {//没有查询到天窗
                    isnotsky = 0;
                } else if (skylightTimeEntity != null) {//查询到天窗
                    Integer endtime = new Long(skylightTimeEntity.getEndTime().getTime()).intValue();
                    if (endtime < createtime) {//天窗结束时间小于报警开始时间  天窗已经关闭
                        isnotsky = 0;
                    } else {//天窗开启
                        isnotsky = 1;
                    }
                }
            }
            Date parse = null;
            Date recoverparse = null;
            Date date = new Date(createtime * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            try {
                parse = sdf.parse(sdf.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (recoverTime != 0) {
                Date recoverdate = new Date(recoverTime * 1000L);
                try {
                    recoverparse = sdf.parse(sdf.format(recoverdate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            alarmsEntity.setBjyjType(bjyjType);
            alarmsEntity.setBjyjcontext(alarmContext);
            alarmsEntity.setQuduan(quduanNmae);
            alarmsEntity.setIsnotsky(isnotsky);
            alarmsEntity.setOpentime(parse);
            alarmsEntity.setEndtime(recoverparse);
            alarmsEntity.setCzid(stationId);
            alarmsEntity.setQuid(sectionId);
            alarmsEntity.setCzName(czname);
            alarmsEntityList.add(alarmsEntity);
        }
        return alarmsEntityList;

    }

    @Override
    public void editAlarmState(AlarmEntity alarmTableEntity) {
        boolean status = true;
       /* alarmTableEntity.setStatus(1);
        String databaseName = "ruixing";
        String tableName = "alarm" + "_%" ;
        List<String> tables = alarmDao.selectLikeTable(databaseName, tableName);
        for (String table : tables) {
            alarmTableDao.editAlarmState(status, table);
        }*/


        alarmTableDao.editAlarmState(status);
    }


    @Override
    public Integer findAllAlarmNumberByXDid(Integer dwdid, Integer xdid) {

      /*  String databaseName = "ruixing";
        List<String> cztables = new ArrayList<>();
        List<Integer> czidlist = new ArrayList<>();
        //查询此电务段下面所有的车站
        List<CheZhanEntity> cheZhanEntityList = cheZhanDao.findczidBydwdNamexdName(dwdid, xdid);
        for (CheZhanEntity cheZhanEntity : cheZhanEntityList) {
            Integer czId = new Long(cheZhanEntity.getCzId()).intValue();
            czidlist.add(czId);
        }
        for (Integer czidone : czidlist) {
            List<String> tables = alarmDao.selectLikeTable(databaseName, "alarm\\_" + czidone + "\\_%");
            for (String table : tables) {
                cztables.add(table);
            }
        }
        StringBuilder sb = new StringBuilder();
        if (cztables.size() != 0) {
            for (String table : cztables) {
                sb.append("SELECT * FROM  ").append(table).append(" UNION ALL ");
            }
            return alarmTableDao.findAllAlarmNumber();
        } else {
            Integer alarmNum = 0;
            return alarmNum;
        }*/
        return alarmTableDao.findAllAlarmNumberByDwdidAndXid(dwdid, xdid);
    }

    @Override
    public Integer findAllAlarmNumberByDWDid(Integer dwdid) {

      /*  String databaseName = "ruixing";
        List<String> cztables = new ArrayList<>();
        List<Integer> czidlist = new ArrayList<>();
        //查询此电务段下面所有的车站
        List<CheZhanEntity> cheZhanEntityList = cheZhanDao.findczidBydwdName(dwdid);
        for (CheZhanEntity cheZhanEntity : cheZhanEntityList) {
            Integer czId = new Long(cheZhanEntity.getCzId()).intValue();
            czidlist.add(czId);
        }
        for (Integer czidone : czidlist) {
            List<String> tables = alarmDao.selectLikeTable(databaseName, "alarm\\_" + czidone + "\\_%");
            for (String table : tables) {
                cztables.add(table);
            }
        }
        StringBuilder sb = new StringBuilder();
        if (cztables.size() != 0) {
            for (String table : cztables) {
                sb.append("SELECT * FROM  ").append(table).append(" UNION ALL ");
            }
            return alarmTableDao.findAllAlarmNumber();
        } else {
            Integer alarmNum = 0;
            return alarmNum;
        }*/

        return alarmTableDao.findAllAlarmNumberByDwdid(dwdid);
    }


    @Override
    public Integer findAllAlarmNumber() {
        /*String databaseName = "ruixing";
        String tableName = "alarm" + "_%";
        List<String> cztables = new ArrayList<>();
        List<String> tables = alarmDao.selectLikeTable(databaseName, tableName);
        for (String table : tables) {
            cztables.add(table);
        }
        StringBuilder sb = new StringBuilder();
        if (cztables.size() != 0) {
            for (String table : cztables) {
                sb.append("SELECT * FROM  ").append(table).append(" UNION ALL ");
            }
            return alarmTableDao.findAllAlarmNumber(sb.toString());
        } else {
            Integer alarmNum = 0;
            return alarmNum;
        }*/
        return alarmTableDao.findAllAlarmNumber();
    }

    @Override
    public Integer findAlarmNumber(Integer czid) {
       /* if (quDuanInfoDaoV2.isTableExist(tableName) == 1) {
            return alarmTableDao.findAlarmNumber(tableName);
        } else {
            return 0;
        }*/
        return alarmTableDao.findAlarmNumber(czid);
    }


    @Override
    public List<TreeNodeUtil> findBaoJingYuJingTree(Integer parentId) {
        List<BaoJingYuJingPropertyEntity> baoJingYuJingPropertyEntities = baoJingYuJingPropertyDao.findBaoJingYuJingTree(parentId);
        List<TreeNodeUtil> treeNodeUtils = new ArrayList<>();
        for (BaoJingYuJingPropertyEntity baoJingYuJingPropertyEntity : baoJingYuJingPropertyEntities) {
            TreeNodeUtil treeNodeUtil = new TreeNodeUtil();
            treeNodeUtil.setId((long) baoJingYuJingPropertyEntity.getId());
            treeNodeUtil.setLabel(baoJingYuJingPropertyEntity.getName());
            treeNodeUtil.setChildren(this.findBaoJingYuJingTree(baoJingYuJingPropertyEntity.getId()));
            treeNodeUtils.add(treeNodeUtil);

        }
        return treeNodeUtils;
    }

    @Override
    public List<BaoJingYuJingEntity> findAllYuJingBaoJing(Integer page, Integer size) {
        return baoJingYuJingDao.findAllYuJingBaoJing();
    }

    @Override
    public List<BaoJingYuJingEntity> findYuJingBaoJingBySouSuo(Integer[] ids, Integer sid, Integer qid,
                                                               Date startTime, Date huifuTime, Integer tianChuang,
                                                               Integer lvChuHuiFu, Integer lvChuKaiTong, Integer page, Integer size) {
        return baoJingYuJingDao.findYuJingBaoJingBySouSuo(ids, sid, qid, startTime, huifuTime, tianChuang, lvChuHuiFu, lvChuKaiTong);
    }

    @Override
    public List<QuDuanBaseEntity> findAllQuDuan() {
        // List<QuDuanBaseEntity> list1=null;
        List<QuDuanBaseEntity> list = quDuanBaseDao.findAllQuDuanName();

        return list;
    }
}
