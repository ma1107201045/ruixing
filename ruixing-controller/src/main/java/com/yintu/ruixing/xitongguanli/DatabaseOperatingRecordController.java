package com.yintu.ruixing.xitongguanli;

import com.github.pagehelper.PageHelper;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/10/31 16:24
 */
@RestController
@RequestMapping("/database/operating/records")
public class DatabaseOperatingRecordController extends SessionController {
    @Autowired
    private DatabaseOperatingRecordService databaseOperatingRecordService;

    /**
     * 备份数据库数据
     *
     * @param request 请求对象
     * @return 返回结果
     */
    @PostMapping
    public Map<String, Object> backup(HttpServletRequest request) {
        databaseOperatingRecordService.backup(request, this.getLoginUserName());
        return ResponseDataUtil.ok("备份数据库信息成功");
    }

    /**
     * 还原数据库数据
     *
     * @param id 记录id
     * @return 返回请求结果
     */
    @GetMapping("/{id}")
    public Map<String, Object> restore(@PathVariable Long id) {
        databaseOperatingRecordService.restore(id, this.getLoginUserName());
        return ResponseDataUtil.ok("还原数据库信息成功");
    }


    @GetMapping
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "id DESC") String orderBy) {
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<DatabaseOperatingRecordEntity> databaseOperatingRecordEntities = databaseOperatingRecordService.findAll();
        return ResponseDataUtil.ok("查询数据库操作信息列表成功", databaseOperatingRecordEntities);
    }

}
