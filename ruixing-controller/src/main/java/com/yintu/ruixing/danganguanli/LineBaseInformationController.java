package com.yintu.ruixing.danganguanli;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiCheZhanXiangMuTypeEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiCheZhanXiangMuTypeService;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
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
 * @Date: 2020/12/8 15:54
 * @Version: 1.0
 */
@RestController
@RequestMapping("/line/base/information")
public class LineBaseInformationController extends SessionController {
    @Autowired
    private LineBaseInformationService lineBaseInformationService;
    @Autowired
    private AnZhuangTiaoShiCheZhanXiangMuTypeService anZhuangTiaoShiCheZhanXiangMuTypeService;
    @Autowired
    private DianWuDuanService dianWuDuanService;

    @PostMapping
    public Map<String, Object> add(@Validated LineBaseInformationEntity entity, @RequestParam Integer[] unitIds) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineBaseInformationService.add(entity, unitIds);
        return ResponseDataUtil.ok("添加线段基本信息成功");
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> remove(@PathVariable Integer id) {
        lineBaseInformationService.remove(id);
        return ResponseDataUtil.ok("删除线段基本信息成功");
    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, @Validated LineBaseInformationEntity entity, @RequestParam Integer[] unitIds) {
        entity.setModifiedBy(this.getLoginUserName());
        lineBaseInformationService.edit(entity, unitIds);
        return ResponseDataUtil.ok("修改线段基本信息成功");
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

    @GetMapping("/XiangMuTypes")
    public Map<String, Object> findXiangMuTypes() {
        List<AnZhuangTiaoShiCheZhanXiangMuTypeEntity> anZhuangTiaoShiCheZhanXiangMuTypeEntities = anZhuangTiaoShiCheZhanXiangMuTypeService.findAll();
        return ResponseDataUtil.ok("查询线段设备类型信息列表成功", anZhuangTiaoShiCheZhanXiangMuTypeEntities);
    }

    @GetMapping("/dianwuduans")
    public Map<String, Object> findDianWuDuans() {
        List<DianWuDuanEntity> dianWuDuanEntities = dianWuDuanService.findAll();
        return ResponseDataUtil.ok("查询线段单位信息列表成功", dianWuDuanEntities);
    }


    @GetMapping("/tree")
    public Map<String, Object> findTree() {
        List<TreeNodeUtil> treeNodeUtils = lineBaseInformationService.findTree();
        return ResponseDataUtil.ok("查询线段基本信息列表树成功", treeNodeUtils);
    }


    @GetMapping("/{tid}")
    public Map<String, Object> findNewVersionByTid(@PathVariable Integer tid) {
        LineBaseInformationEntity lineBaseInformationEntity = lineBaseInformationService.findNewVersionByTid(tid);
        return ResponseDataUtil.ok("查询线段基本信息成功", lineBaseInformationEntity);
    }


}
