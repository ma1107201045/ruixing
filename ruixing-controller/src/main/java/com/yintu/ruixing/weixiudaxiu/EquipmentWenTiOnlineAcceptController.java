package com.yintu.ruixing.weixiudaxiu;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.XianDuanEntity;
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


    //查询所有的问题类型
    @GetMapping("/findWenTiType")
    public Map<String,Object>findWenTiType(){
        List<EquipmentWenTiCustomerWenTiTypeEntity>typeEntityList=equipmentWenTiOnlineAcceptService.findAllWenTiType();
        return ResponseDataUtil.ok("查询成功",typeEntityList);
    }
///////////////////////////客户新增反馈问题/////////////////////////////////////////

    //查询所有的电务段
    @GetMapping("/findAllDianWuDuan")
    public Map<String,Object>findAllDianWuDuan(){
        List<DianWuDuanEntity>dianWuDuanEntityList=equipmentWenTiOnlineAcceptService.findAllDianWuDuan();
        return ResponseDataUtil.ok("查询电务段成功",dianWuDuanEntityList);
    }

    //查询所有的线段
    @GetMapping("/findAllXianDuan")
    public Map<String,Object>findAllXianDuan(){
        List<XianDuanEntity> xianDuanEntityList=equipmentWenTiOnlineAcceptService.findAllXianDuan();
        return ResponseDataUtil.ok("查询线段成功",xianDuanEntityList);
    }

    //新增 客户新增反馈问题
    @PostMapping("/addAcceptFeedback")
    public Map<String,Object>addAcceptFeedback(EquipmentWenTiOnlineAcceptFeedbackEntity equipmentWenTiOnlineAcceptFeedbackEntity){
        String feedbackusername = equipmentWenTiOnlineAcceptFeedbackEntity.getFeedbackusername();
        equipmentWenTiOnlineAcceptFeedbackEntity.setCreatename(feedbackusername);
        equipmentWenTiOnlineAcceptFeedbackEntity.setCreatetime(new Date());
        equipmentWenTiOnlineAcceptFeedbackEntity.setUpdatename(feedbackusername);
        equipmentWenTiOnlineAcceptFeedbackEntity.setUpdatetime(new Date());
        equipmentWenTiOnlineAcceptService.addAcceptFeedback(equipmentWenTiOnlineAcceptFeedbackEntity);
        return ResponseDataUtil.ok("新增成功");
    }

    //




}
