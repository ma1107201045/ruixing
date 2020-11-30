package com.yintu.ruixing.chanpinjiaofu.impl;

import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.master.chanpinjiaofu.ChanPinJiaoFuCostZhiChuDao;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuCostZhiChuEntity;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuEntity;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuCostZhiChuService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Mr.liu
 * @Date 2020/7/4 20:23
 * @Version 1.0
 * 需求:产品交付费用-支出
 */
@Service
@Transactional
public class ChanPinJiaoFuCostZhiChuServiceImpl implements ChanPinJiaoFuCostZhiChuService {
    @Autowired
    private ChanPinJiaoFuCostZhiChuDao chanPinJiaoFuCostZhiChuDao;

    @Override
    public void exportFile(ServletOutputStream outputStream, Integer[] ids) throws IOException {
        //excel标题
        String title = "交付费用之支出费用列表";
        //excel表名
        String[] headers = {"序号",  "项目名称", "材料费", "人工费", "机具折旧维护","低值易耗品","水电费","合计","运杂费","检测费","总计" };
        //获取数据
        List<ChanPinJiaoFuCostZhiChuEntity> chanPinJiaoFuCostZhiChuDaoList = chanPinJiaoFuCostZhiChuDao.selectByCondition(ids);
        chanPinJiaoFuCostZhiChuDaoList = chanPinJiaoFuCostZhiChuDaoList.stream()
                .sorted(Comparator.comparing(ChanPinJiaoFuCostZhiChuEntity::getId).reversed())
                .collect(Collectors.toList());
        //excel元素
        Integer j=0;
        String[][] content = new String[chanPinJiaoFuCostZhiChuDaoList.size()][headers.length];
        for (int i = 0; i < chanPinJiaoFuCostZhiChuDaoList.size(); i++) {
            j++;
            ChanPinJiaoFuCostZhiChuEntity chanPinJiaoFuCostZhiChuEntity = chanPinJiaoFuCostZhiChuDaoList.get(i);
            content[i][0] = j.toString();
            content[i][1] = chanPinJiaoFuCostZhiChuEntity.getXiangmuName();
            content[i][2] = chanPinJiaoFuCostZhiChuEntity.getCailiaoMoney().toString();
            content[i][3] = chanPinJiaoFuCostZhiChuEntity.getRengongMoney().toString();
            content[i][4] = chanPinJiaoFuCostZhiChuEntity.getJijuzhejiuMoney().toString();
            content[i][5] = chanPinJiaoFuCostZhiChuEntity.getDizhiyihaoMoney().toString();
            content[i][6] = chanPinJiaoFuCostZhiChuEntity.getShuidianMoney().toString();
            content[i][7] = chanPinJiaoFuCostZhiChuEntity.getZhizaocost().toString();
            content[i][8] = chanPinJiaoFuCostZhiChuEntity.getYunzaMoney().toString();
            content[i][9] = chanPinJiaoFuCostZhiChuEntity.getJianceMoney().toString();
            content[i][10] = chanPinJiaoFuCostZhiChuEntity.getAllcost().toString();
        }
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public List<ChanPinJiaoFuCostZhiChuEntity> findAllZhiChuCost(Integer page, Integer size) {
        return chanPinJiaoFuCostZhiChuDao.findAllZhiChuCost();
    }

    @Override
    public void deletZhiChuCostByIds(Integer[] ids) {
        chanPinJiaoFuCostZhiChuDao.deletZhiChuCostByIds(ids);
    }

    @Override
    public List<ChanPinJiaoFuCostZhiChuEntity> findZhiChuCostByName(Integer page, Integer size, String xiangMuName) {
        return chanPinJiaoFuCostZhiChuDao.findZhiChuCostByName(xiangMuName);
    }

    @Override
    public void editZhiChuCost(ChanPinJiaoFuCostZhiChuEntity chanPinJiaoFuCostZhiChuEntity) {
        chanPinJiaoFuCostZhiChuDao.editZhiChuCost(chanPinJiaoFuCostZhiChuEntity);
    }

    @Override
    public void addZhiChuCost(ChanPinJiaoFuCostZhiChuEntity chanPinJiaoFuCostZhiChuEntity) {
        chanPinJiaoFuCostZhiChuDao.addZhiChuCost(chanPinJiaoFuCostZhiChuEntity);
    }

    @Override
    public List<ChanPinJiaoFuXiangMuEntity> findXiangMu() {
        return chanPinJiaoFuCostZhiChuDao.findXiangMu();
    }
}
