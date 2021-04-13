package com.yintu.ruixing.paigongguanli;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.guzhangzhenduan.XianDuanEntity;
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


    //根据当前用户查询对应的未完成报工单
    @GetMapping("/findAllNotOverPaiGong")
    public Map<String,Object>findAllNotOverPaiGong(){
        Integer userid = this.getLoginUser().getId().intValue();
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
   // @PutMapping


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
    public Map<String,Object>findPeopleAddressOnMap(){
        List<PaiGongGuanLiRiQinEntity> riQinEntityList=paiGongGuanLiBaoGongService.findPeopleAddressOnMap();
        return ResponseDataUtil.ok("查询成功",riQinEntityList);
    }
}
