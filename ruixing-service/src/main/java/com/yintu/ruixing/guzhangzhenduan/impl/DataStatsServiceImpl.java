package com.yintu.ruixing.guzhangzhenduan.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.StringUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
import com.yintu.ruixing.guzhangzhenduan.DataStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author:lcy
 * @date:2020-05-21 11
 * 数据统计
 */

@Service
@Transactional
public class DataStatsServiceImpl implements DataStatsService {

    @Autowired
    private DataStatsDao dataStatsDao;

    @Autowired
    private QuDuanBaseDao quDuanBaseDao;

    @Autowired
    private TieLuJuDao tieLuJuDao;

    @Autowired
    private DianWuDuanDao dianWuDuanDao;

    @Autowired
    private XianDuanDao xianDuanDao;

    @Autowired
    private CheZhanDao cheZhanDao;

    @Autowired
    private QuDuanInfoDaoV2 quDuanInfoDaoV2;

    @Autowired
    private AlarmTableDao alarmTableDao;

    @Autowired
    private DataStatsService dataStatsService;

    @Override
    public void addDatas(List<String[]> list) {
        for (String[] strings : list) {
            String tljId = strings[0];
            String tljName = strings[1];
            List<TieLuJuEntity> tljname = dataStatsService.findAllTieLuJuByName(tljName);//查询铁路局表中是否有此铁路局
            System.out.println("铁路局个数" + tljname.size());
            TieLuJuEntity luJuEntity = new TieLuJuEntity();
            if (tljname.size() == 0) {//没有此铁路局
                luJuEntity.setTljId(Long.parseLong(tljId));
                luJuEntity.setTljName(tljName);
                dataStatsService.addTieLuJU(luJuEntity);
            }
            String dwdid = strings[2];
            String dwdname = strings[3];
            List<DianWuDuanEntity> dianWuDuanEntityList = dataStatsService.findDianWuDuanBydid(Long.parseLong(dwdid), Long.parseLong(tljId));//查询电务段表中是否有此电务段
            System.out.println("电务段个数" + dianWuDuanEntityList.size());
            if (dianWuDuanEntityList.size() == 0) {//没有此电务段
                Long tljid = dataStatsService.findTLJid(Long.parseLong(tljId));//获取上个铁路局的id
                DianWuDuanEntity duanEntity = new DianWuDuanEntity();
                duanEntity.setTljDwdId(tljid);
                duanEntity.setDwdName(dwdname);
                duanEntity.setDwdId(Long.parseLong(dwdid));
                duanEntity.setTljId(Long.parseLong(strings[0]));
                dataStatsService.addDianWuDuan(duanEntity);
            }
            String xdid = strings[4];
            String xdname = strings[5];
            String xdzgname = strings[6];
            List<XianDuanEntity> xianDuanEntityList = dataStatsService.findAllXianDuanByDwdid(Long.parseLong(dwdid), Long.parseLong(xdid));
            System.out.println("线段个数" + xianDuanEntityList.size());
            if (xianDuanEntityList.size() == 0) {
                Long dwdid1 = dataStatsService.findDWDid(Long.parseLong(dwdid), Long.parseLong(tljId));
                XianDuanEntity xianDuanEntity1 = new XianDuanEntity();
                xianDuanEntity1.setDwdXdId(dwdid1);
                xianDuanEntity1.setXdName(xdname);
                xianDuanEntity1.setXdId(Long.parseLong(xdid));
                xianDuanEntity1.setDwdId(Long.parseLong(strings[2]));
                xianDuanEntity1.setXdZgName(xdzgname);
                dataStatsService.addXianDuan(xianDuanEntity1);
            }
            String czid = strings[7];
            String czname = strings[8];
            String tuzhongjiancheng = strings[9];
            String cztype = strings[10];
            String tongxinbianmaguidaonumber = strings[11];
            String tongxinbianmazhanneinumber = strings[12];
            String jidianbianmaonetoonenumber = strings[13];
            String jidianbianmaNtOnenumber = strings[14];
            String jidianbianmaNtoOneshebeinumber = strings[15];
            String tongxinbianmazhanneidianmahuanumber = strings[16];
            String jidianNtoOneDianMaHuaNumber = strings[17];
            String jidianJiashiGuidaoNumber = strings[18];
            String jidianJiashiDianmahuaNumber = strings[19];
            String jiDianDianMaHuaNumber = strings[20];
            String yuliushebei1 = strings[21];
            String yuliushebei2 = strings[22];
            String yuliushebei3 = strings[23];
            String qujianBisaiType = strings[24];
            String isnoDuantou = strings[25];
            String linzhan1id = strings[26];
            String linzhan1name = strings[27];
            String linzhan1LineType = strings[28];
            String linzhan1OfXianduan = strings[29];
            String linzhan1benDWD = strings[30];
            String linzhan2id = strings[31];
            String linzhan2name = strings[32];
            String linzhan2LineType = strings[33];
            String linzhan2OfXianduan = strings[34];
            String linzhan2benDWD = strings[35];
            String linzhan3id = strings[36];
            String linzhan3name = strings[37];
            String linzhan3LineType = strings[38];
            String linzhan3OfXianduan = strings[39];
            String linzhan3benDWD = strings[40];
            String linzhan4id = strings[41];
            String linzhan4name = strings[42];
            String linzhan4LineType = strings[43];
            String linzhan4OfXianduan = strings[44];
            String linzhan4benDWD = strings[45];
            String linzhan5id = strings[46];
            String linzhan5name = strings[47];
            String linzhan5LineType = strings[48];
            String linzhan5OfXianduan = strings[49];
            String linzhan5benDWD = strings[50];
            String linzhan6id = strings[51];
            String linzhan6name = strings[52];
            String linzhan6LineType = strings[53];
            String linzhan6OfXianduan = strings[54];
            String linzhan6benDWD = strings[55];
            List<CheZhanEntity> cheZhanEntityList = dataStatsService.findallChezhanByCZidAndXDid(Long.parseLong(xdid), Long.parseLong(czid));
            System.out.println("车站个数" + cheZhanEntityList.size());
            if (cheZhanEntityList.size() == 0) {
                Long xianduanid = dataStatsService.findXDid(Long.parseLong(dwdid), Long.parseLong(xdid));
                CheZhanEntity cheZhan = new CheZhanEntity();
                cheZhan.setXdCzId(xianduanid);
                cheZhan.setCzName(czname);
                cheZhan.setCzId(Long.parseLong(czid));
                cheZhan.setXdId(Long.parseLong(xdid));
                cheZhan.setCzNameJianCheng(tuzhongjiancheng);
                cheZhan.setCzType(cztype);
                if (tongxinbianmaguidaonumber.equals("")) {
                    cheZhan.setTongxinbianmaguidaonumber(0);
                } else {
                    cheZhan.setTongxinbianmaguidaonumber(Integer.parseInt(tongxinbianmaguidaonumber));
                }
                if (tongxinbianmazhanneinumber.equals("")) {
                    cheZhan.setTongxinbianmazhanneioneguidaonumber(0);
                } else {
                    cheZhan.setTongxinbianmazhanneioneguidaonumber(Integer.parseInt(tongxinbianmazhanneinumber));
                }
                if (jidianbianmaonetoonenumber.equals("")) {
                    cheZhan.setJidianonetooneguidaonumber(0);
                } else {
                    cheZhan.setJidianonetooneguidaonumber(Integer.parseInt(jidianbianmaonetoonenumber));
                }
                if (jidianbianmaNtOnenumber.equals("")) {
                    cheZhan.setJidianntooneguidaonumber(0);
                } else {
                    cheZhan.setJidianntooneguidaonumber(Integer.parseInt(jidianbianmaNtOnenumber));
                }
                if (jidianbianmaNtoOneshebeinumber.equals("")) {
                    cheZhan.setJidianntooneshebeinumber(0);
                } else {
                    cheZhan.setJidianntooneshebeinumber(Integer.parseInt(jidianbianmaNtoOneshebeinumber));
                }
                if (tongxinbianmazhanneidianmahuanumber.equals("")) {
                    cheZhan.setTongxinbianmadianmahuashebeinumber(0);
                } else {
                    cheZhan.setTongxinbianmadianmahuashebeinumber(Integer.parseInt(tongxinbianmazhanneidianmahuanumber));
                }
                if (jidianNtoOneDianMaHuaNumber.equals("")) {
                    cheZhan.setJidianntoonedianmahuashebeinumber(0);
                } else {
                    cheZhan.setJidianntoonedianmahuashebeinumber(Integer.parseInt(jidianNtoOneDianMaHuaNumber));
                }
                if (jidianJiashiGuidaoNumber.equals("")) {
                    cheZhan.setJidianjiashiguidaonumber(0);
                } else {
                    cheZhan.setJidianjiashiguidaonumber(Integer.parseInt(jidianJiashiGuidaoNumber));
                }
                if (jidianJiashiDianmahuaNumber.equals("")) {
                    cheZhan.setJidianjiashidianmahuashebeinumber(0);
                } else {
                    cheZhan.setJidianjiashidianmahuashebeinumber(Integer.parseInt(jidianJiashiDianmahuaNumber));
                }
                if (jiDianDianMaHuaNumber.equals("")) {
                    cheZhan.setJiDianDianMaHuaNumber(0);
                } else {
                    cheZhan.setJiDianDianMaHuaNumber(Integer.parseInt(jiDianDianMaHuaNumber));
                }
                cheZhan.setYuliushebei1(yuliushebei1);
                cheZhan.setYuliushebei2(yuliushebei2);
                cheZhan.setYuliushebei3(yuliushebei3);
                cheZhan.setQujianbisaitype(qujianBisaiType);
                if (isnoDuantou.equals("是")) {
                    cheZhan.setCzDuanTou(1);
                } else {
                    cheZhan.setCzDuanTou(0);
                }
                if (linzhan1id.equals("——")) {
                    cheZhan.setLinzhan1id(null);
                } else {
                    cheZhan.setLinzhan1id(Integer.parseInt(linzhan1id));
                }
                cheZhan.setLinzhan1name(linzhan1name);
                cheZhan.setLinzhan1linetype(linzhan1LineType);
                cheZhan.setLinzhan1ofxianduan(linzhan1OfXianduan);
                cheZhan.setLinzhan1isnobendwd(linzhan1benDWD);

                if (linzhan2id.equals("——")) {
                    cheZhan.setLinzhan2id(null);
                } else {
                    cheZhan.setLinzhan2id(Integer.parseInt(linzhan2id));
                }
                cheZhan.setLinzhan2name(linzhan2name);
                cheZhan.setLinzhan2linetype(linzhan2LineType);
                cheZhan.setLinzhan2ofxianduan(linzhan2OfXianduan);
                cheZhan.setLinzhan2isnobendwd(linzhan2benDWD);

                if (linzhan3id.equals("——")) {
                    cheZhan.setLinzhan3id(null);
                } else {
                    cheZhan.setLinzhan3id(Integer.parseInt(linzhan3id));
                }
                cheZhan.setLinzhan3name(linzhan3name);
                cheZhan.setLinzhan3linetype(linzhan3LineType);
                cheZhan.setLinzhan3ofxianduan(linzhan3OfXianduan);
                cheZhan.setLinzhan3isnobendwd(linzhan3benDWD);

                if (linzhan4id.equals("——")) {
                    cheZhan.setLinzhan4id(null);
                } else {
                    cheZhan.setLinzhan4id(Integer.parseInt(linzhan4id));
                }
                cheZhan.setLinzhan4name(linzhan4name);
                cheZhan.setLinzhan4linetype(linzhan4LineType);
                cheZhan.setLinzhan4ofxianduan(linzhan4OfXianduan);
                cheZhan.setLinzhan4isnobendwd(linzhan4benDWD);

                if (linzhan5id.equals("——")) {
                    cheZhan.setLinzhan5id(null);
                } else {
                    cheZhan.setLinzhan5id(Integer.parseInt(linzhan5id));
                }
                cheZhan.setLinzhan5name(linzhan5name);
                cheZhan.setLinzhan5linetype(linzhan5LineType);
                cheZhan.setLinzhan5ofxianduan(linzhan5OfXianduan);
                cheZhan.setLinzhan5isnobendwd(linzhan5benDWD);

                if (linzhan6id.equals("——")) {
                    cheZhan.setLinzhan6id(null);
                } else {
                    cheZhan.setLinzhan6id(Integer.parseInt(linzhan6id));
                }
                cheZhan.setLinzhan6name(linzhan6name);
                cheZhan.setLinzhan6linetype(linzhan6LineType);
                cheZhan.setLinzhan6ofxianduan(linzhan6OfXianduan);
                cheZhan.setLinzhan6isnobendwd(linzhan6benDWD);
                dataStatsService.addCheZhan(cheZhan);
            }
        }
    }

