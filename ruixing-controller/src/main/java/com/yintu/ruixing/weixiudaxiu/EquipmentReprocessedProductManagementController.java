package com.yintu.ruixing.weixiudaxiu;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.SessionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 返修品管理
 *
 * @author:mlf
 * @date:2020/8/3 16:50
 */
@RestController
@RequestMapping("/equipment/reprocessed/product/managements")
public class EquipmentReprocessedProductManagementController extends SessionController implements BaseController<EquipmentReprocessedProductManagementEntityWithBLOBs, Integer> {

    @Autowired
    private EquipmentReprocessedProductManagementService equipmentReprocessedProductManagementService;
    @Autowired
    private EquipmentNumberService equipmentNumberService;

    @PostMapping
    public Map<String, Object> add(@Validated EquipmentReprocessedProductManagementEntityWithBLOBs entity) {
        equipmentReprocessedProductManagementService.add(entity);
        return ResponseDataUtil.ok("添加返修品信息成功");
    }

    @Override
    public Map<String, Object> remove(Integer id) {
        return null;
    }

    @DeleteMapping("/{ids}")
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        equipmentReprocessedProductManagementService.remove(ids);
        return ResponseDataUtil.ok("删除返修品信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, @Validated EquipmentReprocessedProductManagementEntityWithBLOBs entity) {
        equipmentReprocessedProductManagementService.edit(entity);
        return ResponseDataUtil.ok("修改返修品信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        EquipmentReprocessedProductManagementEntity equipmentReprocessedProductManagementEntity = equipmentReprocessedProductManagementService.findById(id);
        return ResponseDataUtil.ok("查询返修品信息成功", equipmentReprocessedProductManagementEntity);
    }

    @GetMapping
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "erpm.id DESC") String orderBy,
                                       @RequestParam(value = "equipment_number", required = false) String equipmentNumber) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<EquipmentReprocessedProductManagementEntityWithBLOBs> equipmentReprocessedProductManagementEntityWithBLOBs = equipmentReprocessedProductManagementService.findByCondition(equipmentNumber);
        PageInfo<EquipmentReprocessedProductManagementEntityWithBLOBs> pageInfo = new PageInfo<>(equipmentReprocessedProductManagementEntityWithBLOBs);
        return ResponseDataUtil.ok("查询返修品列表信息成功", pageInfo);
    }


    /**
     * 查询器材编号全部信息
     *
     * @return 返回信息
     */
    @GetMapping("/equipment/numbers")
    @ResponseBody
    public Map<String, Object> findEquipmentNumberAll() {
        List<EquipmentNumberEntity> equipmentNumberEntities = equipmentNumberService.findByCondition(null, null);
        return ResponseDataUtil.ok("查询器材编号信息列表成功", equipmentNumberEntities);
    }
}
