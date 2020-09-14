package com.yintu.ruixing.weixiudaxiu;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
import com.yintu.ruixing.weixiudaxiu.EquipmentEntity;
import com.yintu.ruixing.weixiudaxiu.EquipmentNumberEntity;
import com.yintu.ruixing.weixiudaxiu.EquipmentNumberService;
import com.yintu.ruixing.weixiudaxiu.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 器材编号管理
 *
 * @author:mlf
 * @date:2020/7/13 11:13
 */
@RestController
@RequestMapping("/equipment/numbers")
public class EquipmentNumberController extends SessionController implements BaseController<EquipmentNumberEntity, Integer> {
    @Autowired
    private EquipmentNumberService equipmentNumberService;
    @Autowired
    private EquipmentNumberRecordService equipmentNumberRecordService;
    @Autowired
    private DataStatsService dataStatsService;

    @PostMapping
    public Map<String, Object> add(@Validated EquipmentNumberEntity entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        equipmentNumberService.add(entity);
        return ResponseDataUtil.ok("添加器材编号信息成功");
    }

    @Override
    public Map<String, Object> remove(Integer id) {
        return null;
    }

    @DeleteMapping("/{ids}")
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        equipmentNumberService.remove(ids);
        return ResponseDataUtil.ok("删除器材编号信息成功");

    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, @Validated EquipmentNumberEntity entity) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        equipmentNumberService.edit(entity);
        return ResponseDataUtil.ok("修改器材编号信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        EquipmentNumberEntity equipmentNumberEntity = equipmentNumberService.findById(id);
        return ResponseDataUtil.ok("查询器材编号信息成功", equipmentNumberEntity);
    }

    @GetMapping
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "en.id DESC") String orderBy,
                                       @RequestParam(value = "equipment_number", required = false) String equipmentNumber) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<EquipmentNumberEntity> equipmentNumberEntities = equipmentNumberService.findByCondition(null, equipmentNumber);
        PageInfo<EquipmentNumberEntity> pageInfo = new PageInfo<>(equipmentNumberEntities);
        return ResponseDataUtil.ok("查询器材编号列表信息成功", pageInfo);

    }


    @GetMapping("/railways/bureau")
    public Map<String, Object> findAllTieLuJu() {
        List<TieLuJuEntity> tieLuJuEntities = dataStatsService.findAllTieLuJu();
        return ResponseDataUtil.ok("查询铁路局成功", tieLuJuEntities);
    }

    //根据铁路局的id  查询对应的电务段
    @GetMapping("/signal/depot")
    public Map<String, Object> findDianWuDuanByTid(@RequestParam Integer railwaysBureauId) {
        List<DianWuDuanEntity> dianWuDuanEntities = dataStatsService.findDianWuDuanByTid(railwaysBureauId);
        return ResponseDataUtil.ok("查询电务段成功", dianWuDuanEntities);
    }

    //根据电务段的id  查找线段的名字和id
    @GetMapping("/special/railway/line")
    public Map<String, Object> findXianDuanByDid(@RequestParam Integer signalDepotId) {
        List<XianDuanEntity> xianDuanEntities = dataStatsService.findXianDuanByDid(signalDepotId);
        return ResponseDataUtil.ok("查询线段成功", xianDuanEntities);
    }

    //根据线段的id  查找车站的名字和id
    @GetMapping("/station")
    public Map<String, Object> findCheZhanByXid(@RequestParam Integer specialRailwayLineId) {
        List<CheZhanEntity> cheZhanEntities = dataStatsService.findCheZhanByXid(specialRailwayLineId);
        return ResponseDataUtil.ok("查询车站成功", cheZhanEntities);
    }

    @GetMapping("/{id}/equipment/number/records")
    public Map<String, Object> findEquipmentNumberRecord(@PathVariable Integer id) {
        List<EquipmentNumberRecordEntity> equipmentNumberRecordEntities = equipmentNumberRecordService.findByCondition(null, id);
        return ResponseDataUtil.ok("查询器材编号更换记录列表信息成功", equipmentNumberRecordEntities);
    }

}
