package com.yintu.ruixing.weixiudaxiu;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.danganguanli.CustomerEntity;
import com.yintu.ruixing.danganguanli.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2021/4/21 17:22
 * @Version 1.0
 * 需求:
 */

@RestController
@RequestMapping("returnVisitAll")
public class EquipmentWenTiReturnVisitController extends SessionController {
    @Autowired
    private EquipmentWenTiReturnVisitBaseService equipmentWenTiReturnVisitBaseService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private EquipmentWenTiReturnVisitService equipmentWenTiReturnVisitService;

    //生成任务编号
    @GetMapping("/findRenWuNumber")
    public Map<String, Object> findRenWuNumber(Integer returncycletype) {
        String renWuNumber = equipmentWenTiReturnVisitBaseService.findRenWuNumber(returncycletype);
        return ResponseDataUtil.ok("生成任务编号成功", renWuNumber);
    }

    //查询档案管理列出的人员
    @GetMapping("/findAllCustomer")
    public Map<String, Object> findAllCustomer() {
        List<CustomerEntity> customerEntityList = customerService.findAllCustomer();
        return ResponseDataUtil.ok("查询人员成功", customerEntityList);
    }

    //新增回访任务
    @PostMapping("/addReturnVisitRenWu")
    public Map<String, Object> addReturnVisitRenWu(EquipmentWenTiReturnVisitBaseEntity equipmentWenTiReturnVisitBaseEntity) {
        Integer longinUserid = this.getLoginUser().getId().intValue();
        String longinUsername = this.getLoginUser().getTrueName();
        equipmentWenTiReturnVisitBaseEntity.setAdduserid(longinUserid);
        equipmentWenTiReturnVisitBaseEntity.setCreatename(longinUsername);
        equipmentWenTiReturnVisitBaseEntity.setCreatetime(new Date());
        equipmentWenTiReturnVisitBaseEntity.setUpdatename(longinUsername);
        equipmentWenTiReturnVisitBaseEntity.setUpdatetime(new Date());
        equipmentWenTiReturnVisitBaseService.addReturnVisitRenWu(equipmentWenTiReturnVisitBaseEntity);
        return ResponseDataUtil.ok("新增回访任务成功");
    }

    //查看记录
    @GetMapping("/findRecordById/{id}")
    public Map<String, Object> findRecordById(@PathVariable Integer id) {
        List<EquipmentWenTiReturnVisitRecordmessageEntity> recordmessageEntityList = equipmentWenTiReturnVisitBaseService.findRecordById(id);
        return ResponseDataUtil.ok("查询记录成功", recordmessageEntityList);
    }

    //根据ids  删除对应的回访任务
    @DeleteMapping("/deleteReturnVisitRenWuByIds/{ids}")
    public Map<String, Object> deleteReturnVisitRenWuByIds(@PathVariable Integer[] ids) {
        equipmentWenTiReturnVisitBaseService.deleteReturnVisitRenWuByIds(ids);
        return ResponseDataUtil.ok("删除成功");
    }

    //根据id 编辑对应的回访任务
    @PutMapping("/editReturnVisitRenWuById/{id}")
    public Map<String, Object> editReturnVisitRenWuById(@PathVariable Integer id, EquipmentWenTiReturnVisitBaseEntity equipmentWenTiReturnVisitBaseEntity) {
        String longinUsername = this.getLoginUser().getTrueName();
        equipmentWenTiReturnVisitBaseEntity.setUpdatename(longinUsername);
        equipmentWenTiReturnVisitBaseEntity.setUpdatetime(new Date());
        equipmentWenTiReturnVisitBaseService.editReturnVisitRenWuById(equipmentWenTiReturnVisitBaseEntity);
        return ResponseDataUtil.ok("编辑成功");
    }

    //初始化回访任务  或者根据条件查询回访任务
    @GetMapping("/findAllReturnVisitRenWu")
    public Map<String, Object> findAllReturnVisitRenWu(Integer page, Integer size, String tljName, String dwdName,
                                                       String xdName, Integer returnuserid, Integer returncycletype,
                                                       String statrTime, String endTime, Integer implementstate) {
        PageHelper.startPage(page, size);
        List<EquipmentWenTiReturnVisitBaseEntity> baseEntityList = equipmentWenTiReturnVisitBaseService.findAllReturnVisitRenWu(tljName, dwdName, xdName, returnuserid, returncycletype, statrTime, endTime, implementstate);
        PageInfo<EquipmentWenTiReturnVisitBaseEntity> baseEntityPageInfo = new PageInfo<>(baseEntityList);
        return ResponseDataUtil.ok("查询回访任务成功", baseEntityPageInfo);
    }

    //根据id 是否执行
    @PutMapping("/editImplementstateById{id}")
    public Map<String, Object> editImplementstateById(@PathVariable Integer id, EquipmentWenTiReturnVisitBaseEntity equipmentWenTiReturnVisitBaseEntity) {
        equipmentWenTiReturnVisitBaseService.editImplementstateById(equipmentWenTiReturnVisitBaseEntity);
        return ResponseDataUtil.ok("执行成功");
    }


    //定时任务 每周一的凌晨创建本周需要完成的回访任务
    @Scheduled(cron = "0 0 02 * * ?")
    public void autoAddReturnVisit() {
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.add(Calendar.DAY_OF_MONTH, -1); //设置为前一天
        Date dBefore = calendar.getTime(); //得到前一天的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        equipmentWenTiReturnVisitBaseService.findRenWuByNowTime(today);
    }


    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        int i = calendar.get(Calendar.YEAR);
        Date time = calendar.getTime();
        int i1 = calendar.get(Calendar.MONTH);
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);//获得当前日期属于今年的第几周
        int i2 = calendar.get(3);
        System.out.println(i);
        System.out.println(i1);
        System.out.println(i2);
        System.out.println(time);
        System.out.println(weekOfYear);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String year = "2021-12-25";
        try {
            Date parse = sdf.parse(year);
            Calendar calendar1 = Calendar.getInstance();

            calendar1.setFirstDayOfWeek(Calendar.MONDAY); //美国是以周日为每周的第一天 现把周一设成第一天

            calendar1.setTime(new Date());

            System.out.println("当前周为" + (calendar1.get(Calendar.WEEK_OF_YEAR)));


        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

}