    @Override
    public void editDMHStaCteByCid(CheZhanEntity cheZhanEntity) {
        cheZhanEntity.setCzdmhState(1);
        dataStatsDao.editDMHStaCteByCid(cheZhanEntity);
    }

    @Override
    public List<QuDuanBaseEntity> findDianMaHuaByCid(Integer cid) {
        return quDuanBaseDao.findDianMaHuaByCid(cid);
    }

    @Override
    public List<QuDuanBaseEntity> findDianMaHuaByTid(Integer tid, Integer page, Integer size) {
        return quDuanBaseDao.findDianMaHuaByTid(tid);
    }

    @Override
    public List<QuDuanBaseEntity> findDianMaHuaByDid(Integer did, Integer page, Integer size) {
        return quDuanBaseDao.findDianMaHuaByDid(did);
    }

    @Override
    public List<QuDuanBaseEntity> findDianMaHuaByXid(Integer xid, Integer page, Integer size) {
        return quDuanBaseDao.findDianMaHuaByXid(xid);
    }

    @Override
    public List<QuDuanBaseEntity> findDianMaHuaBycid(Integer cid, Integer page, Integer size) {
        return quDuanBaseDao.findDianMaHuaBycid(cid);
    }

    @Override
    public List<QuDuanBaseEntity> findQuDuanBycid(Integer cid, Integer page, Integer size) {
        return quDuanBaseDao.findQuDuanBycid(cid);
    }

