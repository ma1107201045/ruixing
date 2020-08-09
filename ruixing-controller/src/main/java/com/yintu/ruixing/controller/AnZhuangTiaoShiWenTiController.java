package com.yintu.ruixing.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.FileUploadUtil;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.entity.AnZhuangTiaoShiWenTiEntity;
import com.yintu.ruixing.entity.AnZhuangTiaoShiWenTiFileEntity;
import com.yintu.ruixing.service.AnZhuangTiaoShiWenTiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/8/9 10:29
 * @Version 1.0
 * 需求:
 */
@RestController
@RequestMapping("/wenTiAll")
public class AnZhuangTiaoShiWenTiController {
    @Autowired
    private AnZhuangTiaoShiWenTiService anZhuangTiaoShiWenTiService;

    //新增问题
    @PostMapping("/addWenTi")
    public Map<String, Object> addWenTi(AnZhuangTiaoShiWenTiEntity anZhuangTiaoShiWenTiEntity) {
        anZhuangTiaoShiWenTiService.addWenTi(anZhuangTiaoShiWenTiEntity);
        return ResponseDataUtil.ok("新增问题成功");
    }

    //根据id 编辑问题
    @PutMapping("/editWenTiById/{id}")
    public Map<String,Object>editWenTiById(@PathVariable Integer id,AnZhuangTiaoShiWenTiEntity anZhuangTiaoShiWenTiEntity){
        anZhuangTiaoShiWenTiService.editWenTiById(anZhuangTiaoShiWenTiEntity);
        return ResponseDataUtil.ok("编辑问题成功");
    }

    //初始化页面   或者根据线段名 或问题描述查询数据
    @GetMapping("/findSomeWenTi")
    public Map<String,Object>findSomeWenTi(Integer page,Integer size,String xdname,String wenTiMiaoShu){
        PageHelper.startPage(page,size);
        List<AnZhuangTiaoShiWenTiEntity>wenTiEntityList=anZhuangTiaoShiWenTiService.findSomeWenTi(page,size,xdname,wenTiMiaoShu);
        PageInfo<AnZhuangTiaoShiWenTiEntity>wenTiEntityPageInfo=new PageInfo<>(wenTiEntityList);
        return ResponseDataUtil.ok("查询成功",wenTiEntityPageInfo);
    }

    //批量删除  或者单个删除
    @DeleteMapping("/deleteWenTiByIds/{ids}")
    public Map<String,Object>deleteWenTiByIds(@PathVariable Integer[] ids){
        anZhuangTiaoShiWenTiService.deleteWenTiByIds(ids);
        return ResponseDataUtil.ok("删除成功");
    }


    ////////////////////////文件/////////////////////////////

    //查看反馈文件 或者根据文件名查询
    @GetMapping("/findAllFanKuiFileById")
    public Map<String,Object>findAllFanKuiFileById(@PathVariable Integer id,Integer page,Integer size,String fileName){
        PageHelper.startPage(page,size);
        List<AnZhuangTiaoShiWenTiFileEntity> fileEntityList=anZhuangTiaoShiWenTiService.findAllFanKuiFileById(id,page,size,fileName);
        PageInfo<AnZhuangTiaoShiWenTiFileEntity>fileEntityPageInfo=new PageInfo<>(fileEntityList);
        return ResponseDataUtil.ok("查询反馈文件成功",fileEntityPageInfo);
    }

    //查看输出文件  或者根据文件名查询
    @GetMapping("/findAllShuChuFileById")
    public Map<String,Object>findAllShuChuFileById(@PathVariable Integer id,Integer page,Integer size,String fileName){
        PageHelper.startPage(page,size);
        List<AnZhuangTiaoShiWenTiFileEntity> fileEntityList=anZhuangTiaoShiWenTiService.findAllShuChuFileById(id,page,size,fileName);
        PageInfo<AnZhuangTiaoShiWenTiFileEntity>fileEntityPageInfo=new PageInfo<>(fileEntityList);
        return ResponseDataUtil.ok("查询输出文件成功",fileEntityPageInfo);
    }

    //上传文件
    @PostMapping("/uploads")
    @ResponseBody
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        String filePath = FileUploadUtil.save(multipartFile.getInputStream(), fileName);
        JSONObject jo = new JSONObject();
        jo.put("filePath", filePath);
        jo.put("fileName", fileName);
        return ResponseDataUtil.ok("上传文件成功", jo);
    }

    //新增文件
    @PostMapping("/addFile")
    public Map<String,Object>addFile(AnZhuangTiaoShiWenTiFileEntity anZhuangTiaoShiWenTiFileEntity){
        anZhuangTiaoShiWenTiService.addFile(anZhuangTiaoShiWenTiFileEntity);
        return ResponseDataUtil.ok("新增文件成功");
    }

    //根据id  下载对应的文件
    @GetMapping("/downloads/{id}")
    public void downloadFile(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        AnZhuangTiaoShiWenTiFileEntity anZhuangTiaoShiWenTiFileEntity=anZhuangTiaoShiWenTiService.findById(id);
        if (anZhuangTiaoShiWenTiFileEntity != null) {
            String filePath = anZhuangTiaoShiWenTiFileEntity.getFilePath();
            String fileName = anZhuangTiaoShiWenTiFileEntity.getFileName();
            if (filePath != null && !"".equals(filePath) && fileName != null && !"".equals(fileName)) {
                response.setContentType("application/octet-stream;charset=ISO8859-1");
                response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
                response.addHeader("Pargam", "no-cache");
                response.addHeader("Cache-Control", "no-cache");
                FileUploadUtil.get(response.getOutputStream(), filePath + "\\" + fileName);
            }
        }
    }

    //根据id  单个删除或者批量删除
    @DeleteMapping("deleteFileByIds/{ids}")
    public Map<String,Object>deleteFileByIds(@PathVariable Integer[] ids){
        anZhuangTiaoShiWenTiService.deleteFileByIds(ids);
        return ResponseDataUtil.ok("删除文件成功");
    }


}
