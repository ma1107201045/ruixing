package com.yintu.ruixing.weixiudaxiu;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWenTiPushRecordEntity;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.danganguanli.CustomerEntity;
import com.yintu.ruixing.danganguanli.CustomerService;
import com.yintu.ruixing.master.weixiudaxiu.EquipmentWenTiReturnVisitPushRecordDao;
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
    @PutMapping("/editImplementstateById/{id}")
    public Map<String, Object> editImplementstateById(@PathVariable Integer id, EquipmentWenTiReturnVisitBaseEntity equipmentWenTiReturnVisitBaseEntity) {
        equipmentWenTiReturnVisitBaseService.editImplementstateById(equipmentWenTiReturnVisitBaseEntity);
        return ResponseDataUtil.ok("执行成功");
    }

    //////////////////////////////////////回访任务的首页面//////////////////////////////////////////////////////

    //定时任务 每周一的凌晨创建本周需要完成的回访任务
    @Scheduled(cron = "0 0 02 * * ?")
    public void autoAddWeekReturnVisit() {
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.add(Calendar.DAY_OF_MONTH, -1); //设置为前一天
        Date dBefore = calendar.getTime(); //得到前一天的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        equipmentWenTiReturnVisitBaseService.findRenWuByNowTime(today);
    }

    //定时任务 每月的第一天的凌晨创建本月需要完成的回访任务
    @Scheduled(cron = "0 0 02 * * ?")
    public void autoAddMonthReturnVisit() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        equipmentWenTiReturnVisitBaseService.findRenWuByNowMonth(today);
    }

    //定时任务 每季度的第一个月的第一天的凌晨创建本季度需要完成的回访任务（1月，4月，7月，10月）
    @Scheduled(cron = "0 0 02 * * ?")
    public void autoAddQuarterReturnVisit() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        equipmentWenTiReturnVisitBaseService.findRenWuByNowQuarter(today);
    }

    //定时任务 每年的第一个月的第一天的凌晨创建本年度需要完成的回访任务（1月1日）
    @Scheduled(cron = "0 0 02 * * ?")
    public void autoAddYearReturnVisit() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        equipmentWenTiReturnVisitBaseService.findRenWuByNowYear(today);
    }

    //初始化回访数据  或者根据条件查询
    @GetMapping("/findAllReturnVisit")
    public Map<String, Object> findAllReturnVisit(Integer page, Integer size, Integer returnCycleType, String renWuNumber, String recordNumber,
                                                  String tljName, String dwdName, String xdName, Integer returnUserid, Integer renWuState,
                                                  Integer pushState, String returnWenti, Integer wentiState, String startTime, String endTime,
                                                  Integer typeid ,Integer longinuserid) {//typeid: 1:上，2:本，3:下
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setFirstDayOfWeek(Calendar.MONDAY);
        calendar1.setTime(new Date());
        Integer week = calendar1.get(Calendar.WEEK_OF_YEAR);//本周是第几周
        Calendar calendar = Calendar.getInstance();
        Integer years = calendar.get(Calendar.YEAR);//当前是多少年
        Integer month = calendar.get(Calendar.MONTH) + 1;//当前是几月份
        Integer quarter = 0;//当前是第几季度
        if (1 <= month && month <= 3) {
            quarter = 1;
        }
        if (4 <= month && month <= 6) {
            quarter = 2;
        }
        if (7 <= month && month <= 9) {
            quarter = 3;
        }
        if (10 <= month && month <= 12) {
            quarter = 4;
        }
        Integer oneWeek = 0;//初始化周
        Integer oneMonth = 0;//初始化月
        Integer oneQuarter = 0;//初始化季度
        Integer oneYear = 0;//初始化年
        PageHelper.startPage(page, size);
        if (returnCycleType == 1) {//周任务
            if (typeid == 2) {//本周
                oneWeek = week;
                oneYear = years;
                List<EquipmentWenTiReturnVisitEntity> visitEntityList = equipmentWenTiReturnVisitService.findAllReturnVisit(renWuNumber, recordNumber,
                        tljName, dwdName, xdName, returnUserid, renWuState, pushState, returnWenti, wentiState, startTime, endTime, oneYear.toString(), oneWeek,longinuserid);
                PageInfo<EquipmentWenTiReturnVisitEntity> returnVisitEntityPageInfo = new PageInfo<>(visitEntityList);
                return ResponseDataUtil.ok("查询成功", returnVisitEntityPageInfo);
            }
            if (typeid == 1) {//上周
                if (week == 1) {
                    oneWeek = 52;
                    oneYear = years - 1;
                    List<EquipmentWenTiReturnVisitEntity> visitEntityList = equipmentWenTiReturnVisitService.findAllReturnVisit(renWuNumber, recordNumber,
                            tljName, dwdName, xdName, returnUserid, renWuState, pushState, returnWenti, wentiState, startTime, endTime, oneYear.toString(), oneWeek,longinuserid);
                    PageInfo<EquipmentWenTiReturnVisitEntity> returnVisitEntityPageInfo = new PageInfo<>(visitEntityList);
                    return ResponseDataUtil.ok("查询成功", returnVisitEntityPageInfo);
                } else {
                    oneWeek = week - 1;
                    oneYear = years;
                    List<EquipmentWenTiReturnVisitEntity> visitEntityList = equipmentWenTiReturnVisitService.findAllReturnVisit(renWuNumber, recordNumber,
                            tljName, dwdName, xdName, returnUserid, renWuState, pushState, returnWenti, wentiState, startTime, endTime, oneYear.toString(), oneWeek,longinuserid);
                    PageInfo<EquipmentWenTiReturnVisitEntity> returnVisitEntityPageInfo = new PageInfo<>(visitEntityList);
                    return ResponseDataUtil.ok("查询成功", returnVisitEntityPageInfo);
                }
            }
            if (typeid == 3) {//下周
                if (week == 52) {
                    oneWeek = 1;
                    oneYear = years + 1;
                    List<EquipmentWenTiReturnVisitEntity> visitEntityList = equipmentWenTiReturnVisitService.findAllReturnVisit(renWuNumber, recordNumber,
                            tljName, dwdName, xdName, returnUserid, renWuState, pushState, returnWenti, wentiState, startTime, endTime, oneYear.toString(), oneWeek,longinuserid);
                    PageInfo<EquipmentWenTiReturnVisitEntity> returnVisitEntityPageInfo = new PageInfo<>(visitEntityList);
                    return ResponseDataUtil.ok("查询成功", returnVisitEntityPageInfo);
                } else {
                    oneWeek = week + 1;
                    oneYear = years;
                    List<EquipmentWenTiReturnVisitEntity> visitEntityList = equipmentWenTiReturnVisitService.findAllReturnVisit(renWuNumber, recordNumber,
                            tljName, dwdName, xdName, returnUserid, renWuState, pushState, returnWenti, wentiState, startTime, endTime, oneYear.toString(), oneWeek,longinuserid);
                    PageInfo<EquipmentWenTiReturnVisitEntity> returnVisitEntityPageInfo = new PageInfo<>(visitEntityList);
                    return ResponseDataUtil.ok("查询成功", returnVisitEntityPageInfo);
                }
            }if (typeid == 0) {
                oneWeek =null;
                oneYear = years;
                List<EquipmentWenTiReturnVisitEntity> visitEntityList = equipmentWenTiReturnVisitService.findAllReturnVisit(renWuNumber, recordNumber,
                        tljName, dwdName, xdName, returnUserid, renWuState, pushState, returnWenti, wentiState, startTime, endTime, oneYear.toString(), oneWeek,longinuserid);
                PageInfo<EquipmentWenTiReturnVisitEntity> returnVisitEntityPageInfo = new PageInfo<>(visitEntityList);
                return ResponseDataUtil.ok("查询成功", returnVisitEntityPageInfo);
            }
        }
        if (returnCycleType == 2) {//月任务
            if (typeid == 2) {//本月
                oneMonth = month;
                oneYear = years;
                List<EquipmentWenTiReturnVisitEntity> visitEntityList = equipmentWenTiReturnVisitService.findAllReturnVisit(renWuNumber, recordNumber,
                        tljName, dwdName, xdName, returnUserid, renWuState, pushState, returnWenti, wentiState, startTime, endTime, oneYear.toString(), oneMonth,longinuserid);
                PageInfo<EquipmentWenTiReturnVisitEntity> returnVisitEntityPageInfo = new PageInfo<>(visitEntityList);
                return ResponseDataUtil.ok("查询成功", returnVisitEntityPageInfo);
            }
            if (typeid == 1) {//上月
                if (month == 1) {
                    oneMonth = 12;
                    oneYear = years - 1;
                    List<EquipmentWenTiReturnVisitEntity> visitEntityList = equipmentWenTiReturnVisitService.findAllReturnVisit(renWuNumber, recordNumber,
                            tljName, dwdName, xdName, returnUserid, renWuState, pushState, returnWenti, wentiState, startTime, endTime, oneYear.toString(), oneMonth,longinuserid);
                    PageInfo<EquipmentWenTiReturnVisitEntity> returnVisitEntityPageInfo = new PageInfo<>(visitEntityList);
                    return ResponseDataUtil.ok("查询成功", returnVisitEntityPageInfo);
                } if (typeid == 0) {
                    oneMonth = month - 1;
                    oneYear = years;
                    List<EquipmentWenTiReturnVisitEntity> visitEntityList = equipmentWenTiReturnVisitService.findAllReturnVisit(renWuNumber, recordNumber,
                            tljName, dwdName, xdName, returnUserid, renWuState, pushState, returnWenti, wentiState, startTime, endTime, oneYear.toString(), oneMonth,longinuserid);
                    PageInfo<EquipmentWenTiReturnVisitEntity> returnVisitEntityPageInfo = new PageInfo<>(visitEntityList);
                    return ResponseDataUtil.ok("查询成功", returnVisitEntityPageInfo);
                }
            }
            if (typeid == 3) {//下月
                if (month == 12) {
                    oneMonth = 1;
                    oneYear = years + 1;
                    List<EquipmentWenTiReturnVisitEntity> visitEntityList = equipmentWenTiReturnVisitService.findAllReturnVisit(renWuNumber, recordNumber,
                            tljName, dwdName, xdName, returnUserid, renWuState, pushState, returnWenti, wentiState, startTime, endTime, oneYear.toString(), oneMonth,longinuserid);
                    PageInfo<EquipmentWenTiReturnVisitEntity> returnVisitEntityPageInfo = new PageInfo<>(visitEntityList);
                    return ResponseDataUtil.ok("查询成功", returnVisitEntityPageInfo);
                } else {
                    oneMonth = month + 1;
                    oneYear = years;
                    List<EquipmentWenTiReturnVisitEntity> visitEntityList = equipmentWenTiReturnVisitService.findAllReturnVisit(renWuNumber, recordNumber,
                            tljName, dwdName, xdName, returnUserid, renWuState, pushState, returnWenti, wentiState, startTime, endTime, oneYear.toString(), oneMonth,longinuserid);
                    PageInfo<EquipmentWenTiReturnVisitEntity> returnVisitEntityPageInfo = new PageInfo<>(visitEntityList);
                    return ResponseDataUtil.ok("查询成功", returnVisitEntityPageInfo);
                }
            }if (typeid == 0) {
                oneMonth = null;
                oneYear = years;
                List<EquipmentWenTiReturnVisitEntity> visitEntityList = equipmentWenTiReturnVisitService.findAllReturnVisit(renWuNumber, recordNumber,
                        tljName, dwdName, xdName, returnUserid, renWuState, pushState, returnWenti, wentiState, startTime, endTime, oneYear.toString(), oneMonth,longinuserid);
                PageInfo<EquipmentWenTiReturnVisitEntity> returnVisitEntityPageInfo = new PageInfo<>(visitEntityList);
                return ResponseDataUtil.ok("查询成功", returnVisitEntityPageInfo);
            }
        }
        if (returnCycleType == 3) {//季度任务
            if (typeid == 2) {//本季度
                oneQuarter = quarter;
                oneYear = years;
                List<EquipmentWenTiReturnVisitEntity> visitEntityList = equipmentWenTiReturnVisitService.findAllReturnVisit(renWuNumber, recordNumber,
                        tljName, dwdName, xdName, returnUserid, renWuState, pushState, returnWenti, wentiState, startTime, endTime, oneYear.toString(), oneQuarter,longinuserid);
                PageInfo<EquipmentWenTiReturnVisitEntity> returnVisitEntityPageInfo = new PageInfo<>(visitEntityList);
                return ResponseDataUtil.ok("查询成功", returnVisitEntityPageInfo);
            }
            if (typeid == 1) {//上个季度
                if (quarter == 1) {
                    oneQuarter = 4;
                    oneYear = years - 1;
                    List<EquipmentWenTiReturnVisitEntity> visitEntityList = equipmentWenTiReturnVisitService.findAllReturnVisit(renWuNumber, recordNumber,
                            tljName, dwdName, xdName, returnUserid, renWuState, pushState, returnWenti, wentiState, startTime, endTime, oneYear.toString(), oneQuarter,longinuserid);
                    PageInfo<EquipmentWenTiReturnVisitEntity> returnVisitEntityPageInfo = new PageInfo<>(visitEntityList);
                    return ResponseDataUtil.ok("查询成功", returnVisitEntityPageInfo);
                } else {
                    oneQuarter = quarter - 1;
                    oneYear = years;
                    List<EquipmentWenTiReturnVisitEntity> visitEntityList = equipmentWenTiReturnVisitService.findAllReturnVisit(renWuNumber, recordNumber,
                            tljName, dwdName, xdName, returnUserid, renWuState, pushState, returnWenti, wentiState, startTime, endTime, oneYear.toString(), oneQuarter,longinuserid);
                    PageInfo<EquipmentWenTiReturnVisitEntity> returnVisitEntityPageInfo = new PageInfo<>(visitEntityList);
                    return ResponseDataUtil.ok("查询成功", returnVisitEntityPageInfo);
                }
            }
            if (typeid == 3) {//下个季度
                if (quarter == 4) {
                    oneQuarter = 1;
                    oneYear = years + 1;
                    List<EquipmentWenTiReturnVisitEntity> visitEntityList = equipmentWenTiReturnVisitService.findAllReturnVisit(renWuNumber, recordNumber,
                            tljName, dwdName, xdName, returnUserid, renWuState, pushState, returnWenti, wentiState, startTime, endTime, oneYear.toString(), oneQuarter,longinuserid);
                    PageInfo<EquipmentWenTiReturnVisitEntity> returnVisitEntityPageInfo = new PageInfo<>(visitEntityList);
                    return ResponseDataUtil.ok("查询成功", returnVisitEntityPageInfo);
                } else {
                    oneQuarter = quarter + 1;
                    oneYear = years;
                    List<EquipmentWenTiReturnVisitEntity> visitEntityList = equipmentWenTiReturnVisitService.findAllReturnVisit(renWuNumber, recordNumber,
                            tljName, dwdName, xdName, returnUserid, renWuState, pushState, returnWenti, wentiState, startTime, endTime, oneYear.toString(), oneQuarter,longinuserid);
                    PageInfo<EquipmentWenTiReturnVisitEntity> returnVisitEntityPageInfo = new PageInfo<>(visitEntityList);
                    return ResponseDataUtil.ok("查询成功", returnVisitEntityPageInfo);
                }
            }if (typeid == 0) {
                oneQuarter = null;
                oneYear = years;
                List<EquipmentWenTiReturnVisitEntity> visitEntityList = equipmentWenTiReturnVisitService.findAllReturnVisit(renWuNumber, recordNumber,
                        tljName, dwdName, xdName, returnUserid, renWuState, pushState, returnWenti, wentiState, startTime, endTime, oneYear.toString(), oneQuarter,longinuserid);
                PageInfo<EquipmentWenTiReturnVisitEntity> returnVisitEntityPageInfo = new PageInfo<>(visitEntityList);
                return ResponseDataUtil.ok("查询成功", returnVisitEntityPageInfo);
            }
        }
        if (returnCycleType == 4) {//年任务
            if (typeid == 2) {//本年
                oneYear = years;
                List<EquipmentWenTiReturnVisitEntity> visitEntityList = equipmentWenTiReturnVisitService.findAllReturnVisit(renWuNumber, recordNumber,
                        tljName, dwdName, xdName, returnUserid, renWuState, pushState, returnWenti, wentiState, startTime, endTime, oneYear.toString(), 1,longinuserid);
                PageInfo<EquipmentWenTiReturnVisitEntity> returnVisitEntityPageInfo = new PageInfo<>(visitEntityList);
                return ResponseDataUtil.ok("查询成功", returnVisitEntityPageInfo);
            }
            if (typeid == 1) {//上年
                oneYear = years - 1;
                List<EquipmentWenTiReturnVisitEntity> visitEntityList = equipmentWenTiReturnVisitService.findAllReturnVisit(renWuNumber, recordNumber,
                        tljName, dwdName, xdName, returnUserid, renWuState, pushState, returnWenti, wentiState, startTime, endTime, oneYear.toString(), 1,longinuserid);
                PageInfo<EquipmentWenTiReturnVisitEntity> returnVisitEntityPageInfo = new PageInfo<>(visitEntityList);
                return ResponseDataUtil.ok("查询成功", returnVisitEntityPageInfo);
            }
            if (typeid == 3) {//下年
                oneYear = years + 1;
                List<EquipmentWenTiReturnVisitEntity> visitEntityList = equipmentWenTiReturnVisitService.findAllReturnVisit(renWuNumber, recordNumber,
                        tljName, dwdName, xdName, returnUserid, renWuState, pushState, returnWenti, wentiState, startTime, endTime, oneYear.toString(), 1,longinuserid);
                PageInfo<EquipmentWenTiReturnVisitEntity> returnVisitEntityPageInfo = new PageInfo<>(visitEntityList);
                return ResponseDataUtil.ok("查询成功", returnVisitEntityPageInfo);
            }if (typeid == 0) {
                List<EquipmentWenTiReturnVisitEntity> visitEntityList = equipmentWenTiReturnVisitService.findAllReturnVisit(renWuNumber, recordNumber,
                        tljName, dwdName, xdName, returnUserid, renWuState, pushState, returnWenti, wentiState, startTime, endTime, null, 1,longinuserid);
                PageInfo<EquipmentWenTiReturnVisitEntity> returnVisitEntityPageInfo = new PageInfo<>(visitEntityList);
                return ResponseDataUtil.ok("查询成功", returnVisitEntityPageInfo);
            }
        }
        return ResponseDataUtil.ok("查询成功");
    }


    //根据id 编辑对应的回访任务
    @PutMapping("/editReturnVisitById/{id}")
    public Map<String, Object> editReturnVisitById(@PathVariable Integer id, EquipmentWenTiReturnVisitEntity equipmentWenTiReturnVisitEntity,
                                                   String fristFileName,String firstFilePath,String secondFileName,String secondFilePath) {
        String longinUsername = this.getLoginUser().getTrueName();
        Integer longinUserid = this.getLoginUser().getId().intValue();
        equipmentWenTiReturnVisitEntity.setUpdatename(longinUsername);
        equipmentWenTiReturnVisitEntity.setUpdatetime(new Date());
        equipmentWenTiReturnVisitService.editReturnVisitById(equipmentWenTiReturnVisitEntity,fristFileName,firstFilePath,secondFileName,secondFilePath,longinUserid);
        return ResponseDataUtil.ok("编辑成功");
    }

    //根据id 删除对应的回访任务
    @DeleteMapping("/deleteReturnVisitByIds/{ids}")
    public Map<String,Object>deleteReturnVisitByIds(@PathVariable Integer[] ids){
        equipmentWenTiReturnVisitService.deleteReturnVisitByIds(ids);
        return ResponseDataUtil.ok("删除成功");
    }


