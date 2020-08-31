package com.yintu.ruixing.chanpinjiaofu;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.FileUploadUtil;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuEntity;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuPropertyEntity;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/6/23 16:16
 * @Version 1.0
 * 产品交付模块
 */
@RestController
@RequestMapping("/chanPinJiaoFuAll")
public class ChanPinJiaoFuController {
    @Autowired
    private ChanPinJiaoFuService chanPinJiaoFuService;

    //查询树形结构
    @GetMapping
    public Map<String, Object> findChanPinJiaoFuShuXing() {
        List<TreeNodeUtil> treeNodeUtils = chanPinJiaoFuService.findChanPinJiaoFuShuXing(-1);
        return ResponseDataUtil.ok("查询产品交付树结构成功", treeNodeUtils);
    }

    //当表中没有数据时  添加第一条数据   添加树形数据
    @PostMapping("/addDataShu")
    public Map<String, Object> addDataShu(ChanPinJiaoFuPropertyEntity chanPinJiaoFuPropertyEntity) {
        /*Integer parendid = chanPinJiaoFuService.findAllDataShu();
        if (parendid == null) {
            chanPinJiaoFuService.addfristData(chanPinJiaoFuPropertyEntity);
            return ResponseDataUtil.ok("添加第一条数据成功");
        } else {
            chanPinJiaoFuService.addDataShu(chanPinJiaoFuPropertyEntity);
            return ResponseDataUtil.ok("添加数据成功");
        }*/
        chanPinJiaoFuService.addDataShu(chanPinJiaoFuPropertyEntity);
        return ResponseDataUtil.ok("添加数据成功");
    }

    //根据选择id编辑树形结构数据
    @PutMapping("/editDataShuById/{id}")
    public Map<String, Object> editDataShuById(@PathVariable Integer id, ChanPinJiaoFuPropertyEntity chanPinJiaoFuPropertyEntity) {
        chanPinJiaoFuService.editDataShuById(chanPinJiaoFuPropertyEntity);
        return ResponseDataUtil.ok("修改数据成功");
    }

    //根据id删除树形结构数据
    @DeleteMapping("/deleteDataShuById/{id}")
    public Map<String, Object> deleteDataShuById(@PathVariable Integer id) {
        List<Integer> ids = chanPinJiaoFuService.findParedId(id);
        if (ids.size() == 0) {
            chanPinJiaoFuService.deleteDataShuById(id);
            return ResponseDataUtil.ok("删除数据成功");
        }
        return ResponseDataUtil.error("不能直接删除父级数据");
    }


    //查询所有的项目状态
    @GetMapping("/findAllXiangMuState")
    public Map<String, Object> findAllXiangMuState(Integer page, Integer size) {
        JSONObject js = new JSONObject();
        PageHelper.startPage(page, size);
        List<ChanPinJiaoFuEntity> chanPinJiaoFuEntities = chanPinJiaoFuService.findAllXiangMuState(page, size);
        js.put("chanPinJiaoFuEntities", chanPinJiaoFuEntities);
        PageInfo<ChanPinJiaoFuEntity> pinJiaoFuEntityPageInfo = new PageInfo<>(chanPinJiaoFuEntities);
        js.put("pinJiaoFuEntityPageInfo", pinJiaoFuEntityPageInfo);
        return ResponseDataUtil.ok("查询所有的项目成功", js);
    }


    //根据项目编号和项目名称查询数据
    @GetMapping("/findXiangMuData")
    public Map<String, Object> findXiangMuData(String bianhao, String name, Integer page, Integer size) {
        JSONObject js = new JSONObject();
        PageHelper.startPage(page, size);
        List<ChanPinJiaoFuEntity> chanPinJiaoFuEntities = chanPinJiaoFuService.findXiangMuData(bianhao, name, page, size);
        js.put("chanPinJiaoFuEntities", chanPinJiaoFuEntities);
        PageInfo<ChanPinJiaoFuEntity> pageInfo = new PageInfo<>(chanPinJiaoFuEntities);
        js.put("pageInfo", pageInfo);
        return ResponseDataUtil.ok("查询数据成功", js);
    }

   /* //根据树形结构的id  查询对应的数据
    @GetMapping("/findXiangMuDataById/{id}")
    public Map<String,Object>findXiangMuDataById(@PathVariable Integer id){
        List<ChanPinJiaoFuEntity> chanPinJiaoFuEntities = chanPinJiaoFuService.findXiangMuDataById(id);
        return ResponseDataUtil.ok("查询数据成功",chanPinJiaoFuEntities);
    }*/


    //根据树形结构的id  查询对应的所有数据
    @GetMapping("/findXiangMuDataById/{id}")
    public Map<String, Object> findXiangMuDataById(@PathVariable Integer id) {
        List<ChanPinJiaoFuEntity> chanPinJiaoFuEntitiesss = null;
        Map<String, Object> map = null;
        List<Integer> ids = chanPinJiaoFuService.findIdS(id);
        if (ids.size() > 0) {
            for (Integer idd : ids) {
                List<ChanPinJiaoFuEntity> chanPinJiaoFuEntities = chanPinJiaoFuService.findXiangMuDataById(idd);
                for (ChanPinJiaoFuEntity chanPinJiaoFuEntity : chanPinJiaoFuEntities) {
                    if (chanPinJiaoFuEntity.getShuId() != null) {
                        List<Integer> idds = chanPinJiaoFuService.findIdS(chanPinJiaoFuEntity.getId());
                        if (idds.size() > 0) {
                            for (Integer integer : idds) {
                                List<ChanPinJiaoFuEntity> chanPinJiaoFuEntitiess = chanPinJiaoFuService.findXiangMuDataById(integer);
                            }
                        }
                    } else {
                        List<ChanPinJiaoFuEntity> chanPinJiaoFuEntitiess = chanPinJiaoFuService.findXiangMuDataById(idd);
                        return ResponseDataUtil.ok("dadsd8888", chanPinJiaoFuEntitiess);
                    }
                }
            }
        } else {
            List<ChanPinJiaoFuEntity> chanPinJiaoFuEntitiess = chanPinJiaoFuService.findXiangMuDataById(id);
            return ResponseDataUtil.ok("dadsd9894", chanPinJiaoFuEntitiess);
        }
        return ResponseDataUtil.ok("dadsd", null);
    }

