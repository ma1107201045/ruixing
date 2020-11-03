package com.yintu.ruixing.jiejuefangan.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.jiejuefangan.SolutionDao;
import com.yintu.ruixing.jiejuefangan.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author:mlf
 * @date:2020/7/3 18:07
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class SolutionServiceImpl implements SolutionService {


    @Autowired
    private SolutionDao solutionDao;

    @Override
    public JSONArray workCompletion(Date date) {
        JSONArray ja = new JSONArray();
        List<Integer> yearNoFinish = solutionDao.countTaskStatusByGroupBy(1, date, (short) 1);
        List<Integer> yearFinish = solutionDao.countTaskStatusByGroupBy(1, date, (short) 2);
        List<Integer> monthNoFinish = solutionDao.countTaskStatusByGroupBy(2, date, (short) 1);
        List<Integer> monthFinish = solutionDao.countTaskStatusByGroupBy(2, date, (short) 2);
        for (int i = 0; i < 3; i++) {
            JSONObject jo = new JSONObject(true);
            switch (i) {
                case 0:
                    jo.put("name", "售前技术支持");
                    break;
                case 1:
                    jo.put("name", "招标投标技术支持");
                    break;
                case 2:
                    jo.put("name", "设计联络及后续技术交流");
                    break;
            }
            jo.put("yearNoFinish", yearNoFinish.get(i));
            jo.put("yearFinish", yearFinish.get(i));
            jo.put("monthNoFinish", monthNoFinish.get(i));
            jo.put("monthFinish", monthFinish.get(i));
            ja.add(jo);
        }
        return ja;
    }

    @Override
    public List<Map<String, Object>> biddingProject(Date startDate, Date endDate, Short projectStatus) {
        return solutionDao.selectByDateSectionAndProjectStatus(startDate, endDate, projectStatus);
    }

    @Override
    public long findProjectSum() {
        return solutionDao.selectProjectSum();
    }
}
