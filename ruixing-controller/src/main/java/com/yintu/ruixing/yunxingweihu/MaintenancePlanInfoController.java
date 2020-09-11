package com.yintu.ruixing.yunxingweihu;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.guzhangzhenduan.CheZhanEntity;
import com.yintu.ruixing.guzhangzhenduan.CheZhanService;
import com.yintu.ruixing.weixiudaxiu.EquipmentEntity;
import com.yintu.ruixing.weixiudaxiu.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 维护计划详情
 *
 * @author:mlf
 * @date:2020/7/9 17:17
 */
@Controller
@RequestMapping("/maintenance/plan/infos")
public class MaintenancePlanInfoController extends SessionController implements BaseController<MaintenancePlanInfoEntity, Integer> {

    @Autowired
    private MaintenancePlanInfoService maintenancePlanInfoService;


    @PostMapping
    @ResponseBody
    public Map<String, Object> add(@Validated MaintenancePlanInfoEntity entity) {
        maintenancePlanInfoService.add(entity);
        return ResponseDataUtil.ok("添加维护计划详情息成功");
    }

    @Override
    public Map<String, Object> remove(Integer id) {
        return null;
    }

    @DeleteMapping("/{ids}")
    @ResponseBody
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        maintenancePlanInfoService.remove(ids);
        return ResponseDataUtil.ok("删除维护计划详情息成功");
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Map<String, Object> edit(@PathVariable Integer id, @Validated MaintenancePlanInfoEntity entity) {
        maintenancePlanInfoService.edit(entity);
        return ResponseDataUtil.ok("修改维护计划详情息成功");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Map<String, Object> findById(@PathVariable Integer id) {
        MaintenancePlanInfoEntity maintenancePlanInfoEntity = maintenancePlanInfoService.findById(id);
        return ResponseDataUtil.ok("查询维护计划详情息成功", maintenancePlanInfoEntity);
    }

    @GetMapping
    @ResponseBody
    public Map<String, Object> findByAll(@RequestParam("page_number") Integer pageNumber,
                                         @RequestParam("page_size") Integer pageSize,
                                         @RequestParam(value = "order_by", required = false, defaultValue = "mpi.id DESC") String orderBy,
                                         @RequestParam(value = "maintenance_plan_id") Integer maintenancePlanId) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<MaintenancePlanInfoEntity> maintenancePlanInfoEntities = maintenancePlanInfoService.findByCondition(null, maintenancePlanId, null);
        PageInfo<MaintenancePlanInfoEntity> pageInfo = new PageInfo<>(maintenancePlanInfoEntities);
        return ResponseDataUtil.ok("查询维护计划详情列表信息成功", pageInfo);
    }

    @GetMapping("/template")
    public void templateFile(HttpServletResponse response) throws IOException {
        String fileName = "维护计划详情列表-模板" + DateUtil.now() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        maintenancePlanInfoService.templateFile(response.getOutputStream());
    }

    @PostMapping("/import")
    @ResponseBody
    public Map<String, Object> importInfoFile(@RequestParam("maintenancePlanId") Integer maintenancePlanId, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        maintenancePlanInfoService.importFile(multipartFile.getInputStream(), multipartFile.getOriginalFilename(), maintenancePlanId);
        return ResponseDataUtil.ok("导入维护计划详情信息成功");
    }


    @GetMapping("/export/{ids}")
    public void exportInfoFile(@PathVariable Integer[] ids, HttpServletResponse response) throws IOException {
        String fileName = "维护计划详情列表-导出" + DateUtil.now() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        maintenancePlanInfoService.exportFile(response.getOutputStream(), ids);
    }


}
