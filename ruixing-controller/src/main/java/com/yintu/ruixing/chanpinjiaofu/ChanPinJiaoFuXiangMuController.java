package com.yintu.ruixing.chanpinjiaofu;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.FileUploadUtil;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuEntity;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuFileEntity;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuService;
import com.yintu.ruixing.xitongguanli.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/7/1 13:43
 * @Version 1.0
 * 需求:产品需求所有
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Controller
@RequestMapping("/ChanPinJiaoFuXiangMuAll")
public class ChanPinJiaoFuXiangMuController {
    @Autowired
    private ChanPinJiaoFuXiangMuService chanPinJiaoFuXiangMuService;

    @Autowired
    private UserService userService;

    //创建三级树
    @GetMapping
    @ResponseBody
    public Map<String, Object> findSanJiShu() {
        List<TreeNodeUtil> treeNodeUtils = chanPinJiaoFuXiangMuService.findSanJiShu();
        return ResponseDataUtil.ok("查询成功", treeNodeUtils);
    }

    //新增项目
    @ResponseBody
    @PostMapping("/addXiangMu")
    public Map<String, Object> addXiangMu(ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity) {
        chanPinJiaoFuXiangMuService.addXiangMu(chanPinJiaoFuXiangMuEntity);
        return ResponseDataUtil.ok("添加项目成功");
    }

    //查询消息提醒
    @ResponseBody
    @GetMapping("/findXiaoXi")
    public Map<String,Object>findXiaoXi(){
        List<MessageEntity> contextlist=chanPinJiaoFuXiangMuService.findXiaoXi();
        return ResponseDataUtil.ok("添加消息成功",contextlist);
    }

    //根据消息id   更改信息状态
    @ResponseBody
    @PutMapping("/editXiaoXiById/{id}")
    public Map<String,Object>editXiaoXiById(@PathVariable Integer id,MessageEntity messageEntity){
        chanPinJiaoFuXiangMuService.editXiaoXiById(messageEntity);
        return ResponseDataUtil.ok("更改信息状态成功");
    }
    //添加新消息提醒
    //添加定时任务
    @Scheduled(cron = "0 0 06 * * ?")
    @ResponseBody
    @GetMapping ("/addXiaoXi")
    public void addXiaoXi()throws Exception{
        //获取当前的时间
        SimpleDateFormat dateFormat = new SimpleDateFormat(" yyyy-MM-dd ");
        String currentDate = dateFormat.format( new Date() );
        Date date1 = dateFormat.parse(currentDate);
        //System.out.println("12313"+currentDate);
        //获取 项目的发货提醒日期
        List<ChanPinJiaoFuXiangMuEntity> xiangMuEntityList=chanPinJiaoFuXiangMuService.findAllXiangMu();
        for (ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity : xiangMuEntityList) {
            Integer id = chanPinJiaoFuXiangMuEntity.getId();
            String xiangmuName = chanPinJiaoFuXiangMuEntity.getXiangmuName();
            Date fahuoTixingTime = chanPinJiaoFuXiangMuEntity.getFahuoTixingTime();
            String currentDate1 = dateFormat.format( fahuoTixingTime);
            //System.out.println("fahuoTixingTime"+currentDate1);
            Date date2 = dateFormat.parse(currentDate1);
            int compareTo = date1.compareTo(date2);
            if (compareTo==0){
                MessageEntity messageEntity =new MessageEntity();
                //添加一条消息到消息表
                messageEntity.setContext(xiangmuName+"项目待发货，请及时联系顾客确认供货计划！");
                messageEntity.setType((short)2);
                messageEntity.setStatus((short)1);
                chanPinJiaoFuXiangMuService.addXiaoXi(messageEntity);
            }
        }
    }
    //根据选择的id  修改对应的项目内容
    @ResponseBody
    @PutMapping("/editXiangMuById/{id}")
    public Map<String, Object> editXiangMuById(@PathVariable Integer id, ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity) {
        chanPinJiaoFuXiangMuService.editXiangMuById(chanPinJiaoFuXiangMuEntity);
        return ResponseDataUtil.ok("修改项目数据成功");
    }

