package com.yintu.ruixing.paigongguanli;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuEntity;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/10/10 10:53
 * @Version 1.0
 * 需求:费用管理
 */
@RestController
@RequestMapping("/costListAll")
public class PaiGongGuanLiCostListController extends SessionController {
    @Autowired
    private PaiGongGuanLiCostListService paiGongGuanLiCostListService;

    //查询项目编号和项目名称
    @GetMapping("/findXmNumberAndName")
    public Map<String, Object> findXmNumberAndName() {
        List<ChanPinJiaoFuXiangMuEntity> xiangMuEntityList = paiGongGuanLiCostListService.findXmNumberAndName();
        return ResponseDataUtil.ok("查询成功", xiangMuEntityList);
    }

    //新增费用
    @PostMapping("/addCostList")
    public Map<String, Object> addCostList(@RequestBody JSONArray costlist) {

        Date today = new Date();
        String username = this.getLoginUser().getTrueName();
        Integer senderid = this.getLoginUser().getId().intValue();
        for (Object costlistdatas : costlist) {
            Map<String, Object> costdats = (Map) costlistdatas;
            Map<String, Object> label = (Map) costdats.get("addForm");
            String xiangmuNumber = (String) label.get("codes");
            String xiangmuName = (String) label.get("name");
            String paigongNumber = (String) label.get("dispatch");

            PaiGongGuanLiCostListEntity costListEntity = new PaiGongGuanLiCostListEntity();
            costListEntity.setPaigongnumber(paigongNumber);
            costListEntity.setXiangmuname(xiangmuName);
            costListEntity.setXiangmunumber(xiangmuNumber);
            costListEntity.setCreatename(username);
            costListEntity.setCreatetime(today);
            costListEntity.setUserid(senderid);
            paiGongGuanLiCostListService.addCostList(costListEntity);
            Integer cid = costListEntity.getId();

            List statusdata = (List) label.get("content");
            for (Object statusdatum : statusdata) {
                Map<String, Object> statusdatu = (Map) statusdatum;
                Map<String, Object> list = (Map) statusdatu.get("list");
                String name = (String) list.get("name");
                String money = (String) list.get("money");
                BigDecimal cost = new BigDecimal(money);
                PaiGongGuanLiCostEntity costEntity = new PaiGongGuanLiCostEntity();
                costEntity.setCid(cid);
                costEntity.setName(name);
                costEntity.setCost(cost);
                paiGongGuanLiCostListService.addCost(costEntity);

            }
        }
        return ResponseDataUtil.ok("新增成功");
    }

    //初始化页面  或者根据项目编号模糊查询
    @GetMapping("/findAllCostList")
    public Map<String, Object> findAllCostList(Integer page, Integer size, String xmNumber) {
        PageHelper.startPage(page, size);
        List<PaiGongGuanLiCostListEntity> costListEntityList = paiGongGuanLiCostListService.findAllCostList(page, size, xmNumber);
        PageInfo<PaiGongGuanLiCostListEntity> costListEntityPageInfo = new PageInfo<>(costListEntityList);
        return ResponseDataUtil.ok("查询成功", costListEntityPageInfo);
    }

    //根据id  编辑对应的数据
    @PutMapping("/deitCostListById/{id}")
    public Map<String, Object> deitCostListById(@PathVariable Integer id, PaiGongGuanLiCostListEntity paiGongGuanLiCostListEntity) {
        String username = this.getLoginUser().getTrueName();
        Integer senderid = this.getLoginUser().getId().intValue();
        paiGongGuanLiCostListService.deitCostListById(paiGongGuanLiCostListEntity, username);
        return ResponseDataUtil.ok("编辑成功");
    }

    //根据id 删除单个  或者批量删除
    @DeleteMapping("/deleteCostListByIds/{ids}")
    public Map<String, Object> deleteCostListByIds(@PathVariable Integer[] ids) {
        paiGongGuanLiCostListService.deleteCostListByIds(ids);
        return ResponseDataUtil.ok("删除成功");
    }