    //根据树形结构的id  查询对应的所有数据
    /*@GetMapping("/findXiangMuDataByIds")
    public Map<String, Object> findXiangMuDataByIds(Integer firstid, Integer secondid,
                                                    Integer fileid, Integer page, Integer size) {
        JSONObject js = new JSONObject();
        PageHelper.startPage(page, size);
        List<ChanPinJiaoFuEntity> chanPinJiaoFuEntityList = chanPinJiaoFuService.findXiangMuDataByIds(firstid, secondid, fileid, page, size);
        js.put("chanPinJiaoFuEntityList", chanPinJiaoFuEntityList);
        PageInfo<ChanPinJiaoFuEntity> info = new PageInfo<>(chanPinJiaoFuEntityList);
        js.put("info", info);
        return ResponseDataUtil.ok("查询数据成功", js);
    }*/

    @GetMapping("/findXiangMuDataByIds")
    public Map<String, Object> findXiangMuDataByIds(Integer parentId, Integer id,
                                                    Integer page, Integer size) {
        if (parentId == -1) {
            JSONObject js = new JSONObject();
            PageHelper.startPage(page, size);
            List<ChanPinJiaoFuEntity> chanPinJiaoFuEntityList = chanPinJiaoFuService.findXiangMuDataByIdFirst(id, page, size);
            js.put("chanPinJiaoFuEntityList", chanPinJiaoFuEntityList);
            PageInfo<ChanPinJiaoFuEntity> info = new PageInfo<>(chanPinJiaoFuEntityList);
            js.put("info", info);
            return ResponseDataUtil.ok("查询数据成功", js);
        }
        if (parentId == 1 || parentId == 2 || parentId == 3) {
            JSONObject js = new JSONObject();
            PageHelper.startPage(page, size);
            List<ChanPinJiaoFuEntity> chanPinJiaoFuEntityList = chanPinJiaoFuService.findXiangMuDataByIdSecond(id, page, size);
            js.put("chanPinJiaoFuEntityList", chanPinJiaoFuEntityList);
            PageInfo<ChanPinJiaoFuEntity> info = new PageInfo<>(chanPinJiaoFuEntityList);
            js.put("info", info);
            return ResponseDataUtil.ok("查询数据成功", js);
        } else {
            JSONObject js = new JSONObject();
            PageHelper.startPage(page, size);
            List<ChanPinJiaoFuEntity> chanPinJiaoFuEntityList = chanPinJiaoFuService.findXiangMuDataByIdThird(id, page, size);
            js.put("chanPinJiaoFuEntityList", chanPinJiaoFuEntityList);
            PageInfo<ChanPinJiaoFuEntity> info = new PageInfo<>(chanPinJiaoFuEntityList);
            js.put("info", info);
            return ResponseDataUtil.ok("查询数据成功", js);
        }

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

    //根据id 下载文件
    @GetMapping("/downloads/{id}")
    public void downloadFile(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        ChanPinJiaoFuEntity chanPinJiaoFuEntity = chanPinJiaoFuService.findById(id);
        if (chanPinJiaoFuEntity != null) {
            String filePath = chanPinJiaoFuEntity.getFilePath();
            String fileName = chanPinJiaoFuEntity.getFileName();
            if (filePath != null && !"".equals(filePath) && fileName != null && !"".equals(fileName)) {
                response.setContentType("application/octet-stream;charset=ISO8859-1");
                response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
                response.addHeader("Pargam", "no-cache");
                response.addHeader("Cache-Control", "no-cache");
                FileUploadUtil.get(response.getOutputStream(), filePath + "\\" + fileName);
            }
        }
    }

    //新增项目交付管理状态
    @PostMapping("/addXiangMuData")
    public Map<String, Object> addXiangMuData(ChanPinJiaoFuEntity chanPinJiaoFuEntity) {
        chanPinJiaoFuService.addXiangMuData(chanPinJiaoFuEntity);
        return ResponseDataUtil.ok("新增产品交付状态成功");
    }

    //根据id  编辑项目交付管理状态
    @PutMapping("/editXiangMuDataById/{id}")
    public Map<String, Object> editXiangMuDataById(@PathVariable Integer id, ChanPinJiaoFuEntity chanPinJiaoFuEntity) {
        chanPinJiaoFuService.editXiangMuDataById(chanPinJiaoFuEntity);
        return ResponseDataUtil.ok("编辑产品交付状态成功");
    }

    //根据id  删除对应的项目交付管理状态
    @DeleteMapping("/deletXiangMuDataById/{id}")
    public Map<String, Object> deletXiangMuDataById(@PathVariable Integer id) {
        chanPinJiaoFuService.deletXiangMuDataById(id);
        return ResponseDataUtil.ok("删除成功");
    }

    //根据id 批量删除项目交付管理状态
    @DeleteMapping("/deletXiangMuDataByIds/{ids}")
    public Map<String, Object> deletXiangMuDataByIds(@PathVariable Integer[] ids) {
        chanPinJiaoFuService.deletXiangMuDataByIds(ids);
        return ResponseDataUtil.ok("批量删除数据成功");
    }
}
