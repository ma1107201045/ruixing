package com.yintu.ruixing.weixiudaxiu;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.guzhangzhenduan.CheZhanEntity;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuEntity;
import com.yintu.ruixing.guzhangzhenduan.XianDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.DataStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 现场故障调查管理
 *
 * @author:mlf
 * @date:2020/7/30 19:30
 */
@RestController
@RequestMapping("/equipment/field/faultInvestigation/managements")
public class EquipmentFieldFaultInvestigationManagementController extends SessionController implements BaseController<EquipmentFieldFaultInvestigationManagementEntity, Integer> {

    @Autowired
    private EquipmentFieldFaultInvestigationManagementService fieldFaultInvestigationManagementService;
    @Autowired
    private DataStatsService dataStatsService;

    @PostMapping
    public Map<String, Object> add(@Validated EquipmentFieldFaultInvestigationManagementEntity entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        fieldFaultInvestigationManagementService.add(entity);
        return ResponseDataUtil.ok("添加现场故障调查管理信息成功");
    }

    @Override
    public Map<String, Object> remove(Integer id) {
        return null;
    }

    @DeleteMapping("/{ids}")
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        fieldFaultInvestigationManagementService.remove(ids);
        return ResponseDataUtil.ok("删除现场故障调查管理信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, @Validated EquipmentFieldFaultInvestigationManagementEntity entity) {
        fieldFaultInvestigationManagementService.edit(entity);
        return ResponseDataUtil.ok("修改现场故障调查管理信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        EquipmentFieldFaultInvestigationManagementEntity equipmentFieldFaultInvestigationManagementEntity = fieldFaultInvestigationManagementService.findById(id);
        return ResponseDataUtil.ok("查询现场故障调查管理信息成功", equipmentFieldFaultInvestigationManagementEntity);
    }

    @GetMapping
    public Map<String, Object> findByStartDateAndEndDate(@RequestParam("page_number") Integer pageNumber,
                                                         @RequestParam("page_size") Integer pageSize,
                                                         @RequestParam(value = "order_by", required = false, defaultValue = "effim.id DESC") String orderBy,
                                                         @RequestParam(value = "start_date", required = false) Date startDate,
                                                         @RequestParam(value = "end_date", required = false) Date endDate) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<EquipmentFieldFaultInvestigationManagementEntity> fieldFaultInvestigationManagementEntities = fieldFaultInvestigationManagementService.findByCondition(startDate, endDate);
        PageInfo<EquipmentFieldFaultInvestigationManagementEntity> pageInfo = new PageInfo<>(fieldFaultInvestigationManagementEntities);
        return ResponseDataUtil.ok("查询现场故障调查管理信息列表成功", pageInfo);
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


}
