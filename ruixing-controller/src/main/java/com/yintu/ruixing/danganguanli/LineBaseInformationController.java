package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: mlf
 * @Date: 2020/12/8 15:54
 * @Version: 1.0
 */
@RestController
@RequestMapping("/line/base/information")
public class LineBaseInformationController implements BaseController<LineBaseInformationEntity, Integer> {
    @Autowired
    private LineBaseInformationService lineBaseInformationService;

    @PostMapping
    public Map<String, Object> add(LineBaseInformationEntity entity) {
        lineBaseInformationService.add(entity);
        return ResponseDataUtil.ok("添加线段基本信息成功");
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> remove(@PathVariable Integer id) {
        return null;
    }

    @Override
    public Map<String, Object> edit(Integer id, LineBaseInformationEntity entity) {
        return null;
    }

    @Override
    public Map<String, Object> findById(Integer id) {
        return null;
    }
}
