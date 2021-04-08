package com.yintu.ruixing.anzhuangtiaoshi;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2021/4/6 15:13
 * @Version 1.0
 * 需求:安装调试问题相关配置
 */
@RestController
@RequestMapping("/wentTiypeAll")
public class AnZhuangTiaoShiWenTiTypeController extends SessionController {
    @Autowired
    private AnZhuangTiaoShiWenTiTypeService anZhuangTiaoShiWenTiTypeService;

    //新增问题相关配置
    @PostMapping("/addWenTiType")
    public Map<String, Object> addWenTiType(AnZhuangTiaoShiWenTiTypeEntity anZhuangTiaoShiWenTiTypeEntity) {
        String username = this.getLoginUser().getUsername();
        anZhuangTiaoShiWenTiTypeEntity.setCreateBy(username);
        anZhuangTiaoShiWenTiTypeEntity.setCreateTime(new Date());
        anZhuangTiaoShiWenTiTypeEntity.setModifiedBy(username);
        anZhuangTiaoShiWenTiTypeEntity.setModifiedTime(new Date());
        anZhuangTiaoShiWenTiTypeService.addWenTiType(anZhuangTiaoShiWenTiTypeEntity);
        return ResponseDataUtil.ok("新增成功");
    }

    //初始化查询  或者根据问题名模糊查询
    @GetMapping("/findWenTiType")
    public Map<String, Object> findWenTiType(Integer page, Integer size, String wenTiName, Integer typeId) {
        PageHelper.startPage(page, size);
        List<AnZhuangTiaoShiWenTiEntity> wenTiEntityList = anZhuangTiaoShiWenTiTypeService.findWenTiType(wenTiName, typeId);
        PageInfo<AnZhuangTiaoShiWenTiEntity> wenTiEntityPageInfo = new PageInfo<>(wenTiEntityList);
        return ResponseDataUtil.ok("查询成功", wenTiEntityPageInfo);
    }

    //根据id 编辑对应的问题配置
    @PutMapping("/editById/{id}")
    public Map<String, Object> editById(@PathVariable Integer id, AnZhuangTiaoShiWenTiTypeEntity anZhuangTiaoShiWenTiTypeEntity) {
        String username = this.getLoginUser().getUsername();
        anZhuangTiaoShiWenTiTypeEntity.setModifiedTime(new Date());
        anZhuangTiaoShiWenTiTypeEntity.setModifiedBy(username);
        anZhuangTiaoShiWenTiTypeService.editById(anZhuangTiaoShiWenTiTypeEntity);
        return ResponseDataUtil.ok("编辑成功");
    }

    //根据id  批量删除对应的问题配置
    @DeleteMapping("/deleteByIds/{ids}")
    public Map<String, Object> deleteByIds(@PathVariable Integer[] ids) {
        anZhuangTiaoShiWenTiTypeService.deleteByIds(ids);
        return ResponseDataUtil.ok("删除成功");
    }

    //查询所有的问题类别
    @GetMapping("/findAllType")
    public Map<String,Object>findAllType(Integer typeId){
        List<AnZhuangTiaoShiWenTiEntity>wenTiEntityList=anZhuangTiaoShiWenTiTypeService.findAllType(typeId);
        return ResponseDataUtil.ok("查询成功",wenTiEntityList);
    }
}
