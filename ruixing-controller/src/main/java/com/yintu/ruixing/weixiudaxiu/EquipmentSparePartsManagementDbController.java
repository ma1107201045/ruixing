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


    @Override
    public Map<String, Object> add(EquipmentSparePartsManagementDbEntity entity) {
        return null;
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
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "espmd.id DESC") String orderBy,
                                       @RequestParam(value = "equipment_number", required = false) String equipmentNumber,
                                       @RequestParam(value = "equipment_name", required = false) String equipmentName) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<EquipmentSparePartsManagementDbEntity> equipmentSparePartsManagementDbEntities = equipmentSparePartsManagementDbService.findByCondition(null, equipmentNumber, equipmentName);
        PageInfo<EquipmentSparePartsManagementDbEntity> pageInfo = new PageInfo<>(equipmentSparePartsManagementDbEntities);
        return ResponseDataUtil.ok("查询应急备品管理发货单信息列表成功", pageInfo);
    }

}
