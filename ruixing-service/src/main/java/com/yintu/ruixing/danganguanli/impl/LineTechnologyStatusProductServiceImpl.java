package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.danganguanli.LineTechnologyStatusProductDao;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusProductEntityWithBLOBs;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/24 19:44
 */
@Service
@Transactional
public class LineTechnologyStatusProductServiceImpl implements LineTechnologyStatusProductService {
    @Autowired
    private LineTechnologyStatusProductDao lineTechnologyStatusProductDao;

    @Override
    public void add(LineTechnologyStatusProductEntityWithBLOBs entity) {
        lineTechnologyStatusProductDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        lineTechnologyStatusProductDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(LineTechnologyStatusProductEntityWithBLOBs entity) {
        lineTechnologyStatusProductDao.updateByPrimaryKeyWithBLOBs(entity);
    }

    @Override
    public LineTechnologyStatusProductEntityWithBLOBs findById(Integer id) {
        return lineTechnologyStatusProductDao.selectByPrimaryKey(id);
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public List<LineTechnologyStatusProductEntityWithBLOBs> findByExample(String name, Integer cid) {
        return lineTechnologyStatusProductDao.selectByExample(name, cid);
    }
}
