package com.yintu.ruixing.danganguanli;

import cn.hutool.core.date.DateUtil;
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
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: mlf
 * @Date: 2020/12/8 15:54
 * @Version: 1.0
 */
@Controller
@RequestMapping("/line/base/information")
public class LineBaseInformationController extends SessionController {
    @Autowired
    private LineBaseInformationService lineBaseInformationService;
    @Autowired
    private AnZhuangTiaoShiCheZhanXiangMuTypeService anZhuangTiaoShiCheZhanXiangMuTypeService;
    @Autowired
    private DianWuDuanService dianWuDuanService;

    @PostMapping
    @ResponseBody
    public Map<String, Object> add(@Validated LineBaseInformationEntity entity, @RequestParam Integer[] unitIds) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineBaseInformationService.add(entity, unitIds);
        return ResponseDataUtil.ok("添加线段基本信息成功");
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Map<String, Object> remove(@PathVariable Integer id) {
        lineBaseInformationService.remove(id);
        return ResponseDataUtil.ok("删除线段基本信息成功");
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Map<String, Object> edit(@PathVariable Integer id, @Validated LineBaseInformationEntity entity, @RequestParam Integer[] unitIds) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        lineBaseInformationService.edit(entity, unitIds);
        return ResponseDataUtil.ok("修改线段基本信息成功");
    }

    @GetMapping("/new")
    @ResponseBody
    public Map<String, Object> findNewLineByTid(@RequestParam("page_number") Integer pageNumber,
                                                @RequestParam("page_size") Integer pageSize,
                                                @RequestParam(value = "order_by", required = false, defaultValue = "lbi.id DESC") String orderBy,
                                                @RequestParam("tid") Integer tid) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<LineBaseInformationEntity> lineBaseInformationEntities = lineBaseInformationService.findNewLineByTid(tid);
        PageInfo<LineBaseInformationEntity> pageInfo = new PageInfo<>(lineBaseInformationEntities);
        return ResponseDataUtil.ok("查询最新线段基本信息列表成功", pageInfo);
    }

    @GetMapping("/history")
    @ResponseBody
    public Map<String, Object> findHistoryByExample(@RequestParam("page_number") Integer pageNumber,
                                                    @RequestParam("page_size") Integer pageSize,
                                                    @RequestParam(value = "order_by", required = false, defaultValue = "lbi.id DESC") String orderBy,
                                                    @RequestParam("tid") Integer tid,
                                                    @RequestParam("id") Integer id,
                                                    @RequestParam("name") String name) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<LineBaseInformationEntity> lineBaseInformationEntities = lineBaseInformationService.findHistoryByExample(tid, id, name, null);
        PageInfo<LineBaseInformationEntity> pageInfo = new PageInfo<>(lineBaseInformationEntities);
        return ResponseDataUtil.ok("查询最新线段基本信息列表成功", pageInfo);
    }


    @GetMapping("/XiangMuTypes")
    @ResponseBody
    public Map<String, Object> findXiangMuTypes() {
        List<AnZhuangTiaoShiCheZhanXiangMuTypeEntity> anZhuangTiaoShiCheZhanXiangMuTypeEntities = anZhuangTiaoShiCheZhanXiangMuTypeService.findAll();
        return ResponseDataUtil.ok("查询线段设备类型信息列表成功", anZhuangTiaoShiCheZhanXiangMuTypeEntities);
    }

    @GetMapping("/dianwuduans")
    @ResponseBody
    public Map<String, Object> findDianWuDuans() {
        List<DianWuDuanEntity> dianWuDuanEntities = dianWuDuanService.findAll();
        return ResponseDataUtil.ok("查询线段单位信息列表成功", dianWuDuanEntities);
    }

    @PostMapping("/import")
    @ResponseBody
    public Map<String, Object> importFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String[][] context = lineBaseInformationService.importFile(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
        return ResponseDataUtil.ok("获取导入线段基础信息成功", context);
    }

    @PostMapping("/import/data")
    @ResponseBody
    public Map<String, Object> importDate(@RequestBody String[][] context) {
        lineBaseInformationService.importDate(context, this.getLoginUserName());
        return ResponseDataUtil.ok("导入线段基础信息成功");
    }

    @GetMapping("/template")
    public void templateFile(HttpServletResponse response) throws IOException {
        String fileName = "线段基础信息列表-模板" + DateUtil.now() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        lineBaseInformationService.templateFile(response.getOutputStream());
    }

    @GetMapping("/export/{ids}")
    public void exportFile(@PathVariable Integer[] ids, HttpServletResponse response) throws IOException {
        String fileName = "线段基础信息列表-导出" + DateUtil.now() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        lineBaseInformationService.exportFile(response.getOutputStream(), ids);
    }


    @GetMapping("/tree")
    @ResponseBody
    public Map<String, Object> findTree() {
        List<TreeNodeUtil> treeNodeUtils = lineBaseInformationService.findTree();
        return ResponseDataUtil.ok("查询线段基本信息列表树成功", treeNodeUtils);
    }


}
