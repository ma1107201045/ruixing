package com.yintu.ruixing.controller;

import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.entity.TieLuJuEntity;
import com.yintu.ruixing.service.TieLuJuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

        tieLuJuService.addTieLuJU(tieLuJuEntity);
        return ResponseDataUtil.ok("添加铁路局成功");
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
        tieLuJuService.delTieLuJu(tid);
        return ResponseDataUtil.ok("删除铁路局成功");
    }

}
