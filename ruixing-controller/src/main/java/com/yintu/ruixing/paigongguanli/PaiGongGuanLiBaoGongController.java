package com.yintu.ruixing.paigongguanli;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.guzhangzhenduan.XianDuanEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Mr.liu
 * @Date 2020/10/9 11:06
 * @Version 1.0
 * 需求:报工
 */
@RestController
@RequestMapping("/baoGongAll")
public class PaiGongGuanLiBaoGongController extends SessionController {
    @Autowired
    private PaiGongGuanLiBaoGongService paiGongGuanLiBaoGongService;


    //根据当前用户查询对应的未完成报工单
    @GetMapping("/findAllNotOverPaiGong")
    public Map<String,Object>findAllNotOverPaiGong(){
        Integer userid = this.getLoginUser().getId().intValue();
        List<PaiGongGuanLiPaiGongDanEntity> paiGongDanEntityList=paiGongGuanLiBaoGongService.findAllNotOverPaiGong(userid);
        return ResponseDataUtil.ok("查询未完成的派工单成功",paiGongDanEntityList);
    }

    //根据用户id 查询对应的未完成的报工单
    @GetMapping("/findAllNotOverPaiGongByUid")
    public Map<String,Object>findAllNotOverPaiGongByUid(Integer userid){
        List<PaiGongGuanLiPaiGongDanEntity> paiGongDanEntityList=paiGongGuanLiBaoGongService.findAllNotOverPaiGong(userid);
        return ResponseDataUtil.ok("查询未完成的派工单成功",paiGongDanEntityList);
    }

    //查询所有的线段
    @GetMapping("/findAllXianDuan")
    public Map<String,Object>findAllXianDuan(){
        List<XianDuanEntity>xianDuanEntityList=paiGongGuanLiBaoGongService.findAllXianDuan();
        return ResponseDataUtil.ok("查询线段成功",xianDuanEntityList);
    }


    //新增报工
    @PostMapping("/addBaoGong")
    public Map<String,Object>addBaoGong(@RequestBody JSONArray baoGongDatas){
        String username = this.getLoginUser().getTrueName();
        Integer senderid = this.getLoginUser().getId().intValue();
        paiGongGuanLiBaoGongService.addBaoGongDan(baoGongDatas,username,senderid);
        return ResponseDataUtil.ok("新增成功");
    }

    //初始化报工页面
    @GetMapping("/findAllBaoGong")
    public Map<String,Object>findAllBaoGong(Integer page,Integer size,Integer baoGongType){
        PageHelper.startPage(page,size);
        List<PaiGongGuanLiBaoGongEntity> baoGongEntityList=paiGongGuanLiBaoGongService.findAllBaoGong(baoGongType);
        PageInfo<PaiGongGuanLiBaoGongEntity> baoGongEntityPageInfo=new PageInfo<>(baoGongEntityList);
        return ResponseDataUtil.ok("查询成功",baoGongEntityPageInfo);
    }

    //根据条件查询报工
    @GetMapping("/findBaoGongBySomethings")
    public Map<String,Object>findBaoGongBySomethings(Integer page,Integer size,String startTime,String endTime,Integer userid,String xianDuan,Integer isNotClose,Integer baoGongType){
        PageHelper.startPage(page,size);
        List<PaiGongGuanLiBaoGongEntity> baoGongEntityList=paiGongGuanLiBaoGongService.findBaoGongBySomethings(startTime,endTime,userid,xianDuan,isNotClose,baoGongType);
        PageInfo<PaiGongGuanLiBaoGongEntity> baoGongEntityPageInfo=new PageInfo<>(baoGongEntityList);
        return ResponseDataUtil.ok("查询成功",baoGongEntityPageInfo);
    }

    //根据报工 查询参与报工的人员
    @GetMapping("/findBaoGongUser")
    public Map<String,Object>findBaoGongUser(Integer baoGongType){
        List<PaiGongGuanLiUserEntity> userEntityList=paiGongGuanLiBaoGongService.findBaoGongUser(baoGongType);
        return ResponseDataUtil.ok("查询报工人员成功",userEntityList);
    }

    //根据登录人员id  初始化页面  或者根据中时间查询
    @GetMapping("/findAllBaoGongByUid")
    public Map<String,Object>findAllBaoGongByUid(Integer page, Integer size, Date datetime){
        Integer senderid = this.getLoginUser().getId().intValue();
        PageHelper.startPage(page,size);
        List<PaiGongGuanLiBaoGongEntity> baoGongEntityList=paiGongGuanLiBaoGongService.findAllBaoGongByUid(page,size,senderid,datetime);
        PageInfo<PaiGongGuanLiBaoGongEntity> baoGongEntityPageInfo=new PageInfo<>(baoGongEntityList);
        return ResponseDataUtil.ok("查询成功",baoGongEntityPageInfo);
    }

    //根据id  编辑对应的报工
    @PutMapping("/editBaoGongById/{id}")
    public Map<String,Object>editBaoGongById(@PathVariable Integer id,PaiGongGuanLiBaoGongEntity paiGongGuanLiBaoGongEntity){
        String username = this.getLoginUser().getTrueName();
        Integer senderid = this.getLoginUser().getId().intValue();
        paiGongGuanLiBaoGongService.editBaoGongById(username,senderid,paiGongGuanLiBaoGongEntity);
        return ResponseDataUtil.ok("编辑成功");
    }

    //根据id 单个或者批量删除报工
    @DeleteMapping("/deleteBaoGongByIds/{ids}")
    public Map<String,Object>deleteBaoGongByIds(@PathVariable Integer[] ids){
        paiGongGuanLiBaoGongService.deleteBaoGongByIds(ids);
        return ResponseDataUtil.ok("删除成功");
    }

