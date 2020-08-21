package com.yintu.ruixing.paigongguanli;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.xitongguanli.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/8/18 10:44
 * @Version 1.0
 * 需求: 派工管理  任务
 */
@RestController
@RequestMapping("/TaskAll")
public class PaiGongGuanLiTaskController {
    @Autowired
    private PaiGongGuanLiTaskService paiGongGuanLiTaskService;

    @Autowired
    private UserService userService;
    //查询所有的业务类别
    @GetMapping("/findAllBusinessType")
    public Map<String,Object>findAllBusinessType(){
        List<PaiGongGuanLiBusinessTypeEntity> businessTypeEntityList=paiGongGuanLiTaskService.findAllBusinessType();
        return ResponseDataUtil.ok("查询业务类型成功",businessTypeEntityList);
    }

    //根据业务类别id 查询对应的出差任务
    @GetMapping("/findChuChaiById/{id}")
    public Map<String,Object>findChuChaiById(@PathVariable Integer id){
        List<PaiGongGuanLiBusinessTypeEntity>chuchailist=paiGongGuanLiTaskService.findChuChaiById(id);
        return ResponseDataUtil.ok("查询出差任务成功",chuchailist);
    }
    //新增任务
    @PostMapping("/addTask")
    public Map<String, Object> addTask(PaiGongGuanLiTaskEntity paiGongGuanLiTaskEntity) {
        paiGongGuanLiTaskService.addTask(paiGongGuanLiTaskEntity);
        return ResponseDataUtil.ok("新增任务成功");
    }

    //根据id  编辑对应的任务
    @PutMapping("/editTaskById/{id}")
    public Map<String, Object> editTaskById(@PathVariable Integer id, PaiGongGuanLiTaskEntity paiGongGuanLiTaskEntity) {
        paiGongGuanLiTaskService.editTaskById(paiGongGuanLiTaskEntity);
        return ResponseDataUtil.ok("编辑任务成功");
    }

    //初始化页面   或者根据任务名进行模糊查询
    @GetMapping("/findSomeTask")
    public Map<String, Object> findSomeTask(Integer page, Integer size, String taskname) {
        PageHelper.startPage(page, size);
        List<PaiGongGuanLiTaskEntity> taskEntityList = paiGongGuanLiTaskService.findSomeTask(page, size, taskname);
        PageInfo<PaiGongGuanLiTaskEntity> taskEntityPageInfo = new PageInfo<>(taskEntityList);
        return ResponseDataUtil.ok("查询任务数据成功", taskEntityPageInfo);
    }

    //删除   待定


    /////////////////////人员能力配置////////////////////////////


    //查询所有人员姓名
    @GetMapping("/findAllUserName")
    public Map<String,Object>findAllUserName(String truename){
        List<UserEntity> userEntities=userService.findByTruename(truename);
        return ResponseDataUtil.ok("查询姓名成功",userEntities);
    }

    //新增人员
    @PostMapping("/addUser")
    public Map<String,Object>addUser(Integer[] uid){
        paiGongGuanLiTaskService.addUser(uid);
        return ResponseDataUtil.ok("新增人员成功");
    }

    //初始化人员能力配置列表  或者根据人员名查询
    @GetMapping("/findSomeUserPowerScore")
    public Map<String,Object>findSomeUserPowerScore(Integer page,Integer size,String userName){
        PageHelper.startPage(page,size);
        List<PaiGongGuanLiTaskUserEntity>taskUserEntityList=paiGongGuanLiTaskService.findSomeUserPowerScore(page,size,userName);
        PageInfo<PaiGongGuanLiTaskUserEntity>taskUserEntityPageInfo=new PageInfo<>(taskUserEntityList);
        return ResponseDataUtil.ok("查询人员能力配置数据成功",taskUserEntityPageInfo);
    }

    //根据人员id  查询所有的任务能力数据
    @GetMapping("/findUserPowerScoreById/{id}")
    public Map<String,Object>findUserPowerScoreById(@PathVariable Integer id,Integer page,Integer size){
        PageHelper.startPage(page,size);
        List<PaiGongGuanLiTaskUserEntity>taskUserEntityList=paiGongGuanLiTaskService.findUserPowerScoreById(page,size,id);
        PageInfo<PaiGongGuanLiTaskUserEntity>taskUserEntityPageInfo=new PageInfo<>(taskUserEntityList);
        return ResponseDataUtil.ok("查询人员能力配置数据成功",taskUserEntityPageInfo);
    }

    //根据id  编辑对应的分数
    @PutMapping("/editUserPowerScoreById/{id}")
    public Map<String,Object>editUserPowerScoreById(@PathVariable Integer id,PaiGongGuanLiTaskUserEntity paiGongGuanLiTaskUserEntity){
        paiGongGuanLiTaskService.editUserPowerScoreById(paiGongGuanLiTaskUserEntity);
        return ResponseDataUtil.ok("编辑分数成功");
    }

    //

}
