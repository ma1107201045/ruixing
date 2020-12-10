package com.yintu.ruixing.guzhangzhenduan.impl;

import com.yintu.ruixing.master.guzhangzhenduan.BaoJingYuJingBaseDao;
import com.yintu.ruixing.guzhangzhenduan.BaoJingYuJingBaseEntity;
import com.yintu.ruixing.guzhangzhenduan.BaoJingYuJingBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/9/26 16:40
 * @Version 1.0
 * 需求:
 */
@Service
@Transactional
public class BaoJingYuJingBaseServiceImpl implements BaoJingYuJingBaseService {
    @Autowired
    private BaoJingYuJingBaseDao baoJingYuJingBaseDao;


    @Override
    public List<BaoJingYuJingBaseEntity> findBJDataBySomething(Integer page, Integer size, String context) {
        return baoJingYuJingBaseDao.findBJDataBySomething(context);
    }

    @Override
    public String findAlarmContext(Integer alarmcode, Integer bjyjType) {
        return baoJingYuJingBaseDao.findAlarmContext(alarmcode, bjyjType);
    }

    @Override
    public List<BaoJingYuJingBaseEntity> findAllBaoJing(Integer page, Integer size) {
        return baoJingYuJingBaseDao.findAllBaoJing();
    }

    @Override
    public void deleteBJYJdDataByids(Integer[] ids) {
        for (Integer id : ids) {
            baoJingYuJingBaseDao.deleteByPrimaryKey(id);
        }
    }

    @Override
    public void editBJYJDataByid(BaoJingYuJingBaseEntity baoJingYuJingBaseEntity, String username) {
        baoJingYuJingBaseEntity.setUpdatename(username);
        baoJingYuJingBaseEntity.setUpdatetime(new Date());
        baoJingYuJingBaseDao.updateByPrimaryKeySelective(baoJingYuJingBaseEntity);
    }

    @Override
    public List<BaoJingYuJingBaseEntity> findBJYJData(Integer page, Integer size, String context, Integer bjyjType) {
        return baoJingYuJingBaseDao.findBJYJData(context, bjyjType);
    }

    @Override
    public void addBaoJing(BaoJingYuJingBaseEntity baoJingYuJingBaseEntity, String username) {
        baoJingYuJingBaseEntity.setCreatename(username);
        baoJingYuJingBaseEntity.setCreatetime(new Date());
        baoJingYuJingBaseDao.insertSelective(baoJingYuJingBaseEntity);
    }
}