    //关闭报工单
    @PutMapping("/closeBaoGongById/{id}")
    public Map<String,Object>closeBaoGongById(@PathVariable Integer id,PaiGongGuanLiBaoGongEntity paiGongGuanLiBaoGongEntity){
        String username = this.getLoginUser().getTrueName();
        Integer senderid = this.getLoginUser().getId().intValue();
        paiGongGuanLiBaoGongEntity.setIsnotover(0);
        paiGongGuanLiBaoGongService.editBaoGongById(username,senderid,paiGongGuanLiBaoGongEntity);
        return ResponseDataUtil.ok("关闭报工成功");
    }

    ////////////////////////文件上传/////////////////////////////////
    @PostMapping("/addFile")
    public Map<String,Object>addFile(PaiGongGuanLiBaoGongFileEntity paiGongGuanLiBaoGongFileEntity){
        String username = this.getLoginUser().getTrueName();
        paiGongGuanLiBaoGongFileEntity.setCreatetime(new Date());
        paiGongGuanLiBaoGongFileEntity.setCreatename(username);
        paiGongGuanLiBaoGongFileEntity.setUpdatetime(new Date());
        paiGongGuanLiBaoGongFileEntity.setUpdatename(username);
        paiGongGuanLiBaoGongService.addFile(paiGongGuanLiBaoGongFileEntity);
        return ResponseDataUtil.ok("新增文件成功");
    }


    @DeleteMapping("/deleteFileByIds/{ids}")
    public Map<String,Object>deleteFileByIds(@PathVariable Integer[] ids){
        paiGongGuanLiBaoGongService.deleteFileByIds(ids);
        return ResponseDataUtil.ok("删除文件成功");
    }

    //根据报工id  type查询对应的文件
    @GetMapping("/findFileByBid/{bid}")
    public Map<String,Object>findFileByBid(@PathVariable Integer bid,Integer baoGongType,Integer page,Integer size){
        PageHelper.startPage(page,size);
        List<PaiGongGuanLiBaoGongFileEntity>fileEntityList=paiGongGuanLiBaoGongService.findFileByBid(bid,baoGongType);
        PageInfo<PaiGongGuanLiBaoGongFileEntity> fileEntityPageInfo=new PageInfo<>(fileEntityList);
        return ResponseDataUtil.ok("查询文件成功",fileEntityPageInfo);
    }

    //////////////////////首页展示/////////////////////////

    //人员日程视图
    @GetMapping("/findAllBaoGongAndAllComment")
    public Map<String,Object>findAllBaoGongAndAllComment(Integer userid,String riqi){
        List<PaiGongGuanLiBaoGongEntity> baoGongEntityList=paiGongGuanLiBaoGongService.findAllBaoGongAndAllComment(userid,riqi);
        return ResponseDataUtil.ok("查询成功",baoGongEntityList);
    }


    //人员分布视图
    @GetMapping("/findAllChuChaiPeopele")
    public Map<String,Object>findAllChuChaiPeopele(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date nowDate=new Date();
        long nowtime = nowDate.getTime();
        long lasttime=nowtime-108000000;
        String nowDateTime = sdf.format(nowDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lasttime);
        String lastDateTime = sdf.format(calendar.getTime());
        List<PaiGongGuanLiBaoGongEntity>paiGongDanEntityList=paiGongGuanLiBaoGongService.findAllChuChaiPeopele(lastDateTime,nowDateTime);
        return ResponseDataUtil.ok("查询成功",paiGongDanEntityList);
    }
















    //人员分布情况
    @GetMapping("/findPropleAddress")
    public Map<String,Object>findPropleAddress(Integer page,Integer size){
        PageHelper.startPage(page,size);
        List<PaiGongGuanLiRiQinEntity> riQinEntityList=paiGongGuanLiBaoGongService.findPropleAddress(page,size);
        PageInfo<PaiGongGuanLiRiQinEntity> riQinEntityPageInfo=new PageInfo<>(riQinEntityList);
        return ResponseDataUtil.ok("查询成功",riQinEntityPageInfo);
    }


    //人员地图分布
    @GetMapping("/findPeopleAddressOnMap")
    public Map<String,Object>findPeopleAddressOnMap(){
        List<PaiGongGuanLiRiQinEntity> riQinEntityList=paiGongGuanLiBaoGongService.findPeopleAddressOnMap();
        return ResponseDataUtil.ok("查询成功",riQinEntityList);
    }


    public static void main(String[] args) {
        Date dNow = new Date();//当前时间
        long time = dNow.getTime();
        System.out.println("ttttt"+time);

        // Date dBefore = new Date();

        Calendar calendar = Calendar.getInstance(); //得到日历

       // calendar.setTime(dNow);//把当前时间赋给日历

        calendar.add(Calendar.DAY_OF_MONTH,-1); //设置为前一天

        Calendar calendar1 = Calendar.getInstance(); //得到日历

        // calendar.setTime(dNow);//把当前时间赋给日历

        calendar1.add(Calendar.DAY_OF_MONTH,1);

        Date dBefore = calendar.getTime(); //得到前一天的时间
        Date dBefore1 = calendar1.getTime(); //得到前一天的时间

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式

        String defaultStartDate = sdf.format(dBefore); //格式化前一天
        String defaultStartDate1 = sdf.format(dBefore1); //格式化前一天

        String defaultEndDate = sdf.format(new Date()); //格式化当前时间

        System.out.println("前一天的时间是：" + defaultStartDate);
        System.out.println("前一天的时间是：" + defaultStartDate1);

        System.out.println("生成的时间是：" + defaultEndDate);



        String uu= UUID.randomUUID().toString();
        String uu2= UUID.randomUUID().toString();
        System.out.println("uuu"+uu);
        System.out.println("uuu"+uu2);



    }
}
