package com.yintu.ruixing.weixiudaxiu;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 整改管理
 *
 * @author:mlf
 * @date:2020/7/28 17:05
 */
@RestController
@RequestMapping("/equipment/overhaul/managements")
public class EquipmentOverhaulManagementController extends SessionController implements BaseController<EquipmentOverhaulManagementEntityWithBLOBs, Integer> {
    @Autowired
    private EquipmentOverhaulManagementService equipmentOverhaulManagementService;
    @Autowired
    private EquipmentNumberService equipmentNumberService;

    @PostMapping
    public Map<String, Object> add(@Validated EquipmentOverhaulManagementEntityWithBLOBs entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        equipmentOverhaulManagementService.add(entity);
        return ResponseDataUtil.ok("添加整改管理信息成功");
    }

    @Override
    public Map<String, Object> remove(Integer id) {
        return null;
    }

    @DeleteMapping("/{ids}")
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        equipmentOverhaulManagementService.remove(ids);
        return ResponseDataUtil.ok("删除整改管理信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, @Validated EquipmentOverhaulManagementEntityWithBLOBs entity) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        equipmentOverhaulManagementService.edit(entity);
        return ResponseDataUtil.ok("修改整改管理信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        EquipmentOverhaulManagementEntityWithBLOBs equipmentOverhaulManagementEntityWithBLOBs = equipmentOverhaulManagementService.findById(id);
        return ResponseDataUtil.ok("查询整改管理信息成功", equipmentOverhaulManagementEntityWithBLOBs);
    }

    @GetMapping
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "eom.id DESC") String orderBy,
                                       @RequestParam(value = "equipment_number", required = false) String equipmentNumber) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<EquipmentOverhaulManagementEntityWithBLOBs> equipmentOverhaulManagementEntityWithBLOBs = equipmentOverhaulManagementService.findByCondition(null, equipmentNumber);
        PageInfo<EquipmentOverhaulManagementEntityWithBLOBs> pageInfo = new PageInfo<>(equipmentOverhaulManagementEntityWithBLOBs);
        return ResponseDataUtil.ok("查询整改管理信息列表成功", pageInfo);
    }

    /**
     * 查询器材编号全部信息
     *
     * @return
     */
    @GetMapping("/equipment/numbers")
    @ResponseBody
    public Map<String, Object> findEquipmentNumberAll() {
        List<EquipmentNumberEntity> equipmentNumberEntities = equipmentNumberService.findByCondition(null, null);
        equipmentNumberEntities = equipmentNumberEntities.stream()
                .sorted(Comparator.comparing(EquipmentNumberEntity::getId).reversed())
                .collect(Collectors.toList());
        return ResponseDataUtil.ok("查询器材编号信息列表成功", equipmentNumberEntities);
    }
}
