package com.yintu.ruixing.paigongguanli;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuEntity;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiPaiGongDanShenQingDao;
import com.yintu.ruixing.xitongguanli.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Mr.liu
 * @Date 2020/8/22 19:31
 * @Version 1.0
 * 需求:  派工单
 */
@RestController
@RequestMapping("/PaiGongDanAll")
public class PaiGongGuanLiPaiGongDanController extends SessionController {
    @Autowired
    private PaiGongGuanLiPaiGongDanService paiGongGuanLiPaiGongDanService;
    @Autowired
    private PaiGongGuanLiUserService paiGongGuanLiUserService;
    @Autowired
    private PaiGongGuanLiPaiGongDanShenQingService paiGongGuanLiPaiGongDanShenQingService;



    //自动创建派工单编号
    @GetMapping("/findPaiGongDanNum")
    public Map<String, Object> findPaiGongDanNum(String suoxie) {
        Date today = new Date();
        int month = DateUtil.month(today) + 1;
        String monthStr = Integer.toString(month).length() == 1 ? "0" + month : Integer.toString(month);
        int year = DateUtil.year(today);
        if (suoxie.equals("")) {
            return null;
        } else {
            String paigongdannum = paiGongGuanLiPaiGongDanService.findPaiGongDanNum(suoxie);
            if (paigongdannum == null) {
                String paiGongDanNum = suoxie + year + monthStr + "0001";
                return ResponseDataUtil.ok("自动生成派工单号成功", paiGongDanNum);
            } else {
                String substring = paigongdannum.substring(4, 14);
                Integer i = Integer.parseInt(substring) + 1;
                String paiGongDanNum = suoxie + i.toString();
                return ResponseDataUtil.ok("自动生成派工单号成功", paiGongDanNum);
            }
        }
    }

    //查询所有派工的人员姓名
    @GetMapping("/findAllPaiGongUser")
    public Map<String,Object>findAllPaiGongUser(){
        String name=null;
        List<PaiGongGuanLiUserEntity>userEntityList=paiGongGuanLiUserService.findAllUser(name);
        return ResponseDataUtil.ok("查询派工人员姓名成功",userEntityList);
    }
    //新增派工单
    @PostMapping("/addPaiGongDan")
    public Map<String, Object> addPaiGongDan(PaiGongGuanLiPaiGongDanEntity paiGongGuanLiPaiGongDanEntity) {
        String username = this.getLoginUser().getTrueName();
        Integer senderid = this.getLoginUser().getId().intValue();
        paiGongGuanLiPaiGongDanEntity.setCreatename(username);
        paiGongGuanLiPaiGongDanEntity.setCreatetime(new Date());
        paiGongGuanLiPaiGongDanEntity.setUpdatename(username);
        paiGongGuanLiPaiGongDanEntity.setUpdatetime(new Date());
        paiGongGuanLiPaiGongDanService.addPaiGongDan(paiGongGuanLiPaiGongDanEntity, username, senderid);
        return ResponseDataUtil.ok("新增派工单成功");
    }

    //初始化页面  或者根据编号模糊查询
    @GetMapping("/findPaiGongDan")
    public Map<String, Object> findPaiGongDan(Integer page, Integer size, String paiGongNumber,
                                              String startTime,String endTime,String xdName,
                                              String czName,String renWuShuXing,Integer peopeleId,Integer paiGongState) {
        PageHelper.startPage(page, size);
        List<PaiGongGuanLiPaiGongDanEntity> paiGongDanEntityList =
                paiGongGuanLiPaiGongDanService.findPaiGongDan(page, size, paiGongNumber,startTime,endTime,xdName,czName,renWuShuXing,peopeleId,paiGongState);
        PageInfo<PaiGongGuanLiPaiGongDanEntity> paiGongDanEntityPageInfo = new PageInfo<>(paiGongDanEntityList);
        return ResponseDataUtil.ok("查询成功", paiGongDanEntityPageInfo);
    }

    //根据派工单编号  查询关联单据
    @GetMapping("/findOnePaiGongDanByNum")
    public Map<String, Object> findOnePaiGongDanByNum(String PaiGongDanNum) {
        List<PaiGongGuanLiPaiGongDanEntity> paiGongDanEntityList = paiGongGuanLiPaiGongDanService.findOnePaiGongDanByNum(PaiGongDanNum);
        return ResponseDataUtil.ok("查询管理单据成功", paiGongDanEntityList);
    }


