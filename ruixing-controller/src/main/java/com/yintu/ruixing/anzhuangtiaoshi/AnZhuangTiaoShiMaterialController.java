package com.yintu.ruixing.anzhuangtiaoshi;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiMaterialEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiMaterialOutInEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiMaterialService;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.xitongguanli.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/8/10 15:16
 * @Version 1.0
 * 需求:安装调试  库存管理
 */
@RestController
@RequestMapping("/MaterialAll")
public class AnZhuangTiaoShiMaterialController extends SessionController {
    @Autowired
    private AnZhuangTiaoShiMaterialService anZhuangTiaoShiMaterialService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;


    //查询所有的库存物料  或者根据物料编码查询
    @GetMapping("findAllMaterial")
    public Map<String, Object> findAllMaterial(Integer page, Integer size, String materialName) {
        PageHelper.startPage(page, size);
        List<AnZhuangTiaoShiMaterialEntity> materialEntityList = anZhuangTiaoShiMaterialService.findAllMaterial(page, size, materialName);
        PageInfo<AnZhuangTiaoShiMaterialEntity> materialEntityPageInfo = new PageInfo<>(materialEntityList);
        return ResponseDataUtil.ok("查询所有的物料数据成功", materialEntityPageInfo);
    }

    //新增物料类别
    @PostMapping("/addMaterial")
    public Map<String, Object> addMaterial(AnZhuangTiaoShiMaterialEntity anZhuangTiaoShiMaterialEntity) {
        anZhuangTiaoShiMaterialService.addMaterial(anZhuangTiaoShiMaterialEntity);
        return ResponseDataUtil.ok("新增物料成功");
    }

    //根据物料类别id  编辑对应的物料
    @PutMapping("/editMaterialById/{id}")
    public Map<String, Object> editMaterialById(@PathVariable Integer id, AnZhuangTiaoShiMaterialEntity anZhuangTiaoShiMaterialEntity) {
        anZhuangTiaoShiMaterialService.editMaterialById(anZhuangTiaoShiMaterialEntity);
        return ResponseDataUtil.ok("编辑物料数据成功");
    }

    //初始化类别数据  或者根据物料编号模糊查询类别
    @GetMapping("/findAllMaterialDatas")
    public Map<String, Object> findAllMaterialDatas(Integer page, Integer size, String materialNumber,String materialsname,String materialsguige) {
        PageHelper.startPage(page, size);
        List<AnZhuangTiaoShiMaterialEntity> materialEntityList = anZhuangTiaoShiMaterialService.findAllMaterialDatas(page, size, materialNumber,materialsname,materialsguige);
        PageInfo<AnZhuangTiaoShiMaterialEntity> materialEntityPageInfo = new PageInfo<>(materialEntityList);
        return ResponseDataUtil.ok("查询物料数据成功", materialEntityPageInfo);
    }

