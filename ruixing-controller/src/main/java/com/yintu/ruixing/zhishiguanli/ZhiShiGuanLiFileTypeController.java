package com.yintu.ruixing.zhishiguanli;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.FileUploadUtil;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.xitongguanli.UserService;
import com.yintu.ruixing.zhishiguanli.ZhiShiGuanLiFileTypeEntity;
import com.yintu.ruixing.zhishiguanli.ZhiShiGuanLiFileTypeFileEntity;
import com.yintu.ruixing.zhishiguanli.ZhiShiGuanLiFileTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
public class ZhiShiGuanLiFileTypeController extends SessionController {
    @Autowired
    private ZhiShiGuanLiFileTypeService zhiShiGuanLiFileTypeService;

    //树形结构图.
    @GetMapping
    public Map<String,Object>findShuXing(){
        List<TreeNodeUtil> treeNodeUtils=zhiShiGuanLiFileTypeService.findShuXing(0);
        return ResponseDataUtil.ok("查询成功", treeNodeUtils);
    }

    //根据parentid  查询对应的文件类型.
    @GetMapping("/findFileTypeByParentid/{parentid}")
    public Map<String,Object>findFileTypeByParentid(@PathVariable Integer parentid){
        List<ZhiShiGuanLiFileTypeEntity> fileTypeEntityList=zhiShiGuanLiFileTypeService.findFileTypeByParentid(parentid);
        return ResponseDataUtil.ok("查询成功",fileTypeEntityList);
    }

    //粘贴文件。
    @PutMapping("/pasteFileByParentid")
    public Map<String,Object>pasteFileByParentid(Integer parentid,Integer[] fileid,ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity){
        zhiShiGuanLiFileTypeService.pasteFileByParentid(parentid,fileid,zhiShiGuanLiFileTypeFileEntity);
        return ResponseDataUtil.ok("操作成功");
    }

    //复制文件.
    @PostMapping("/copyFileByParentid")
    public Map<String,Object>copyFileByParentid(Integer parentid,Integer[] fileid,ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity){
        String username = this.getLoginUser().getTrueName();
        zhiShiGuanLiFileTypeService.copyFileByParentid(parentid,fileid,zhiShiGuanLiFileTypeFileEntity,username);
        return ResponseDataUtil.ok("操作成功");
    }
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
    public Map<String, Object> addFileType(ZhiShiGuanLiFileTypeEntity zhiShiGuanLiFileTypeEntity,Integer parentid) {
        String username = this.getLoginUser().getTrueName();
        zhiShiGuanLiFileTypeService.addFileType(zhiShiGuanLiFileTypeEntity,username,parentid);
        return ResponseDataUtil.ok("新增文件类型成功");
    }

    //根据id 编辑对应的文件类型
    @PutMapping("/editFileTypeById/{id}")
    public Map<String, Object> editFileTypeById(@PathVariable Integer id, ZhiShiGuanLiFileTypeEntity zhiShiGuanLiFileTypeEntity) {
        String username = this.getLoginUser().getTrueName();
        zhiShiGuanLiFileTypeService.editFileTypeById(zhiShiGuanLiFileTypeEntity,username);
        return ResponseDataUtil.ok("编辑文件类型成功");
    }

