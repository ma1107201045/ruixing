package com.yintu.ruixing.guzhangzhenduan.impl;

import com.yintu.ruixing.guzhangzhenduan.ListDao;
import com.yintu.ruixing.guzhangzhenduan.CheZhanEntity;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuEntity;
import com.yintu.ruixing.guzhangzhenduan.XianDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.ListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * TODO
 *
 * @description:
 * @author: Qiao
 * @time: 2020/5/21 17:08
 */
@Service
@Transactional
public class ListServiceImpl implements ListService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ListDao ld;

    @Override
    public List<DianWuDuanEntity> findXDAndCZByDWDId(Integer dwdid) {

        //根据铁路局id获得电务段信息
        List<DianWuDuanEntity> dianWuDuanEntityDtos = ld.selectDwdListByDwdId(dwdid);
        logger.debug(dianWuDuanEntityDtos.toString());
        //遍历电务段
        for (DianWuDuanEntity dianWuDuanEntityDto : dianWuDuanEntityDtos) {
            //根据遍历的电务段的id 获取对应的线段信息
            List<XianDuanEntity> xianDuanEntityDtos = ld.selectXdListByDwdId(dianWuDuanEntityDto.getDid());
            logger.debug(xianDuanEntityDtos.toString());
            dianWuDuanEntityDto.setXianDuanEntities(xianDuanEntityDtos);
            for (XianDuanEntity xianDuanEntityDto : xianDuanEntityDtos) {
                //遍历线段  获取线段id  然后获取对应的车站
                List<CheZhanEntity> cheZhanEntities = ld.selectCzListByXdId(xianDuanEntityDto.getXid());
                logger.debug(cheZhanEntities.toString());
                xianDuanEntityDto.setCheZhanEntities(cheZhanEntities);
            }
        }
        return dianWuDuanEntityDtos;

    }

    @Override
    public List<TieLuJuEntity> findOneTwoDatas() {
        //获取铁路局信息
        List<TieLuJuEntity> tieLuJuEntityDtos = ld.selectTieLuJuList();
        logger.debug(tieLuJuEntityDtos.toString());
        //遍历铁路局
        for (TieLuJuEntity tieLuJuEntityDto : tieLuJuEntityDtos) {
            //获取铁路局id
            long tId = tieLuJuEntityDto.getTid();
            logger.debug("铁路局id" + tId);
            //根据铁路局id获得电务段信息
            List<DianWuDuanEntity> dianWuDuanEntityDtos = ld.selectDwdListBytId(tId);
            logger.debug(dianWuDuanEntityDtos.toString());
            //保存电务段信息
            tieLuJuEntityDto.setDianWuDuanEntities(dianWuDuanEntityDtos);
        }
        return tieLuJuEntityDtos;
    }

    @Override
    public Object getMenuList() {
        //获取铁路局信息
        List<TieLuJuEntity> tieLuJuEntityDtos = ld.selectTieLuJuList();
        logger.debug(tieLuJuEntityDtos.toString());
        //遍历铁路局
        for (TieLuJuEntity tieLuJuEntityDto : tieLuJuEntityDtos) {
            //获取铁路局id
            long tId = tieLuJuEntityDto.getTid();
            logger.debug("铁路局id" + tId);
            //根据铁路局id获得电务段信息
            List<DianWuDuanEntity> dianWuDuanEntityDtos = ld.selectDwdListBytId(tId);
            logger.debug(dianWuDuanEntityDtos.toString());
            //保存电务段信息
            tieLuJuEntityDto.setDianWuDuanEntities(dianWuDuanEntityDtos);
            //遍历电务段
            for (DianWuDuanEntity dianWuDuanEntityDto : dianWuDuanEntityDtos) {
                //根据遍历的电务段的id 获取对应的线段信息
                List<XianDuanEntity> xianDuanEntityDtos = ld.selectXdListByDwdId(dianWuDuanEntityDto.getDid());
                logger.debug(xianDuanEntityDtos.toString());
                dianWuDuanEntityDto.setXianDuanEntities(xianDuanEntityDtos);
                for (XianDuanEntity xianDuanEntityDto : xianDuanEntityDtos) {
                    //遍历线段  获取线段id  然后获取对应的车站
                    List<CheZhanEntity> cheZhanEntities = ld.selectCzListByXdId(xianDuanEntityDto.getXid());
                    logger.debug(cheZhanEntities.toString());
                    xianDuanEntityDto.setCheZhanEntities(cheZhanEntities);
                }
            }
        }
        return tieLuJuEntityDtos;
    }

    @Override
    public Object getErJi() {
        //获取铁路局信息
        List<TieLuJuEntity> tieLuJuEntityDtos = ld.TieLuJuList();
        logger.debug(tieLuJuEntityDtos.toString());
        //遍历铁路局
        for (TieLuJuEntity tieLuJuEntityDto : tieLuJuEntityDtos) {
            //获取铁路局id
            long tId = tieLuJuEntityDto.getTid();
            //根据铁路局id获得电务段信息
            List<DianWuDuanEntity> dianWuDuanEntityDtos = ld.DwdListBytId(tId);
            logger.debug(dianWuDuanEntityDtos.toString());
            //保存电务段信息
            tieLuJuEntityDto.setDianWuDuanEntities(dianWuDuanEntityDtos);
        }
        return tieLuJuEntityDtos;
    }

    @Override
    public Object getSanJi() {
        List<TieLuJuEntity> tieLuJuEntityDtos = ld.TieLuJuList();
        //logger.debug("铁路局"+tieLuJuEntityDtos);
        //遍历铁路局
        for (TieLuJuEntity tieLuJuEntityDto : tieLuJuEntityDtos) {
            //获取铁路局id
            long tId = tieLuJuEntityDto.getTid();
            //logger.debug("铁路局id"+tId);
            //根据铁路局id获得电务段信息
            List<DianWuDuanEntity> dianWuDuanEntityDtos = ld.DwdListBytId(tId);
            //System.out.println("电务段"+dianWuDuanEntityDtos);
            //保存电务段信息
            tieLuJuEntityDto.setDianWuDuanEntities(dianWuDuanEntityDtos);
            //遍历电务段
            for (DianWuDuanEntity dianWuDuanEntityDto : dianWuDuanEntityDtos) {
                //根据遍历的电务段的id 获取对应的线段信息
                List<XianDuanEntity> xianDuanEntityDtos = ld.XdListByDwdId(dianWuDuanEntityDto.getDid());
                //logger.debug("线段"+xianDuanEntityDtos);
                dianWuDuanEntityDto.setXianDuanEntities(xianDuanEntityDtos);
            }
        }
        return tieLuJuEntityDtos;
    }
}