    //根据id  单个或者批量删除库存物料
    @DeleteMapping("/deleteMaterialByIds/{ids}")
    public Map<String, Object> deleteMaterialByIds(@PathVariable Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            Integer totalNumber = anZhuangTiaoShiMaterialService.totalNumber(ids[i]);
            if (totalNumber == 0) {
                anZhuangTiaoShiMaterialService.deleteMaterialByIds(ids[i]);
                return ResponseDataUtil.ok("删除成功");
            } else {
                return ResponseDataUtil.error("物料库存数量不为0,无法删除");
            }

        }
        return ResponseDataUtil.ok("ok");
    }


    //////////////////////////////物料出入库////////////////////////////////

    //物料出库 初始化  或者根据物料编码查询数据
    @GetMapping("/findAllOutMaterial")
    public Map<String, Object> findAllOutMaterial(Integer page, Integer size, String materialNumber,String materialsname,String materialsguige) {
        PageHelper.startPage(page, size);
        List<AnZhuangTiaoShiMaterialOutInEntity> materialOutInEntityList = anZhuangTiaoShiMaterialService.findAllOutMaterial(page, size, materialNumber,materialsname,materialsguige);
        PageInfo<AnZhuangTiaoShiMaterialOutInEntity> materialOutInEntityPageInfo = new PageInfo<>(materialOutInEntityList);
        return ResponseDataUtil.ok("查询物料出库数据成功", materialOutInEntityPageInfo);
    }

    //物料入库 初始化  或者根据物料编码查询数据
    @GetMapping("/findAllInMaterial")
    public Map<String, Object> findAllInMaterial(Integer page, Integer size, String materialNumber,String materialsname,String materialsguige) {
        PageHelper.startPage(page, size);
        List<AnZhuangTiaoShiMaterialOutInEntity> materialOutInEntityList = anZhuangTiaoShiMaterialService.findAllInMaterial(page, size, materialNumber,materialsname,materialsguige);
        PageInfo<AnZhuangTiaoShiMaterialOutInEntity> materialOutInEntityPageInfo = new PageInfo<>(materialOutInEntityList);
        return ResponseDataUtil.ok("查询物料出库数据成功", materialOutInEntityPageInfo);
    }

    //查询所有的物料类别
    @GetMapping("/findAllMaterials")
    public Map<String, Object> findAllMaterials() {
        List<AnZhuangTiaoShiMaterialEntity> materialEntityList = anZhuangTiaoShiMaterialService.findAllMaterials();
        return ResponseDataUtil.ok("查询物料类别成功", materialEntityList);
    }

    //新增物料出库
    @PostMapping("/addOutMaterial")
    public Map<String, Object> addOutMaterial(AnZhuangTiaoShiMaterialOutInEntity anZhuangTiaoShiMaterialOutInEntity, AnZhuangTiaoShiMaterialEntity anZhuangTiaoShiMaterialEntity, Integer mid, Integer yujingnumber) {
        String username = this.getLoginUser().getTrueName();
        AnZhuangTiaoShiMaterialEntity materialEntityList = anZhuangTiaoShiMaterialService.fiandMaterial(mid);
        Integer totalnumber = materialEntityList.getTotalnumber();
        if (totalnumber >= anZhuangTiaoShiMaterialOutInEntity.getMaterialsoutnumber()) {
            anZhuangTiaoShiMaterialService.addOutMaterial(anZhuangTiaoShiMaterialOutInEntity);
            Integer total = totalnumber - anZhuangTiaoShiMaterialOutInEntity.getMaterialsoutnumber();
            anZhuangTiaoShiMaterialEntity.setTotalnumber(total);
            anZhuangTiaoShiMaterialService.editMaterial(anZhuangTiaoShiMaterialEntity, mid);
            String materialsname = anZhuangTiaoShiMaterialEntity.getMaterialsname();
            String materialsguige = anZhuangTiaoShiMaterialEntity.getMaterialsguige();
            List<Integer> uids = new ArrayList<>();
            if (yujingnumber >= total) {
                //获取所有的人员id  发消息给所有人
                String truename = null;
                List<UserEntity> userEntitiess = userService.findByTruename(truename);
                for (UserEntity entitiess : userEntitiess) {
                    Integer id = entitiess.getId().intValue();
                    uids.add(id);
                }
                for (Integer uid : uids) {
                    MessageEntity messageEntity = new MessageEntity();
                    messageEntity.setCreateBy(username);//创建人
                    messageEntity.setCreateTime(new Date());//创建时间
                    messageEntity.setContext("“物料名:" + materialsname +"规格:"+materialsguige+ "”的物料数量低于预警线，请及时申请采购！");
                    messageEntity.setType((short) 3);
                    messageEntity.setMessageType((short) 1);
                    messageEntity.setProjectId(mid);
                    messageEntity.setReceiverId(uid);
                    messageEntity.setStatus((short) 1);
                    messageService.sendMessage(messageEntity);
                }
            }
            return ResponseDataUtil.ok("新增物料出库数据成功");
        } else {
            return ResponseDataUtil.error("库存物料小于出库数量,请正确确认出库数量");
        }

    }

    //新增物料入库
    @PostMapping("/addInMaterial")
    public Map<String, Object> addInMaterial(AnZhuangTiaoShiMaterialOutInEntity anZhuangTiaoShiMaterialOutInEntity, AnZhuangTiaoShiMaterialEntity anZhuangTiaoShiMaterialEntity, Integer mid) {
        AnZhuangTiaoShiMaterialEntity materialEntityList = anZhuangTiaoShiMaterialService.fiandMaterial(mid);
        Integer totalnumber = materialEntityList.getTotalnumber();
        anZhuangTiaoShiMaterialService.addInMaterial(anZhuangTiaoShiMaterialOutInEntity);
        Integer total = totalnumber + anZhuangTiaoShiMaterialOutInEntity.getMaterialsinnumber();
        anZhuangTiaoShiMaterialEntity.setTotalnumber(total);
        anZhuangTiaoShiMaterialService.editMaterial(anZhuangTiaoShiMaterialEntity, mid);
        return ResponseDataUtil.ok("新增物料入库数据成功");
    }

    //根据id 是删除对应的出入库数据
    @DeleteMapping("/deleteByIds/{ids}")
    public Map<String,Object>deleteByIds(@PathVariable Integer[] ids){
        anZhuangTiaoShiMaterialService.deleteByIds(ids);
        return ResponseDataUtil.ok("删除数据成功");
    }
}
