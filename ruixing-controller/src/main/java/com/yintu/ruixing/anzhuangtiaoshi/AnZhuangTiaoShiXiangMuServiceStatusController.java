package com.yintu.ruixing.anzhuangtiaoshi;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public Map<String, Object> addServiceStatus(AnZhuangTiaoShiXiangMuServiceStatusEntity anZhuangTiaoShiXiangMuServiceStatusEntity) {
        String username = this.getLoginUser().getTrueName();
        anZhuangTiaoShiXiangMuServiceStatusService.addServiceStatus(anZhuangTiaoShiXiangMuServiceStatusEntity, username);
        return ResponseDataUtil.ok("新增服务状态标识成功");
    }

    //根据id  编辑服务状态标识
    @PutMapping("/editServiceStatusById/{id}")
    public Map<String, Object> editServiceStatusById(@PathVariable Integer id, AnZhuangTiaoShiXiangMuServiceStatusEntity anZhuangTiaoShiXiangMuServiceStatusEntity) {
        String username = this.getLoginUser().getTrueName();
        anZhuangTiaoShiXiangMuServiceStatusService.editServiceStatusById(anZhuangTiaoShiXiangMuServiceStatusEntity, username,id);
        return ResponseDataUtil.ok("编辑服务状态标识成功");
    }

    //初始化页面  或者根据服务状态标识名查询
    @GetMapping("/findAllOrSomething")
    public Map<String, Object> findAllOrSomething(Integer page, Integer size, String serviceName) {
        PageHelper.startPage(page, size);
        List<AnZhuangTiaoShiXiangMuServiceStatusEntity> serviceStatusEntityList = anZhuangTiaoShiXiangMuServiceStatusService.findAllOrSomething(page, size, serviceName);
        PageInfo<AnZhuangTiaoShiXiangMuServiceStatusEntity> statusEntityPageInfo = new PageInfo<>(serviceStatusEntityList);
        return ResponseDataUtil.ok("查询成功", statusEntityPageInfo);
    }

    //查询所有的服务状态标识   用于展示在首页或者新增页面
    @GetMapping("/findAllServiceStatus")
    public Map<String, Object> findAllServiceStatus() {
        List<AnZhuangTiaoShiXiangMuServiceStatusEntity> serviceStatusEntityList = anZhuangTiaoShiXiangMuServiceStatusService.findAllServiceStatus();
        return ResponseDataUtil.ok("查询成功", serviceStatusEntityList);
    }

    //根据id  批量或者单个删除服务状态标识
    @DeleteMapping("/deleteServiceStatusByIds/{ids}")
    public Map<String,Object>deleteServiceStatusByIds(@PathVariable Integer[] ids){
        anZhuangTiaoShiXiangMuServiceStatusService.deleteServiceStatusByIds(ids);
        return ResponseDataUtil.ok("删除成功");
    }


}
