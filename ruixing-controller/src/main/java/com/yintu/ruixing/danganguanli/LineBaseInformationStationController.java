package com.yintu.ruixing.danganguanli;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: mlf
 * @Date: 2020/12/24 18:29
 * @Version: 1.0
 */
@RestController
@RequestMapping("/line/base/information/station")
public class LineBaseInformationStationController extends SessionController {

    @Autowired
    private LineBaseInformationStationService lineBaseInformationStationService;
    @Autowired
    private DianWuDuanService dianWuDuanService;

    @PostMapping
    @ResponseBody
    public Map<String, Object> add(@Validated LineBaseInformationStationEntity entity, @RequestParam Integer[] unitIds) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineBaseInformationStationService.add(entity, unitIds);
        return ResponseDataUtil.ok("添加车站基本信息成功");
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Map<String, Object> remove(@PathVariable Integer id) {
        lineBaseInformationStationService.remove(id);
        return ResponseDataUtil.ok("删除车站基本信息成功");
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Map<String, Object> edit(@PathVariable Integer id, @Validated LineBaseInformationStationEntity entity, @RequestParam Integer[] unitIds) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineBaseInformationStationService.edit(entity, unitIds);
        return ResponseDataUtil.ok("修改车站基本信息成功");
    }

    @GetMapping
    @ResponseBody
    public Map<String, Object> findHistoryByExample(@RequestParam("page_number") Integer pageNumber,
                                                    @RequestParam("page_size") Integer pageSize,
                                                    @RequestParam(value = "order_by", required = false, defaultValue = "lbi.id DESC") String orderBy,
                                                    @RequestParam("tid") Integer tid,
                                                    @RequestParam("id") Integer id,
                                                    @RequestParam("name") String name) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<LineBaseInformationStationEntity> LineBaseInformationStationEntities = lineBaseInformationStationService.findHistoryByExample(tid, id, name, null);
        PageInfo<LineBaseInformationStationEntity> pageInfo = new PageInfo<>(LineBaseInformationStationEntities);
        return ResponseDataUtil.ok("查询车站基本信息列表成功", pageInfo);
    }

    @GetMapping("/dianwuduans")
    @ResponseBody
    public Map<String, Object> findDianWuDuans() {
        List<DianWuDuanEntity> dianWuDuanEntities = dianWuDuanService.findAll();
        return ResponseDataUtil.ok("查询线段单位信息列表成功", dianWuDuanEntities);
    }

}
