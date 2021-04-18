package com.yintu.ruixing.paigongguanli;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.xitongguanli.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
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
public class PaiGongGuanLiTaskController extends SessionController{
    @Autowired
    private PaiGongGuanLiTaskService paiGongGuanLiTaskService;
    @Autowired
    private PaiGongGuanLiUserService paiGongGuanLiUserService;
    @Autowired
    private UserService userService;





    //查询所有的业务类别
    @GetMapping("/findAllBusinessType")
    public Map<String, Object> findAllBusinessType() {
        List<PaiGongGuanLiBusinessTypeEntity> businessTypeEntityList = paiGongGuanLiTaskService.findAllBusinessType();
        return ResponseDataUtil.ok("查询业务类型成功", businessTypeEntityList);
    }

    //根据业务类别id 查询对应的出差任务
    @GetMapping("/findChuChaiById/{id}")
    public Map<String, Object> findChuChaiById(@PathVariable Integer id) {
        List<PaiGongGuanLiBusinessTypeEntity> chuchailist = paiGongGuanLiTaskService.findChuChaiById(id);
        return ResponseDataUtil.ok("查询出差任务成功", chuchailist);
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

    //根据id 批量或者单个删除任务
    @DeleteMapping("/deleteTaskByIds/{ids}")
    public Map<String, Object> deleteTaskByIds(@PathVariable Integer[] ids) {
        paiGongGuanLiTaskService.deleteTaskByIds(ids);
        return ResponseDataUtil.ok("删除任务成功");
    }


    /////////////////////人员能力配置////////////////////////////


    //选择派工人员配置
    @PostMapping("/addPGGLuser")
    public Map<String,Object>addPGGLuser(PaiGongGuanLiUserEntity paiGongGuanLiUserEntity){
        String username = this.getLoginUser().getTrueName();
        paiGongGuanLiUserEntity.setCreateBy(username);
        paiGongGuanLiUserEntity.setCreateTime(new Date());
        paiGongGuanLiUserEntity.setModifiedBy(username);
        paiGongGuanLiUserEntity.setModifiedTime(new Date());
        paiGongGuanLiUserEntity.setIsdelete(1);
        paiGongGuanLiUserService.addPGGLuser(paiGongGuanLiUserEntity,username);
        return ResponseDataUtil.ok("添加派工人员成功");
    }

    //删除派工人员配置
    @PutMapping("/deleteById/{id}")
    public Map<String,Object>deleteById(@PathVariable Integer id,PaiGongGuanLiUserEntity paiGongGuanLiUserEntity){
        String username = this.getLoginUser().getTrueName();
        paiGongGuanLiUserEntity.setModifiedBy(username);
        paiGongGuanLiUserEntity.setModifiedTime(new Date());
        paiGongGuanLiUserEntity.setIsdelete(0);
        paiGongGuanLiUserService.deleteById(paiGongGuanLiUserEntity);
        return ResponseDataUtil.ok("删除人员成功");
    }

    //查询所有的派工人员
    @GetMapping("/findAllUser")
    public Map<String,Object>findAllUser(String name,Integer page,Integer size){
        PageHelper.startPage(page,size);
        List<PaiGongGuanLiUserEntity>paiGongGuanLiUserEntityList=paiGongGuanLiUserService.findAllUser(name);
        PageInfo<PaiGongGuanLiUserEntity>userEntityPageInfo=new PageInfo<>(paiGongGuanLiUserEntityList);
        return ResponseDataUtil.ok("查询人员成功",userEntityPageInfo);
    }

    //定时添加配置人员的每年状态（每年的12月1号）
    @Scheduled(cron = "0 0 02 * * ?")
    @ResponseBody
    @GetMapping("/autoAddPeopleState")
    public void autoAddPeopleState(){
        paiGongGuanLiUserService.autoAddPeopleState();
    }


    //查询所有人员姓名
    @GetMapping("/findAllUserName")
    public Map<String, Object> findAllUserName(String truename) {
        List<UserEntity> userEntities = userService.findByTruename(truename);
        return ResponseDataUtil.ok("查询姓名成功", userEntities);
    }

    //新增人员
    @PostMapping("/addUser")
    public Map<String, Object> addUser(Integer[] uid) {
        paiGongGuanLiTaskService.addUser(uid);
        return ResponseDataUtil.ok("新增人员成功");
    }

    //初始化人员能力配置列表  或者根据人员名查询
    @GetMapping("/findSomeUserPowerScore")
    public Map<String, Object> findSomeUserPowerScore(Integer page, Integer size, String userName) {
        PageHelper.startPage(page, size);
        List<PaiGongGuanLiTaskUserEntity> taskUserEntityList = paiGongGuanLiTaskService.findSomeUserPowerScore(page, size, userName);
        PageInfo<PaiGongGuanLiTaskUserEntity> taskUserEntityPageInfo = new PageInfo<>(taskUserEntityList);
        return ResponseDataUtil.ok("查询人员能力配置数据成功", taskUserEntityPageInfo);
    }

    //根据人员id  查询所有的任务能力数据
    @GetMapping("/findUserPowerScoreById/{id}")
    public Map<String, Object> findUserPowerScoreById(@PathVariable Integer id, String taskTotalName) {
        //PageHelper.startPage(page, size);
        List<PaiGongGuanLiTaskUserEntity> taskUserEntityList = paiGongGuanLiTaskService.findUserPowerScoreById(id, taskTotalName);
        //PageInfo<PaiGongGuanLiTaskUserEntity> taskUserEntityPageInfo = new PageInfo<>(taskUserEntityList);
        return ResponseDataUtil.ok("查询人员能力配置数据成功", taskUserEntityList);
    }

    //根据id  编辑对应的分数
    @PutMapping("/editUserPowerScoreById/{id}")
    public Map<String, Object> editUserPowerScoreById(@PathVariable Integer id, PaiGongGuanLiTaskUserEntity paiGongGuanLiTaskUserEntity) {
        paiGongGuanLiTaskService.editUserPowerScoreById(paiGongGuanLiTaskUserEntity);
        return ResponseDataUtil.ok("编辑分数成功");
    }

    //新增任务
    @PostMapping("/addTaskScore")
    public Map<String, Object> addTaskScore(PaiGongGuanLiTaskUserEntity paiGongGuanLiTaskUserEntity) {
        paiGongGuanLiTaskService.addTaskScore(paiGongGuanLiTaskUserEntity);
        return ResponseDataUtil.ok("新增成功");
    }


    //查询所有任务
    @GetMapping("/findAllTasks")
    public Map<String, Object> findAllTasks() {
        List<PaiGongGuanLiTaskEntity> taskEntityList = paiGongGuanLiTaskService.findAllTasks();
        return ResponseDataUtil.ok("查询成功", taskEntityList);
    }

    //根据任务id  单个或者批量删除
    @DeleteMapping("/deleteUserTaskByIds/{ids}")
    public Map<String, Object> deleteUserTaskByIds(@PathVariable Integer[] ids) {
        paiGongGuanLiTaskService.deleteUserTaskByIds(ids);
        return ResponseDataUtil.ok("删除成功");
    }

    //查询注册的所有人员
    @GetMapping("/users")
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "id DESC") String orderBy,
                                       @RequestParam(value = "search_text", required = false) String username) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<UserEntity> userEntities = userService.findAllOrByUsername(username);
        PageInfo<UserEntity> pageInfo = new PageInfo<>(userEntities);
        return ResponseDataUtil.ok("查询用户列表成功", pageInfo);
    }

}