    @Override
    public List<QuDuanBaseEntity> findQuDuanByXid(Integer xid, Integer page, Integer size) {
        return quDuanBaseDao.findQuDuanByXid(xid);
    }

    @Override
    public List<QuDuanBaseEntity> findQuDuanByDid(Integer did, Integer page, Integer size) {
        return quDuanBaseDao.findQuDuanByDid(did);
    }

    @Override
    public List<QuDuanBaseEntity> findQuDuanByTid(Integer tid, Integer page, Integer size) {
        return quDuanBaseDao.findQuDuanByTid(tid);
    }

    @Override
    public List<CheZhanEntity> findCheZhanByIds(Integer parseInt, Integer parseInt1) {
        return cheZhanDao.findCheZhanByIds(parseInt, parseInt1);
    }

    @Override
    public List<QuDuanBaseEntity> findQuDuanByIds(Integer parseInt, Integer parseInt1) {
        return quDuanBaseDao.findQuDuanByIds(parseInt, parseInt1);
    }

    @Override
    public List<CheZhanEntity> findStartCheZhan(Integer firstCZid) {
        List<CheZhanEntity> fridtchezhan = cheZhanDao.findStartCheZhan(firstCZid);
        for (CheZhanEntity cheZhanEntity : fridtchezhan) {
            if (cheZhanEntity.getCzDuanTou() == 1) {
                return null;
            }
        }
        return fridtchezhan;
    }

