package com.yintu.ruixing.danganguanli;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/25 9:50
 */
@RestController
@RequestMapping("/line/technology/status/system/manufacturers")
public class LineTechnologyStatusSystemManufacturerController extends SessionController implements BaseController<LineTechnologyStatusSystemManufacturerEntity, Integer> {
    @Autowired
    private LineTechnologyStatusSystemManufacturerService lineTechnologyStatusSystemManufacturerService;

    @PostMapping
    public Map<String, Object> add(@Validated LineTechnologyStatusSystemManufacturerEntity entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineTechnologyStatusSystemManufacturerService.add(entity);
        return ResponseDataUtil.ok("添加线段技术状态系统厂家信息成功");
    }

    @Override
    public Map<String, Object> remove(Integer id) {
        return null;
    }

    @DeleteMapping("/{ids}")
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        lineTechnologyStatusSystemManufacturerService.remove(ids);
        return ResponseDataUtil.ok("删除线段技术状态系统厂家信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, LineTechnologyStatusSystemManufacturerEntity entity) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineTechnologyStatusSystemManufacturerService.edit(entity);
        return ResponseDataUtil.ok("修改线段技术状态系统厂家信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        LineTechnologyStatusSystemManufacturerEntity entity = lineTechnologyStatusSystemManufacturerService.findById(id);
        return ResponseDataUtil.ok("查询线段技术状态系统厂家信息成功", entity);
    }

    @GetMapping
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "ltssm.id DESC") String orderBy,
                                       @RequestParam(value = "name", required = false) String name,
                                       @RequestParam(value = "cid", required = false) Integer cid) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<LineTechnologyStatusSystemManufacturerEntity> lineTechnologyStatusSystemManufacturerEntities = lineTechnologyStatusSystemManufacturerService.findByExample(name, cid);
        PageInfo<LineTechnologyStatusSystemManufacturerEntity> pageInfo = new PageInfo<>(lineTechnologyStatusSystemManufacturerEntities);
        return ResponseDataUtil.ok("查询线段技术状态系统厂家信息列表成功", pageInfo);
    }
}
