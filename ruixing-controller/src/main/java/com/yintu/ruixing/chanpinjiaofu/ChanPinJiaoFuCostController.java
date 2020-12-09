package com.yintu.ruixing.chanpinjiaofu;

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
 * @Date 2020/12/9 15:23
 * @Version 1.0
 * 需求:产品交付费用管理
 */

@RestController
@RequestMapping("/ChanPinJiaoFuCostAll")
public class ChanPinJiaoFuCostController extends SessionController {
    @Autowired
    private ChanPinJiaoFuCostService chanPinJiaoFuCostService;

    //查询所有的项目名和编号
    @GetMapping("/findXiangMu")
    public Map<String, Object> findXiangMu() {
        List<ChanPinJiaoFuXiangMuEntity> xiangMuEntities = chanPinJiaoFuCostService.findXiangMu();
        return ResponseDataUtil.ok("查询项目名和编号成功", xiangMuEntities);
    }

    //新增费用
    @PostMapping("/addCost")
    public Map<String, Object> addCost(ChanPinJiaoFuCostEntity chanPinJiaoFuCostEntity) {
        String username = this.getLoginUser().getTrueName();
        chanPinJiaoFuCostService.addCost(chanPinJiaoFuCostEntity,username);
        return ResponseDataUtil.ok("新增费用数据成功");
    }

    //初始化查询页面  或者 根据项目名模糊查询
    @GetMapping("/findCost")
    public Map<String, Object> findCost(String xmName, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<ChanPinJiaoFuEntity> chanPinJiaoFuEntityList = chanPinJiaoFuCostService.findCost(xmName, page, size);
        PageInfo<ChanPinJiaoFuEntity> chanPinJiaoFuEntityPageInfo = new PageInfo<>(chanPinJiaoFuEntityList);
        return ResponseDataUtil.ok("查询费用数据成功", chanPinJiaoFuEntityPageInfo);
    }

    //根据费用id  编辑对应的费用数据
    @PutMapping("/editCostById/{id}")
    public Map<String, Object> editCostById(@PathVariable Integer id, ChanPinJiaoFuCostEntity chanPinJiaoFuCostEntity) {
        String username = this.getLoginUser().getTrueName();
        chanPinJiaoFuCostService.editCostById(chanPinJiaoFuCostEntity,username);
        return ResponseDataUtil.ok("编辑费用数据成功");
    }

    //根据id 批量删除费用数据 或者单个删除
    @DeleteMapping("/deleteByIds/{ids}")
    public Map<String,Object>deleteByIds(@PathVariable Integer[] ids){
        chanPinJiaoFuCostService.deleteByIds(ids);
        return ResponseDataUtil.ok("删除费用数据成功");
    }
}


