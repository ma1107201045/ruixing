package com.yintu.ruixing.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.entity.BiddingEntity;
import com.yintu.ruixing.entity.BiddingFileEntity;
import com.yintu.ruixing.service.BiddingFileService;
import com.yintu.ruixing.service.BiddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author:mlf
 * @date:2020/7/2 11:54
 */
@RestController
@RequestMapping("/biddings")
public class BiddingController extends SessionController implements BaseController<BiddingEntity, Integer> {
    @Autowired
    private BiddingService biddingService;

    @Autowired
    private BiddingFileService biddingFileService;

    @PostMapping
    public Map<String, Object> add(BiddingEntity entity) {
        biddingService.add(entity);
        return ResponseDataUtil.ok("添加投招标技术支持信息成功");
    }


    @DeleteMapping("/{id}")
    public Map<String, Object> remove(@PathVariable Integer id) {
        biddingService.remove(id);
        return ResponseDataUtil.ok("删除投招标技术支持信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, BiddingEntity entity) {
        biddingService.edit(entity);
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
    public Map<String, Object> findAll() {
        List<BiddingEntity> biddingEntities = biddingService.findAll();
        return ResponseDataUtil.ok("查询投招标技术支持信息列表成功", biddingEntities);
    }

    @GetMapping("/search")
    public Map<String, Object> findBySearch(@RequestParam("page_number") Integer pageNumber,
                                            @RequestParam("page_size") Integer pageSize,
                                            @RequestParam(value = "order_by", required = false, defaultValue = "bf.id DESC") String orderBy,
                                            @RequestParam(value = "year", required = false) Integer year,
                                            @RequestParam(value = "project_name", required = false) String projectName,
                                            @RequestParam(value = "type", required = false) String type) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<BiddingFileEntity> biddingFileEntities = biddingFileService.findByYearAndProjectNameAndType(year, projectName, type);
        PageInfo<BiddingFileEntity> pageInfo = new PageInfo<>(biddingFileEntities);
        return ResponseDataUtil.ok("查询招投标技术支持以及文件信息列表成功", pageInfo);
    }

}
