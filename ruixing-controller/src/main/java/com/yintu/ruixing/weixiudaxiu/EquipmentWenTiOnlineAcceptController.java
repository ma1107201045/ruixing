package com.yintu.ruixing.weixiudaxiu;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2021/4/27 15:49
 * @Version 1.0
 * 需求:问题在线受理和反馈
 */
@RestController
@RequestMapping("/onlineAcceptAll")
public class EquipmentWenTiOnlineAcceptController extends SessionController {
    @Autowired
    private EquipmentWenTiOnlineAcceptService equipmentWenTiOnlineAcceptService;


    //新增问题反馈类型
    @RequestMapping("/addWenTiType")
    public Map<String,Object>addWenTiType(EquipmentWenTiCustomerWenTiTypeEntity equipmentWenTiCustomerWenTiTypeEntity){
        String username = this.getLoginUser().getTrueName();
        equipmentWenTiCustomerWenTiTypeEntity.setCreatename(username);
        equipmentWenTiCustomerWenTiTypeEntity.setCreatetime(new Date());
        equipmentWenTiCustomerWenTiTypeEntity.setUpdatename(username);
        equipmentWenTiCustomerWenTiTypeEntity.setUpdatetime(new Date());
        equipmentWenTiOnlineAcceptService.addWenTiType(equipmentWenTiCustomerWenTiTypeEntity);
        return ResponseDataUtil.ok("新增问题类型成功");
    }

    //根据id  编辑对应的问题类型
    @PutMapping("/editWenTiTypeById/{id}")
    public Map<String,Object>editWenTiTypeById(@PathVariable Integer id,EquipmentWenTiCustomerWenTiTypeEntity equipmentWenTiCustomerWenTiTypeEntity){
        String username = this.getLoginUser().getTrueName();
        equipmentWenTiCustomerWenTiTypeEntity.setUpdatename(username);
        equipmentWenTiCustomerWenTiTypeEntity.setUpdatetime(new Date());
        equipmentWenTiOnlineAcceptService.editWenTiTypeById(equipmentWenTiCustomerWenTiTypeEntity);
        return ResponseDataUtil.ok("编辑问题类型成功");
    }

    //初始化问题类型列表
    @GetMapping("/findAllWenTiType")
    public Map<String,Object>findAllWenTiType(Integer page,Integer size){
        PageHelper.startPage(page,size);
        List<EquipmentWenTiCustomerWenTiTypeEntity>typeEntityList=equipmentWenTiOnlineAcceptService.findAllWenTiType();
        PageInfo<EquipmentWenTiCustomerWenTiTypeEntity>tiTypeEntityPageInfo=new PageInfo<>(typeEntityList);
        return ResponseDataUtil.ok("查询问题类型成功",tiTypeEntityPageInfo);
    }

    //根据id 删除对应的问题类型
    @DeleteMapping("/deleteWenTiTypeByIds/{ids}")
    public Map<String,Object>deleteWenTiTypeByIds(@PathVariable Integer[] ids){
        equipmentWenTiOnlineAcceptService.deleteWenTiTypeByIds(ids);
        return ResponseDataUtil.ok("删除问题类型成功");
    }
///////////////////////////////////////员工查看客户的反馈问题//////////////////////////////////////////////////////////////
    //初始化页面 或者根据条件查询
    @GetMapping("/findAllAcceptFeedback")
    public Map<String,Object>findAllAcceptFeedback(Integer page,Integer size,String number,String statrTime,String endTime,
                                                   String tljName,String dwdName,String xdName,String feedbackName,String wentiType,
                                                   String wentiMiaoshu,Integer feedbackState,Integer acceptUserid,Integer wentiState,
                                                   Integer pushState){
        PageHelper.startPage(page,size);
        List<EquipmentWenTiOnlineAcceptFeedbackEntity>feedbackEntityList=equipmentWenTiOnlineAcceptService.findAllAcceptFeedback(number,statrTime,endTime,
                tljName,dwdName,xdName,feedbackName,wentiType,wentiMiaoshu,feedbackState,acceptUserid,wentiState,pushState);
        PageInfo<EquipmentWenTiOnlineAcceptFeedbackEntity>feedbackEntityPageInfo=new PageInfo<>(feedbackEntityList);
        return ResponseDataUtil.ok("查询成功",feedbackEntityPageInfo);
    }

    //员工根据id  编辑对应的反馈问题
    @PutMapping("/editAcceptFeedbackByIdByUser/{id}")
    public Map<String,Object>editAcceptFeedbackById(@PathVariable Integer id,EquipmentWenTiOnlineAcceptFeedbackEntity equipmentWenTiOnlineAcceptFeedbackEntity,
                                                    String fileName, String filePath){
        String username = this.getLoginUser().getTrueName();
        Integer longinUserid = this.getLoginUser().getId().intValue();
        equipmentWenTiOnlineAcceptFeedbackEntity.setUpdatename(username);
        equipmentWenTiOnlineAcceptFeedbackEntity.setUpdatetime(new Date());
        equipmentWenTiOnlineAcceptService.editAcceptFeedbackByIdByUser(equipmentWenTiOnlineAcceptFeedbackEntity,fileName,filePath,longinUserid);
        return ResponseDataUtil.ok("编辑成功");
    }

