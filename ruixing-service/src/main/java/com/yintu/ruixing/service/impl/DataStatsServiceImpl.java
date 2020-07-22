package com.yintu.ruixing.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.dao.*;
import com.yintu.ruixing.entity.*;
import com.yintu.ruixing.service.DataStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public String findQDJsonByCid(Integer cid) {
        return cheZhanDao.findQDJsonByCid(cid);
    }

    @Override
    public String findXDJsonByXid(Integer xid) {
        return xianDuanDao.findXDJsonByXid(xid);
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
    public List<DianWuDuanEntity> findDianWuDuanBydid(long parseLong) {
        return dianWuDuanDao.findDianWuDuanBydid(parseLong);
    }

    @Override
    public List<XianDuanEntity> findAllXianDuanByDwdid(long parseLong) {
        return xianDuanDao.findAllXianDuanByDwdid(parseLong);
    }

    @Override
    public Integer findxianduanid(long parseLong) {
        return cheZhanDao.findxianduanid(parseLong);
    }

    @Override
    public Integer findLastParentid() {
        return quDuanBaseDao.lastParentid() ;
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
    public List<CheZhanEntity> findallChezhanByName(String czname) {
        return cheZhanDao.findallChezhanByName(czname);
    }

    @Override
    public List<XianDuanEntity> findAllXianDuanByName(String xdname) {
        return xianDuanDao.findAllXianDuanByName(xdname);
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
        cheZhanDao.insertChezhan(cheZhan);
    }

    @Override
    public Long findXDid(long parseLong) {
        return xianDuanDao.findid(parseLong);
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
    public Long findDWDid(long parseLong) {
        return dianWuDuanDao.dwdid(parseLong);
    }

    @Override
    public List<XianDuanEntity> findAllXianDuan() {
        return xianDuanDao.findAllXianDuan();
    }

    @Override
    public void addDianWuDuan(DianWuDuanEntity duanEntity) {
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
       List<DataStatsEntity> tieLuJuEntities= dataStatsDao.findTieLuJuById(tid);
        return tieLuJuEntities;
    }

    @Override
    public List<DataStatsEntity> findDianWuDuanCheZhanById(Long did, Integer page, Integer size) {
        List<DataStatsEntity> dianwuduan= dataStatsDao.findDianWuDuanCheZhanById(did);
        return dianwuduan;
    }

    @Override
    public List<DataStatsEntity> findXianDuanCheZhanById(Long xid, Integer page, Integer size) {
        List<DataStatsEntity> xianduan= dataStatsDao.findXianDuanCheZhanById(xid);
        return xianduan;
    }

    @Override
    public List<DataStatsEntity> findCheZhanById(Long cid, Integer page, Integer size) {
        List<DataStatsEntity> chezhan= dataStatsDao.findCheZhanById(cid);
        return chezhan;
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
    public List<QuDuanBaseEntity> findAllQuDuan(Integer page,Integer size) {
        List<QuDuanBaseEntity> quDuanBaseEntityList=quDuanBaseDao.findAllQuDuan();
        for (QuDuanBaseEntity quDuanBaseEntity : quDuanBaseEntityList) {
            if (quDuanBaseEntity.getLine().equals("站内")){
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
        Integer i=quDuanBaseDao.lastParentid();//查询表中最后一列数据的id
        quDuanBaseEntity.setParentId(i);//得到新增数据的parentid
        if (quDuanBaseEntity.getLine().equals("站内")){
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
        Integer parentid=quDuanBaseDao.findParentid(id);//根据区段id  查询出对应的parentid
        Integer id1=quDuanBaseDao.findId(id);//根据传来的id  作为parentid  查找出对应的id
        quDuanBaseDao.updateParentid(id1,parentid);
        quDuanBaseDao.deleteByPrimaryKey(id);
    }

    @Override
    public void deletQuDuanByIds(Integer[] ids) {
        Integer fristid=ids[0];//获取数组的第一个数据id
        Integer lastid=ids[ids.length-1];//获得数组的最后一个数据id
        Integer parentid=quDuanBaseDao.findParentid(fristid);//查找出对应的parentid
        Integer id1=quDuanBaseDao.findId(lastid);//作为parentid  查找对应的id
        quDuanBaseDao.updateParentid(id1,parentid);
        quDuanBaseDao.deletQuDuanByIds(ids);
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
    public List<DataStatsEntity> findAllCheZhan(Integer page, Integer size) {
        return dataStatsDao.findAllCheZhan();
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
    public int  delCheZhanListById(int[] ids) {
      //  String[] id = ids.split(",");
        return dataStatsDao.delCheZhanListById(ids);
    }





    //分页查询
    @Override
    public PageInfo<DataStatsEntity> findPage(Integer page, Integer size) {
        //分页
        PageHelper.startPage(page,size);
        //集合查询
        List<DataStatsEntity> all = dataStatsDao.findAll();

        return new PageInfo<DataStatsEntity>(all);
    }


}
