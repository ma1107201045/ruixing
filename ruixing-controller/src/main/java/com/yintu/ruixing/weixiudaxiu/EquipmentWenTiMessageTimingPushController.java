package com.yintu.ruixing.weixiudaxiu;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Action;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2021/5/7 14:38
 * @Version 1.0
 * 需求:消息定时推送
 */
@RestController
@RequestMapping("/messagePushAll")
public class EquipmentWenTiMessageTimingPushController extends SessionController {
    @Autowired
    private EquipmentWenTiMessageTimingPushService equipmentWenTiMessageTimingPushService;

    //自动生成编号
    @GetMapping("/findNumber")
    public Map<String,Object>findNumber(){
        String number=equipmentWenTiMessageTimingPushService.findNumber();
        return ResponseDataUtil.ok("生成编号成功",number);
    }

    //新增信息推送
    @PostMapping("/addMessagePush")
    public Map<String,Object>addMessagePush(EquipmentWenTiMessageTimingPushEntity equipmentWenTiMessageTimingPushEntity){
        Integer longinUserid = this.getLoginUser().getId().intValue();
        String longinUsername = this.getLoginUser().getTrueName();
        equipmentWenTiMessageTimingPushEntity.setAdduserid(longinUserid);
        equipmentWenTiMessageTimingPushEntity.setCreatename(longinUsername);
        equipmentWenTiMessageTimingPushEntity.setCreatetime(new Date());
        equipmentWenTiMessageTimingPushEntity.setUpdatename(longinUsername);
        equipmentWenTiMessageTimingPushEntity.setUpdatetime(new Date());
        equipmentWenTiMessageTimingPushService.addMessagePush(equipmentWenTiMessageTimingPushEntity);
        return ResponseDataUtil.ok("新增信息推送成功");
    }

    //根据id 编辑对应的推送信息
    @PutMapping("/editById/{id}")
    public Map<String,Object>editById(@PathVariable Integer id,EquipmentWenTiMessageTimingPushEntity equipmentWenTiMessageTimingPushEntity){
        String longinUsername = this.getLoginUser().getTrueName();
        equipmentWenTiMessageTimingPushEntity.setUpdatename(longinUsername);
        equipmentWenTiMessageTimingPushEntity.setUpdatetime(new Date());
        equipmentWenTiMessageTimingPushService.editById(equipmentWenTiMessageTimingPushEntity);
        return ResponseDataUtil.ok("编辑信息推送成功");
    }

    //根据id  删除对应的推送信息
    @DeleteMapping("/deleteByIds/{ids}")
    public Map<String,Object>deleteByIds(@PathVariable Integer[] ids){
        equipmentWenTiMessageTimingPushService.deleteByIds(ids);
        return ResponseDataUtil.ok("删除成功");
    }

    //初始化列表  或者根据条件查询
    @GetMapping("/findAllMessagePush")
    public Map<String,Object>findAllMessagePush(Integer page,Integer size,String tljName,String dwdName,String xdName,
                                                String startTime,String endTime,Integer implementState,Integer pushType){
        PageHelper.startPage(page,size);
        List<EquipmentWenTiMessageTimingPushEntity>pushEntityList=equipmentWenTiMessageTimingPushService.findAllMessagePush(tljName,
                dwdName,xdName,startTime,endTime,implementState,pushType);
        PageInfo<EquipmentWenTiMessageTimingPushEntity>pushEntityPageInfo=new PageInfo<>(pushEntityList);
        return ResponseDataUtil.ok("查询成功",pushEntityPageInfo);
    }

    //查看记录
    @GetMapping("/findRecordByPid/{pid}")
    public Map<String,Object>findRecordByPid(@PathVariable Integer pid){
        List<EquipmentWenTiReturnVisitRecordmessageEntity> recordmessageEntityList = equipmentWenTiMessageTimingPushService.findRecordByPid(pid);
        return ResponseDataUtil.ok("查询记录成功", recordmessageEntityList);
    }

