package com.yintu.ruixing.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.entity.ZhiShiGuanLiFileTypeEntity;
import com.yintu.ruixing.entity.ZhiShiGuanLiFileTypeFileEntity;
import com.yintu.ruixing.service.ZhiShiGuanLiFileTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/8/11 17:20
 * @Version 1.0
 * 需求:知识管理  文件类型
 */
@RestController
@RequestMapping("/zhiShiGuanLiAll")
public class ZhiShiGuanLiFileTypeController {
    @Autowired
    private ZhiShiGuanLiFileTypeService zhiShiGuanLiFileTypeService;

    //初始化页面  或者根据文档分类名进行模糊查询
    @GetMapping("findSomeFileType")
    public Map<String, Object> findSomeFileType(Integer page, Integer size, String fileTypeName) {
        PageHelper.startPage(page, size);
        List<ZhiShiGuanLiFileTypeEntity> fileTypeEntityList = zhiShiGuanLiFileTypeService.findSomeFileType(page, size, fileTypeName);
        PageInfo<ZhiShiGuanLiFileTypeEntity> fileTypeEntityPageInfo = new PageInfo<>(fileTypeEntityList);
        return ResponseDataUtil.ok("查询文件类型成功", fileTypeEntityPageInfo);
    }

    //新增文件类型
    @PostMapping("/addFileType")
    public Map<String, Object> addFileType(ZhiShiGuanLiFileTypeEntity zhiShiGuanLiFileTypeEntity) {
        zhiShiGuanLiFileTypeService.addFileType(zhiShiGuanLiFileTypeEntity);
        return ResponseDataUtil.ok("新增文件类型成功");
    }

    //根据id 编辑对应的文件类型
    @PutMapping("/editFileTypeById/{id}")
    public Map<String, Object> editFileTypeById(@PathVariable Integer id, ZhiShiGuanLiFileTypeEntity zhiShiGuanLiFileTypeEntity) {
        zhiShiGuanLiFileTypeService.editFileTypeById(zhiShiGuanLiFileTypeEntity);
        return ResponseDataUtil.ok("编辑文件类型成功");
    }

    //根据id  批量删除文件类型  或者单个删除
    @DeleteMapping("/deleteFileTypeByIds/{ids}")
    public Map<String, Object> deleteFileTypeByIds(@PathVariable Integer[] ids) {
        return null;
    }


    ///////////////////////文件////////////////////////////////

    //新增文件
    @PostMapping("/addFile")
    public Map<String,Object>addFile(ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity){
        zhiShiGuanLiFileTypeService.addFile(zhiShiGuanLiFileTypeFileEntity);
        return ResponseDataUtil.ok("新增文件成功");
    }

    //更新文件版本
    @PutMapping("updateFileById")
    public Map<String,Object>updateFileById(Integer id,ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity){
        ZhiShiGuanLiFileTypeFileEntity fileEntity=zhiShiGuanLiFileTypeService.findFile(id);
        String fileName = fileEntity.getFileName();
        Date createtime = fileEntity.getCreatetime();
        String filePath = fileEntity.getFilePath();
        Integer id1 = fileEntity.getId();
        Integer tid = fileEntity.getTid();
        zhiShiGuanLiFileTypeService.addOneFile(fileName,createtime,filePath,id1,tid);
        zhiShiGuanLiFileTypeService.updateFileById(zhiShiGuanLiFileTypeFileEntity);
        return ResponseDataUtil.ok("更新成功");
    }
}
