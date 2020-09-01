package com.yintu.ruixing.jiejuefangan;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuEntity;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 投招标技术支持
 *
 * @author:mlf
 * @date:2020/7/2 11:54
 */
@RestController
@RequestMapping("/biddings")
public class BiddingController extends SessionController implements BaseController<BiddingEntity, Integer> {
    @Autowired
    private BiddingService biddingService;
    @Autowired
    private SolutionLogService solutionLogService;
    @Autowired
    private TieLuJuService tieLuJuService;

    @PostMapping
    public Map<String, Object> add(@Validated BiddingEntity entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        biddingService.add(entity, this.getLoginTrueName());
        return ResponseDataUtil.ok("添加投招标技术支持信息成功");
    }

    @Override
    public Map<String, Object> remove(Integer id) {
        return null;
    }

    @DeleteMapping("/{ids}")
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        biddingService.remove(ids);
        return ResponseDataUtil.ok("删除售前技术支持信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, @Validated BiddingEntity entity) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        biddingService.edit(entity, this.getLoginTrueName());
        return ResponseDataUtil.ok("修改投招标技术支持信息成功");
    }


    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        BiddingEntity biddingEntity = biddingService.findById(id);
        return ResponseDataUtil.ok("查询投招标技术支持信息成功", biddingEntity);
    }

    @GetMapping
    public Map<String, Object> findTree() {
        List<TreeNodeUtil> treeNodeUtils = biddingService.findByTree();
        return ResponseDataUtil.ok("查询投招标技术支持信息树成功", treeNodeUtils);
    }


    @GetMapping("/list")
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "id DESC") String orderBy,
                                       @RequestParam(value = "year", required = false) Integer year,
                                       @RequestParam(value = "project_name", required = false) String projectName) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<BiddingEntity> biddingEntities = biddingService.findByExample(year, projectName);
        PageInfo<BiddingEntity> pageInfo = new PageInfo<>(biddingEntities);
        return ResponseDataUtil.ok("查询投招标技术支持信息列表成功", pageInfo);
    }
    @GetMapping("/{id}/log")
    @ResponseBody
    public Map<String, Object> findLogByExample(@PathVariable Integer id) {
        List<SolutionLogEntity> solutionLogEntities = solutionLogService.findByExample(new SolutionLogEntity(null, null, null, (short) 2, (short) 1, id, null));
        return ResponseDataUtil.ok("查询招投标技术支持文件日志信息列表成功", solutionLogEntities);
    }

    @GetMapping("/tielujus")
    public Map<String, Object> findTieLuJus() {
        List<TieLuJuEntity> tieLuJuEntities = tieLuJuService.findAllTieLuJu();
        return ResponseDataUtil.ok("查询铁路局列表信息成功", tieLuJuEntities);
    }


}