    /////////////////////////////信息推送记录/////////////////////////////////////
    //根据推送信息id 自动生成对应的记录编号
    @GetMapping("/findRecordNumberByPid/{pid}")
    public Map<String,Object>findRecordNumberByPid(@PathVariable Integer pid){
        String recordNumber=equipmentWenTiMessageTimingPushService.findRecordNumberByPid(pid);
        return ResponseDataUtil.ok("记录编号生成",recordNumber);
    }

    //新增信息推送记录
    @PostMapping("/addMessagePushRecord")
    public Map<String,Object>addMessagePushRecord(EquipmentWenTiMessageTimingPushRecordEntity equipmentWenTiMessageTimingPushRecordEntity,
                                                  String filename,String filepath){
        Integer longinUserid = this.getLoginUser().getId().intValue();
        String longinUsername = this.getLoginUser().getTrueName();
        Integer pushtype = equipmentWenTiMessageTimingPushRecordEntity.getPushtype();//推送类型：1：有输入触发，2：无输入触发'
        if (pushtype==1){
            equipmentWenTiMessageTimingPushRecordEntity.setRecorduserid(longinUserid);
            equipmentWenTiMessageTimingPushRecordEntity.setRecordusername(longinUsername);
            equipmentWenTiMessageTimingPushRecordEntity.setRecordstate("未完成");
        }
        equipmentWenTiMessageTimingPushRecordEntity.setIsnotsuccess(0);
        equipmentWenTiMessageTimingPushRecordEntity.setCreatename(longinUsername);
        equipmentWenTiMessageTimingPushRecordEntity.setCreatetime(new Date());
        equipmentWenTiMessageTimingPushRecordEntity.setUpdatename(longinUsername);
        equipmentWenTiMessageTimingPushRecordEntity.setUpdatetime(new Date());
        equipmentWenTiMessageTimingPushService.addMessagePushRecord(equipmentWenTiMessageTimingPushRecordEntity,filename,filepath,longinUserid);
        return ResponseDataUtil.ok("新增成功");
    }

    //根据id 编辑对应的信息推送记录
    @PutMapping("/editMessagePushRecordById/{id}")
    public Map<String,Object>editMessagePushRecordById(@PathVariable Integer id,EquipmentWenTiMessageTimingPushRecordEntity equipmentWenTiMessageTimingPushRecordEntity,
                                                       String filename,String filepath){
        String longinUsername = this.getLoginUser().getTrueName();
        Integer longinUserid = this.getLoginUser().getId().intValue();
        equipmentWenTiMessageTimingPushRecordEntity.setUpdatename(longinUsername);
        equipmentWenTiMessageTimingPushRecordEntity.setUpdatetime(new Date());
        equipmentWenTiMessageTimingPushService.editMessagePushRecordById(equipmentWenTiMessageTimingPushRecordEntity,filename,filepath,longinUserid);
        return ResponseDataUtil.ok("编辑成功");
    }

    //根据id 删除对应的记录
    @DeleteMapping("/deleteMessagePushRecordByIds/{ids}")
    public Map<String,Object>deleteMessagePushRecordByIds(@PathVariable Integer[] ids){
        equipmentWenTiMessageTimingPushService.deleteMessagePushRecordByIds(ids);
        return ResponseDataUtil.ok("删除成功");
    }

    //根据出发类型 初始化对应的记录
    @GetMapping("/findAllMessagePushRecord")
    public Map<String,Object>findAllMessagePushRecord(Integer page,Integer size,Integer pushtype){
        PageHelper.startPage(page,size);
        List<EquipmentWenTiMessageTimingPushRecordEntity>pushRecordEntityList=equipmentWenTiMessageTimingPushService.findAllMessagePushRecord(pushtype);
        PageInfo<EquipmentWenTiMessageTimingPushRecordEntity>pushRecordEntityPageInfo=new PageInfo<>(pushRecordEntityList);
        return ResponseDataUtil.ok("查询成功",pushRecordEntityPageInfo);
    }

    //推送





}
