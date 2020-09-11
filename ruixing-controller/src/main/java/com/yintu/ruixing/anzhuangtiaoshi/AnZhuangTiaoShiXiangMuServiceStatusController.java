package com.yintu.ruixing.anzhuangtiaoshi;

import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/9/11 18:54
 * @Version 1.0
 * 需求:安装调试  服务状态标识
 */
@RestController
@RequestMapping("/ServiceStatusAll")
public class AnZhuangTiaoShiXiangMuServiceStatusController extends SessionController {
    @Autowired
    private AnZhuangTiaoShiXiangMuServiceStatusService anZhuangTiaoShiXiangMuServiceStatusService;

    //新增服务状态标识
    @PostMapping("/addServiceStatus")
    public Map<String,Object>addServiceStatus(AnZhuangTiaoShiXiangMuServiceStatusEntity anZhuangTiaoShiXiangMuServiceStatusEntity){
        String username = this.getLoginUser().getTrueName();
        anZhuangTiaoShiXiangMuServiceStatusService.addServiceStatus(anZhuangTiaoShiXiangMuServiceStatusEntity,username);
        return ResponseDataUtil.ok("新增服务状态标识成功");
    }

    //根据id  编辑服务状态标识
    @PutMapping("/editServiceStatusById/{id}")
    public Map<String,Object>editServiceStatusById(@PathVariable Integer id,AnZhuangTiaoShiXiangMuServiceStatusEntity anZhuangTiaoShiXiangMuServiceStatusEntity){
        String username = this.getLoginUser().getTrueName();
        anZhuangTiaoShiXiangMuServiceStatusService.editServiceStatusById(anZhuangTiaoShiXiangMuServiceStatusEntity,username);
        return ResponseDataUtil.ok("编辑服务状态标识成功");
    }

}
