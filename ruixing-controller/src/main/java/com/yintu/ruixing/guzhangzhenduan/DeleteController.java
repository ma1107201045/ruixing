package com.yintu.ruixing.guzhangzhenduan;


import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.guzhangzhenduan.DeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author:lcy
 * @date:2020-06-02 20
 * 分页
 */
@RestController
@RequestMapping("/delete")
public class DeleteController {
    @Autowired
    private DeleteService deleteService;

    @DeleteMapping ("/deleteByIds/{idtype}/{ids}")
    public Map<String, Object> delTieLuJU (@PathVariable String idtype,@PathVariable int[] ids) {

        if (idtype.equals("tid")) {
            deleteService.delTieLuJU(ids);
            return ResponseDataUtil.ok("批量删除铁路局信息成功");
        }
        if (idtype.equals("did")) {
            deleteService.delDianDuDuan(ids);
            return ResponseDataUtil.ok("批量删除电务段信息成功");
        }
        if (idtype.equals("xid")) {
            deleteService.delXianDuan(ids);
            return ResponseDataUtil.ok("批量删除线段信息成功");
        }
        if (idtype.equals("cid")) {
            deleteService.delCheZhan(ids);
            return ResponseDataUtil.ok("批量删除车站信息成功");
        }
        return ResponseDataUtil.ok("删除成功");
    }
}
