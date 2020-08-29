package com.yintu.ruixing.jiejuefangan;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuEntity;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 设计联络以及后续技术交流
 *
 * @author:mlf
 * @date:2020/7/3 11:31
 */
@RestController
@RequestMapping("/design/liaisons")
public class DesignLiaisonController extends SessionController implements BaseController<DesignLiaisonEntity, Integer> {

    @Autowired
    private DesignLiaisonService designLiaisonService;

    @Autowired
    private DesignLiaisonFileService designLiaisonFileService;

    @Autowired
    private TieLuJuService tieLuJuService;

    @PostMapping
    public Map<String, Object> add(@Validated DesignLiaisonEntity entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        designLiaisonService.add(entity);
        return ResponseDataUtil.ok("添加设计联络及后续技术交流信息成功");
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> remove(@PathVariable Integer id) {
        designLiaisonService.remove(id);
        return ResponseDataUtil.ok("删除设计联络及后续技术交流信息成功");

    }

    @PutMapping("/{id}")
    public Map<String, Object> edit(@PathVariable Integer id, @Validated DesignLiaisonEntity entity) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        designLiaisonService.edit(entity);
        return ResponseDataUtil.ok("修改设计联络及后续技术交流信息成功");
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        DesignLiaisonEntity designLiaisonEntity = designLiaisonService.findById(id);
        return ResponseDataUtil.ok("查询设计联络及后续技术交流信息成功", designLiaisonEntity);
    }

    @GetMapping
    public Map<String, Object> findTree() {
        List<TreeNodeUtil> treeNodeUtils = designLiaisonService.findByTree();
        return ResponseDataUtil.ok("查询设计联络及后续技术交流信息树成功", treeNodeUtils);
    }

    @GetMapping("/list")
    public Map<String, Object> findAll() {
        List<DesignLiaisonEntity> designLiaisonEntities = designLiaisonService.findAll();
        return ResponseDataUtil.ok("查询设计联络及后续技术交流信息列表成功", designLiaisonEntities);
    }

    @GetMapping("/search")
    public Map<String, Object> findBySearch(@RequestParam("page_number") Integer pageNumber,
                                            @RequestParam("page_size") Integer pageSize,
                                            @RequestParam(value = "order_by", required = false, defaultValue = "dlf.id DESC") String orderBy,
                                            @RequestParam(value = "year", required = false) Integer year,
                                            @RequestParam(value = "project_name", required = false) String projectName,
                                            @RequestParam(value = "type", required = false) String type) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<DesignLiaisonFileEntity> designLiaisonEntities = designLiaisonFileService.findByYearAndProjectNameAndType(year, projectName, type);
        PageInfo<DesignLiaisonFileEntity> pageInfo = new PageInfo<>(designLiaisonEntities);
        return ResponseDataUtil.ok("查询设计联络及后续技术交流信息以及文件信息列表成功", pageInfo);
    }

    @GetMapping("/tielujus")
    public Map<String, Object> findTieLuJus() {
        List<TieLuJuEntity> tieLuJuEntities = tieLuJuService.findAllTieLuJu();
        return ResponseDataUtil.ok("查询铁路局列表信息成功", tieLuJuEntities);
    }


}