    @Override
    public List<CheZhanEntity> findEndCheZhan(Integer endCZid) {
        List<CheZhanEntity> entityList = cheZhanDao.findEndCheZhan(endCZid);
        for (CheZhanEntity cheZhanEntity : entityList) {
            if (cheZhanEntity.getCzDuanTou() == 1) {
                return null;
            }
        }
        return entityList;
    }

    @Override
    public Integer findFirstCZid(Integer xid) {
        return cheZhanDao.findFirstCZid(xid);
    }

    @Override
    public Integer findEndCZid(Integer xid) {
        return cheZhanDao.findEndCZid(xid);
    }

    @Override
    public String findDMHJsonByCid(Integer cid) {
        return cheZhanDao.findDMHJsonByCid(cid);
    }

    @Override
    public String findQDJsonByCid(Integer cid) {
        return cheZhanDao.findQDJsonByCid(cid);
    }

    @Override
    public String findOneXDJsonByXid(Integer xid) {
        return xianDuanDao.findXDJsonByXid(xid);
    }

    @Override
    public List<String> findXDJsonByDid(Integer did) {
        return xianDuanDao.findXDJsonByDid(did);
    }

    @Override
    public String findDWDJsonByDid(Integer did) {
        return dianWuDuanDao.findDWDJsonByDid(did);
    }

