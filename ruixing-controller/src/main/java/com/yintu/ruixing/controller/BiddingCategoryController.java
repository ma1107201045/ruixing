package com.yintu.ruixing.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.entity.SolutionEntity;
import com.yintu.ruixing.entity.SolutionStatusEntity;
import com.yintu.ruixing.service.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author:mlf
 * @date:2020/6/28 16:48
 * 解决方案：招投标技术支持模块
 */
@RestController
@RequestMapping("/bidding/category")
public class BiddingCategoryController extends SessionController {
    @Autowired
    private SolutionService solutionService;

    private final Short FLAG = new Short("2");//模块标识


    @PostMapping
    public Map<String, Object> add(SolutionEntity solutionEntity) {
        Integer parentId = solutionEntity.getParentId();
        String name = solutionEntity.getName();
        if (parentId == null)
            throw new BaseRuntimeException("父级ID不能为空");
        if (name == null || name.isEmpty())
            throw new BaseRuntimeException("目录名不能为空");
        solutionEntity.setType(FLAG);
        solutionService.add(solutionEntity);
        return ResponseDataUtil.ok("添加投招标技术支持类别成功");
    }


    @DeleteMapping("/{id}")
    public Map<String, Object> remove(@PathVariable Integer id) {
        solutionService.remove(id);//删除此类别id
        solutionService.removeTreeByIdAndType(id, FLAG);//删除此类别下的子节点
        return ResponseDataUtil.ok("删除投招标技术支持类别成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(SolutionEntity solutionEntity) {
        Integer parentId = solutionEntity.getParentId();
        String name = solutionEntity.getName();
        if (parentId == null)
            throw new BaseRuntimeException("父级ID不能为空");
        if (name == null || name.isEmpty())
            throw new BaseRuntimeException("目录名不能为空");
        solutionEntity.setType(FLAG);
        solutionService.edit(solutionEntity);
        return ResponseDataUtil.ok("修改投招标技术支持类别成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        SolutionEntity solutionEntity = solutionService.findById(id);
        return ResponseDataUtil.ok("查询投招标技术支持类别成功", solutionEntity);
    }

    @GetMapping
    public Map<String, Object> findAll() {
        List<TreeNodeUtil> treeNodeUtils = solutionService.findTreeByParentIdAndType(-1, FLAG);
        return ResponseDataUtil.ok("查询投招标技术支持类别树成功", treeNodeUtils);
    }

    @GetMapping("/list")
    public Map<String, Object> findList(@RequestParam("parentId") Integer parentId) {
        List<SolutionEntity> solutionEntities = solutionService.findByParentIdAndType(parentId, FLAG);
        return ResponseDataUtil.ok("查询投招标技术支持类别列表成功", solutionEntities);
    }

    @GetMapping("/{id}/status")
    public Map<String, Object> findStatus(@PathVariable Integer id, @RequestParam("name_type") Short nameType,
                                          @RequestParam("page_number") Integer pageNumber,
                                          @RequestParam("page_size") Integer pageSize,
                                          @RequestParam(value = "sortby", required = false) String sortby,
                                          @RequestParam(value = "order", required = false) String order) {
        String orderBy = "ss.id DESC";
        if (sortby != null && !"".equals(sortby) && order != null && !"".equals(order))
            orderBy = sortby + " " + order;
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<SolutionStatusEntity> solutionStatusEntities = solutionService.findStatusById(id, nameType, FLAG);
        PageInfo<SolutionStatusEntity> pageInfo = new PageInfo<>(solutionStatusEntities);
        return ResponseDataUtil.ok("查询投招标技术支持状态列表成功", pageInfo);
    }
}
