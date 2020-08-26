package com.yintu.ruixing.jiejuefangan;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author:mlf
 * @date:2020/7/3 18:01
 */
@RestController
@RequestMapping("/solutions")
public class SolutionController extends SessionController {
    @Autowired
    private SolutionService solutionService;


    @GetMapping("/work/completion")
    public Map<String, Object> workCompletion(@RequestParam("date") Date date) {
        JSONArray ja = solutionService.workCompletion(date);
        return ResponseDataUtil.ok("查询工作完成进度成功", ja);
    }

    @GetMapping("/bidding/project")
    public Map<String, Object> biddingProject(@RequestParam("page_number") Integer pageNumber,
                                              @RequestParam("page_size") Integer pageSize,
                                              @RequestParam(value = "order_by", required = false, defaultValue = "project_date DESC") String orderBy,
                                              @RequestParam("start_date") Date startDate,
                                              @RequestParam("end_date") Date endDate) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<Map<String, Object>> maps = solutionService.biddingProject(startDate, endDate, (short) 3);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(maps);
        return ResponseDataUtil.ok("查询中标项目成功", pageInfo);
    }
}
