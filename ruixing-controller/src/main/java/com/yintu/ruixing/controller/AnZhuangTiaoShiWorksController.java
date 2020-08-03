package com.yintu.ruixing.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.FileUploadUtil;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.entity.*;
import com.yintu.ruixing.service.AnZhuangTiaoShiWorksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/7/27 19:36
 * @Version 1.0
 * 需求:安装调试现场作业
 */
@RestController
@RequestMapping("/AnZhuangTiaoShiWorksAll")
public class AnZhuangTiaoShiWorksController {
    @Autowired
    private AnZhuangTiaoShiWorksService anZhuangTiaoShiWorksService;

    //新增车站
    @PostMapping("/addWorksCheZhan")
    public Map<String,Object>addWorksCheZhan(AnZhuangTiaoShiWorksCheZhanEntity anZhuangTiaoShiWorksCheZhanEntity,String[] chezhanname){
        anZhuangTiaoShiWorksService.addWorksCheZhan(anZhuangTiaoShiWorksCheZhanEntity,chezhanname);
        return ResponseDataUtil.ok("新增车站成功");
    }

    //根据线段id  批量编辑对应的车站数据
    @PutMapping("/editWorksCheZhanByXid/{xid}")
    public Map<String,Object>editWorksCheZhanByXid(@PathVariable Integer xid,AnZhuangTiaoShiWorksCheZhanEntity anZhuangTiaoShiWorksCheZhanEntity){
        anZhuangTiaoShiWorksService.editWorksCheZhanByXid(anZhuangTiaoShiWorksCheZhanEntity);
        return ResponseDataUtil.ok("编辑成功");
    }

    //根据线段id 查询对应的车站
    @GetMapping("/findCheZhanByXid/{xid}")
    public Map<String,Object>findCheZhanByXid(@PathVariable Integer xid){
        List<AnZhuangTiaoShiCheZhanEntity>cheZhanEntities=anZhuangTiaoShiWorksService.findCheZhanByXid(xid);
        return ResponseDataUtil.ok("查询对应车站成功",cheZhanEntities);
    }

    //根据线段id  查询对应的车站数据
    @GetMapping("findCheZhanDatasByXid/{xid}")
    public Map<String,Object>findCheZhanDatasByXid(@PathVariable Integer xid,Integer page,Integer size){
        PageHelper.startPage(page,size);
        List<AnZhuangTiaoShiWorksCheZhanEntity>cheZhanEntities=anZhuangTiaoShiWorksService.findCheZhanDatasByXid(xid,page,size);
        PageInfo<AnZhuangTiaoShiWorksCheZhanEntity> cheZhanEntityPageInfo=new PageInfo<>(cheZhanEntities);
        return ResponseDataUtil.ok("查询对应的车站信息成功",cheZhanEntityPageInfo);
    }

    //根据车站id  查询对应的数据
    @GetMapping("/findWorksDatasByCid/{cid}")
    public Map<String,Object>findWorksDatasByCid(@PathVariable Integer cid,Integer page,Integer size){
        PageHelper.startPage(page,size);
        List<AnZhuangTiaoShiWorkNameLibraryEntity> libraryEntityList=anZhuangTiaoShiWorksService.findWorksDatasByCid(cid,page,size);
        PageInfo<AnZhuangTiaoShiWorkNameLibraryEntity> libraryEntityPageInfo=new PageInfo<>(libraryEntityList);
        return ResponseDataUtil.ok("查询对应的作业项数据成功",libraryEntityPageInfo);
    }

    //对车站下的作业项 进行编辑
    @PostMapping("/addWorksDatas")
    public Map<String,Object>addWorksDatas(AnZhuangTiaoShiWorksDingEntity anZhuangTiaoShiWorksDingEntity){
        anZhuangTiaoShiWorksService.addWorksDatas(anZhuangTiaoShiWorksDingEntity);
        return ResponseDataUtil.ok("编辑成功");
    }

    //自动显示登录人的名字
    @GetMapping("/findWorker")
    public Map<String,Object>findWorker(){
        SessionController sessionController=new SessionController();
        String username = sessionController.getLoginUser().getUsername();
        return ResponseDataUtil.ok("查询姓名成功",username);
    }

