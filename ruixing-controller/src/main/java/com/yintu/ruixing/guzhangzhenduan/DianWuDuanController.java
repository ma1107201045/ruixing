package com.yintu.ruixing.guzhangzhenduan;

import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanEntity;
import com.yintu.ruixing.guzhangzhenduan.DianWuDuanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author:lcy
 * @date:2020-05-26 11
 * 电务段相关
 */
@RestController
@RequestMapping("/dianwuduanAll")
public class DianWuDuanController {
    @Autowired
    private DianWuDuanService dianWuDuanService;
    //查询电务段
    @GetMapping("/findDianWuDuanById/{did}")
    public Map<String,Object>findDianWuDuanById(@PathVariable Long did){
        DianWuDuanEntity dianWuDuanEntity=dianWuDuanService.findDianWuDuanById(did);
        return ResponseDataUtil.ok("查询电务段成功",dianWuDuanEntity);
    }

    //新增电务段
    @PostMapping("/addDianWuDuan")
    public Map<String,Object> addDianWuDuan(DianWuDuanEntity dianWuDuanEntity){
        long tid = dianWuDuanEntity.getTljId();
        long did = dianWuDuanEntity.getDwdId();
        List<DianWuDuanEntity> dianWuDuanEntityList=dianWuDuanService.finddwdByIds(tid,did);
        if (dianWuDuanEntityList.size()==0) {
            dianWuDuanService.addDianWuDuan(dianWuDuanEntity);
            return ResponseDataUtil.ok("新增电务段成功");
        }else {
            return ResponseDataUtil.error("不可以添加已有电务段");
        }
    }

    //修改电务段
    @PutMapping("/editDianWuDuan/{did}")
    public Map<String,Object>editDianWuDuan(Long did, DianWuDuanEntity dianWuDuanEntity){
        dianWuDuanService.editDianWuDuan(dianWuDuanEntity);
        return ResponseDataUtil.ok("修改电务段信息成功");
    }
    //删除电务段
    @DeleteMapping("/delDianWuDuan/{did}")
    public Map<String,Object>delDianWuDuan(@PathVariable Long did){
        List<Integer> list=dianWuDuanService.findId(did);
        if (list.size()<=0){
            dianWuDuanService.delDianWuDuan(did);
            return ResponseDataUtil.ok("删除电务段成功");
        }else {
            return ResponseDataUtil.error("删除电务段失败");
        }

    }

}