    //根据id  批量删除文件类型  或者单个删除
    @DeleteMapping("/deleteFileTypeByIds/{ids}")
    public Map<String, Object> deleteFileTypeByIds(@PathVariable Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            List<ZhiShiGuanLiFileTypeFileEntity> fileEntityList = zhiShiGuanLiFileTypeService.findFiles(ids[i]);
            if (fileEntityList.size() == 0) {
                zhiShiGuanLiFileTypeService.deleteFileTypeByIds(ids[i]);
            }else {
                return ResponseDataUtil.error("文件类型下面存在有文件,不能删除");
            }
        }
        return ResponseDataUtil.ok("删除文件类型成功");
    }


    ///////////////////////文件////////////////////////////////

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
    public Map<String, Object> addFile(ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity) {
        //登录人名
        String username = this.getLoginUser().getTrueName();
        zhiShiGuanLiFileTypeService.addFile(zhiShiGuanLiFileTypeFileEntity,username);
        return ResponseDataUtil.ok("新增文件成功");
    }

    //更新文件版本
    @PutMapping("updateFileById")
    public Map<String, Object> updateFileById(Integer id, ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity) {
        ZhiShiGuanLiFileTypeFileEntity fileEntity = zhiShiGuanLiFileTypeService.findFile(id);
        //登录人名
        String username = this.getLoginUser().getTrueName();
        String fileName = fileEntity.getFileName();
        Date createtime = fileEntity.getCreatetime();
        String filePath = fileEntity.getFilePath();
        Integer id1 = fileEntity.getId();
        Integer tid = fileEntity.getTid();
        zhiShiGuanLiFileTypeService.addOneFile(fileName, createtime, filePath, id1,username);
        zhiShiGuanLiFileTypeService.updateFileById(zhiShiGuanLiFileTypeFileEntity,id,username);
        return ResponseDataUtil.ok("更新成功");
    }

    //文件初始化   或者根据文件名查询文件
    @GetMapping("/findSomeFile")
    public Map<String, Object> findSomeFile(Integer page, Integer size, String fileName,Integer id) {
        PageHelper.startPage(page, size);
        List<ZhiShiGuanLiFileTypeFileEntity> fileEntityList = zhiShiGuanLiFileTypeService.findSomeFile(page, size, fileName,id);
        PageInfo<ZhiShiGuanLiFileTypeFileEntity> fileTypeFileEntityPageInfo = new PageInfo<>(fileEntityList);
        return ResponseDataUtil.ok("查询文件成功", fileTypeFileEntityPageInfo);
    }

    //根据 时间 排序.
    @GetMapping("/findSomeFileByTime")
    public Map<String, Object> findSomeFileByTime(Integer page, Integer size,Integer id) {
        PageHelper.startPage(page, size);
        List<ZhiShiGuanLiFileTypeFileEntity> fileEntityList = zhiShiGuanLiFileTypeService.findSomeFileByTime(page, size,id);
        PageInfo<ZhiShiGuanLiFileTypeFileEntity> fileTypeFileEntityPageInfo = new PageInfo<>(fileEntityList);
        return ResponseDataUtil.ok("查询文件成功", fileTypeFileEntityPageInfo);
    }

    //根据 大小 排序.
    @GetMapping("/findSomeFileBySize")
    public Map<String, Object> findSomeFileBySize(Integer page, Integer size,Integer id) {
        PageHelper.startPage(page, size);
        List<ZhiShiGuanLiFileTypeFileEntity> fileEntityList = zhiShiGuanLiFileTypeService.findSomeFileBySize(page, size,id);
        PageInfo<ZhiShiGuanLiFileTypeFileEntity> fileTypeFileEntityPageInfo = new PageInfo<>(fileEntityList);
        return ResponseDataUtil.ok("查询文件成功", fileTypeFileEntityPageInfo);
    }

    //根据文件id  查询其历史版本文件
    @GetMapping("/findFileById/{id}")
    public Map<String, Object> findFileById(@PathVariable Integer id, Integer page, Integer size, String fileName) {
        PageHelper.startPage(page, size);
        List<ZhiShiGuanLiFileTypeFileEntity> fileEntityList = zhiShiGuanLiFileTypeService.findFileById(id, page, size, fileName);
        PageInfo<ZhiShiGuanLiFileTypeFileEntity> fileTypeFileEntityPageInfo = new PageInfo<>(fileEntityList);
        return ResponseDataUtil.ok("查询文件成功", fileTypeFileEntityPageInfo);
    }

    //根据id  下载对应的文件
    @GetMapping("/downloads/{id}")
    public void downloadFile(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        ZhiShiGuanLiFileTypeFileEntity anZhuangTiaoShiWenTiFileEntity = zhiShiGuanLiFileTypeService.findById(id);
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


    //根据id 批量删除  或者单个删除历史版本文件
    @DeleteMapping("/deleteUpdataFileByIds/{ids}")
    public Map<String, Object> deleteUpdataFileByIds(@PathVariable Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            zhiShiGuanLiFileTypeService.deleteUpdataFileByIds(ids[i]);
        }
        return ResponseDataUtil.ok("删除文件成功");
    }


    //根据id 批量删除  或者单个删除文件
    @DeleteMapping("/deleteFileByIds/{ids}")
    public Map<String, Object> deleteFileByIds(@PathVariable Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            List<ZhiShiGuanLiFileTypeFileEntity> fileEntityList = zhiShiGuanLiFileTypeService.findFileByParentid(ids[i]);
            if (fileEntityList.size() == 0) {
                zhiShiGuanLiFileTypeService.deleteFileByIds(ids[i]);
            }else {
                return ResponseDataUtil.error("存在历史更新文件,不能删除");
            }
        }
        return ResponseDataUtil.ok("删除文件成功");
    }
}
