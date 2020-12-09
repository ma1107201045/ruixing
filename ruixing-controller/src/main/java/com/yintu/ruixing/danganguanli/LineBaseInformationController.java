package com.yintu.ruixing.danganguanli;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: mlf
 * @Date: 2020/12/8 15:54
 * @Version: 1.0
 */
@RestController
@RequestMapping("/line/base/information")
public class LineBaseInformationController implements BaseController<LineBaseInformationEntity, Integer> {
    @Autowired
    private LineBaseInformationService lineBaseInformationService;

    @PostMapping
    public Map<String, Object> add(LineBaseInformationEntity entity) {
        lineBaseInformationService.add(entity);
        return ResponseDataUtil.ok("添加线段基本信息成功");
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> remove(@PathVariable Integer id) {
        lineBaseInformationService.remove(id);
        return ResponseDataUtil.ok("删除线段基本信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, LineBaseInformationEntity entity) {
        lineBaseInformationService.remove(id);
        return ResponseDataUtil.ok("修改线段基本信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        return ResponseDataUtil.ok("查询线段基本信息成功", lineBaseInformationService.findById(id));
    }

    @GetMapping
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "lbi.id DESC") String orderBy) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<LineBaseInformationEntity> lineBaseInformationEntities = lineBaseInformationService.findByExample(null);
        PageInfo<LineBaseInformationEntity> pageInfo = new PageInfo<>(lineBaseInformationEntities);
        return ResponseDataUtil.ok("查询线段基本信息列表成功", pageInfo);
    }

}
