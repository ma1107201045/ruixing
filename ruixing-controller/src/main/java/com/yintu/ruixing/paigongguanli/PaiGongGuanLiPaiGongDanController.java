package com.yintu.ruixing.paigongguanli;

import cn.hutool.core.date.DateUtil;
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
 * @Date 2020/8/22 19:31
 * @Version 1.0
 * 需求:  派工单
 */
@RestController
@RequestMapping("/PaiGongDanAll")
public class PaiGongGuanLiPaiGongDanController extends SessionController {
    @Autowired
    private PaiGongGuanLiPaiGongDanService paiGongGuanLiPaiGongDanService;

    //自动创建派工单编号
    @GetMapping("/findPaiGongDanNum")
    public Map<String, Object> findPaiGongDanNum(String suoxie) {
        Date today = new Date();
        int month = DateUtil.month(today) + 1;
        String monthStr = Integer.toString(month).length() == 1 ? "0" + month : Integer.toString(month);
        int year = DateUtil.year(today);
        String paigongdannum = paiGongGuanLiPaiGongDanService.findPaiGongDanNum(suoxie);
        if (suoxie==null){
            return ResponseDataUtil.ok(" ");
        }
        if (paigongdannum == null) {
            String paiGongDanNum = suoxie + year + monthStr + "0001";
            return ResponseDataUtil.ok("自动生成派工单号成功", paiGongDanNum);
        } else {
            String substring = paigongdannum.substring(8, 12);
            Integer i = Integer.parseInt(substring) + 1;
            String paiGongDanNum = suoxie + year + monthStr + i.toString();
            return ResponseDataUtil.ok("自动生成派工单号成功", paiGongDanNum);
        }
    }


    //新增派工单
    @PostMapping("/addPaiGongDan")
    public Map<String, Object> addPaiGongDan(PaiGongGuanLiPaiGongDanEntity paiGongGuanLiPaiGongDanEntity) {
        String username = this.getLoginUser().getTrueName();
        Integer senderid = this.getLoginUser().getId().intValue();
        paiGongGuanLiPaiGongDanService.addPaiGongDan(paiGongGuanLiPaiGongDanEntity, username, senderid);
        return ResponseDataUtil.ok("新增派工单成功");
    }

    //初始化页面  或者根据编号模糊查询
    @GetMapping("/findPaiGongDan")
    public Map<String,Object>findPaiGongDan(Integer page,Integer size,String paiGongNumber){
        PageHelper.startPage(page,size);
        List<PaiGongGuanLiPaiGongDanEntity> paiGongDanEntityList = paiGongGuanLiPaiGongDanService.findPaiGongDan(page, size, paiGongNumber);
        PageInfo<PaiGongGuanLiPaiGongDanEntity> paiGongDanEntityPageInfo=new PageInfo<>(paiGongDanEntityList);
        return ResponseDataUtil.ok("查询成功",paiGongDanEntityPageInfo);
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
        paiGongGuanLiPaiGongDanService.editPaiGongDanById(id,senderid, uid,  paiGongGuanLiPaiGongDanEntity, username);
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
    public Map<String,Object>findAllBuiness(){
        List<PaiGongGuanLiBusinessTypeEntity> businessTypeEntityList=paiGongGuanLiPaiGongDanService.findAllBuiness();
        return ResponseDataUtil.ok("查询成功",businessTypeEntityList);
    }

    //根据业务类型  查询对应的出差任务
    @GetMapping("/findBuinessById/{id}")
    public Map<String,Object>findBuinessById(@PathVariable Integer id){
        List<PaiGongGuanLiBusinessTypeEntity> businessTypeEntityList=paiGongGuanLiPaiGongDanService.findBuinessById(id);
        return ResponseDataUtil.ok("查询成功",businessTypeEntityList);
    }

    //查询所有的派工数据
    @GetMapping("/findAllPaiGongDan")
    public Map<String,Object>findAllPaiGongDan(){
        List<PaiGongGuanLiPaiGongDanEntity>paiGongDanEntityList=paiGongGuanLiPaiGongDanService.findAllPaiGongDan();
        return ResponseDataUtil.ok("查询成功",paiGongDanEntityList);
    }









    public static void main(String[] args) {
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
    }
}