    //根据id  删除对应的项目
    @ResponseBody
    @DeleteMapping("/deletXiagMuById/{id}")
    public Map<String, Object> deletXiagMuById(@PathVariable Integer id) {
        chanPinJiaoFuXiangMuService.deletXiagMuById(id);
        return ResponseDataUtil.ok("删除数据成功");
    }

    //查询所有的数据
    @ResponseBody
    @GetMapping("/findAll")
    public Map<String, Object> findAll(Integer page, Integer size) {
        JSONObject js = new JSONObject();
        PageHelper.startPage(page, size);
        List<ChanPinJiaoFuXiangMuEntity> chanPinJiaoFuXiangMuEntities = chanPinJiaoFuXiangMuService.findAll(page, size);
        js.put("chanPinJiaoFuXiangMuEntities", chanPinJiaoFuXiangMuEntities);
        PageInfo<ChanPinJiaoFuXiangMuEntity> pageInfo = new PageInfo<>(chanPinJiaoFuXiangMuEntities);
        js.put("pageInfo", pageInfo);
        return ResponseDataUtil.ok("查询所有数据成功", pageInfo);
    }

    //根据项目编号 和项目名称  进行模糊查询
    @ResponseBody
    @GetMapping("/findXiangMuData")
    public Map<String, Object> findXiangMuData(String xiangMuBianHao, String xiangMuName, Integer page, Integer size) {
        JSONObject js = new JSONObject();
        PageHelper.startPage(page, size);
        List<ChanPinJiaoFuXiangMuEntity> chanPinJiaoFuXiangMuEntities = chanPinJiaoFuXiangMuService.findXiangMuData(xiangMuBianHao, xiangMuName, page, size);
        js.put("chanPinJiaoFuXiangMuEntities", chanPinJiaoFuXiangMuEntities);
        PageInfo<ChanPinJiaoFuXiangMuEntity> pageInfo = new PageInfo<>(chanPinJiaoFuXiangMuEntities);
        js.put("pageInfo", pageInfo);
        return ResponseDataUtil.ok("查询数据成功", js);
    }

    //根据树的id  查询对应的数据
    @ResponseBody
    @GetMapping("/findXiangMuByIds")
    public Map<String, Object> findXiangMuByIds(Integer stateid, Integer id, Integer typeid, Integer page, Integer size) {
        JSONObject js = new JSONObject();
        PageHelper.startPage(page, size);
        List<ChanPinJiaoFuXiangMuEntity> chanPinJiaoFuXiangMuEntities = chanPinJiaoFuXiangMuService.findXiangMuByIds(stateid, id, typeid, page, size);
        js.put("chanPinJiaoFuXiangMuEntities", chanPinJiaoFuXiangMuEntities);
        PageInfo<ChanPinJiaoFuXiangMuEntity> pageInfo = new PageInfo<>(chanPinJiaoFuXiangMuEntities);
        js.put("pageInfo", pageInfo);
        return ResponseDataUtil.ok("查询数据成功", js);
    }

    //新增文件列表
    @ResponseBody
    @PostMapping("/addXiangMuFile")
    public Map<String, Object> addXiangMuFile(ChanPinJiaoFuXiangMuFileEntity chanPinJiaoFuXiangMuFileEntity,Integer[] uids) {
        chanPinJiaoFuXiangMuService.addXiangMuFile(chanPinJiaoFuXiangMuFileEntity,uids);
        return ResponseDataUtil.ok("新增文件列表成功");
    }

