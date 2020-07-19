package com.yintu.ruixing.controller;

import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.entity.DianWuDuanEntity;
import com.yintu.ruixing.entity.XianDuanEntity;
import com.yintu.ruixing.service.XianDuanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author:lcy
 * @date:2020-05-26 11
 * 线段相关
 */
@RestController
@RequestMapping("/xianduanAll")
public class XianDuanController {
    @Autowired
    private XianDuanService xianDuanService;

    //新增线段
    @PostMapping("/addXianDuan")
    public Map<String, Object> addXianDuan(XianDuanEntity xianDuanEntity,Long[] dwdids,Long[] dids) {
        xianDuanService.addXianDuan(xianDuanEntity,dwdids,dids);
        return ResponseDataUtil.ok("新增线段成功");
    }

    //删除线段
    @DeleteMapping("/delXianDuan/{xid}")
    public Map<String, Object> delXianDuan(@PathVariable Long xid) {
        List<Integer> list=xianDuanService.findId(xid);
        if (list.size()<=0){
            xianDuanService.delXianDuan(xid);
            return ResponseDataUtil.ok("删除线段成功");
        }else {
            return ResponseDataUtil.error("删除线段失败");
        }

    }
    //根据更改线段
    @PutMapping("/editXianDuan/{xid}")
    public Map<String,Object>editXianDuan(Long xid, XianDuanEntity xianDuanEntity){
        xianDuanService.editXianDuan(xianDuanEntity);
        return ResponseDataUtil.ok("修改线段信息成功");
    }
    //根据id查询线段
    @GetMapping("/findXianDuanById/{xid}")
    public Map<String,Object>findXianDuanById(@PathVariable Long xid){
        XianDuanEntity xianDuanEntity=xianDuanService.findXianDuanById(xid);
        return ResponseDataUtil.ok("查询线段成功",xianDuanEntity);
    }
}
