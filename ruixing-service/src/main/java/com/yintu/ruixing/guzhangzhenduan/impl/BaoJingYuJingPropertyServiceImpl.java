package com.yintu.ruixing.guzhangzhenduan.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.system.SystemUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.StringUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
import com.yintu.ruixing.yuanchengzhichi.RemoteSupportAlarmDao;
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
    @Autowired
    private RemoteSupportAlarmDao alarmDao;

    @Override
    public List<AlarmEntity> findSomeAlarmDatasByChoose(Date starTime, Date endTime, Integer dwdid, Integer xdid, Integer czid, Integer page, Integer size) {
        List<String> times = new ArrayList<>();
        Page<AlarmEntity> alarmEntityList = new Page<>();
        String databaseName = "ruixing";
        boolean isTime = false;
        if (starTime != null && endTime != null) { //判断时间

            if (starTime.after(endTime))
                throw new BaseRuntimeException("开始时间不能大于结束日期");
            long diffMonth = DateUtil.betweenMonth(starTime, endTime, true);
            Date diffTime = starTime;
            for (long i = 0; i < diffMonth + 1L; i++) {
                int month = DateUtil.month(diffTime) + 1;
                times.add(DateUtil.year(diffTime) + "" + (month > 9 ? month : "0" + month));
                diffTime = DateUtil.offsetMonth(diffTime, 1);
            }
            isTime = true;
        }

        if (dwdid != null && xdid == null && czid == null) {
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
            if (!isTime) {
                for (String table : cztables) {
                    sb.append("SELECT * FROM (SELECT * FROM ").append(table).append(" ORDER BY id DESC) AS ").append(table).append(" UNION ALL ");
                }
            } else {//对比时间减少拼接sql
                for (String time : times) {
                    for (String table : cztables) {
                        for (Integer czidone : czidlist) {
                            if (("alarm_" + czidone + "_" + time).equals(table)) {
                                sb.append("SELECT * FROM (SELECT * FROM ").append(table).append(" ORDER BY id DESC) AS ").append(table).append(" UNION ALL ");
                                break;
                            }
                        }
                    }
                }
            }
            if (!"".equals(sb.toString())) {
                PageHelper.startPage(page, size);
                Page<AlarmTableEntity> alarmTableEntityList = (Page<AlarmTableEntity>) alarmTableDao.findAllAlarmDatasBySomethings(sb.toString(), starTime, endTime);
                alarmEntityList.setTotal(alarmTableEntityList.getTotal());
                for (AlarmTableEntity alarmTableEntity : alarmTableEntityList) {
                    AlarmEntity alarmEntity = new AlarmEntity();

                    Integer stationId = alarmTableEntity.getStationId();//车站id
                    Integer sectionId = alarmTableEntity.getSectionId();//区段id
                    Integer alarmcode = alarmTableEntity.getAlarmcode();//报警预警
                    Integer alarmlevel = alarmTableEntity.getAlarmlevel();//报警预警类别
                    Integer createtime = alarmTableEntity.getCreatetime();//开始时间
                    Integer czNumber = cheZhanDao.findCzNumber(stationId);

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
                    Date date = new Date(createtime * 1000L);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    try {
                        parse = sdf.parse(sdf.format(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    alarmEntity.setBjyjType(bjyjType);
                    alarmEntity.setBjyjcontext(alarmContext);
                    alarmEntity.setQuduan(quduanNmae);
                    alarmEntity.setIsnotsky(isnotsky);
                    alarmEntity.setOpentime(parse);
                    alarmEntityList.add(alarmEntity);
                }
            }

        }

        if (dwdid != null && xdid != null && czid == null) {
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
            if (!isTime) {
                for (String table : cztables) {
                    sb.append("SELECT * FROM (SELECT * FROM ").append(table).append(" ORDER BY id DESC) AS ").append(table).append(" UNION ALL ");
                }
            } else {//对比时间减少拼接sql
                for (String time : times) {
                    for (String table : cztables) {
                        for (Integer czidone : czidlist) {
                            if (("alarm_" + czidone + "_" + time).equals(table)) {
                                sb.append("SELECT * FROM (SELECT * FROM ").append(table).append(" ORDER BY id DESC) AS ").append(table).append(" UNION ALL ");
                                break;
                            }
                        }
                    }
                }
            }
            if (!"".equals(sb.toString())) {
                PageHelper.startPage(page, size);
                Page<AlarmTableEntity> alarmTableEntityList = (Page<AlarmTableEntity>) alarmTableDao.findAllAlarmDatasBySomethings(sb.toString(), starTime, endTime);
                alarmEntityList.setTotal(alarmTableEntityList.getTotal());


                for (AlarmTableEntity alarmTableEntity : alarmTableEntityList) {
                    AlarmEntity alarmEntity = new AlarmEntity();

                    Integer stationId = alarmTableEntity.getStationId();//车站id
                    Integer sectionId = alarmTableEntity.getSectionId();//区段id
                    Integer alarmcode = alarmTableEntity.getAlarmcode();//报警预警
                    Integer alarmlevel = alarmTableEntity.getAlarmlevel();//报警预警类别
                    Integer createtime = alarmTableEntity.getCreatetime();//开始时间
                    Integer czNumber = cheZhanDao.findCzNumber(stationId);

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
                    Date date = new Date(createtime * 1000L);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    try {
                        parse = sdf.parse(sdf.format(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    alarmEntity.setBjyjType(bjyjType);
                    alarmEntity.setBjyjcontext(alarmContext);
                    alarmEntity.setQuduan(quduanNmae);
                    alarmEntity.setIsnotsky(isnotsky);
                    alarmEntity.setOpentime(parse);
                    alarmEntityList.add(alarmEntity);
                }
            }
        }


        if (dwdid != null && xdid != null && czid != null) {


            List<String> cztables = new ArrayList<>();
            List<Integer> czidlist = new ArrayList<>();
            //查询此电务段下面所有的车站
            Integer czId = cheZhanDao.findCzid(czid);

            List<String> tables = alarmDao.selectLikeTable(databaseName, "alarm\\_" + czId + "\\_%");
            for (String table : tables) {
                cztables.add(table);
            }

            StringBuilder sb = new StringBuilder();
            if (!isTime) {
                for (String table : cztables) {
                    sb.append("SELECT * FROM (SELECT * FROM ").append(table).append(" ORDER BY id DESC) AS ").append(table).append(" UNION ALL ");
                }
            } else {//对比时间减少拼接sql
                for (String time : times) {
                    for (String table : cztables) {
                        for (Integer czidone : czidlist) {
                            if (("alarm_" + czidone + "_" + time).equals(table)) {
                                sb.append("SELECT * FROM (SELECT * FROM ").append(table).append(" ORDER BY id DESC) AS ").append(table).append(" UNION ALL ");
                                break;
                            }
                        }
                    }
                }
            }


            if (!"".equals(sb.toString())) {
                PageHelper.startPage(page, size);
                Page<AlarmTableEntity> alarmTableEntityList = (Page<AlarmTableEntity>) alarmTableDao.findAllAlarmDatasBySomethings(sb.toString(), starTime, endTime);
                alarmEntityList.setTotal(alarmTableEntityList.getTotal());

                for (AlarmTableEntity alarmTableEntity : alarmTableEntityList) {
                    AlarmEntity alarmEntity = new AlarmEntity();

                    Integer stationId = alarmTableEntity.getStationId();//车站id
                    Integer sectionId = alarmTableEntity.getSectionId();//区段id
                    Integer alarmcode = alarmTableEntity.getAlarmcode();//报警预警
                    Integer alarmlevel = alarmTableEntity.getAlarmlevel();//报警预警类别
                    Integer createtime = alarmTableEntity.getCreatetime();//开始时间
                    Integer czNumber = cheZhanDao.findCzNumber(stationId);

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
                    Date date = new Date(createtime * 1000L);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    try {
                        parse = sdf.parse(sdf.format(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    alarmEntity.setBjyjType(bjyjType);
                    alarmEntity.setBjyjcontext(alarmContext);
                    alarmEntity.setQuduan(quduanNmae);
                    alarmEntity.setIsnotsky(isnotsky);
                    alarmEntity.setOpentime(parse);
                    alarmEntityList.add(alarmEntity);
                }
            }
        }


        return alarmEntityList;
    }


    @Override
    public List<AlarmEntity> findAllHistoryAlarmDatas(Integer page, Integer size, String tableName) {
        Page<AlarmEntity> alarmEntityList = new Page<>();
        Page<AlarmTableEntity> alarmTableEntityList = (Page<AlarmTableEntity>) alarmTableDao.findAllAlarmDatas(tableName);
        alarmEntityList.setTotal(alarmTableEntityList.getTotal());
        for (AlarmTableEntity alarmTableEntity : alarmTableEntityList) {
            AlarmEntity alarmEntity = new AlarmEntity();

            Integer stationId = alarmTableEntity.getStationId();//车站id
            Integer sectionId = alarmTableEntity.getSectionId();//区段id
            Integer alarmcode = alarmTableEntity.getAlarmcode();//报警预警
            Integer alarmlevel = alarmTableEntity.getAlarmlevel();//报警预警类别
            Integer createtime = alarmTableEntity.getCreatetime();//开始时间
            Integer czNumber = cheZhanDao.findCzNumber(stationId);

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
            Date date = new Date(createtime * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            try {
                parse = sdf.parse(sdf.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            alarmEntity.setBjyjType(bjyjType);
            alarmEntity.setBjyjcontext(alarmContext);
            alarmEntity.setQuduan(quduanNmae);
            alarmEntity.setIsnotsky(isnotsky);
            alarmEntity.setOpentime(parse);
            alarmEntityList.add(alarmEntity);
        }
        return alarmEntityList;
    }


    @Override
    public List<AlarmEntity> findAllNotReadAlarmDatasByCZid(Integer page, Integer size, Integer czid) {
        Date dayTime = new Date();
        String tableName = StringUtil.getBaoJingYuJingTableName(czid, dayTime);
        PageHelper.startPage(page, size);
        Page<AlarmEntity> alarmEntityList = new Page<>();
        Page<AlarmTableEntity> alarmTableEntityList = (Page<AlarmTableEntity>) alarmTableDao.findAllNotReadAlarmDatasByCZid(tableName);
        alarmEntityList.setTotal(alarmTableEntityList.getTotal());
        for (AlarmTableEntity alarmTableEntity : alarmTableEntityList) {
            AlarmEntity alarmEntity = new AlarmEntity();

            Integer stationId = alarmTableEntity.getStationId();//车站id
            Integer sectionId = alarmTableEntity.getSectionId();//区段id
            Integer alarmcode = alarmTableEntity.getAlarmcode();//报警预警
            Integer alarmlevel = alarmTableEntity.getAlarmlevel();//报警预警类别
            Integer createtime = alarmTableEntity.getCreatetime();//开始时间
            Integer czNumber = cheZhanDao.findCzNumber(stationId);

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
            Date date = new Date(createtime * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            try {
                parse = sdf.parse(sdf.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            alarmEntity.setBjyjType(bjyjType);
            alarmEntity.setBjyjcontext(alarmContext);
            alarmEntity.setQuduan(quduanNmae);
            alarmEntity.setIsnotsky(isnotsky);
            alarmEntityList.add(alarmEntity);
            alarmEntity.setOpentime(parse);
        }
        return alarmEntityList;
    }

    @Override
    public List<AlarmEntity> findAllNotReadAlarmDatas(Integer page, Integer size) {

        String databaseName = "ruixing";
        Date dayTime = new Date();
        int month = DateUtil.month(dayTime) + 1;
        String monthStr = Integer.toString(month).length() == 1 ? "0" + month : Integer.toString(month);
        String tableName = "alarm" + "_%" + DateUtil.year(dayTime) + monthStr;
        List<String> cztables = new ArrayList<>();
        List<String> tables = alarmDao.selectLikeTable(databaseName, tableName);
        for (String table : tables) {
            cztables.add(table);
        }
        StringBuilder sb = new StringBuilder();
        for (String table : cztables) {
            sb.append("SELECT * FROM (SELECT * FROM ").append(table).append(" ORDER BY id DESC) AS ").append(table).append(" UNION ALL ");
        }
        PageHelper.startPage(page, size);
        Page<AlarmEntity> alarmEntityList = new Page<>();
        Page<AlarmTableEntity> alarmTableEntityList = (Page<AlarmTableEntity>) alarmTableDao.findAllNotReadAlarmDatas(sb.toString());
        alarmEntityList.setTotal(alarmTableEntityList.getTotal());
        for (AlarmTableEntity alarmTableEntity : alarmTableEntityList) {
            AlarmEntity alarmEntity = new AlarmEntity();

            Integer stationId = alarmTableEntity.getStationId();//车站id
            Integer sectionId = alarmTableEntity.getSectionId();//区段id
            Integer alarmcode = alarmTableEntity.getAlarmcode();//报警预警
            Integer alarmlevel = alarmTableEntity.getAlarmlevel();//报警预警类别
            Integer createtime = alarmTableEntity.getCreatetime();//开始时间
            Integer czNumber = cheZhanDao.findCzNumber(stationId);

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
            Date date = new Date(createtime * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            try {
                parse = sdf.parse(sdf.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            alarmEntity.setBjyjType(bjyjType);
            alarmEntity.setBjyjcontext(alarmContext);
            alarmEntity.setQuduan(quduanNmae);
            alarmEntity.setIsnotsky(isnotsky);
            alarmEntityList.add(alarmEntity);
            alarmEntity.setOpentime(parse);
        }
        return alarmEntityList;

    }

    @Override
    public void editAlarmState(AlarmTableEntity alarmTableEntity) {
        alarmTableEntity.setStatus(1);
        Integer status = 1;
        String databaseName = "ruixing";
        Date dayTime = new Date();
        int month = DateUtil.month(dayTime) + 1;
        String monthStr = Integer.toString(month).length() == 1 ? "0" + month : Integer.toString(month);
        String tableName = "alarm" + "_%" + DateUtil.year(dayTime) + monthStr;
        List<String> cztables = new ArrayList<>();
        List<String> tables = alarmDao.selectLikeTable(databaseName, tableName);
        for (String table : tables) {
            alarmTableDao.editAlarmState(status, table);
        }
    }


    @Override
    public Integer findAllAlarmNumberByXDid(Integer dwdid, Integer xdid) {
        String databaseName = "ruixing";
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
            return alarmTableDao.findAllAlarmNumber(sb.toString());
        } else {
            Integer alarmNum = 0;
            return alarmNum;
        }
    }

    @Override
    public Integer findAllAlarmNumberByDWDid(Integer dwdid) {
        String databaseName = "ruixing";
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
            return alarmTableDao.findAllAlarmNumber(sb.toString());
        } else {
            Integer alarmNum = 0;
            return alarmNum;
        }
    }


    @Override
    public Integer findAllAlarmNumber() {
        String databaseName = "ruixing";
        Date dayTime = new Date();
        int month = DateUtil.month(dayTime) + 1;
        String monthStr = Integer.toString(month).length() == 1 ? "0" + month : Integer.toString(month);
        String tableName = "alarm" + "_%" + DateUtil.year(dayTime) + monthStr;
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
        }
    }

    @Override
    public Integer findAlarmNumber(String tableName) {
        if (quDuanInfoDaoV2.isTableExist(tableName) == 1) {
            return alarmTableDao.findAlarmNumber(tableName);
        } else {
            return 0;
        }
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
