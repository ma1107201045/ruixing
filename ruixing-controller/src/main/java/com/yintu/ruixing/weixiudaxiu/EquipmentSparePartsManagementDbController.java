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
 * 应急备品管理->发货单
 *
 * @author:mlf
 * @date:2020/7/30 11:19
 */
@RestController
@RequestMapping("/equipment/spare/parts/management/dbs")
public class EquipmentSparePartsManagementDbController extends SessionController implements BaseController<EquipmentSparePartsManagementDbEntity, Integer> {

    @Autowired
    private EquipmentSparePartsManagementDbService equipmentSparePartsManagementDbService;
    @Autowired
    private EquipmentService equipmentService;

    @PostMapping
    public Map<String, Object> add(@Validated EquipmentSparePartsManagementDbEntity entity) {
        equipmentSparePartsManagementDbService.add(entity);
        return ResponseDataUtil.ok("添加应急备品管理发货单信息成功");
    }

    @Override
    public Map<String, Object> remove(Integer id) {
        return null;
    }

    @DeleteMapping("/{ids}")
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        equipmentSparePartsManagementDbService.remove(ids);
        return ResponseDataUtil.ok("删除应急备品管理发货单信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, @Validated EquipmentSparePartsManagementDbEntity entity) {
        equipmentSparePartsManagementDbService.edit(entity);
        return ResponseDataUtil.ok("修改应急备品管理发货单信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        EquipmentSparePartsManagementDbEntity equipmentSparePartsManagementDbEntity = equipmentSparePartsManagementDbService.findById(id);
        return ResponseDataUtil.ok("查询应急备品管理发货单信息成功", equipmentSparePartsManagementDbEntity);
    }

    @GetMapping
    public Map<String, Object> findById(@RequestParam("page_number") Integer pageNumber,
                                        @RequestParam("page_size") Integer pageSize,
                                        @RequestParam(value = "order_by", required = false, defaultValue = "espd.id DESC") String orderBy,
                                        @RequestParam(value = "equipment_name", required = false) String equipmentName) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<EquipmentSparePartsManagementDbEntity> equipmentSparePartsManagementDbEntities = equipmentSparePartsManagementDbService.findByCondition(null, equipmentName);
        PageInfo<EquipmentSparePartsManagementDbEntity> pageInfo = new PageInfo<>(equipmentSparePartsManagementDbEntities);
        return ResponseDataUtil.ok("查询应急备品管理发货单信息列表成功", pageInfo);
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
