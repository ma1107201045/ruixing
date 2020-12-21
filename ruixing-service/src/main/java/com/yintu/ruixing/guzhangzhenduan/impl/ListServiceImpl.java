package com.yintu.ruixing.guzhangzhenduan.impl;

import com.yintu.ruixing.master.guzhangzhenduan.ListDao;
import com.yintu.ruixing.guzhangzhenduan.CheZhanEntity;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuEntity;
import com.yintu.ruixing.guzhangzhenduan.XianDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.ListService;
import com.yintu.ruixing.xitongguanli.UserDataEntity;
import com.yintu.ruixing.xitongguanli.UserDataService;
import com.yintu.ruixing.xitongguanli.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private UserDataService userDataService;

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

    @Override
    public List<TieLuJuEntity> findOneTwoDatas(UserEntity userEntity) {
        //获取铁路局信息
        List<TieLuJuEntity> tieLuJuEntityDtos = ld.selectTieLuJuList();
        List<UserDataEntity> userDataEntities = userDataService.findByUserId(userEntity.getId());

        if (userEntity.getIsCustomer() == 1) { //判断当前用户是否是顾客
            tieLuJuEntityDtos = tieLuJuEntityDtos.stream().filter(tieLuJuEntity -> {
                boolean result = false;
                for (UserDataEntity userDataEntity : userDataEntities) {
                    if (tieLuJuEntity.getTid() == userDataEntity.getTId()) {
                        result = true;
                        break;
                    }
                }
                return result;
            }).collect(Collectors.toList());
        }

        logger.debug(tieLuJuEntityDtos.toString());
        //遍历铁路局
        for (TieLuJuEntity tieLuJuEntityDto : tieLuJuEntityDtos) {
            //获取铁路局id
            long tId = tieLuJuEntityDto.getTid();
            logger.debug("铁路局id" + tId);
            //根据铁路局id获得电务段信息
            List<DianWuDuanEntity> dianWuDuanEntityDtos = ld.selectDwdListBytId(tId);

            if (userEntity.getIsCustomer() == 1) { //判断当前用户是否是顾客
                dianWuDuanEntityDtos = dianWuDuanEntityDtos.stream().filter(dianWuDuanEntity -> {
                    boolean result = false;
                    for (UserDataEntity userDataEntity : userDataEntities) {
                        if (dianWuDuanEntity.getDid() == userDataEntity.getDId()) {
                            result = true;
                            break;
                        }
                    }
                    return result;
                }).collect(Collectors.toList());
            }

            logger.debug(dianWuDuanEntityDtos.toString());
            //保存电务段信息
            tieLuJuEntityDto.setDianWuDuanEntities(dianWuDuanEntityDtos);
        }
        return tieLuJuEntityDtos;
    }

    @Override
    public List<DianWuDuanEntity> findXDAndCZByDWDId(Integer dwdid, UserEntity userEntity) {
        //根据铁路局id获得电务段信息
        List<DianWuDuanEntity> dianWuDuanEntityDtos = ld.selectDwdListByDwdId(dwdid);

        List<UserDataEntity> userDataEntities = userDataService.findByUserId(userEntity.getId());
        if (userEntity.getIsCustomer() == 1) { //判断当前用户是否是顾客
            dianWuDuanEntityDtos = dianWuDuanEntityDtos.stream().filter(dianWuDuanEntity -> {
                boolean result = false;
                for (UserDataEntity userDataEntity : userDataEntities) {
                    if (dianWuDuanEntity.getDid() == userDataEntity.getDId()) {
                        result = true;
                        break;
                    }
                }
                return result;
            }).collect(Collectors.toList());
        }

        logger.debug(dianWuDuanEntityDtos.toString());
        //遍历电务段
        for (DianWuDuanEntity dianWuDuanEntityDto : dianWuDuanEntityDtos) {
            //根据遍历的电务段的id 获取对应的线段信息
            List<XianDuanEntity> xianDuanEntityDtos = ld.selectXdListByDwdId(dianWuDuanEntityDto.getDid());

            if (userEntity.getIsCustomer() == 1) { //判断当前用户是否是顾客
                xianDuanEntityDtos = xianDuanEntityDtos.stream().filter(xianDuanEntity -> {
                    boolean result = false;
                    for (UserDataEntity userDataEntity : userDataEntities) {
                        if (xianDuanEntity.getXid().equals(userDataEntity.getXId())) {
                            result = true;
                            break;
                        }
                    }
                    return result;
                }).collect(Collectors.toList());
            }

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
    public Object getMenuList() {
        //获取铁路局信息
        List<TieLuJuEntity> tieLuJuEntityDtos = ld.selectTieLuJuList();
        logger.debug(tieLuJuEntityDtos.toString());
        //遍历铁路局
        for (TieLuJuEntity tieLuJuEntityDto : tieLuJuEntityDtos) {
            //获取铁路局id
            long tId = tieLuJuEntityDto.getTid();
            tieLuJuEntityDto.setLabel(tId*(new Date().getTime()));
            logger.debug("铁路局id" + tId);
            //根据铁路局id获得电务段信息
            List<DianWuDuanEntity> dianWuDuanEntityDtos = ld.selectDwdListBytId(tId);
            logger.debug(dianWuDuanEntityDtos.toString());
            //保存电务段信息
            tieLuJuEntityDto.setDianWuDuanEntities(dianWuDuanEntityDtos);
            //遍历电务段
            for (DianWuDuanEntity dianWuDuanEntityDto : dianWuDuanEntityDtos) {
                //根据遍历的电务段的id 获取对应的线段信息
                dianWuDuanEntityDto.setLabel(dianWuDuanEntityDto.getDid()*(new Date().getTime()));
                List<XianDuanEntity> xianDuanEntityDtos = ld.selectXdListByDwdId(dianWuDuanEntityDto.getDid());
                logger.debug(xianDuanEntityDtos.toString());
                dianWuDuanEntityDto.setXianDuanEntities(xianDuanEntityDtos);
                for (XianDuanEntity xianDuanEntityDto : xianDuanEntityDtos) {
                    //遍历线段  获取线段id  然后获取对应的车站
                    xianDuanEntityDto.setLabel(xianDuanEntityDto.getXid()*(new Date().getTime()));
                    List<CheZhanEntity> cheZhanEntities = ld.selectCzListByXdId(xianDuanEntityDto.getXid());
                    logger.debug(cheZhanEntities.toString());
                    xianDuanEntityDto.setCheZhanEntities(cheZhanEntities);
                    for (CheZhanEntity cheZhanEntity : cheZhanEntities) {
                        cheZhanEntity.setLabel(cheZhanEntity.getCid()*(new Date().getTime()));
                    }
                }
            }
        }
        return tieLuJuEntityDtos;
    }

}
