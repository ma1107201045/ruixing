package com.yintu.ruixing.jiejuefangan;

import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author:mlf
 * @date:2020/7/3 18:01
 */
@RestController
@RequestMapping("/solutions")
@Slf4j
public class SolutionController extends SessionController {
    @Autowired
    private SolutionService solutionService;


    @GetMapping("/work/completion")
    public Map<String, Object> workCompletion(HttpServletRequest request, @RequestParam("date") Date date) {

        log.error("getRemoteHost:" + request.getRemoteHost());
        log.error("getRemotePort:" + request.getRemotePort());
        log.error("getRemoteAddr:" + request.getRemoteAddr());
        log.error("getRemoteUser:" + request.getRemoteUser());
        log.error("-------------------------------------");
        log.error("getScheme:" + request.getScheme());
        log.error("getServerName:" + request.getServerName());
        log.error("getServerPort:" + request.getServerPort());
        log.error("getRequestURL:" + request.getRequestURL());
        log.error("getRequestURI:" + request.getRequestURI());
        log.error("isSecure:" + request.isSecure());
        log.error("-------------------------------------");
        log.error("getIpAddress:" + getIpAddress(request));
        JSONArray ja = solutionService.workCompletion(date);
        return ResponseDataUtil.ok("查询工作完成进度成功", ja);
    }

    public String getIpAddress(HttpServletRequest request) {
        if (request == null)
            return "";
        String ip = request.getHeader("X-Requested-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            log.error("X-Forwarded-For---------------------");
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            log.error("Proxy-Client-IP---------------------");
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            log.error("WL-Proxy-Client-IP---------------------");
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            log.error("HTTP_CLIENT_IP---------------------");
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            log.error("HTTP_X_FORWARDED_FOR---------------------");
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            log.error("request.getRemoteAddr()---------------------");
            ip = request.getRemoteAddr();
        }
//        if ("0:0:0:0:0:0:0:1".equals(ip)) {
//            ip = "127.0.0.1";
//        }
        return ip;
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
