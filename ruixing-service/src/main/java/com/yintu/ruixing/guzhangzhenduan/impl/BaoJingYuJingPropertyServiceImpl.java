package com.yintu.ruixing.guzhangzhenduan.impl;

import com.github.pagehelper.Page;
import com.yintu.ruixing.common.util.StringUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
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
    public List<AlarmEntity> findSomeAlarmDatasByChoose(Date starTime, Date endTime, Integer dwdid, Integer xdid, Integer czid) {
        Integer a = 0;
        Date dayTime = new Date();
        List<AlarmEntity> alarmEntityList = new ArrayList<>();
        if (dwdid != null && xdid == null && czid == null) {
            //查询此电务段下面所有的车站
            List<CheZhanEntity> cheZhanEntityList = cheZhanDao.findczidBydwdName(dwdid);
            for (CheZhanEntity cheZhanEntity : cheZhanEntityList) {
                Integer czId = new Long(cheZhanEntity.getCzId()).intValue();
                String tableName = StringUtil.getBaoJingYuJingTableName(czId, dayTime);
                if (quDuanInfoDaoV2.isTableExist(tableName) == 0) {
                    a++;
                } else {
                    Long startime = null;
                    Long endtimee = null;
                    if (starTime != null && endTime != null) {
                        startime = starTime.getTime() / 1000;
                        endtimee = endTime.getTime() / 1000;
                    }
                    List<AlarmTableEntity> alarmTableEntityList = alarmTableDao.findAllAlarmDatasByTimes(tableName, startime, endtimee);
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
        }
        if (dwdid != null && xdid != null && czid == null) {
            List<CheZhanEntity> cheZhanEntityList = cheZhanDao.findczidBydwdNamexdName(dwdid, xdid);
            for (CheZhanEntity cheZhanEntity : cheZhanEntityList) {
                Integer czId = new Long(cheZhanEntity.getCzId()).intValue();
                String tableName = StringUtil.getBaoJingYuJingTableName(czId, dayTime);
                if (quDuanInfoDaoV2.isTableExist(tableName) == 0) {
                    a++;
                } else {
                    Long startime = null;
                    Long endtimee = null;
                    if (starTime != null && endTime != null) {
                        startime = starTime.getTime() / 1000;
                        endtimee = endTime.getTime() / 1000;
                    }
                    List<AlarmTableEntity> alarmTableEntityList = alarmTableDao.findAllAlarmDatasByTimes(tableName, startime, endtimee);
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
        }
        if (dwdid != null && xdid != null && czid != null) {
            Integer czId = cheZhanDao.findCzid(czid);
            String tableName = StringUtil.getBaoJingYuJingTableName(czId, dayTime);
            if (quDuanInfoDaoV2.isTableExist(tableName) == 0) {
                a++;
            } else {
                Long startime = null;
                Long endtimee = null;
                if (starTime != null && endTime != null) {
                    startime = starTime.getTime() / 1000;
                    endtimee = endTime.getTime() / 1000;
                }
                List<AlarmTableEntity> alarmTableEntityList = alarmTableDao.findAllAlarmDatasByTimes(tableName, startime, endtimee);
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
    public List<AlarmEntity> findAllNotReadAlarmDatas(Integer page, Integer size, String tableName) {
        Page<AlarmEntity> alarmEntityList = new Page<>();
        Page<AlarmTableEntity> alarmTableEntityList = (Page<AlarmTableEntity>) alarmTableDao.findAllNotReadAlarmDatas(tableName);
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
    public void editAlarmState(AlarmTableEntity alarmTableEntity, String tableName) {
        alarmTableEntity.setStatus(1);
        Integer status = 1;
        alarmTableDao.editAlarmState(status, tableName);
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
