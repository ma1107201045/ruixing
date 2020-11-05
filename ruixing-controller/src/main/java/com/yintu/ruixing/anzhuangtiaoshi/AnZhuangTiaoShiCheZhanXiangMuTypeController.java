package com.yintu.ruixing.anzhuangtiaoshi;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/7/11 17:38
 * @Version 1.0
 * 需求:安装调试项目类型
 */
@RestController
@RequestMapping("/AnZhuangTiaoShiCheZhanXiangMuTypeAll")
public class AnZhuangTiaoShiCheZhanXiangMuTypeController {
    @Autowired
    private AnZhuangTiaoShiCheZhanXiangMuTypeService anZhuangTiaoShiCheZhanXiangMuTypeService;

    //新增项目类型
    @PostMapping("/addXiangMuType")
    public Map<String, Object> addXiangMuType(AnZhuangTiaoShiCheZhanXiangMuTypeEntity anZhuangTiaoShiCheZhanXiangMuTypeEntity) {
        anZhuangTiaoShiCheZhanXiangMuTypeService.addXiangMuType(anZhuangTiaoShiCheZhanXiangMuTypeEntity);
        return ResponseDataUtil.ok("添加项目类型成功");
    }

    //查询所有的项目类型
    @GetMapping("/findAllXiangMuType")
    public Map<String, Object> findAllXiangMuType(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<AnZhuangTiaoShiCheZhanXiangMuTypeEntity> xiangMuTypeEntities = anZhuangTiaoShiCheZhanXiangMuTypeService.findAllXiangMuType(page, size);
        PageInfo<AnZhuangTiaoShiCheZhanXiangMuTypeEntity> xiangMuTypeEntityPageInfo = new PageInfo<>(xiangMuTypeEntities);
        return ResponseDataUtil.ok("查询所有类型成功", xiangMuTypeEntityPageInfo);
    }

    //根据类型id  编辑对应的类型
    @PutMapping("/editXiangMuTypeById/{id}")
    public Map<String, Object> editXiangMuTypeById(@PathVariable Integer id, AnZhuangTiaoShiCheZhanXiangMuTypeEntity anZhuangTiaoShiCheZhanXiangMuTypeEntity) {
        anZhuangTiaoShiCheZhanXiangMuTypeService.editXiangMuTypeById(anZhuangTiaoShiCheZhanXiangMuTypeEntity);
        return ResponseDataUtil.ok("编辑项目类型成功");
    }

    //根据类型id  单个或者批量删除
    @DeleteMapping("/deletXiangMuTypeByIds/{ids}")
    public Map<String, Object> deletXiangMuTypeByIds(@PathVariable Integer[] ids) {
        anZhuangTiaoShiCheZhanXiangMuTypeService.deletXiangMuTypeByIds(ids);
        return ResponseDataUtil.ok("删除项目类型成功");
    }

    //根据项目类型名字  进行模糊查询
    @GetMapping("/findXiangMuTypeByName")
    public Map<String, Object> findXiangMuTypeByName(String xmname, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<AnZhuangTiaoShiCheZhanXiangMuTypeEntity> xiangMuTypeEntities = anZhuangTiaoShiCheZhanXiangMuTypeService.findXiangMuTypeByName(xmname, page, size);
        PageInfo<AnZhuangTiaoShiCheZhanXiangMuTypeEntity> xiangMuTypeEntityPageInfo = new PageInfo<>(xiangMuTypeEntities);
        return ResponseDataUtil.ok("查询成功", xiangMuTypeEntityPageInfo);
    }
}
