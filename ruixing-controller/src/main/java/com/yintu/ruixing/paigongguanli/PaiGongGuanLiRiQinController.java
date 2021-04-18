package com.yintu.ruixing.paigongguanli;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.xitongguanli.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/8/24 18:10
 * @Version 1.0
 * 需求:派工管理   日勤
 */
@RestController
@RequestMapping("/RiQinAll")
public class PaiGongGuanLiRiQinController extends SessionController {
    @Autowired
    private PaiGongGuanLiRiQinService paiGongGuanLiRiQinService;
    @Autowired
    private UserService userService;


    //查询员工姓名  等信息
    @GetMapping("/findAllUserName")
    public Map<String, Object> findAllUserName(String truename) {
        List<UserEntity> userEntities = userService.findAllOrByUsername(truename);
        return ResponseDataUtil.ok("查询姓名成功", userEntities);
    }

    //查询所有的日勤管理数据
    @GetMapping("/findAllRiQin")
    public Map<String,Object>findAllRiQin(Integer page,Integer size){
        PageHelper.startPage(page,size);
        List<PaiGongGuanLiRiQinEntity> riQinEntityList=paiGongGuanLiRiQinService.findAllRiQin(page,size);
        PageInfo<PaiGongGuanLiRiQinEntity> riQinEntityPageInfo=new PageInfo<>(riQinEntityList);
        return ResponseDataUtil.ok("查询日勤数据成功",riQinEntityPageInfo);
    }

    //根据名字查询对应的日勤管理数据
    @GetMapping("/findAllRiQinByUserName")
    public Map<String,Object>findAllRiQinByUserName(Integer page,Integer size,String username){
        PageHelper.startPage(page,size);
        List<PaiGongGuanLiRiQinEntity> riQinEntityList=paiGongGuanLiRiQinService.findAllRiQinByUserName(page,size,username);
        PageInfo<PaiGongGuanLiRiQinEntity> riQinEntityPageInfo=new PageInfo<>(riQinEntityList);
        return ResponseDataUtil.ok("查询日勤数据成功",riQinEntityPageInfo);
    }

///////////////////////////////////////////////////////////////////////////////////////
    //根据uid  查询此人所有的日勤状态
    @GetMapping("/findAllRiQinByUid")
    public Map<String,Object>findAllRiQinByUid(){
        Integer senderid = this.getLoginUser().getId().intValue();
        List<PaiGongGuanLiRiQinEntity> riQinEntityList=paiGongGuanLiRiQinService.findAllRiQinByUid(senderid);
        return ResponseDataUtil.ok("查询日勤状态成功",riQinEntityList);
    }

    //新增日勤
    @PostMapping("/addRiQin")
    public Map<String,Object>addRiQin(PaiGongGuanLiRiQinEntity paiGongGuanLiRiQinEntity){
        String username = this.getLoginUser().getTrueName();
        Integer senderid = this.getLoginUser().getId().intValue();
        paiGongGuanLiRiQinService.addRiQin(paiGongGuanLiRiQinEntity,senderid);
        return ResponseDataUtil.ok("新增日勤成功");
    }

    //根据日勤日期  编辑对应的数据
    @PutMapping("/editRiQinByDate")
    public Map<String,Object>editRiQinById(PaiGongGuanLiRiQinEntity paiGongGuanLiRiQinEntity){
        paiGongGuanLiRiQinService.editRiQinById(paiGongGuanLiRiQinEntity);
        return ResponseDataUtil.ok("编辑日勤数据成功");
    }


    //查询所有人的日勤数据
    @GetMapping("/findAllRiQinDatas")
    public Map<String,Object>findAllRiQinDatas(Integer page,Integer size){
        PageHelper.startPage(page,size);
        List<PaiGongGuanLiRiQinEntity>riQinEntityList=paiGongGuanLiRiQinService.findAllRiQinDatas(page,size);
        PageInfo<PaiGongGuanLiRiQinEntity> riQinEntityPageInfo=new PageInfo<>(riQinEntityList);
        return ResponseDataUtil.ok("查询成功",riQinEntityPageInfo);
    }


    //展示所有人的日勤数据
    @GetMapping("/findAllPeopleRiQinDatas")
    public Map<String,Object>findAllPeopleRiQinDatas(){
        JSONObject js =paiGongGuanLiRiQinService.findAllPeopleRiQinDatas();
        return ResponseDataUtil.ok("查询成功",js);
    }

    //查看所有人的历史日勤数据
    @GetMapping("/findAllPeopleHistoryRiQinDatas")
    public Map<String,Object>findAllPeopleHistoryRiQinDatas(String riqi){
        JSONObject js =paiGongGuanLiRiQinService.findAllPeopleHistoryRiQinDatas(riqi);
        return ResponseDataUtil.ok("查询成功",js);
    }


    //更改人员日勤状态
    @PutMapping("/editUserDayState")
    public Map<String,Object>editUserDayState(Integer userid,String dayTime,Integer dayState){
        paiGongGuanLiRiQinService.editUserDayState(userid,dayTime,dayState);
        return ResponseDataUtil.ok("修改状态成功");
    }









    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println("ssss="+actualMaximum);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //获取前月的第一天
        Calendar cal_1=Calendar.getInstance();//获取当前日期
        cal_1.add(Calendar.MONTH, 0);
        cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        String firstDay = format.format(cal_1.getTime());
        System.out.println("ffffff="+firstDay);

        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.DAY_OF_MONTH,2);//设置为1号,当前日期既为本月第一天
        String lastDay = format.format(cale.getTime());
        System.out.println("llll="+lastDay);


//获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String monthfirst = format.format(c.getTime());
        System.out.println("===============nowfirst:" + monthfirst);

//获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String monthlast = format.format(ca.getTime());
        System.out.println("===============last:" + monthlast);




    }

}
