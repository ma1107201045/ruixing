package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/24 10:09
 */
@RestController
@RequestMapping("/")
public class LineTechnologyStatusStationUnitController extends SessionController implements BaseController<LineTechnologyStatusStationUnitEntity, Integer> {
    @Autowired
    private LineTechnologyStatusStationUnitService lineTechnologyStatusStationUnitService;

    @Override
    public Map<String, Object> add(LineTechnologyStatusStationUnitEntity entity) {
        return null;
    }

    @Override
    public Map<String, Object> remove(Integer id) {
        return null;
    }

    @Override
    public Map<String, Object> edit(Integer id, LineTechnologyStatusStationUnitEntity entity) {
        return null;
    }

    @Override
    public Map<String, Object> findById(Integer id) {
        return null;
    }
}