    //根据id  编辑派工单
    @PutMapping("/editPaiGongDanById/{id}")
    public Map<String, Object> editPaiGongDanById(@PathVariable Integer id, Integer uid, PaiGongGuanLiPaiGongDanEntity paiGongGuanLiPaiGongDanEntity) {
        String username = this.getLoginUser().getTrueName();
        Integer senderid = this.getLoginUser().getId().intValue();
        paiGongGuanLiPaiGongDanEntity.setUpdatetime(new Date());
        paiGongGuanLiPaiGongDanEntity.setUpdatename(username);
        paiGongGuanLiPaiGongDanService.editPaiGongDanById(id, senderid, uid, paiGongGuanLiPaiGongDanEntity, username);
        return ResponseDataUtil.ok("编辑成功");
    }

    //根据id  单个或者批量删除派工单
    @DeleteMapping("/deletePaiGongDanByIds/{ids}")
    public Map<String, Object> deletePaiGongDanByIds(@PathVariable Integer[] ids) {
        paiGongGuanLiPaiGongDanService.deletePaiGongDanByIds(ids);
        return ResponseDataUtil.ok("删除成功");
    }


    //根据派工单id  派工人员是否接受派工
    @PostMapping("/addRecordMessage")
    public Map<String, Object> addRecordMessage(Integer receiverid, Integer id, Integer isNotRefuse, String reason) {
        String username = this.getLoginUser().getTrueName();
        Integer senderid = this.getLoginUser().getId().intValue();
        paiGongGuanLiPaiGongDanService.doSomeThing(receiverid, senderid, id, isNotRefuse, reason, username);
        return ResponseDataUtil.ok("操作成功");
    }


    //根据派工单id  确定是否要同意完成派工
    @PostMapping("/addRecordMessageByid")
    public Map<String, Object> addRecordMessageByid(Integer receiverid, Integer id, Integer isNotRefuse, String reason) {
        String username = this.getLoginUser().getTrueName();
        Integer senderid = this.getLoginUser().getId().intValue();
        paiGongGuanLiPaiGongDanService.doSomeThingg(receiverid, senderid, id, isNotRefuse, reason, username);
        return ResponseDataUtil.ok("操作成功");
    }


    //查询所有的业务类别
    @GetMapping("/findAllBuiness")
    public Map<String, Object> findAllBuiness() {
        List<PaiGongGuanLiBusinessTypeEntity> businessTypeEntityList = paiGongGuanLiPaiGongDanService.findAllBuiness();
        return ResponseDataUtil.ok("查询成功", businessTypeEntityList);
    }

    //根据业务类型  查询对应的出差任务
    @GetMapping("/findBuinessById/{id}")
    public Map<String, Object> findBuinessById(@PathVariable Integer id) {
        List<PaiGongGuanLiBusinessTypeEntity> businessTypeEntityList = paiGongGuanLiPaiGongDanService.findBuinessById(id);
        return ResponseDataUtil.ok("查询成功", businessTypeEntityList);
    }

    //查询所有的派工数据
    @GetMapping("/findAllPaiGongDan")
    public Map<String, Object> findAllPaiGongDan() {
        List<PaiGongGuanLiPaiGongDanEntity> paiGongDanEntityList = paiGongGuanLiPaiGongDanService.findAllPaiGongDan();
        return ResponseDataUtil.ok("查询成功", paiGongDanEntityList);
    }


    //查看时间轴
    @GetMapping("/findRecordMessageByid/{id}")
    public Map<String, Object> findRecordMessageByid(@PathVariable Integer id) {
        List<PaiGongGuanLiPaiGongDanRecordMessageEntity> recordMessageEntityList = paiGongGuanLiPaiGongDanService.findRecordMessageByid(id);
        return ResponseDataUtil.ok("查询成功", recordMessageEntityList);
    }

    //查询消息提醒
    @GetMapping("/findXiaoXi")
    public Map<String, Object> findXiaoXi() {
        Integer senderid = this.getLoginUser().getId().intValue();
        List<MessageEntity> contextlist = paiGongGuanLiPaiGongDanService.findXiaoXi(senderid);
        return ResponseDataUtil.ok("查询消息成功", contextlist);
    }

