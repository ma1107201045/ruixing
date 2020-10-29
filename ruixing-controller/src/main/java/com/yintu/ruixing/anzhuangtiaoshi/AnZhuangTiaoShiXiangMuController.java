package com.yintu.ruixing.anzhuangtiaoshi;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuFileEntity;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/7/10 20:43
 * @Version 1.0
 * 需求:安装调试模块
 */
@RestController
@RequestMapping("/AnZhuangTiaoShiAll")
public class AnZhuangTiaoShiXiangMuController extends SessionController {
    @Autowired
    private AnZhuangTiaoShiXiangMuService anZhuangTiaoShiXiangMuService;

    //安装调试的三级树
    @RequestMapping
    public Map<String, Object> findSanJiShu() {
        List<TreeNodeUtil> treeNodeUtils = anZhuangTiaoShiXiangMuService.findSanJiShu();
        return ResponseDataUtil.ok("查询成功", treeNodeUtils);
    }

    //新增三级树中的项目
    @PostMapping("/addSanJiShuXiangMu")
    public Map<String,Object>addSanJiShuXiangMu(AnZhuangTiaoShiXiangMuEntity anZhuangTiaoShiXiangMuEntity)throws Exception{
        anZhuangTiaoShiXiangMuService.addSanJiShuXiangMu(anZhuangTiaoShiXiangMuEntity);
        return ResponseDataUtil.ok("新增项目成功");
    }

    //  根据id  编辑三级树的项目
    @PutMapping("/editSanJiShuById/{id}")
    public Map<String, Object> editSanJiShu(@PathVariable Integer id, AnZhuangTiaoShiXiangMuEntity anZhuangTiaoShiXiangMuEntity) {
        anZhuangTiaoShiXiangMuService.editSanJiShu(anZhuangTiaoShiXiangMuEntity);
        return ResponseDataUtil.ok("修改项目成功");
    }
    //根据id   删除三级树的项目
    @DeleteMapping("/deletSanJiShuById/{id}")
    public Map<String,Object>deletSanJiShuById(@PathVariable Integer id){
        Integer chezhantotal=anZhuangTiaoShiXiangMuService.findCheZhanTotal(id);
        if (chezhantotal==0){
            anZhuangTiaoShiXiangMuService.deletSanJiShuById(id);
            return ResponseDataUtil.ok("删除项目成功");
        }else {
            return ResponseDataUtil.error("此线段下有车站,不能删除");
        }
    }


    //查询消息提醒
    @GetMapping("/findXiaoXi")
    public Map<String, Object> findXiaoXi() {
        Integer senderid = this.getLoginUser().getId().intValue();
        List<MessageEntity> contextlist = anZhuangTiaoShiXiangMuService.findXiaoXi(senderid);
        return ResponseDataUtil.ok("查询消息成功", contextlist);
    }

    //查询所有的项目类型
    @GetMapping("/findAllXiangMuType")
    public Map<String,Object>findAllXiangMuType(){
        List<AnZhuangTiaoShiCheZhanXiangMuTypeEntity> xiangMuTypeEntities=anZhuangTiaoShiXiangMuService.findAllXiangMuType();
        return ResponseDataUtil.ok("查询项目类型成功",xiangMuTypeEntities);
    }

    //查询关联项目及其编号
    @GetMapping("/findXiangMuAndBianHao")
    public Map<String,Object>findXiangMuAndBianHao(){
        List<ChanPinJiaoFuXiangMuFileEntity> chanPinJiaoFuXiangMuFileEntities=anZhuangTiaoShiXiangMuService.findXiangMuAndBianHao();
        return ResponseDataUtil.ok("查询关联项目及其编号成功",chanPinJiaoFuXiangMuFileEntities);
    }


    //根据三级树的类型  查询类型下的数据
    @GetMapping("/findXianDuanDataByLeiXing")
    public Map<String,Object>findXianDuanDataByLeiXing(Integer leiXingId ,Integer page,Integer size){
        PageHelper.startPage(page,size);
        List<AnZhuangTiaoShiXiangMuEntity>xiangMuEntities=anZhuangTiaoShiXiangMuService.findXianDuanDataByLeiXing(leiXingId,page,size);
        PageInfo<AnZhuangTiaoShiXiangMuEntity>xiangMuEntityPageInfo=new PageInfo<>(xiangMuEntities);
        return ResponseDataUtil.ok("查询数据成功",xiangMuEntityPageInfo);
    }

    //查询所有的项目 和 年份
    @GetMapping("/findXianDuanNameAndYear")
    public Map<String,Object>findXianDuanNameAndYear(){
        List<AnZhuangTiaoShiXiangMuEntity> xiangMuEntities=anZhuangTiaoShiXiangMuService.findXianDuanNameAndYear();
        return ResponseDataUtil.ok("查询线段名和年份成功",xiangMuEntities);
    }

    //根据项目名 或年份 或项目类型 或项目状态查询数据
    @GetMapping("/findXianDuanBySomedata")
    public Map<String,Object>findXianDuanBySomedata(Integer page,Integer size,
                                                    String xdname,String year,
                                                    String xdtype,Integer xdleixing){
        if (year.equals("NaN")) {
            year = null;
        }if (xdtype.equals("")){
            xdtype=null;
        }if (xdname.equals("")){
            xdname=null;
        }
        PageHelper.startPage(page,size);
        List<AnZhuangTiaoShiXiangMuEntity> xiangMuEntities=anZhuangTiaoShiXiangMuService.findXianDuanBySomedata(page,size,xdname,year,xdtype,xdleixing);
        PageInfo<AnZhuangTiaoShiXiangMuEntity> xiangMuEntityPageInfo=new PageInfo<>(xiangMuEntities);
        return ResponseDataUtil.ok("查询数据成功",xiangMuEntityPageInfo);
    }

    //根据id 进行单个或者批量下载到Excel
    @GetMapping("/downLoadByIds/{ids}")
    public void exportFile(@PathVariable Integer[] ids, HttpServletResponse response) throws IOException {
        String fileName = "安装调试线段列表" + System.currentTimeMillis() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        anZhuangTiaoShiXiangMuService.exportFile(response.getOutputStream(), ids);
    }

    //查询近一个月开通的项目
    @GetMapping("/findLastMonthXiangMu")
    public Map<String,Object>findLastMonthXiangMu(Integer page,Integer size)throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        Date end = c.getTime();
        String today= format.format(end);//当前日期
        c.add(Calendar.MONTH, -1);
        Date start = c.getTime();
        String lastMothDay = format.format(start);//前一月
        PageHelper.startPage(page,size);
        List<AnZhuangTiaoShiXiangMuEntity> xiangMuEntityList = anZhuangTiaoShiXiangMuService.findLastMonthXiangMu(today, lastMothDay);
        PageInfo<AnZhuangTiaoShiXiangMuEntity>xiangMuEntityPageInfo=new PageInfo<>(xiangMuEntityList);
        return ResponseDataUtil.ok("查询成功",xiangMuEntityPageInfo);
    }



}