    @Override
    public DianWuDuanEntity findDWDJsonAndChezhanInfoByDid(Integer did) {
        Date dayTime = new Date();
        DianWuDuanEntity dianWuDuanEntity = new DianWuDuanEntity();
        String dwdJson = dianWuDuanDao.findDWDJsonByDid(did);
        dianWuDuanEntity.setDwdJson(dwdJson);
        List<XianDuanEntity> xianDuanEntities = this.findXianDuanByDid(did);

        List<CheZhanEntity> cheZhanEntities = new ArrayList<>();
        for (XianDuanEntity xianDuanEntity : xianDuanEntities) {
            List<CheZhanEntity> cheZhanEntityList = cheZhanDao.findCheZhanDatasByXid(xianDuanEntity.getXid().intValue());
            for (CheZhanEntity cheZhanEntity : cheZhanEntityList) {
                int czid = (int) cheZhanEntity.getCzId();
                String tableName = StringUtil.getBaoJingYuJingTableName(czid, dayTime);
                if (quDuanInfoDaoV2.isTableExist(tableName) == 1) {
                    Integer alarmNumber = alarmTableDao.findAlarmNumber(tableName);
                    cheZhanEntity.setAlarmNumber(alarmNumber);
                } else {
                    cheZhanEntity.setAlarmNumber(0);
                }
            }
            cheZhanEntities.addAll(cheZhanEntityList);
        }
        dianWuDuanEntity.setCheZhanEntities(cheZhanEntities);
        return dianWuDuanEntity;
    }

    @Override
    public XianDuanEntity findXDJsonByXid(Integer xid) {
        Date dayTime = new Date();
        XianDuanEntity xianDuanEntity = new XianDuanEntity();
        String xdJson = xianDuanDao.findXDJsonByXid(xid);
        xianDuanEntity.setXdJson(xdJson);
        List<CheZhanEntity> cheZhanEntityList = cheZhanDao.findCheZhanDatasByXid(xid);
        for (CheZhanEntity cheZhanEntity : cheZhanEntityList) {
            int czid = (int) cheZhanEntity.getCzId();
            String tableName = StringUtil.getBaoJingYuJingTableName(czid, dayTime);
            if (quDuanInfoDaoV2.isTableExist(tableName) == 1) {
                Integer alarmNumber = alarmTableDao.findAlarmNumber(tableName);
                cheZhanEntity.setAlarmNumber(alarmNumber);
            } else {
                cheZhanEntity.setAlarmNumber(0);
            }
        }
        xianDuanEntity.setCheZhanEntities(cheZhanEntityList);

        return xianDuanEntity;
    }

    @Override
    public List<CheZhanEntity> findSomeCheZhanByXid(Integer xid) {
        return cheZhanDao.findSomeCheZhanByXid(xid);
    }


    @Override
    public List<QuDuanBaseEntity> findQuDuanByQuDuanYunYingName(String qudunyunyingname) {
        return quDuanBaseDao.findQuDuanByQuDuanYunYingName(qudunyunyingname);
    }