    //根据id 查询对应的派工单数据
    @GetMapping("/findPaiGongDanByid/{id}")
    public Map<String,Object>findPaiGongDanByid(@PathVariable Integer id){
        PaiGongGuanLiPaiGongDanEntity paiGongDanEntity=paiGongGuanLiPaiGongDanService.findPaiGongDanByid(id);
        return ResponseDataUtil.ok("查询成功",paiGongDanEntity);
    }

    //查询所有的已派工人员
    @GetMapping("/finaAlreadyPaiGongPeople")
    public Map<String,Object>finaAlreadyPaiGongPeople(){
        List<PaiGongGuanLiUserEntity> userEntityList=paiGongGuanLiUserService.finaAlreadyPaiGongPeople();
        return ResponseDataUtil.ok("查询派工人员成功",userEntityList);
    }

    //取消派工
    @PutMapping("/quXiaoPaiGong/{id}")
    public Map<String,Object>quXiaoPaiGong(@PathVariable Integer id,PaiGongGuanLiPaiGongDanEntity paiGongGuanLiPaiGongDanEntity){
        String username = this.getLoginUser().getTrueName();
        Integer userid = this.getLoginUser().getId().intValue();
        paiGongGuanLiPaiGongDanService.quXiaoPaiGong(paiGongGuanLiPaiGongDanEntity,userid,username);
        return ResponseDataUtil.ok("取消派工成功");
    }


    //任务标记
    @PutMapping("/editTaskSignById/{id}")
    public Map<String,Object>editTaskSignById(@PathVariable Integer id,PaiGongGuanLiPaiGongDanEntity paiGongGuanLiPaiGongDanEntity){
        String username = this.getLoginUser().getTrueName();
        Integer userid = this.getLoginUser().getId().intValue();
        paiGongGuanLiPaiGongDanEntity.setUpdatename(username);
        paiGongGuanLiPaiGongDanEntity.setUpdatetime(new Date());
        paiGongGuanLiPaiGongDanService.editTaskSignById(paiGongGuanLiPaiGongDanEntity,username,userid);
        return ResponseDataUtil.ok("变更派工任务标记成功");
    }

    //改派
    @PutMapping("/editGaiPiaUserById/{id}")
    public Map<String,Object>editGaiPiaUserById(@PathVariable Integer id,PaiGongGuanLiPaiGongDanEntity paiGongGuanLiPaiGongDanEntity){
        Integer senderid = this.getLoginUser().getId().intValue();
        String username = this.getLoginUser().getTrueName();
        paiGongGuanLiPaiGongDanEntity.setUpdatename(username);
        paiGongGuanLiPaiGongDanEntity.setUpdatetime(new Date());
        paiGongGuanLiPaiGongDanService.editGaiPiaUserById(paiGongGuanLiPaiGongDanEntity,username,senderid);
        return ResponseDataUtil.ok("改派人员成功");
    }

    //添加申请
    @PostMapping("/addShenQind")
    public Map<String,Object>addShenQind(PaiGongGuanLiPaiGongDanShenQingEntity paiGongGuanLiPaiGongDanShenQingEntity){
        String username = this.getLoginUser().getTrueName();
        paiGongGuanLiPaiGongDanShenQingEntity.setCreatename(username);
        paiGongGuanLiPaiGongDanShenQingEntity.setCreatetime(new Date());
        paiGongGuanLiPaiGongDanShenQingEntity.setUpdatename(username);
        paiGongGuanLiPaiGongDanShenQingEntity.setUpdatetime(new Date());
        paiGongGuanLiPaiGongDanShenQingService.addShenQind(paiGongGuanLiPaiGongDanShenQingEntity,username);
        return ResponseDataUtil.ok("添加申请成功");
    }

    //查看申请
    @GetMapping("/findShenQing")
    public Map<String,Object>findShenQing(Integer paiGongId){
        Integer userid = this.getLoginUser().getId().intValue();
        List<PaiGongGuanLiPaiGongDanShenQingEntity>shenQingEntityList=paiGongGuanLiPaiGongDanShenQingService.findShenQing(paiGongId,userid);
        return ResponseDataUtil.ok("查询申请成功",shenQingEntityList);
    }

