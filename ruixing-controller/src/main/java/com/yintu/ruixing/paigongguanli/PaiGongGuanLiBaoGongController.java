package com.yintu.ruixing.paigongguanli;

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
 * @Date 2020/10/9 11:06
 * @Version 1.0
 * 需求:报工
 */
@RestController
@RequestMapping("/baoGongAll")
public class PaiGongGuanLiBaoGongController extends SessionController {

    @Autowired
    private PaiGongGuanLiBaoGongService paiGongGuanLiBaoGongService;



    //新增报工
    @PostMapping("/addBaoGong")
    public Map<String,Object>addBaoGong(PaiGongGuanLiBaoGongEntity paiGongGuanLiBaoGongEntity){
        String username = this.getLoginUser().getTrueName();
        Integer senderid = this.getLoginUser().getId().intValue();
        paiGongGuanLiBaoGongService.addBaoGong(paiGongGuanLiBaoGongEntity,username,senderid);
        return ResponseDataUtil.ok("新增成功");
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

    //////////////////////首页展示/////////////////////////

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
    public Map<String,Object>findPeopleAddressOnMap(Integer page,Integer size){
        PageHelper.startPage(page,size);
        List<PaiGongGuanLiRiQinEntity> riQinEntityList=paiGongGuanLiBaoGongService.findPeopleAddressOnMap(page,size);
        PageInfo<PaiGongGuanLiRiQinEntity> riQinEntityPageInfo=new PageInfo<>(riQinEntityList);
        return ResponseDataUtil.ok("查询成功",riQinEntityPageInfo);
    }
}
