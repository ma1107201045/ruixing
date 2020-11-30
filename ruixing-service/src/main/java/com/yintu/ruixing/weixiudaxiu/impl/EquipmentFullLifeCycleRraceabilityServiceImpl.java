package com.yintu.ruixing.weixiudaxiu.impl;

import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.master.danganguanli.EquipmentFullLifeCycleRraceabilityDao;
import com.yintu.ruixing.danganguanli.EquipmentFullLifeCycleRraceabilityEntity;
import com.yintu.ruixing.weixiudaxiu.EquipmentFullLifeCycleRraceabilityService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/26 10:11
 */
@Service
public class EquipmentFullLifeCycleRraceabilityServiceImpl implements EquipmentFullLifeCycleRraceabilityService {
    @Autowired
    private EquipmentFullLifeCycleRraceabilityDao equipmentFullLifeCycleRraceabilityDao;


    @Override
    public List<EquipmentFullLifeCycleRraceabilityEntity> findEquipmentLife(Integer[] ids, String czName, String equipmentName) {
        return equipmentFullLifeCycleRraceabilityDao.selectEquipmentLife(ids, czName, equipmentName);
    }

    @Override
    public void exportFile(OutputStream outputStream, Integer[] ids) throws IOException {
        //excel标题
        String title = "设备全生命周期追溯列表";
        //excel表名
        String[] headers = {"序号", "车站", "器材", "24位产品序列号", "电务段检修所编号", "位置号", "所属区段", "返修次数"};
        //获取数据
        List<EquipmentFullLifeCycleRraceabilityEntity> equipmentFullLifeCycleRraceabilityEntities = this.findEquipmentLife(ids, null, null);
        equipmentFullLifeCycleRraceabilityEntities = equipmentFullLifeCycleRraceabilityEntities.stream()
                .sorted(Comparator.comparing(EquipmentFullLifeCycleRraceabilityEntity::getId).reversed())
                .collect(Collectors.toList());
        //excel元素
        String[][] content = new String[equipmentFullLifeCycleRraceabilityEntities.size()][headers.length];

        for (int i = 0; i < equipmentFullLifeCycleRraceabilityEntities.size(); i++) {
            EquipmentFullLifeCycleRraceabilityEntity equipmentFullLifeCycleRraceabilityEntity = equipmentFullLifeCycleRraceabilityEntities.get(i);
            content[i][0] = equipmentFullLifeCycleRraceabilityEntity.getId().toString();
            content[i][1] = equipmentFullLifeCycleRraceabilityEntity.getCzName();
            content[i][2] = equipmentFullLifeCycleRraceabilityEntity.getEquipmentName();
            content[i][3] = equipmentFullLifeCycleRraceabilityEntity.getEquipmentNumber();
            content[i][7] = String.valueOf(equipmentFullLifeCycleRraceabilityEntity.getReprocessedProductCount());
        }

        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }
}
