package com.yintu.ruixing.yunxingweihu;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
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
 * 维护计划类别
 *
 * @author:mlf
 * @date:2020/7/8 15:53
 */
@Controller
@RequestMapping("/maintenance/plans")
public class MaintenancePlanController extends SessionController implements BaseController<MaintenancePlanEntity, Integer> {

    @Autowired
    private MaintenancePlanService maintenancePlanService;

    @Autowired
    private MaintenancePlanInfoService maintenancePlanInfoService;

    @PostMapping
    @ResponseBody
    public Map<String, Object> add(@Validated MaintenancePlanEntity entity) {
        maintenancePlanService.add(entity);
        return ResponseDataUtil.ok("添加维护计划信息成功");
    }

    @Override
    public Map<String, Object> remove(Integer id) {
        return null;
    }


    @DeleteMapping("/{ids}")
    @ResponseBody
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        maintenancePlanService.remove(ids);
        return ResponseDataUtil.ok("删除维护计划信息成功");
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Map<String, Object> edit(@PathVariable Integer id, @Validated MaintenancePlanEntity entity) {
        maintenancePlanService.edit(entity);
        return ResponseDataUtil.ok("修改维护计划信息成功");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Map<String, Object> findById(@PathVariable Integer id) {
        MaintenancePlanEntity maintenancePlanEntity = maintenancePlanService.findById(id);
        return ResponseDataUtil.ok("查询维护计划信息成功", maintenancePlanEntity);
    }


    @GetMapping
    @ResponseBody
    public Map<String, Object> findByAll(@RequestParam("page_number") Integer pageNumber,
                                         @RequestParam("page_size") Integer pageSize,
                                         @RequestParam(value = "order_by", required = false, defaultValue = "id DESC") String orderBy,
                                         @RequestParam(value = "name", required = false) String name) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<MaintenancePlanEntity> maintenancePlanEntities = maintenancePlanService.findByName(name);
        PageInfo<MaintenancePlanEntity> pageInfo = new PageInfo<>(maintenancePlanEntities);
        return ResponseDataUtil.ok("查询维护计划信息列表成功", pageInfo);
    }


    @PostMapping("/import")
    @ResponseBody
    public Map<String, Object> importFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        maintenancePlanService.importFile(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
        return ResponseDataUtil.ok("导入维护计划信息成功");
    }

    @GetMapping("/template")
    public void templateFile(HttpServletResponse response) throws IOException {
        String fileName = "维护计划列表-模板" + DateUtil.now() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        maintenancePlanService.templateFile(response.getOutputStream());
    }

    @GetMapping("/export/{ids}")
    public void exportFile(@PathVariable Integer[] ids, HttpServletResponse response) throws IOException {
        String fileName = "维护计划列表-导出" + DateUtil.now() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        maintenancePlanService.exportFile(response.getOutputStream(), ids);
    }


    @GetMapping("/{id}/maintenance/plan/infos")
    @ResponseBody
    public Map<String, Object> findMaintenancePlanInfoById(@PathVariable Integer id, @RequestParam("page_number") Integer pageNumber,
                                                           @RequestParam("page_size") Integer pageSize,
                                                           @RequestParam(value = "order_by", required = false, defaultValue = "mpi.id DESC") String orderBy) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<MaintenancePlanInfoEntity> maintenancePlanInfoEntities = maintenancePlanInfoService.findByCondition(null, id, null);
        PageInfo<MaintenancePlanInfoEntity> pageInfo = new PageInfo<>(maintenancePlanInfoEntities);
        return ResponseDataUtil.ok("查询维护计划详情列表信息成功", pageInfo);
    }

}
