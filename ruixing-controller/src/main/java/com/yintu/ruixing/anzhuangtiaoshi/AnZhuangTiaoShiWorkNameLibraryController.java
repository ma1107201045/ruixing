package com.yintu.ruixing.anzhuangtiaoshi;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWorkNameLibraryEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWorkNameLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/7/16 16:46
 * @Version 1.0
 * 需求:安装调试的现场作业的作业项页面
 */
@RestController
@RequestMapping("/AnZhuangTiaoShiWorkNameLibraryAll")
public class AnZhuangTiaoShiWorkNameLibraryController {
    @Autowired
    private AnZhuangTiaoShiWorkNameLibraryService anZhuangTiaoShiWorkNameLibraryService;

    //新增作业项名称
    @PostMapping("/addWorkName")
    public Map<String,Object>addWorkName(AnZhuangTiaoShiWorkNameLibraryEntity anZhuangTiaoShiWorkNameLibraryEntity){
        anZhuangTiaoShiWorkNameLibraryService.addWorkName(anZhuangTiaoShiWorkNameLibraryEntity);
        return ResponseDataUtil.ok("添加作业项名称成功");
    }
    //根据作业项名 模糊查询  或者查询全部
    @GetMapping("/findWorkName")
    public Map<String,Object>findWorkName(Integer page,Integer size,String workname){
        PageHelper.startPage(page,size);
        List<AnZhuangTiaoShiWorkNameLibraryEntity>workNameLibraryEntities=anZhuangTiaoShiWorkNameLibraryService.findWorkName(page,size,workname);
        PageInfo<AnZhuangTiaoShiWorkNameLibraryEntity> workNameLibraryEntityPageInfo=new PageInfo<>(workNameLibraryEntities);
        return ResponseDataUtil.ok("查询作业项成功",workNameLibraryEntityPageInfo);
    }
    //根据id  编辑对应的作业项名称
    @PutMapping("/editWorkNameById/{id}")
    public Map<String,Object>editWorkNameById(@PathVariable Integer id,AnZhuangTiaoShiWorkNameLibraryEntity anZhuangTiaoShiWorkNameLibraryEntity){
        anZhuangTiaoShiWorkNameLibraryService.editWorkNameById(anZhuangTiaoShiWorkNameLibraryEntity);
        return ResponseDataUtil.ok("编辑作业项成功");
    }
    //根据id  单个或者批量删除
    @DeleteMapping("/deleteWorkNameByIds/{ids}")
    public Map<String,Object>deleteWorkNameByIds(@PathVariable Integer[] ids){
        anZhuangTiaoShiWorkNameLibraryService.deleteWorkNameByIds(ids);
        return ResponseDataUtil.ok("删除作业项名称成功");
    }

    //查询所有的作业项
    @GetMapping("/findAllWorkName")
    public Map<String,Object>findAllWorkName(){
        List<AnZhuangTiaoShiWorkNameLibraryEntity>workNameLibraryEntities=anZhuangTiaoShiWorkNameLibraryService.findAllWorkName();
        return ResponseDataUtil.ok("查询所有作业项成功",workNameLibraryEntities);
    }

    //根据作业版本id  查询对应的作业项
    @GetMapping("/findAllWorksById/{id}")
    public Map<String,Object>findAllWorksById(@PathVariable Integer id){
        List<AnZhuangTiaoShiWorkNameLibraryEntity>workNameLibraryEntityList=anZhuangTiaoShiWorkNameLibraryService.findAllWorksById(id);
        return ResponseDataUtil.ok("查询作业项成功",workNameLibraryEntityList);
    }
}