    @Override
    public List<DianWuDuanEntity> findDianWuDuanBydid(long parseLong, long parseLong1) {
        return dianWuDuanDao.findDianWuDuanBydid(parseLong, parseLong1);
    }

    @Override
    public List<XianDuanEntity> findAllXianDuanByDwdid(long parseLong, long parseLong1) {
        return xianDuanDao.findAllXianDuanByDwdid(parseLong, parseLong1);
    }

    @Override
    public Integer findxianduanid(long parseLong) {
        return cheZhanDao.findxianduanid(parseLong);
    }

    @Override
    public Integer findLastParentid() {
        Integer parentId = quDuanBaseDao.lastParentid();
        if (parentId == null) {
            return 0;
        }
        return parentId;
    }

    @Override
    public List<QuDuanBaseEntity> findQuDuanByCid(Long cid) {
        return quDuanBaseDao.findQuDuanByCid(cid);
    }

    @Override
    public Long findchezhanid(long parseLong) {
        return cheZhanDao.findchezhanid(parseLong);
    }

    @Override
    public List<CheZhanEntity> findallChezhanByCZidAndXDid(long parseLong, long parseLong1) {
        return cheZhanDao.findallChezhanByCZidAndXDid(parseLong, parseLong1);
    }

    @Override
    public List<XianDuanEntity> findAllXianDuanByName(String xdname) {
        return xianDuanDao.findAllXianDuanByName(xdname);
    }

    @Override
    public List<CheZhanEntity> findallChezhanByName(String czname) {
        return cheZhanDao.findallChezhanByName(czname);
    }

    @Override
    public List<DianWuDuanEntity> findDianWuDuanByName(String dwdname) {
        return dianWuDuanDao.findDianWuDuanByName(dwdname);
    }

    @Override
    public List<TieLuJuEntity> findAllTieLuJuByName(String tljName) {
        return tieLuJuDao.findAllTieLuJuByName(tljName);
    }

    @Override
    public void addCheZhan(CheZhanEntity cheZhan) {
        cheZhan.setCzState(0);
        cheZhan.setPublicMessage(0);
        cheZhan.setCzdmhState(0);
        cheZhan.setCzLianJieType(0);
        cheZhan.setCzStutrs(0);
        cheZhanDao.insertChezhan(cheZhan);
    }

    @Override
    public Long findXDid(long parseLong, long parseLong1) {
        return xianDuanDao.findid(parseLong, parseLong1);
    }

    @Override
    public List<CheZhanEntity> findallChezhan() {
        return cheZhanDao.selectAll();
    }

    @Override
    public void addXianDuan(XianDuanEntity xianDuanEntity1) {
        xianDuanEntity1.setXdState(0);
        xianDuanDao.addXianDuan(xianDuanEntity1);
    }

    @Override
    public Long findDWDid(long parseLong, long parseLong1) {
        return dianWuDuanDao.dwdid(parseLong, parseLong1);
    }

    @Override
    public List<XianDuanEntity> findAllXianDuan() {
        return xianDuanDao.findAllXianDuan();
    }

    @Override
    public void addDianWuDuan(DianWuDuanEntity duanEntity) {
        duanEntity.setDwdState(0);
        dianWuDuanDao.addDianWuDuan(duanEntity);
    }

    @Override
    public Long findTLJid(long parseLong) {
        return tieLuJuDao.findTLJid(parseLong);
    }

    @Override
    public List<DianWuDuanEntity> findDianWuDuan() {
        return dianWuDuanDao.findDianWuDuan();
    }

    @Override
    public void addTieLuJU(TieLuJuEntity stringList) {
        tieLuJuDao.addTieLuJU(stringList);
    }

    //查询所有数据
    @Override
    public List<DataStatsEntity> findAll() {
        return dataStatsDao.findAll();
    }


    @Override
    public List<DataStatsEntity> findTieLuJuById(Long tid, Integer page, Integer size) {
        List<DataStatsEntity> tieLuJuEntities = dataStatsDao.findTieLuJuById(tid);
        return tieLuJuEntities;
    }

