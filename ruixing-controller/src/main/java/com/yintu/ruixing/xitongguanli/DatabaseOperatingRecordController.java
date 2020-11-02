package com.yintu.ruixing.xitongguanli;

import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/10/31 16:24
 */
@RestController
@RequestMapping("/database/operating/record")
public class DatabaseOperatingRecordController extends SessionController {
    @Autowired
    private DatabaseOperatingRecordService databaseOperatingRecordService;

    @PostMapping
    public Map<String, Object> backup(HttpServletRequest request) {
        databaseOperatingRecordService.backup(request, this.getLoginUserName());
        return ResponseDataUtil.ok("备份成功");
    }

}