    //查询所有的作业项配置版本
    @GetMapping("/findAllWorks")
    public Map<String,Object>findAllWorks(){
        List<AnZhuangTiaoShiWorkNameTotalEntity> workNameTotalEntityList=anZhuangTiaoShiWorksService.findAllWorks();
        return ResponseDataUtil.ok("查询所有的版本数据成功",workNameTotalEntityList);
    }

    //////////////////////////文件////////////////////////////////////

    //文件上传
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

    //根据 线段id  查询对应的所有的输入文件
    @GetMapping("/findShuRuFileByXid/{id}")
    public Map<String,Object>findShuRuFileByXid(@PathVariable Integer id,Integer page,Integer size){
        PageHelper.startPage(page,size);
        List<AnZhuangTiaoShiWorksFileEntity> fileEntities=anZhuangTiaoShiWorksService.findShuRuFileByXid(id,page,size);
        PageInfo<AnZhuangTiaoShiWorksFileEntity> fileEntityPageInfo=new PageInfo<>(fileEntities);
        return ResponseDataUtil.ok("查询输入文件成功",fileEntityPageInfo);
    }

    //根据 线段id  查询对应的所有的输出文件
    @GetMapping("/findShuChuFileByXid/{id}")
    public Map<String,Object>findShuChuFileByXid(@PathVariable Integer id,Integer page,Integer size){
        PageHelper.startPage(page,size);
        List<AnZhuangTiaoShiWorksFileEntity> fileEntities=anZhuangTiaoShiWorksService.findShuChuFileByXid(id,page,size);
        PageInfo<AnZhuangTiaoShiWorksFileEntity> fileEntityPageInfo=new PageInfo<>(fileEntities);
        return ResponseDataUtil.ok("查询输出文件成功",fileEntityPageInfo);
    }

    //新增文件
    @PostMapping("/addFile")
    public Map<String,Object>addFile(AnZhuangTiaoShiWorksFileEntity anZhuangTiaoShiWorksFileEntity){
        anZhuangTiaoShiWorksService.addFile(anZhuangTiaoShiWorksFileEntity);
        return ResponseDataUtil.ok("添加文件数据成功");
    }

    //根据文件id  编辑对应的文件数据
    @PutMapping("/editFileById/{id}")
    public Map<String,Object>editFileById(@PathVariable Integer id ,AnZhuangTiaoShiWorksFileEntity anZhuangTiaoShiWorksFileEntity){
        anZhuangTiaoShiWorksService.editFileById(anZhuangTiaoShiWorksFileEntity);
        return ResponseDataUtil.ok("编辑文件数据成功");
    }

    //根据id 下载文件
    @GetMapping("/downLoads/{id}")
    public void downloadFile(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        AnZhuangTiaoShiWorksFileEntity fileEntity=anZhuangTiaoShiWorksService.findById(id);
        if (fileEntity != null) {
            String filePath = fileEntity.getFilePath();
            String fileName = fileEntity.getFileName();
            if (filePath != null && !"".equals(filePath) && fileName != null && !"".equals(fileName)) {
                response.setContentType("application/octet-stream;charset=ISO8859-1");
                response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
                response.addHeader("Pargam", "no-cache");
                response.addHeader("Cache-Control", "no-cache");
                FileUploadUtil.get(response.getOutputStream(), filePath + "\\" + fileName);
            }
        }
    }
    //根据文件名 查询对应的文件
    @GetMapping("/findFileByNmae")
    public Map<String,Object>findFileByNmae(Integer page,Integer size,Integer xdid,Integer filetype,String filename){
        PageHelper.startPage(page,size);
        List<AnZhuangTiaoShiWorksFileEntity>fileEntities=anZhuangTiaoShiWorksService.findFileByNmae(page,size,xdid,filetype,filename);
        PageInfo<AnZhuangTiaoShiWorksFileEntity>fileEntityPageInfo=new PageInfo<>(fileEntities);
        return ResponseDataUtil.ok("查询成功",fileEntityPageInfo);
    }

    //根据文件id  批量删除 或者单个删除文件
    @DeleteMapping("/deletFileByIds/{ids}")
    public Map<String,Object>deletFileByIds(@PathVariable Integer[] ids){
        anZhuangTiaoShiWorksService.deletFileByIds(ids);
        return ResponseDataUtil.ok("删除文件成功");
    }

}
