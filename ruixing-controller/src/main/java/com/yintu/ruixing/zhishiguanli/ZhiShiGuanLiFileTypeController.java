package com.yintu.ruixing.zhishiguanli;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.FileUploadUtil;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.xitongguanli.AuditConfigurationEntity;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.xitongguanli.UserService;
import com.yintu.ruixing.zhishiguanli.ZhiShiGuanLiFileTypeEntity;
import com.yintu.ruixing.zhishiguanli.ZhiShiGuanLiFileTypeFileEntity;
import com.yintu.ruixing.zhishiguanli.ZhiShiGuanLiFileTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
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

    @Autowired
    private UserService userService;

    //树形结构图.
    @GetMapping
    public Map<String, Object> findShuXing() {
        List<TreeNodeUtil> treeNodeUtils = zhiShiGuanLiFileTypeService.findShuXing(0);
        return ResponseDataUtil.ok("查询成功", treeNodeUtils);
    }

    //根据parentid  查询对应的文件类型.
    @GetMapping("/findFileTypeByParentid/{parentid}")
    public Map<String, Object> findFileTypeByParentid(@PathVariable Integer parentid) {
        List<ZhiShiGuanLiFileTypeEntity> fileTypeEntityList = zhiShiGuanLiFileTypeService.findFileTypeByParentid(parentid);
        return ResponseDataUtil.ok("查询成功", fileTypeEntityList);
    }

    //转移文件。
    @PutMapping("/pasteFileByParentid")
    public Map<String, Object> pasteFileByParentid(Integer parentid, Integer[] fileid, ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity) {
        zhiShiGuanLiFileTypeService.pasteFileByParentid(parentid, fileid, zhiShiGuanLiFileTypeFileEntity);
        return ResponseDataUtil.ok("操作成功");
    }

    //复制文件.
    @PostMapping("/copyFileByParentid")
    public Map<String, Object> copyFileByParentid(Integer parentid, Integer[] fileid, ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity) {
        String username = this.getLoginUser().getTrueName();
        zhiShiGuanLiFileTypeService.copyFileByParentid(parentid, fileid, zhiShiGuanLiFileTypeFileEntity, username);
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
    public Map<String, Object> addFileType(ZhiShiGuanLiFileTypeEntity zhiShiGuanLiFileTypeEntity, Integer parentid) {
        String username = this.getLoginUser().getTrueName();
        zhiShiGuanLiFileTypeService.addFileType(zhiShiGuanLiFileTypeEntity, username, parentid);
        return ResponseDataUtil.ok("新增文件类型成功");
    }

    //根据id 编辑对应的文件类型
    @PutMapping("/editFileTypeById/{id}")
    public Map<String, Object> editFileTypeById(@PathVariable Integer id, ZhiShiGuanLiFileTypeEntity zhiShiGuanLiFileTypeEntity) {
        String username = this.getLoginUser().getTrueName();
        zhiShiGuanLiFileTypeService.editFileTypeById(zhiShiGuanLiFileTypeEntity, username);
        return ResponseDataUtil.ok("编辑文件类型成功");
    }

    //根据id  批量删除文件类型  或者单个删除
    @DeleteMapping("/deleteFileTypeByIds/{ids}")
    public Map<String, Object> deleteFileTypeByIds(@PathVariable Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            List<ZhiShiGuanLiFileTypeFileEntity> fileEntityList = zhiShiGuanLiFileTypeService.findFiles(ids[i]);
            if (fileEntityList.size() == 0) {
                zhiShiGuanLiFileTypeService.deleteFileTypeByIds(ids[i]);
            } else {
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

    //查询审核角色
    @GetMapping("/auditors")
    @ResponseBody
    public Map<String, Object> findRoles() {
        List<AuditConfigurationEntity> auditConfigurationEntities = zhiShiGuanLiFileTypeService.findAudit((short) 4, (short) 1, (short) 1);
        if (auditConfigurationEntities.isEmpty()) {
            return ResponseDataUtil.ok("查询审核角色成功", zhiShiGuanLiFileTypeService.findTree());
        }
        return ResponseDataUtil.ok("查询审核角色成功", new ArrayList<>());
    }

    //查询文件日志消息
    @GetMapping("/findRecordmessageByFileid/{id}")
    public Map<String, Object> findRecordmessageByFileid(@PathVariable Integer id) {
        List<ZhiShiGuanLiFileTypeFileRecordmessageEntity> recordmessageEntityList = zhiShiGuanLiFileTypeService.findRecordmessageByFileid(id);
        return ResponseDataUtil.ok("查询日志数据成功", recordmessageEntityList);
    }

    //查询消息
    @GetMapping("/findXiaoXi")
    public Map<String, Object> findXiaoXi() {
        Integer senderid = this.getLoginUser().getId().intValue();
        List<MessageEntity> contextlist = zhiShiGuanLiFileTypeService.findXiaoXi(senderid);
        return ResponseDataUtil.ok("查询消息成功", contextlist);
    }

    //审核文件
    @PutMapping("/editAuditorByFileid/{id}")
    public Map<String, Object> editAuditorByFileid(@PathVariable Integer id, Short isPass, Integer passUserId,
                                                   String accessoryName, String accessoryPath, String context) {
        String username = this.getLoginUser().getTrueName();
        Integer receiverid = this.getLoginUser().getId().intValue();
        zhiShiGuanLiFileTypeService.editAuditorByFileid(id, isPass, passUserId, username, receiverid, accessoryName, accessoryPath, context);
        return ResponseDataUtil.ok("审核成功");
    }

    //查询转交审核人姓名
    @GetMapping("/findZhuanJiaoAuditorName")
    public Map<String, Object> findAllAuditorNamre(Integer fileid) {
        List<UserEntity> userEntities =zhiShiGuanLiFileTypeService.findZhuanJiaoAuditorName(fileid);
        return ResponseDataUtil.ok("查询姓名成功", userEntities);
    }

    //新增文件
    @PostMapping("/addFile")
    public Map<String, Object> addFile(ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity) {//, Integer[] auditorid, Integer[] sort
        String username = this.getLoginUser().getTrueName();//登录人名
        Integer uid = this.getLoginUser().getId().intValue();//登录人id
        zhiShiGuanLiFileTypeService.addFile(zhiShiGuanLiFileTypeFileEntity, username, uid);
        return ResponseDataUtil.ok("新增文件成功");
    }

    //更新文件版本
    @PutMapping("updateFileById")
    public Map<String, Object> updateFileById(Integer id, ZhiShiGuanLiFileTypeFileEntity zhiShiGuanLiFileTypeFileEntity) {//, Integer[] auditorid, Integer[] sort
        ZhiShiGuanLiFileTypeFileEntity fileEntity = zhiShiGuanLiFileTypeService.findFile(id);
        String username = this.getLoginUser().getTrueName();//登录人名
        Integer uid = this.getLoginUser().getId().intValue();//登录人id
        String fileName = fileEntity.getFileName();
        Date createtime = fileEntity.getCreatetime();
        String filePath = fileEntity.getFilePath();
        Integer filesize = fileEntity.getFilesize();
        Integer id1 = fileEntity.getId();
        Integer tid = fileEntity.getTid();
        zhiShiGuanLiFileTypeService.addOneFile(fileName, createtime, filePath, id1, username, filesize);//新增历史文件
        zhiShiGuanLiFileTypeService.updateFileById(zhiShiGuanLiFileTypeFileEntity, id, username, uid);//添加新文件
        return ResponseDataUtil.ok("更新成功");
    }

    //根据文件目录查询 本目录下所有的文件
    @GetMapping("/findFileByTypeid")
    public Map<String,Object>findFileByTypeid(Integer page, Integer size, Integer id){
        PageHelper.startPage(page, size);
        List<ZhiShiGuanLiFileTypeFileEntity> fileEntityList = zhiShiGuanLiFileTypeService.findFileByTypeid(id);
        PageInfo<ZhiShiGuanLiFileTypeFileEntity> fileTypeFileEntityPageInfo = new PageInfo<>(fileEntityList);
        return ResponseDataUtil.ok("查询文件成功", fileTypeFileEntityPageInfo);
    }

    //文件初始化   或者根据文件名查询文件
    @GetMapping("/findSomeFile")
    public Map<String, Object> findSomeFile(Integer page, Integer size, String fileName, Integer id, Integer typeid) {//typeid ；1 时间  2 大小
        Integer uid = this.getLoginUser().getId().intValue();//登录人id
        if (typeid == null) {
            PageHelper.startPage(page, size);
            List<ZhiShiGuanLiFileTypeFileEntity> fileEntityList = zhiShiGuanLiFileTypeService.findSomeFile(page, size, fileName, id, uid);
            PageInfo<ZhiShiGuanLiFileTypeFileEntity> fileTypeFileEntityPageInfo = new PageInfo<>(fileEntityList);
            return ResponseDataUtil.ok("查询文件成功", fileTypeFileEntityPageInfo);
        }
        if (typeid == 1) {
            PageHelper.startPage(page, size);
            List<ZhiShiGuanLiFileTypeFileEntity> fileEntityList = zhiShiGuanLiFileTypeService.findSomeFileByTime(page, size, fileName, id, uid);
            PageInfo<ZhiShiGuanLiFileTypeFileEntity> fileTypeFileEntityPageInfo = new PageInfo<>(fileEntityList);
            return ResponseDataUtil.ok("查询文件成功", fileTypeFileEntityPageInfo);
        } else {
            PageHelper.startPage(page, size);
            List<ZhiShiGuanLiFileTypeFileEntity> fileEntityList = zhiShiGuanLiFileTypeService.findSomeFileBySize(page, size, fileName, id, uid);
            PageInfo<ZhiShiGuanLiFileTypeFileEntity> fileTypeFileEntityPageInfo = new PageInfo<>(fileEntityList);
            return ResponseDataUtil.ok("查询文件成功", fileTypeFileEntityPageInfo);
        }
    }

    //根据文件id  查询对应的数据
    @GetMapping("/findFileDatasById/{id}")
    public Map<String, Object> findFileDatasById(@PathVariable Integer id) {
        Integer uid = this.getLoginUser().getId().intValue();//登录人id
        ZhiShiGuanLiFileTypeFileEntity fileTypeFileEntities = zhiShiGuanLiFileTypeService.findFileDatasById(id, uid);
        return ResponseDataUtil.ok("查询成功", fileTypeFileEntities);
    }

   /* //根据 时间 排序.
    @GetMapping("/findSomeFileByTime")
    public Map<String, Object> findSomeFileByTime(Integer page, Integer size, String fileName, Integer id) {
        PageHelper.startPage(page, size);
        List<ZhiShiGuanLiFileTypeFileEntity> fileEntityList = zhiShiGuanLiFileTypeService.findSomeFileByTime(page, size, fileName, id);
        PageInfo<ZhiShiGuanLiFileTypeFileEntity> fileTypeFileEntityPageInfo = new PageInfo<>(fileEntityList);
        return ResponseDataUtil.ok("查询文件成功", fileTypeFileEntityPageInfo);
    }

    //根据 大小 排序.
    @GetMapping("/findSomeFileBySize")
    public Map<String, Object> findSomeFileBySize(Integer page, Integer size, String fileName, Integer id) {
        PageHelper.startPage(page, size);
        List<ZhiShiGuanLiFileTypeFileEntity> fileEntityList = zhiShiGuanLiFileTypeService.findSomeFileBySize(page, size, fileName, id,);
        PageInfo<ZhiShiGuanLiFileTypeFileEntity> fileTypeFileEntityPageInfo = new PageInfo<>(fileEntityList);
        return ResponseDataUtil.ok("查询文件成功", fileTypeFileEntityPageInfo);
    }*/

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
            } else {
                return ResponseDataUtil.error("存在历史更新文件,不能删除");
            }
        }
        return ResponseDataUtil.ok("删除文件成功");
    }
}
