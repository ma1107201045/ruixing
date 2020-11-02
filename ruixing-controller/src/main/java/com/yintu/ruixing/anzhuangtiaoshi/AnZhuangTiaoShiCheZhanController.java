package com.yintu.ruixing.anzhuangtiaoshi;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/7/11 16:46
 * @Version 1.0
 * 需求:安装调试的车站
 */
@RestController
@RequestMapping("/AnZhuangTiaoShiCheZhanAll")
public class AnZhuangTiaoShiCheZhanController {
    @Autowired
    private AnZhuangTiaoShiCheZhanService anZhuangTiaoShiCheZhanService;

    //在线段  下面添加车站信息
    @PostMapping("/addCheZhan")
    public Map<String, Object> addCheZhan(AnZhuangTiaoShiCheZhanEntity anZhuangTiaoShiCheZhanEntity) {
        anZhuangTiaoShiCheZhanService.addCheZhan(anZhuangTiaoShiCheZhanEntity);
        return ResponseDataUtil.ok("添加车站信息成功");
    }

    //根据线段id  进行查询对应的所有车站信息
    @GetMapping("/findCheZhanById/{id}")
    public Map<String, Object> findCheZhanById(@PathVariable Integer id, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<AnZhuangTiaoShiCheZhanEntity> cheZhanEntities = anZhuangTiaoShiCheZhanService.findCheZhanById(id, page, size);
        PageInfo<AnZhuangTiaoShiCheZhanEntity> cheZhanEntityPageInfo = new PageInfo<>(cheZhanEntities);
        return ResponseDataUtil.ok("查询车站信息成功", cheZhanEntityPageInfo);
    }

    //根据车站id  编辑对应的车站信息
    @PutMapping("/editCheZhanById/{id}")
    public Map<String, Object> editCheZhanById(@PathVariable Integer id, AnZhuangTiaoShiCheZhanEntity anZhuangTiaoShiCheZhanEntity) {
        anZhuangTiaoShiCheZhanService.editCheZhanById(anZhuangTiaoShiCheZhanEntity);
        return ResponseDataUtil.ok("编辑车站信息成功");
    }

    //根据车站id  删除单个或者批量删除
    @DeleteMapping("/deleteCheZhanByIds/{ids}")
    public Map<String, Object> deleteCheZhanByIds(@PathVariable Integer[] ids) {
        anZhuangTiaoShiCheZhanService.deleteCheZhanByIds(ids);
        return ResponseDataUtil.ok("删除成功");
    }

    //根据车站名  进行模糊查询
    @GetMapping("/findCheZhanByName")
    public Map<String, Object> findCheZhanByName(String czname, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<AnZhuangTiaoShiCheZhanEntity> cheZhanEntities = anZhuangTiaoShiCheZhanService.findCheZhanByName(czname, page, size);
        PageInfo<AnZhuangTiaoShiCheZhanEntity> cheZhanEntityPageInfo = new PageInfo<>(cheZhanEntities);
        return ResponseDataUtil.ok("查询车站成功", cheZhanEntityPageInfo);
    }

    //根据id 进行单个或者批量下载到Excel
    @GetMapping("/downLoadByIds/{ids}")
    public void exportFile(@PathVariable Integer[] ids, HttpServletResponse response) throws IOException {
        String fileName = "安装调试车站信息列表" + System.currentTimeMillis() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        anZhuangTiaoShiCheZhanService.exportFile(response.getOutputStream(), ids);
    }

}
