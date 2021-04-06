package com.yintu.ruixing.guzhangzhenduan;

import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuEntity;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author:lcy
 * @date:2020-05-26 10
 */
@RestController
@RequestMapping("/tielujuAll")
public class TieLuJuController {
    @Autowired
    private TieLuJuService tieLuJuService;
    //查询铁路局
    @GetMapping("/findTieLuJuById/{tid}")
    public Map<String,Object> findTieLuJuById(@PathVariable Long tid){
        TieLuJuEntity tieLuJuEntity=tieLuJuService.findTieLuJuById(tid);
        return ResponseDataUtil.ok("查询铁路局成功",tieLuJuEntity);
    }
    //新增铁路局
    @PostMapping("/addTieLuJU")
    public Map<String,Object> addTieLuJU(TieLuJuEntity tieLuJuEntity){
        long tljId = tieLuJuEntity.getTljId();
        List<TieLuJuEntity> tieLuJuEntityList=tieLuJuService.findTLJlist(tljId);
        if (tieLuJuEntityList.size()==0) {
            tieLuJuService.addTieLuJU(tieLuJuEntity);
            return ResponseDataUtil.ok("添加铁路局成功");
        }else {
            return ResponseDataUtil.error("不可以添加已有铁路局");
        }
    }
    //修改铁路局信息
    @PutMapping("/editTieLuJu/{tid}")
    public Map<String,Object> editTieLuJu(@PathVariable Long tid ,TieLuJuEntity tieLuJuEntity){
        tieLuJuService.editTieLuJuById(tieLuJuEntity);
        return ResponseDataUtil.ok("修改铁路局信息成功");
    }
    //删除铁路局
    @DeleteMapping("/delTieLuJu/{tid}")
    public Map<String,Object> delTieLuJu(@PathVariable Long tid){
        List<Integer> list=tieLuJuService.findId(tid);
        if (list.size()<=0){
            tieLuJuService.delTieLuJu(tid);
            return ResponseDataUtil.ok("删除铁路局成功");
        }else {
            return ResponseDataUtil.error("删除铁路局失败");
        }

    }

}
