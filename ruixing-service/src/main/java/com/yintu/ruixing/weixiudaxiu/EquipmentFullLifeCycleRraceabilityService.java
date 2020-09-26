package com.yintu.ruixing.weixiudaxiu;

import com.yintu.ruixing.danganguanli.EquipmentFullLifeCycleRraceabilityEntity;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/26 10:11
 */
public interface EquipmentFullLifeCycleRraceabilityService {

    List<EquipmentFullLifeCycleRraceabilityEntity> findEquipmentLife(Integer[] ids, String czName, String equipmentName);

    /**
     * 批量导出excel数据
     *
     * @param outputStream 输出流
     * @param ids          id集合
     * @throws IOException io异常
     */
    void exportFile(OutputStream outputStream, Integer[] ids) throws IOException;

}
