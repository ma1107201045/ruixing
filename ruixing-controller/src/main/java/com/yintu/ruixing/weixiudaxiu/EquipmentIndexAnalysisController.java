package com.yintu.ruixing.weixiudaxiu;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/equipments/index/analysis")
public class EquipmentIndexAnalysisController extends SessionController {
    @Autowired
    private EquipmentIndexAnalysisService equipmentIndexAnalysisService;

    @GetMapping
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "eia.id DESC") String orderBy,
                                       @RequestParam(value = "q_name", required = false) String quDuanYunYingName) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<EquipmentIndexAnalysisEntity> equipmentIndexAnalysisEntities = equipmentIndexAnalysisService.findByCondition(quDuanYunYingName);
        PageInfo<EquipmentIndexAnalysisEntity> pageInfo = new PageInfo<>(equipmentIndexAnalysisEntities);
        return ResponseDataUtil.ok("查询区段指标性分析列表信息成功", pageInfo);

    }

}
