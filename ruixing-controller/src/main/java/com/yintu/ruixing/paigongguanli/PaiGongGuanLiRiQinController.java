package com.yintu.ruixing.paigongguanli;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.xitongguanli.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/8/24 18:10
 * @Version 1.0
 * 需求:派工管理   日勤
 */
@RestController
@RequestMapping("/RiQinAll")
public class PaiGongGuanLiRiQinController {
    @Autowired
    private PaiGongGuanLiRiQinService paiGongGuanLiRiQinService;

    @Autowired
    private UserService userService;

    //查询员工姓名  等信息
    @GetMapping("/findAllUserName")
    public Map<String, Object> findAllUserName(String truename) {
        List<UserEntity> userEntities = userService.findAllOrByUsername(truename);
        return ResponseDataUtil.ok("查询姓名成功", userEntities);
    }

    //查询所有的日勤管理数据
    @GetMapping("/findAllRiQin")
    public Map<String,Object>findAllRiQin(Integer page,Integer size){
        PageHelper.startPage(page,size);
        List<PaiGongGuanLiRiQinEntity> riQinEntityList=paiGongGuanLiRiQinService.findAllRiQin(page,size);
        PageInfo<PaiGongGuanLiRiQinEntity> riQinEntityPageInfo=new PageInfo<>(riQinEntityList);
        return ResponseDataUtil.ok("查询日勤数据成功",riQinEntityPageInfo);
    }

    //根据名字查询对应的日勤管理数据
    @GetMapping("/findAllRiQinByUserName")
    public Map<String,Object>findAllRiQinByUserName(Integer page,Integer size,String username){
        PageHelper.startPage(page,size);
        List<PaiGongGuanLiRiQinEntity> riQinEntityList=paiGongGuanLiRiQinService.findAllRiQinByUserName(page,size,username);
        PageInfo<PaiGongGuanLiRiQinEntity> riQinEntityPageInfo=new PageInfo<>(riQinEntityList);
        return ResponseDataUtil.ok("查询日勤数据成功",riQinEntityPageInfo);
    }

    //新增日勤
    @PostMapping("/addRiQin")
    public Map<String,Object>addRiQin(PaiGongGuanLiRiQinEntity paiGongGuanLiRiQinEntity){
        paiGongGuanLiRiQinService.addRiQin(paiGongGuanLiRiQinEntity);
        return ResponseDataUtil.ok("新增日勤成功");
    }

    //根据日勤id  编辑对应的数据
    @PutMapping("/editRiQinById/{id}")
    public Map<String,Object>editRiQinById(@PathVariable Integer id,PaiGongGuanLiRiQinEntity paiGongGuanLiRiQinEntity){
        paiGongGuanLiRiQinService.editRiQinById(paiGongGuanLiRiQinEntity);
        return ResponseDataUtil.ok("编辑日勤数据成功");
    }
}