    @Override
    public List<DataStatsEntity> findDianWuDuanCheZhanById(Long did, Integer page, Integer size) {
        List<DataStatsEntity> dianwuduan = dataStatsDao.findDianWuDuanCheZhanById(did);
        return dianwuduan;
    }

    @Override
    public List<DataStatsEntity> findXianDuanCheZhanById(Long xid, Integer page, Integer size) {
        List<DataStatsEntity> xianduan = dataStatsDao.findXianDuanCheZhanById(xid);
        return xianduan;
    }

    @Override
    public List<DataStatsEntity> findCheZhanById(Long cid, Integer page, Integer size) {
        List<DataStatsEntity> chezhan = dataStatsDao.findCheZhanById(cid);
        return chezhan;
    }


    @Override
    public void editStateByDid(DianWuDuanEntity dianWuDuanEntity) {
        dianWuDuanEntity.setDwdState(1);
        dataStatsDao.editStateByDid(dianWuDuanEntity);
    }

    @Override
    public void editStateByXid(XianDuanEntity xianDuanEntity) {
        xianDuanEntity.setXdState(1);
        dataStatsDao.editStateByXid(xianDuanEntity);
    }

    @Override
    public void editStateByCid(CheZhanEntity cheZhanEntity) {
        cheZhanEntity.setCzState(1);
        dataStatsDao.editStateByCid(cheZhanEntity);
    }


    @Override
    public List<TieLuJuEntity> findAllTieLuJu() {
        return dataStatsDao.findAllTieLuJu();
    }

    @Override
    public List<DianWuDuanEntity> findDianWuDuanByTid(Integer tid) {
        return dataStatsDao.findDianWuDuanByTid(tid);
    }

    @Override
    public List<XianDuanEntity> findXianDuanByDid(Integer did) {
        return dataStatsDao.findXianDuanByDid(did);
    }

    @Override
    public List<CheZhanEntity> findCheZhanByXid(Integer xid) {
        return dataStatsDao.findCheZhanByXid(xid);
    }

    @Override
    public List<DianWuDuanEntity> findAllDianWuDuan() {
        return dataStatsDao.findAllDianWuDuan();
    }

    @Override
    public List<QuDuanBaseEntity> findAllQuDuan(Integer page, Integer size) {
        List<QuDuanBaseEntity> quDuanBaseEntityList = quDuanBaseDao.findAllQuDuan();
        for (QuDuanBaseEntity quDuanBaseEntity : quDuanBaseEntityList) {
            if (quDuanBaseEntity.getLine().equals("站内")) {
                quDuanBaseEntity.getXianDuanEntity().setXdName(null);

            }
        }
        return quDuanBaseEntityList;
    }

    @Override
    public List<QuDuanBaseEntity> findAllDianMaHua(Integer page, Integer size) {
        return quDuanBaseDao.findAllDianMaHua();
    }

    @Override
    public List<QuDuanBaseEntity> findAllQuDuanByCid(Integer cid, Integer page, Integer size) {
        return quDuanBaseDao.findAllQuDuanByCid(cid);
    }

    @Override
    public List<QuDuanBaseEntity> findAllDianMaHuaByCid(Integer cid, Integer page, Integer size) {
        return quDuanBaseDao.findAllDianMaHuaByCid(cid);
    }

    @Override
    public void addQuDuan(QuDuanBaseEntity quDuanBaseEntity) {
        Integer i = quDuanBaseDao.lastParentid();//查询表中最后一列数据的id
        if (i == null) {
            i = 0;
        }
        quDuanBaseEntity.setParentId(i);//得到新增数据的parentid
        if (quDuanBaseEntity.getLine().equals("站内")) {
            quDuanBaseEntity.setType(0);
            quDuanBaseEntity.setLeftRight(null);

        }
        quDuanBaseDao.insertSelective(quDuanBaseEntity);
    }
 /*   @Override
    public void addQuDuan(QuDuanBaseEntity quDuanBaseEntity) {
        quDuanBaseEntity.setParentId(0);
       quDuanBaseDao.insertSelective(quDuanBaseEntity);

    }*/

