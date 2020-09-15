package com.yintu.ruixing.weixiudaxiu;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
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

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 故障器材特征管理
 *
 * @author:mlf
 * @date:2020/7/29 17:41
 */
@RestController
@RequestMapping("/equipment/fault/managements")
public class EquipmentFaultManagementController extends SessionController {
    @Autowired
    private EquipmentFaultManagementService equipmentFaultManagementService;

    @GetMapping
    public Map<String, Object> findByCondition(@RequestParam("page_number") Integer pageNumber,
                                               @RequestParam("page_size") Integer pageSize,
                                               @RequestParam(value = "order_by", required = false, defaultValue = "efm.id DESC") String orderBy,
                                               @RequestParam(value = "start_date", required = false) Date startDate,
                                               @RequestParam(value = "end_date", required = false) Date endDate) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<EquipmentFaultManagementEntity> equipmentFaultManagementEntities = equipmentFaultManagementService.findByCondition(startDate, endDate);
        PageInfo<EquipmentFaultManagementEntity> pageInfo = new PageInfo<>(equipmentFaultManagementEntities);
        return ResponseDataUtil.ok("查询故障器材特征管理信息列表成功", pageInfo);
    }

}
