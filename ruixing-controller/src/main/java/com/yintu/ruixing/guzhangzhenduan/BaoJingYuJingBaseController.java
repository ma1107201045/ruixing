package com.yintu.ruixing.guzhangzhenduan;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/9/26 16:37
 * @Version 1.0
 * 需求: 报警预警的基础信息表
 */
@RestController
@RequestMapping("/BaoJingYuJingBaseAll")
public class BaoJingYuJingBaseController extends SessionController {
    @Autowired
    private BaoJingYuJingBaseService baoJingYuJingBaseService;

    //新增报警或预警信息
    @PostMapping("/addBaoJing")
    public Map<String, Object> addBaoJing(BaoJingYuJingBaseEntity baoJingYuJingBaseEntity) {
        Integer senderid = this.getLoginUser().getId().intValue();
        String username = this.getLoginUser().getTrueName();
        baoJingYuJingBaseService.addBaoJing(baoJingYuJingBaseEntity, username);
        return ResponseDataUtil.ok("新增报警数据成功");
    }

    //初始化页面
    @GetMapping("/findAllBaoJing")
    public Map<String, Object> findAllBaoJing(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<BaoJingYuJingBaseEntity> baoJingYuJingBaseEntities = baoJingYuJingBaseService.findAllBaoJing(page, size);
        PageInfo<BaoJingYuJingBaseEntity> baseEntityPageInfo = new PageInfo<>(baoJingYuJingBaseEntities);
        return ResponseDataUtil.ok("查询成功", baseEntityPageInfo);
    }

    //根据id  编辑对应的报警预警数据
    @PutMapping("/editBJYJDataByid/{id}")
    public Map<String, Object> editBJYJDataByid(@PathVariable Integer id, BaoJingYuJingBaseEntity baoJingYuJingBaseEntity) {
        String username = this.getLoginUser().getTrueName();
        baoJingYuJingBaseService.editBJYJDataByid(baoJingYuJingBaseEntity, username);
        return ResponseDataUtil.ok("编辑数据成功");
    }

    //根据id  单个或者批量删除数据
    @DeleteMapping("/deleteBJYJdDataByids/{ids}")
    public Map<String,Object>deleteBJYJdDataByids(@PathVariable Integer[] ids){
        baoJingYuJingBaseService.deleteBJYJdDataByids(ids);
        return ResponseDataUtil.ok("删除成功");
    }

    //查询报警预警数据
    @GetMapping("/findBJYJData")
    public Map<String, Object> findBJYJData(Integer page, Integer size, String context, Integer bjyjType) {
        PageHelper.startPage(page, size);
        List<BaoJingYuJingBaseEntity> baoJingYuJingBaseEntities = baoJingYuJingBaseService.findBJYJData(page, size, context, bjyjType);
        PageInfo<BaoJingYuJingBaseEntity> baseEntityPageInfo = new PageInfo<>(baoJingYuJingBaseEntities);
        return ResponseDataUtil.ok("查询成功", baseEntityPageInfo);
    }

    //根据输入内容 查询报警的数据
    @GetMapping("/findBJDataBySomething")
    public Map<String, Object> findBJDataBySomething(Integer page, Integer size, String context) {
        PageHelper.startPage(page, size);
        List<BaoJingYuJingBaseEntity> baoJingYuJingBaseEntities = baoJingYuJingBaseService.findBJDataBySomething(page, size, context);
        PageInfo<BaoJingYuJingBaseEntity> baseEntityPageInfo = new PageInfo<>(baoJingYuJingBaseEntities);
        return ResponseDataUtil.ok("查询成功", baseEntityPageInfo);
    }
}