    @Override
    public void editQuDuanById(QuDuanBaseEntity quDuanBaseEntity) {
        quDuanBaseDao.updateByPrimaryKeySelective(quDuanBaseEntity);
    }

    @Override
    public void deletQuDuanById(Integer id) {
        Integer parentid = quDuanBaseDao.findParentid(id);//根据区段id  查询出对应的parentid
        Integer id1 = quDuanBaseDao.findId(id);//根据传来的id  作为parentid  查找出对应的id
        quDuanBaseDao.updateParentid(id1, parentid);
        quDuanBaseDao.deleteByPrimaryKey(id);
    }

    @Override
    public void deletQuDuanByIds(Integer[] ids) {
        Integer fristid = ids[0];//获取数组的第一个数据id
        Integer lastid = ids[ids.length - 1];//获得数组的最后一个数据id
        Integer parentid = quDuanBaseDao.findParentid(fristid);//查找出对应的parentid
        Integer id1 = quDuanBaseDao.findId(lastid);//作为parentid  查找对应的id
        quDuanBaseDao.updateParentid(id1, parentid);
        quDuanBaseDao.deletQuDuanByIds(ids);
    }

    @Override
    public void qingChuaByDid(DianWuDuanEntity dianWuDuanEntity) {
        dianWuDuanEntity.setDwdState(0);
        dianWuDuanEntity.setDwdJson(null);
        dataStatsDao.qingChuaByDid(dianWuDuanEntity);
    }

    @Override
    public void qingChuaByXid(XianDuanEntity xianDuanEntity) {
        xianDuanEntity.setXdState(0);
        xianDuanEntity.setXdJson(null);
        dataStatsDao.qingChuaByXid(xianDuanEntity);
    }

    @Override
    public void qingChuaByCid(CheZhanEntity cheZhanEntity) {
        cheZhanEntity.setCzState(0);
        cheZhanEntity.setCzJson(null);
        dataStatsDao.qingChuaByCid(cheZhanEntity);
    }

    @Override
    public void qingChuaDMHByCid(CheZhanEntity cheZhanEntity) {
        cheZhanEntity.setCzdmhState(0);
        cheZhanEntity.setCzdmhJson(null);
        dataStatsDao.qingChuaDMHByCid(cheZhanEntity);
    }

    @Override
    public List<DataStatsEntity> findAllCheZhan(Integer page, Integer size) {
        return dataStatsDao.findAllCheZhan();
    }

    @Override
    public Integer findNumBycid(Integer id) {
        return cheZhanDao.findNumBycid(id);
    }




/*
    @Override
    public List<DataStatsEntity> findDianWuDuanById(Long tid, Long did, Integer page, Integer size) {
         List<DataStatsEntity> dataStatEntities =dataStatsDao.findDianWuDuanById(tid,did);
        System.out.println("从后端传来的数据"+ dataStatEntities);
        return dataStatEntities;
    }
    @Override
    public List<DataStatsEntity> findXianDuanById(Long tid, Long did, Long xid, Integer page, Integer size) {
        List<DataStatsEntity> dataStatEntities = dataStatsDao.findXianDuanById(tid,did,xid);
        return dataStatEntities;
    }
    @Override
    public List<DataStatsEntity> findCheZhanById(Long tid, Long did, Long xid, Long cid, Integer page, Integer size) {
        List<DataStatsEntity> dataStatEntities = dataStatsDao.findCheZhanById(tid,did,xid,cid);
        System.out.println("后端车站信息"+ dataStatEntities);
        return dataStatEntities;
    }

    */


    @Override
    public int delCheZhanListById(int[] ids) {
        //  String[] id = ids.split(",");
        return dataStatsDao.delCheZhanListById(ids);
    }


    //分页查询
    @Override
    public PageInfo<DataStatsEntity> findPage(Integer page, Integer size) {
        //分页
        PageHelper.startPage(page, size);
        //集合查询
        List<DataStatsEntity> all = dataStatsDao.findAll();

        return new PageInfo<DataStatsEntity>(all);
    }


}
