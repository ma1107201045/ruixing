package com.yintu.ruixing.jiejuefangan;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 售前技术支持
 *
 * @author:mlf
 * @date:2020/6/30 19:12
 */
@RestController
@RequestMapping("/pre/sales")
public class PreSaleController extends SessionController implements BaseController<PreSaleEntity, Integer> {

    @Autowired
    private PreSaleService preSaleService;
    @Autowired
    private SolutionLogService solutionLogService;


    @PostMapping
    public Map<String, Object> add(@Validated PreSaleEntity entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        preSaleService.add(entity, this.getLoginTrueName());
        return ResponseDataUtil.ok("添加售前技术支持信息成功");
    }

    @Override
    public Map<String, Object> remove(Integer id) {
        return null;
    }

    @DeleteMapping("/{ids}")
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        preSaleService.remove(ids);
        return ResponseDataUtil.ok("删除售前技术支持信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, @Validated PreSaleEntity entity) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        preSaleService.edit(entity, this.getLoginTrueName());
        return ResponseDataUtil.ok("修改售前技术支持信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        PreSaleEntity preSaleEntity = preSaleService.findById(id);
        return ResponseDataUtil.ok("查询售前技术支持信息成功", preSaleEntity);
    }

    @GetMapping
    public Map<String, Object> findTree() {
        List<TreeNodeUtil> treeNodeUtils = preSaleService.findByTree();
        return ResponseDataUtil.ok("查询售前技术支持信息树成功", treeNodeUtils);
    }

    @GetMapping("/list")
    public Map<String, Object> findByExample(@RequestParam("page_number") Integer pageNumber,
                                             @RequestParam("page_size") Integer pageSize,
                                             @RequestParam(value = "order_by", required = false, defaultValue = "id DESC") String orderBy,
                                             @RequestParam(value = "year", required = false) Integer year,
                                             @RequestParam(value = "project_name", required = false) String projectName) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<PreSaleEntity> preSaleEntities = preSaleService.findByExample(year, projectName);
        PageInfo<PreSaleEntity> pageInfo = new PageInfo<>(preSaleEntities);
        return ResponseDataUtil.ok("查询售前技术支持项目信息列表成功", pageInfo);
    }

    @GetMapping("/{id}/log")
    public Map<String, Object> findLogByExample(@PathVariable Integer id) {
        List<SolutionLogEntity> solutionLogEntities = solutionLogService.findByExample(new SolutionLogEntity(null, null, null, (short) 1, (short) 1, id, null));
        return ResponseDataUtil.ok("查询售前技术支持项目日志信息列表成功", solutionLogEntities);
    }

}
