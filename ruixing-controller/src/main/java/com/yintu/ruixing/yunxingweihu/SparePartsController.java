package com.yintu.ruixing.yunxingweihu;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
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
 * 备品实验
 *
 * @author:mlf
 * @date:2020/7/11 14:23
 */
@Controller
@RequestMapping("/spare/parts")
public class SparePartsController extends SessionController implements BaseController<SparePartsEntity, Integer> {

    @Autowired
    private SparePartsService sparePartsService;

    @Autowired
    private SparePartsInfoService sparePartsInfoService;

    @Autowired
    private DataStatsService dataStatsService;

    @PostMapping
    @ResponseBody
    public Map<String, Object> add(@Validated SparePartsEntity entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        entity.setIsStart((short) 1);
        sparePartsService.add(entity);
        return ResponseDataUtil.ok("添加备品试验信息成功");
    }

    @Override
    public Map<String, Object> remove(Integer id) {
        return null;
    }


    @DeleteMapping("/{ids}")
    @ResponseBody
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        sparePartsService.remove(ids);
        return ResponseDataUtil.ok("删除备品试验信息成功");
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Map<String, Object> edit(@PathVariable Integer id, @Validated SparePartsEntity entity) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        sparePartsService.edit(entity);
        return ResponseDataUtil.ok("修改备品试验信息成功");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Map<String, Object> findById(@PathVariable Integer id) {
        SparePartsEntity sparePartsEntity = sparePartsService.findById(id);
        return ResponseDataUtil.ok("查询备品试验信息成功", sparePartsEntity);
    }


    @GetMapping
    @ResponseBody
    public Map<String, Object> findByAll(@RequestParam("page_number") Integer pageNumber,
                                         @RequestParam("page_size") Integer pageSize,
                                         @RequestParam(value = "order_by", required = false, defaultValue = "mp.id DESC") String orderBy,
                                         @RequestParam(value = "name", required = false) String name) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<SparePartsEntity> sparePartsEntities = sparePartsService.findByExample(null, name);
        PageInfo<SparePartsEntity> pageInfo = new PageInfo<>(sparePartsEntities);
        return ResponseDataUtil.ok("查询备品试验信息列表成功", pageInfo);
    }


    @PostMapping("/import")
    @ResponseBody
    public Map<String, Object> importFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String[][] context = sparePartsService.importFile(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
        return ResponseDataUtil.ok("获取导入备品试验信息成功", context);
    }

    @PostMapping("/import/data")
    @ResponseBody
    public Map<String, Object> importDate(@RequestBody String[][] context) {
        sparePartsService.importDate(context, this.getLoginUserName());
        return ResponseDataUtil.ok("导入备品试验信息成功");
    }


    @GetMapping("/template")
    public void templateFile(HttpServletResponse response) throws IOException {
        String fileName = "备品试验列表-模板" + DateUtil.now() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        sparePartsService.templateFile(response.getOutputStream());
    }

    @GetMapping("/export/{ids}")
    public void exportFile(@PathVariable Integer[] ids, HttpServletResponse response) throws IOException {
        String fileName = "备品试验列表-导出" + DateUtil.now() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        sparePartsService.exportFile(response.getOutputStream(), ids);
    }


    @GetMapping("/{id}/spare/parts/infos")
    @ResponseBody
    public Map<String, Object> findMaintenancePlanInfoById(@PathVariable Integer id, @RequestParam("page_number") Integer pageNumber,
                                                           @RequestParam("page_size") Integer pageSize,
                                                           @RequestParam(value = "order_by", required = false, defaultValue = "spi.id DESC") String orderBy,
                                                           @RequestParam(value = "context", required = false) String context) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<SparePartsInfoEntity> sparePartsInfoEntities = sparePartsInfoService.findByCondition(null, context, id, null);
        PageInfo<SparePartsInfoEntity> pageInfo = new PageInfo<>(sparePartsInfoEntities);
        return ResponseDataUtil.ok("查询备品试验记录列表信息成功", pageInfo);
    }

    @GetMapping("/railways/bureau")
    @ResponseBody
    public Map<String, Object> findAllTieLuJu() {
        List<TieLuJuEntity> tieLuJuEntities = dataStatsService.findAllTieLuJu();
        return ResponseDataUtil.ok("查询铁路局成功", tieLuJuEntities);
    }

    //根据铁路局的id  查询对应的电务段
    @GetMapping("/signal/depot")
    @ResponseBody
    public Map<String, Object> findDianWuDuanByTid(@RequestParam Integer railwaysBureauId) {
        List<DianWuDuanEntity> dianWuDuanEntities = dataStatsService.findDianWuDuanByTid(railwaysBureauId);
        return ResponseDataUtil.ok("查询电务段成功", dianWuDuanEntities);
    }

    //根据电务段的id  查找线段的名字和id
    @GetMapping("/special/railway/line")
    @ResponseBody
    public Map<String, Object> findXianDuanByDid(@RequestParam Integer signalDepotId) {
        List<XianDuanEntity> xianDuanEntities = dataStatsService.findXianDuanByDid(signalDepotId);
        return ResponseDataUtil.ok("查询线段成功", xianDuanEntities);
    }

    //根据线段的id  查找车站的名字和id
    @GetMapping("/station")
    @ResponseBody
    public Map<String, Object> findCheZhanByXid(@RequestParam Integer specialRailwayLineId) {
        List<CheZhanEntity> cheZhanEntities = dataStatsService.findCheZhanByXid(specialRailwayLineId);
        return ResponseDataUtil.ok("查询车站成功", cheZhanEntities);
    }
}
