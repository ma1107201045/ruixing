package com.yintu.ruixing.weixiudaxiu;

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
 * 应急备品管理->应急备品管理
 *
 * @author:mlf
 * @date:2020/09/18 11:19
 */
@RestController
@RequestMapping("/equipment/spare/parts/managements")
public class EquipmentSparePartsManagementController extends SessionController implements BaseController<EquipmentSparePartsManagementEntity, Integer> {

    @Autowired
    private EquipmentSparePartsManagementService equipmentSparePartsManagementService;

    @PostMapping
    public Map<String, Object> add(@Validated EquipmentSparePartsManagementEntity entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        equipmentSparePartsManagementService.add(entity, this.getLoginTrueName());
        return ResponseDataUtil.ok("添加应急备品管理信息成功");
    }

    @Override
    public Map<String, Object> remove(Integer id) {
        return null;
    }

    @DeleteMapping("/{ids}")
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        equipmentSparePartsManagementService.remove(ids);
        return ResponseDataUtil.ok("删除应急备品管理信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, @Validated EquipmentSparePartsManagementEntity entity) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        equipmentSparePartsManagementService.edit(entity);
        return ResponseDataUtil.ok("修改应急备品管理信息成功");
    }

    /**
     * 增加库存数量
     *
     * @param id        id
     * @param putAmount 增量值
     * @return 返回成功提示
     */
    @PutMapping("/{id}/put")
    public Map<String, Object> put(@PathVariable Integer id, Integer putAmount) {
        equipmentSparePartsManagementService.put(this.getLoginUserName(), this.getLoginTrueName(), id, putAmount);
        return ResponseDataUtil.ok("添加应急备品管理库存数量信息成功");
    }


    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        EquipmentSparePartsManagementEntity equipmentSparePartsManagementEntity = equipmentSparePartsManagementService.findById(id);
        return ResponseDataUtil.ok("查询应急备品管理成功", equipmentSparePartsManagementEntity);
    }

    @GetMapping
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "espm.id DESC") String orderBy,
                                       @RequestParam(value = "material_number", required = false) String materialNumber,
                                       @RequestParam(value = "equipment_name", required = false) String equipmentName) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<EquipmentSparePartsManagementEntity> equipmentSparePartsManagementEntities = equipmentSparePartsManagementService.findByCondition(materialNumber, equipmentName);
        PageInfo<EquipmentSparePartsManagementEntity> pageInfo = new PageInfo<>(equipmentSparePartsManagementEntities);
        return ResponseDataUtil.ok("查询应急备品管理信息列表成功", pageInfo);
    }

    @GetMapping("/{id}/record")
    public Map<String, Object> findRecordById(@PathVariable Integer id) {

        return null;
    }

}