    //根据id 查询费用列表
    @GetMapping("/findAllCostByCid/{id}")
    public Map<String, Object> findAllCostByCid(@PathVariable Integer id) {
        List<PaiGongGuanLiCostEntity> costEntityList = paiGongGuanLiCostListService.findAllCostByCid(id);
        return ResponseDataUtil.ok("查询成功", costEntityList);
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    //根据id 编辑费用数据
    @PutMapping("/editCostById/{id}")
    public Map<String, Object> editCostById(@PathVariable Integer id, PaiGongGuanLiCostEntity paiGongGuanLiCostEntity) {
        paiGongGuanLiCostListService.editCostById(paiGongGuanLiCostEntity);
        return ResponseDataUtil.ok("编辑成功");
    }

    //根据id  单个删除或者批量删除费用数据
    @DeleteMapping("/deleteCostByIds/{ids}")
    public Map<String, Object> deleteCostByIds(@PathVariable Integer[] ids) {
        paiGongGuanLiCostListService.deleteCostByIds(ids);
        return ResponseDataUtil.ok("删除成功");
    }

    //新增费用数据
    @PostMapping("/addCost")
    public Map<String, Object> addCost(@RequestBody JSONObject cost) {

        Map<String, Object> costdats = (Map) cost;
        List label = (List) costdats.get("contentTwo");
        Integer cid = (Integer) costdats.get("cid");
        for (int i = 0; i < label.size(); i++) {
            Map<String, Object> list = (Map) label.get(i);
            Map<String, Object> list1 = (Map) list.get("list");
            String name = (String) list1.get("name");
            String money = (String) list1.get("money");
            BigDecimal costt = new BigDecimal(money);
            PaiGongGuanLiCostEntity costEntity = new PaiGongGuanLiCostEntity();
            costEntity.setCid(cid);
            costEntity.setName(name);
            costEntity.setCost(costt);
            paiGongGuanLiCostListService.addCost(costEntity);
        }

        return ResponseDataUtil.ok("新增成功");
    }


    //根据人员id  查询对应的相关数据
    @GetMapping("/findDatasByUid")
    public Map<String,Object>findDatasByUid(Integer page,Integer size,Integer uid){
        PageHelper.startPage(page,size);
        List<PaiGongGuanLiCostListEntity> costListEntityList=paiGongGuanLiCostListService.findDatasByUid(page,size,uid);
        PageInfo<PaiGongGuanLiCostListEntity> costListEntityPageInfo=new PageInfo<>(costListEntityList);
        return ResponseDataUtil.ok("查询成功",costListEntityPageInfo);
    }

    //统计总的费用和
    @GetMapping("/findAllCost")
    public Map<String,Object>findAllCost(){
        BigDecimal costAll=paiGongGuanLiCostListService.findAllCost();
        return ResponseDataUtil.ok("查询成功",costAll);
    }

    //根据项目名  查询对应的数据
    @GetMapping("/findDatasByXMname")
    public Map<String,Object>findDatasByXMname(Integer page,Integer size,String xmName){
        PageHelper.startPage(page,size);
        List<PaiGongGuanLiCostListEntity> costListEntityList=paiGongGuanLiCostListService.findDatasByXMname(page,size,xmName);
        PageInfo<PaiGongGuanLiCostListEntity> costListEntityPageInfo=new PageInfo<>(costListEntityList);
        return ResponseDataUtil.ok("查询成功",costListEntityPageInfo);
    }

    //根据业务类别  查询对应的数据
    @GetMapping("/findDatasByYWtype")
    public Map<String,Object>findDatasByYWtype(Integer page,Integer size,String ywType){
        PageHelper.startPage(page,size);
        List<PaiGongGuanLiCostListEntity> costListEntityList=paiGongGuanLiCostListService.findDatasByYWtype(page,size,ywType);
        PageInfo<PaiGongGuanLiCostListEntity> costListEntityPageInfo=new PageInfo<>(costListEntityList);
        return ResponseDataUtil.ok("查询成功",costListEntityPageInfo);
    }


}
