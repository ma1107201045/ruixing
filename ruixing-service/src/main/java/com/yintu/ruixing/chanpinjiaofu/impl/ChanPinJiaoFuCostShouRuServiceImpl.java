package com.yintu.ruixing.chanpinjiaofu.impl;

import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.master.chanpinjiaofu.ChanPinJiaoFuCostShouRuDao;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuCostShouRuEntity;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuCostShouRuService;
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
 * @Date 2020/7/4 19:39
 * @Version 1.0
 * 需求:产品交付费用相关
 */
@Service
@Transactional
public class ChanPinJiaoFuCostShouRuServiceImpl implements ChanPinJiaoFuCostShouRuService {
    @Autowired
    private ChanPinJiaoFuCostShouRuDao chanPinJiaoFuCostShouRuDao;

    @Override
    public List<ChanPinJiaoFuCostShouRuEntity> findAllShouRuCost(Integer page, Integer size) {
        return chanPinJiaoFuCostShouRuDao.findAllShouRuCost();
    }

    @Override
    public void exportFile(ServletOutputStream outputStream, Integer[] ids)throws IOException {
        //excel标题
        String title = "交付费用之收入费用列表";
        //excel表名
        String[] headers = {"序号",  "项目名称", "设备销售合同金额", "备品销售合同金额", "总计" };
        //获取数据
        List<ChanPinJiaoFuCostShouRuEntity> chanPinJiaoFuCostShouRuEntities = chanPinJiaoFuCostShouRuDao.selectByCondition(ids);
        chanPinJiaoFuCostShouRuEntities = chanPinJiaoFuCostShouRuEntities.stream()
                .sorted(Comparator.comparing(ChanPinJiaoFuCostShouRuEntity::getId).reversed())
                .collect(Collectors.toList());
        //excel元素
        Integer j=0;
        String[][] content = new String[chanPinJiaoFuCostShouRuEntities.size()][headers.length];
        for (int i = 0; i < chanPinJiaoFuCostShouRuEntities.size(); i++) {
            j++;
            ChanPinJiaoFuCostShouRuEntity chanPinJiaoFuCostShouRuEntity = chanPinJiaoFuCostShouRuEntities.get(i);
            content[i][0] =j.toString() ;
            content[i][1] = chanPinJiaoFuCostShouRuEntity.getXiangmuName();
            content[i][2] = chanPinJiaoFuCostShouRuEntity.getShebeiXiaoshouHetongMoney().toString();
            content[i][3] = chanPinJiaoFuCostShouRuEntity.getBeipinXiaoshouHetongMoney().toString();
            content[i][4] = chanPinJiaoFuCostShouRuEntity.getAllcost().toString();
        }
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }


    @Override
    public void deletShouRuCostByIds(Integer[] ids) {
        chanPinJiaoFuCostShouRuDao.deletShouRuCostByIds(ids);
    }

    @Override
    public List<ChanPinJiaoFuCostShouRuEntity> findShouRuCostByName(Integer page, Integer size, String xiangMuName) {
        return chanPinJiaoFuCostShouRuDao.findShouRuCostByName(xiangMuName);
    }

    @Override
    public void editShouRuCost(ChanPinJiaoFuCostShouRuEntity chanPinJiaoFuCostShouRuEntity) {
        chanPinJiaoFuCostShouRuDao.editShouRuCost(chanPinJiaoFuCostShouRuEntity);
    }

    @Override
    public void addShouRuCost(ChanPinJiaoFuCostShouRuEntity chanPinJiaoFuCostShouRuEntity) {
        chanPinJiaoFuCostShouRuDao.addShouRuCost(chanPinJiaoFuCostShouRuEntity);
    }


}