///////////////////////////////////回访文件///////////////////////////////////////////////
    //新增文件
    @PostMapping("/addFile")
    public Map<String,Object>addFile(EquipmentWenTiReturnVisitFileEntity equipmentWenTiReturnVisitFileEntity){
        String longinUsername = this.getLoginUser().getTrueName();
        Integer longinUserid = this.getLoginUser().getId().intValue();
        equipmentWenTiReturnVisitFileEntity.setCreatename(longinUsername);
        equipmentWenTiReturnVisitFileEntity.setCreatetime(new Date());
        equipmentWenTiReturnVisitFileEntity.setUid(longinUserid);
        equipmentWenTiReturnVisitFileEntity.setUpdatename(longinUsername);
        equipmentWenTiReturnVisitFileEntity.setUpdatetime(new Date());
        equipmentWenTiReturnVisitService.addFile(equipmentWenTiReturnVisitFileEntity);
        return ResponseDataUtil.ok("新增文件成功");
    }

    //根据类型  查询回访任务文件
    @GetMapping("/findFie")
    public Map<String,Object>findFie(Integer filetype,Integer vid){
        List<EquipmentWenTiReturnVisitFileEntity>fileEntityList=equipmentWenTiReturnVisitService.findFie(filetype,vid);
        return ResponseDataUtil.ok("查询文件成功",fileEntityList);
    }

    //根据id 删除对应的文件
    @DeleteMapping("/deleteFileById/{id}")
    public Map<String,Object>deleteFileById(@PathVariable Integer id){
        equipmentWenTiReturnVisitService.deleteFileById(id);
        return ResponseDataUtil.ok("删除文件成功");
    }


    ///////////////////////////评论///////////////////////////////
    //添加评论
    @PostMapping("/addComment")
    public Map<String,Object>addComment(EquipmentWenTiReturnVisitCommentEntity equipmentWenTiReturnVisitCommentEntity){
        String longinUsername = this.getLoginUser().getTrueName();
        Integer longinUserid = this.getLoginUser().getId().intValue();
        equipmentWenTiReturnVisitCommentEntity.setCreatename(longinUsername);
        equipmentWenTiReturnVisitCommentEntity.setCreatetime(new Date());
        equipmentWenTiReturnVisitCommentEntity.setUserid(longinUserid);
        equipmentWenTiReturnVisitCommentEntity.setUpdatename(longinUsername);
        equipmentWenTiReturnVisitCommentEntity.setUpdatetime(new Date());
        equipmentWenTiReturnVisitCommentEntity.setUsername(longinUsername);
        equipmentWenTiReturnVisitCommentEntity.setCommenttype(1);
        equipmentWenTiReturnVisitService.addComment(equipmentWenTiReturnVisitCommentEntity);
        return ResponseDataUtil.ok("新增评论成功");
    }

    //根据回访id 查询评论
    @GetMapping("/findCommentByVid/{vid}")
    public Map<String,Object>findCommentByVid(@PathVariable Integer vid){
        List<EquipmentWenTiReturnVisitCommentEntity>commentEntityList=equipmentWenTiReturnVisitService.findCommentByVid(vid);
        return ResponseDataUtil.ok("查询评论成功",commentEntityList);
    }

    ///////////////////////////////////////手机推送消息////////////////////////////////////////////////
    //根据id  查询对应的回访信息
    @GetMapping("/findOneReturnVisitById/{id}")
    public Map<String,Object>findOneReturnVisitById(@PathVariable Integer id){
        EquipmentWenTiReturnVisitEntity returnVisitEntity=equipmentWenTiReturnVisitService.findOneReturnVisitById(id);
        return ResponseDataUtil.ok("查询成功",returnVisitEntity);
    }

    //手机推送消息
    @PostMapping("/pushMessage")
    public Map<String,Object>pushMessage(EquipmentWenTiReturnVisitPushRecordEntity equipmentWenTiReturnVisitPushRecordEntity){
        String username = this.getLoginUser().getTrueName();
        equipmentWenTiReturnVisitPushRecordEntity.setCreatename(username);
        equipmentWenTiReturnVisitPushRecordEntity.setCreatetime(new Date());
        equipmentWenTiReturnVisitPushRecordEntity.setUpdatename(username);
        equipmentWenTiReturnVisitPushRecordEntity.setUpdatetime(new Date());
        equipmentWenTiReturnVisitService.pushMessage(equipmentWenTiReturnVisitPushRecordEntity);
        return ResponseDataUtil.ok("发送消息成功");
    }

    //回访id  查看推送记录
    @GetMapping("/findPushMessageRecordById/{vid}")
    public Map<String,Object>findPushMessageRecordById(@PathVariable Integer vid,Integer page,Integer size){
        PageHelper.startPage(page,size);
        List<EquipmentWenTiReturnVisitPushRecordEntity>pushRecordEntityList=equipmentWenTiReturnVisitService.findPushMessageRecordById(vid);
        PageInfo<EquipmentWenTiReturnVisitPushRecordEntity>pushRecordEntityPageInfo=new PageInfo<>(pushRecordEntityList);
        return ResponseDataUtil.ok("查询推送记录成功",pushRecordEntityPageInfo);
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
