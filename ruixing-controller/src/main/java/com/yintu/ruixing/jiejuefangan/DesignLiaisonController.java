package com.yintu.ruixing.jiejuefangan;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuEntity;
import com.yintu.ruixing.guzhangzhenduan.TieLuJuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 设计联络以及后续技术交流
 *
 * @author:mlf
 * @date:2020/7/3 11:31
 */
@Controller
@RequestMapping("/design/liaisons")
public class DesignLiaisonController extends SessionController implements BaseController<DesignLiaisonEntity, Integer> {

    @Autowired
    private DesignLiaisonService designLiaisonService;

    @Autowired
    private SolutionLogService solutionLogService;

    @Autowired
    private TieLuJuService tieLuJuService;

    @PostMapping
    @ResponseBody
    public Map<String, Object> add(@Validated DesignLiaisonEntity entity) {
        entity.setCreateBy(this.getLoginUserName());
        entity.setCreateTime(new Date());
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        designLiaisonService.add(entity, this.getLoginTrueName());
        return ResponseDataUtil.ok("添加设计联络及后续技术交流信息成功");
    }

    @Override
    public Map<String, Object> remove(Integer id) {
        return null;
    }

    @DeleteMapping("/{ids}")
    @ResponseBody
    public Map<String, Object> remove(@PathVariable Integer[] ids) {
        designLiaisonService.remove(ids);
        return ResponseDataUtil.ok("删除设计联络及后续技术交流信息成功");

    }

    @PutMapping("/{id}")
    @ResponseBody
    public Map<String, Object> edit(@PathVariable Integer id, @Validated DesignLiaisonEntity entity) {
        entity.setModifiedBy(this.getLoginUserName());
        entity.setModifiedTime(new Date());
        designLiaisonService.edit(entity, this.getLoginTrueName());
        return ResponseDataUtil.ok("修改设计联络及后续技术交流信息成功");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Map<String, Object> findById(@PathVariable Integer id) {
        DesignLiaisonEntity designLiaisonEntity = designLiaisonService.findById(id);
        return ResponseDataUtil.ok("查询设计联络及后续技术交流信息成功", designLiaisonEntity);
    }

    @GetMapping
    @ResponseBody
    public Map<String, Object> findTree() {
        List<TreeNodeUtil> treeNodeUtils = designLiaisonService.findByTree();
        return ResponseDataUtil.ok("查询设计联络及后续技术交流信息树成功", treeNodeUtils);
    }

    @GetMapping("/list")
    @ResponseBody
    public Map<String, Object> findByExample(@RequestParam("page_number") Integer pageNumber,
                                             @RequestParam("page_size") Integer pageSize,
                                             @RequestParam(value = "order_by", required = false, defaultValue = "id DESC") String orderBy,
                                             @RequestParam(value = "year", required = false) Integer year,
                                             @RequestParam(value = "project_name", required = false) String projectName) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<DesignLiaisonEntity> designLiaisonEntities = designLiaisonService.findByExample(year, projectName);
        PageInfo<DesignLiaisonEntity> pageInfo = new PageInfo<>(designLiaisonEntities);
        return ResponseDataUtil.ok("查询设计联络及后续技术交流信息列表成功", pageInfo);
    }

    @GetMapping("/export/{ids}")
    public void exportFile(@PathVariable Integer[] ids, HttpServletResponse response) throws IOException {
        String fileName = "设计联络及后续交流-项目列表" + System.currentTimeMillis() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        designLiaisonService.exportFile(response.getOutputStream(), ids);
    }

    @GetMapping("/{id}/log")
    @ResponseBody
    public Map<String, Object> findLogByExample(@PathVariable Integer id) {
        List<SolutionLogEntity> solutionLogEntities = solutionLogService.findByExample(new SolutionLogEntity(null, null, null, (short) 3, (short) 1, id, null));
        return ResponseDataUtil.ok("查询设计联络及后续技术交流项目日志信息列表成功", solutionLogEntities);
    }

    @GetMapping("/projects")
    @ResponseBody
    public Map<String, Object> findProjects() {
        List<Map<String, Object>> maps = designLiaisonService.findProjectsByProjectStatus();
        return ResponseDataUtil.ok("查询项目总和信息列表成功", maps);
    }

    @GetMapping("/tielujus")
    @ResponseBody
    public Map<String, Object> findTieLuJus() {
        List<TieLuJuEntity> tieLuJuEntities = tieLuJuService.findAllTieLuJu();
        return ResponseDataUtil.ok("查询铁路局列表信息成功", tieLuJuEntities);
    }


}
