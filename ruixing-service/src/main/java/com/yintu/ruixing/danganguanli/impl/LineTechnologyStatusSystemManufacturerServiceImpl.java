package com.yintu.ruixing.danganguanli.impl;

import com.yintu.ruixing.master.danganguanli.LineTechnologyStatusSystemManufacturerDao;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusSystemManufacturerEntity;
import com.yintu.ruixing.danganguanli.LineTechnologyStatusSystemManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/25 9:41
 */
@Service
@Transactional
public class LineTechnologyStatusSystemManufacturerServiceImpl implements LineTechnologyStatusSystemManufacturerService {
    @Autowired
    private LineTechnologyStatusSystemManufacturerDao lineTechnologyStatusSystemManufacturerDao;

    @Override

    public void add(LineTechnologyStatusSystemManufacturerEntity entity) {
        lineTechnologyStatusSystemManufacturerDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        lineTechnologyStatusSystemManufacturerDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(LineTechnologyStatusSystemManufacturerEntity entity) {
        lineTechnologyStatusSystemManufacturerDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public LineTechnologyStatusSystemManufacturerEntity findById(Integer id) {
        List<LineTechnologyStatusSystemManufacturerEntity> lineTechnologyStatusSystemManufacturerEntities = lineTechnologyStatusSystemManufacturerDao.selectByExample(new Integer[]{id}, null, null);
        return lineTechnologyStatusSystemManufacturerEntities.isEmpty() ? null : lineTechnologyStatusSystemManufacturerEntities.get(0);
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public List<LineTechnologyStatusSystemManufacturerEntity> findByExample(String name, Integer cid) {
        return lineTechnologyStatusSystemManufacturerDao.selectByExample(null, name, cid);
    }
}