    //员工受理
    @PutMapping("/acceptById/{id}")
    public Map<String,Object>acceptById(@PathVariable Integer id,EquipmentWenTiOnlineAcceptFeedbackEntity equipmentWenTiOnlineAcceptFeedbackEntity){
        String longinUsername = this.getLoginUser().getTrueName();
        Integer longinUserid = this.getLoginUser().getId().intValue();
        equipmentWenTiOnlineAcceptFeedbackEntity.setAccepttime(new Date());
        equipmentWenTiOnlineAcceptFeedbackEntity.setAcceptuserid(longinUserid);
        equipmentWenTiOnlineAcceptFeedbackEntity.setAcceptusername(longinUsername);
        equipmentWenTiOnlineAcceptService.acceptById(equipmentWenTiOnlineAcceptFeedbackEntity);
        return ResponseDataUtil.ok("受理成功");
    }

    /////////////////////////////////////评论///////////////////////////////////////
    //员工评论 添加评论
    @PostMapping("/addComment")
    public Map<String, Object> addComment(EquipmentWenTiReturnVisitCommentEntity equipmentWenTiReturnVisitCommentEntity) {
        String longinUsername = this.getLoginUser().getTrueName();
        Integer longinUserid = this.getLoginUser().getId().intValue();
        equipmentWenTiReturnVisitCommentEntity.setCreatename(longinUsername);
        equipmentWenTiReturnVisitCommentEntity.setCreatetime(new Date());
        equipmentWenTiReturnVisitCommentEntity.setUserid(longinUserid);
        equipmentWenTiReturnVisitCommentEntity.setUpdatename(longinUsername);
        equipmentWenTiReturnVisitCommentEntity.setUpdatetime(new Date());
        equipmentWenTiReturnVisitCommentEntity.setUsername(longinUsername);
        equipmentWenTiReturnVisitCommentEntity.setCommenttype(2);
        equipmentWenTiOnlineAcceptService.addComment(equipmentWenTiReturnVisitCommentEntity);
        return ResponseDataUtil.ok("新增评论成功");
    }

    //员工根据反馈fid 查询评论
    @GetMapping("/findCommentByFid/{fid}")
    public Map<String, Object> findCommentByFid(@PathVariable Integer fid) {
        List<EquipmentWenTiReturnVisitCommentEntity> commentEntityList = equipmentWenTiOnlineAcceptService.findCommentByFid(fid);
        return ResponseDataUtil.ok("查询评论成功", commentEntityList);
    }

    ///////////////////////////////////////手机推送消息////////////////////////////////////////////////
    //根据id  查询对应的回访信息
    @GetMapping("/findOneAcceptFeedbackById/{id}")
    public Map<String, Object> findOneAcceptFeedbackById(@PathVariable Integer id) {
        EquipmentWenTiOnlineAcceptFeedbackPushRecordEntity feedbackPushRecordEntity = equipmentWenTiOnlineAcceptService.findOneAcceptFeedbackById(id);
        return ResponseDataUtil.ok("查询成功", feedbackPushRecordEntity);
    }

    //手机推送消息
    @PostMapping("/pushMessage")
    public Map<String, Object> pushMessage(EquipmentWenTiOnlineAcceptFeedbackPushRecordEntity equipmentWenTiOnlineAcceptFeedbackPushRecordEntity) {
        String username = this.getLoginUser().getTrueName();
        equipmentWenTiOnlineAcceptFeedbackPushRecordEntity.setCreatename(username);
        equipmentWenTiOnlineAcceptFeedbackPushRecordEntity.setCreatetime(new Date());
        equipmentWenTiOnlineAcceptFeedbackPushRecordEntity.setUpdatename(username);
        equipmentWenTiOnlineAcceptFeedbackPushRecordEntity.setUpdatetime(new Date());
        equipmentWenTiOnlineAcceptService.pushMessage(equipmentWenTiOnlineAcceptFeedbackPushRecordEntity);
        return ResponseDataUtil.ok("发送消息成功");
    }

    //回访id  查看推送记录
    @GetMapping("/findPushMessageRecordById/{vid}")
    public Map<String, Object> findPushMessageRecordById(@PathVariable Integer fid, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<EquipmentWenTiOnlineAcceptFeedbackPushRecordEntity> pushRecordEntityList = equipmentWenTiOnlineAcceptService.findPushMessageRecordById(fid);
        PageInfo<EquipmentWenTiOnlineAcceptFeedbackPushRecordEntity> pushRecordEntityPageInfo = new PageInfo<>(pushRecordEntityList);
        return ResponseDataUtil.ok("查询推送记录成功", pushRecordEntityPageInfo);
    }

}
