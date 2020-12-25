package com.yintu.ruixing.danganguanli;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: mlf
 * @Date: 2020/12/24 18:29
 * @Version: 1.0
 */
@Controller
@RequestMapping("/line/base/information/station")
public class LineBaseInformationStationController extends SessionController {

    @Autowired
    private LineBaseInformationStationService lineBaseInformationStationService;
    @Autowired
    private DianWuDuanService dianWuDuanService;

    @PostMapping
    @ResponseBody
    public Map<String, Object> add(@Validated LineBaseInformationStationEntity entity, @RequestParam Integer[] unitIds) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineBaseInformationStationService.add(entity, unitIds);
        return ResponseDataUtil.ok("添加车站基本信息成功");
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Map<String, Object> remove(@PathVariable Integer id) {
        lineBaseInformationStationService.remove(id);
        return ResponseDataUtil.ok("删除车站基本信息成功");
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Map<String, Object> edit(@PathVariable Integer id, @Validated LineBaseInformationStationEntity entity, @RequestParam Integer[] unitIds) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineBaseInformationStationService.edit(entity, unitIds);
        return ResponseDataUtil.ok("修改车站基本信息成功");
    }

    @GetMapping
    @ResponseBody
    public Map<String, Object> findByExample(@RequestParam("page_number") Integer pageNumber,
                                             @RequestParam("page_size") Integer pageSize,
                                             @RequestParam(value = "order_by", required = false, defaultValue = "lbi.id DESC") String orderBy,
                                             @RequestParam("line_base_information_id") Integer lineBaseInformationId,
                                             @RequestParam("id") Integer id,
                                             @RequestParam("name") String name) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<LineBaseInformationStationEntity> LineBaseInformationStationEntities = lineBaseInformationStationService.findByExample(lineBaseInformationId, id, name, null);
        PageInfo<LineBaseInformationStationEntity> pageInfo = new PageInfo<>(LineBaseInformationStationEntities);
        return ResponseDataUtil.ok("查询车站基本信息列表成功", pageInfo);
    }

    @GetMapping("/dianwuduans")
    @ResponseBody
    public Map<String, Object> findDianWuDuans() {
        List<DianWuDuanEntity> dianWuDuanEntities = dianWuDuanService.findAll();
        return ResponseDataUtil.ok("查询线段单位信息列表成功", dianWuDuanEntities);
    }

    @PostMapping("/import")
    @ResponseBody
    public Map<String, Object> importFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String[][] context = lineBaseInformationStationService.importFile(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
        return ResponseDataUtil.ok("获取导入线段基础信息成功", context);
    }

    @PostMapping("/import/data")
    @ResponseBody
    public Map<String, Object> importDate(@RequestBody Map<String, Object> map) {
        lineBaseInformationStationService.importDate((Integer) map.get("xid"), (List<List<String>>) map.get("data"), this.getLoginUserName());
        return ResponseDataUtil.ok("导入线段基础信息成功");
    }

    @GetMapping("/template")
    public void templateFile(HttpServletResponse response) throws IOException {
        String fileName = "车站基础信息列表-模板" + DateUtil.now() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        lineBaseInformationStationService.templateFile(response.getOutputStream());
    }

    @GetMapping("/export/{ids}")
    public void exportFile(@PathVariable Integer[] ids, HttpServletResponse response) throws IOException {
        String fileName = "车站基础信息列表-导出" + DateUtil.now() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        lineBaseInformationStationService.exportFile(response.getOutputStream(), ids);
    }


}