    //根据id  修改文件列表
    @ResponseBody
    @PutMapping("/editXiangMuFileById/{id}")
    public Map<String, Object> editXiangMuFileById(@PathVariable Integer id,Integer[] uids, ChanPinJiaoFuXiangMuFileEntity chanPinJiaoFuXiangMuFileEntity) {
        chanPinJiaoFuXiangMuService.editXiangMuFileById(chanPinJiaoFuXiangMuFileEntity,id,uids);
        return ResponseDataUtil.ok("修改数据成功");
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
        ChanPinJiaoFuXiangMuFileEntity chanPinJiaoFuXiangMuFileEntity = chanPinJiaoFuXiangMuService.findById(id);
        if (chanPinJiaoFuXiangMuFileEntity != null) {
            String filePath = chanPinJiaoFuXiangMuFileEntity.getFilePath();
            String fileName = chanPinJiaoFuXiangMuFileEntity.getFileName();
            if (filePath != null && !"".equals(filePath) && fileName != null && !"".equals(fileName)) {
                response.setContentType("application/octet-stream;charset=ISO8859-1");
                response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
                response.addHeader("Pargam", "no-cache");
                response.addHeader("Cache-Control", "no-cache");
                FileUploadUtil.get(response.getOutputStream(), filePath + "\\" + fileName);
            }
        }
    }

    //根据id  删除对应的文件
    @ResponseBody
    @DeleteMapping("/deletXiangMuFileById/{id}")
    public Map<String, Object> deletXiangMuFileById(@PathVariable Integer id) {
        chanPinJiaoFuXiangMuService.deletXiangMuFileById(id);
        return ResponseDataUtil.ok("删除文件成功");
    }

    //根据id  单个或者批量删除文件
    @ResponseBody
    @DeleteMapping("/deletXiangMuFileByIds/{ids}")
    public Map<String, Object> deletXiangMuFileByIds(@PathVariable Integer[] ids) {
        chanPinJiaoFuXiangMuService.deletXiangMuFileByIds(ids);
        return ResponseDataUtil.ok("单个或者批量删除文件成功");
    }

    //查询所有审核人姓名
    @ResponseBody
    @GetMapping("/findAllAuditorName")
    public Map<String,Object>findAllAuditorNamre(String truename){
        List<UserEntity> userEntities=userService.findByTruename(truename);
        return ResponseDataUtil.ok("查询姓名成功",userEntities);
    }

    //根据文件id  查询对应的审核人名字
    @ResponseBody
    @GetMapping("/findAllAuditorNameById/{id}")
    public Map<String,Object> findAllAuditorNameById(@PathVariable Integer id ){
        List<UserEntity > userEntities=chanPinJiaoFuXiangMuService.findAllAuditorNameById(id);
        return ResponseDataUtil.ok("查询审核人名成功",userEntities);

    }
    //////////////////////////////交付情况统计/////////////////////////////////////

    //统计各个状态的项目数量
    @ResponseBody
    @GetMapping("/findJiaoFuQingKuangNumberAll")
    public Map<String, Object> findJiaoFuQingKuangNumberAll() {
        // Map<String,Object> map=new HashMap<>();
        Map<String, Object> map = chanPinJiaoFuXiangMuService.findJiaoFuQingKuangNumberAll();
        return ResponseDataUtil.ok("查询统计数量成功", map);
    }

    //查询各个状态的列表
    @ResponseBody
    @GetMapping("/findJiaoFuQingKuangList")
    public Map<String, Object> findJiaoFuQingKuangList(String choiceTing, Integer page, Integer size) {
        if (choiceTing.equals("daiQianShu") || choiceTing.equals("daiYanGong")) {
            PageHelper.startPage(page, size);
            List<ChanPinJiaoFuXiangMuEntity> chanPinJiaoFuXiangMuEntities = chanPinJiaoFuXiangMuService.findJiaoFuQingKuangList(choiceTing, page, size);
            PageInfo<ChanPinJiaoFuXiangMuEntity> pageInfo = new PageInfo<>(chanPinJiaoFuXiangMuEntities);
            return ResponseDataUtil.ok("查询数据成功", pageInfo);
        } else {
            PageHelper.startPage(page, size);
            List<ChanPinJiaoFuXiangMuEntity> chanPinJiaoFuXiangMuEntities = chanPinJiaoFuXiangMuService.findJiaoFuQingKuangLists(choiceTing, page, size);
            PageInfo<ChanPinJiaoFuXiangMuEntity> pageInfo = new PageInfo<>(chanPinJiaoFuXiangMuEntities);
            return ResponseDataUtil.ok("查询数据成功", pageInfo);
        }
    }
}