    //审批申请
    @PutMapping("/editShenQingById/{id}")
    public Map<String,Object>editShenQingById(@PathVariable Integer id,PaiGongGuanLiPaiGongDanShenQingEntity paiGongGuanLiPaiGongDanShenQingEntity){
        String username = this.getLoginUser().getTrueName();
        paiGongGuanLiPaiGongDanShenQingEntity.setUpdatename(username);
        paiGongGuanLiPaiGongDanShenQingEntity.setUpdatetime(new Date());
        paiGongGuanLiPaiGongDanShenQingService.editShenQingById(paiGongGuanLiPaiGongDanShenQingEntity,username);
        return ResponseDataUtil.ok("审批申请成功");
    }


    //根据项目类型  任务属性  筛选符合条件的人员
    @GetMapping("/findUserBySomething")
    public Map<String,Object>findUserBySomething(String xiangMuType,String reWuShuXing,String chuChaiType,String yeWuType,String startTime,String endTime){
        List<PaiGongGuanLiUserEntity> userEntityList=paiGongGuanLiPaiGongDanService.findUserBySomething(xiangMuType,reWuShuXing,chuChaiType,yeWuType,startTime,endTime);
        return ResponseDataUtil.ok("查询符合条件的人员成功",userEntityList);
    }

    //定时更改出差人员的状态
    @Scheduled(cron = "0 0 02 * * ?")
    @ResponseBody
    @GetMapping("/updatePeopleState")
    public void updatePeopleState(){
       // Calendar calendar = Calendar.getInstance(); //得到日历
      //  calendar.add(Calendar.DAY_OF_MONTH, -1); //设置为前一天
      //  Date dBefore = calendar.getTime(); //得到前一天的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        List<Integer>peopleId=paiGongGuanLiPaiGongDanService.findChuChaiPeopleing();
        for (Integer pid : peopleId) {
            paiGongGuanLiPaiGongDanService.updateUserOtherstate(today,pid);
        }

    }


    //////////////////模块首页展示//////////////////////////
    @GetMapping("/findAllPaiGongOnHome")
    public Map<String,Object>findAllPaiGongOnHome(Integer page,Integer size){
        PageHelper.startPage(page,size);
        List<PaiGongGuanLiPaiGongDanEntity> paiGongDanEntityList = paiGongGuanLiPaiGongDanService.findAllPaiGongOnHome(page, size);
        PageInfo<PaiGongGuanLiPaiGongDanEntity> paiGongDanEntityPageInfo = new PageInfo<>(paiGongDanEntityList);
        return ResponseDataUtil.ok("查询成功", paiGongDanEntityPageInfo);
    }








    public static void main(String[] args) throws ParseException {
        Date today = new Date();
        int month = DateUtil.month(today) + 1;
        String monthStr = Integer.toString(month).length() == 1 ? "0" + month : Integer.toString(month);
        int year = DateUtil.year(today);
        System.out.println(month);
        System.out.println(year);
        String num = "sl 2020 08 0001";
        String substring1 = num.substring(8, 12);
        String substring = num.substring(8);
        System.out.println(substring);
        System.out.println(substring1);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today1 = sdf.format(new Date());
        String today2="2021-04-15";
        long time1 = sdf.parse(today1).getTime();
        long time2 = sdf.parse(today2).getTime();
        System.out.println("time1="+time1);
        System.out.println("time2="+time2);



        Calendar calendar = Calendar.getInstance(); //得到日历
        Date time=sdf.parse("2021-04-19");
        calendar.setTime(time);
        int week=calendar.get(Calendar.DAY_OF_WEEK)-1;
        System.out.println("week="+week);

//////////////////////////////////
        String today21="2021-3-1";
        Date parse = sdf.parse(today21);
        Calendar cal11 = Calendar.getInstance();
        cal11.setTime(parse);
         int first = cal11.getActualMinimum(Calendar.DAY_OF_MONTH);
        cal11.set(Calendar.DAY_OF_MONTH, first);
        System.out.println("123456="+cal11.getTime());
        String format11 = sdf.format(cal11.getTime());
        System.out.println("1234564445="+format11);

        Calendar cal22 = Calendar.getInstance();
        cal22.setTime(parse);
        Integer last = cal22.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal22.set(Calendar.DAY_OF_MONTH, last);
        System.out.println("987456="+cal22.getTime());
        String format22 = sdf.format(cal22.getTime());
        System.out.println("987456666666="+format22);






    }
}
