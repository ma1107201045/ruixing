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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 备品实验
 *
 * @author:mlf
 * @date:2020/7/11 14:23
 */
@RestController
@RequestMapping("/spare/parts")
public class SparePartsController extends SessionController implements BaseController<SparePartsEntity, Integer> {

    @Autowired
    private SparePartsService sparePartsService;
    @Autowired
    private CheZhanService cheZhanService;
    @Autowired
    private EquipmentService equipmentService;

    @PostMapping
    public Map<String, Object> add(@Validated SparePartsEntity entity) {
        sparePartsService.add(entity);
        return ResponseDataUtil.ok("添加备品实验信息成功");
    }

    @Override
    public Map<String, Object> remove(Integer id) {

        return null;
    }

    @DeleteMapping("/{ids}")
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        sparePartsService.delete(ids);
        return ResponseDataUtil.ok("删除备品实验信息成功");
    }


    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, @Validated SparePartsEntity entity) {
        sparePartsService.edit(entity);
        return ResponseDataUtil.ok("更新备品实验信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        SparePartsEntity sparePartsEntity = sparePartsService.findById(id);
        return ResponseDataUtil.ok("查询备品实验信息成功", sparePartsEntity);
    }

    @GetMapping
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "sp.id DESC") String orderBy,
                                       @RequestParam(value = "equipment_name", required = false) String equipmentName) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<SparePartsEntity> sparePartsEntities = sparePartsService.findByCondition(null, equipmentName);
        PageInfo<SparePartsEntity> pageInfo = new PageInfo<>(sparePartsEntities);
        return ResponseDataUtil.ok("查询备品实验列表信息成功", pageInfo);
    }

    @PostMapping("/import")
    @ResponseBody
    public Map<String, Object> importFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        sparePartsService.importFile(multipartFile.getInputStream(),multipartFile.getOriginalFilename());
        return ResponseDataUtil.ok("导入备品实验信息成功");
    }

    @GetMapping("/template")
    public void templateFile(HttpServletResponse response) throws IOException {
        String fileName = "备品实验列表-模板" + DateUtil.now() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO-8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), StandardCharsets.ISO_8859_1));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        sparePartsService.templateFile(response.getOutputStream());
    }

    @GetMapping("/export/{ids}")
    public void exportFile(@PathVariable Integer[] ids, HttpServletResponse response) throws IOException {
        String fileName = "备品实验列表-导出" + DateUtil.now() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO-8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), StandardCharsets.ISO_8859_1));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        sparePartsService.exportFile(response.getOutputStream(), ids);
    }

    /**
     * 查询全部车站信息
     *
     * @return
     */
    @GetMapping("/chezhans")
    @ResponseBody
    public Map<String, Object> findCheZhanAll() {
        List<CheZhanEntity> cheZhanEntities = cheZhanService.findAll();
        return ResponseDataUtil.ok("查询车站信息列表成功", cheZhanEntities);
    }


    /**
     * 查询设备全部信息
     *
     * @return
     */
    @GetMapping("/equipments")
    @ResponseBody
    public Map<String, Object> findEquipmentAll() {
        List<EquipmentEntity> equipmentEntities = equipmentService.findAll();
        return ResponseDataUtil.ok("查询设备信息列表成功", equipmentEntities);
    }
}
