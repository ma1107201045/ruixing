package com.yintu.ruixing.weixiudaxiu;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.danganguanli.EquipmentFullLifeCycleRraceabilityEntity;
import com.yintu.ruixing.weixiudaxiu.EquipmentFullLifeCycleRraceabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/26 10:14
 */
@Controller
@RequestMapping("/equipment/full/life/cycle/rraceabilities")
public class EquipmentFullLifeCycleRraceabilityController {

    @Autowired
    private EquipmentFullLifeCycleRraceabilityService equipmentFullLifeCycleRraceabilityService;

    @GetMapping
    @ResponseBody
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "id DESC") String orderBy,
                                       @RequestParam(value = "cz_name", required = false) String czName,
                                       @RequestParam(value = "equipment_name", required = false) String equipmentName) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<EquipmentFullLifeCycleRraceabilityEntity> equipmentFullLifeCycleRraceabilityEntities = equipmentFullLifeCycleRraceabilityService.findEquipmentLife(null, czName, equipmentName);
        PageInfo<EquipmentFullLifeCycleRraceabilityEntity> pageInfo = new PageInfo<>(equipmentFullLifeCycleRraceabilityEntities);
        return ResponseDataUtil.ok("查询设备全生命周期列表成功", pageInfo);
    }

    @GetMapping("/export/{ids}")
    public void exportFile(@PathVariable Integer[] ids, HttpServletResponse response) throws IOException {
        String fileName = "设备全生命周期追溯信息列表-导出" + DateUtil.now() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        equipmentFullLifeCycleRraceabilityService.exportFile(response.getOutputStream(), ids);
    }


}
