package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.xitongguanli.UserEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author:mlf
 * @date:2020/5/28 14:15
 */
@Controller
@RequestMapping("/customers")
public class CustomerController extends SessionController {


    @PostMapping
    @ResponseBody
    public Map<String, Object> add(UserEntity userEntity, @RequestParam Long[] roleIds, @RequestParam Long[] departmentIds) {


        return null;
    }

    @DeleteMapping("/{ids}")
    @ResponseBody
    public Map<String, Object> remove(@PathVariable Long[] ids) {
        return ResponseDataUtil.ok("删除客户成功");
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Map<String, Object> edit(@PathVariable Long id, UserEntity userEntity, @RequestParam Long[] roleIds, @RequestParam Long[] departmentIds) {
        return null;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Map<String, Object> findById(@PathVariable Long id) {
        return null;
    }

    @GetMapping
    @ResponseBody
    public Map<String, Object> findAll(@RequestParam("page_number") Integer pageNumber,
                                       @RequestParam("page_size") Integer pageSize,
                                       @RequestParam(value = "order_by", required = false, defaultValue = "id DESC") String orderBy,
                                       @RequestParam(value = "search_text", required = false) String username) {
        return null;
    }



    @GetMapping("/departments")
    @ResponseBody
    public Map<String, Object> findDepartments() {
        return null;
    }

    @GetMapping("/export/{ids}")
    public void exportFile(@PathVariable Long[] ids, HttpServletResponse response) throws IOException {
        return;
    }


}
