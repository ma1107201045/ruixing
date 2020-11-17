package com.yintu.ruixing.guzhangzhenduan.impl;

import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
import com.yintu.ruixing.guzhangzhenduan.GuZhangStudyService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author:lcy
 * @date:2020-06-04 16
 */
@Service
@Transactional
public class GuZhangStudyServiceImpl implements GuZhangStudyService {
    @Autowired
    private GuZhangStudyDao guZhangStudyDao;

    @Autowired
    private QuDuanInfoDao quDuanInfoDao;

    @Autowired
    private QuDuanInfoDaoV2 quDuanInfoDaoV2;
    @Override //1
    public List<GuZhangStudyEntity> findGuZhangList(Integer page, Integer size) {
        return guZhangStudyDao.findGuZhangList();
    }

    @Override
    public GuZhangStudyEntity findGuZhangById(Long id) {
        return guZhangStudyDao.selectByPrimaryKey(id);
    }

    @Override
    public void addGuZhang(GuZhangStudyEntity guZhangStudyEntity) {
        guZhangStudyDao.addGuZhang(guZhangStudyEntity);
    }

    @Override
    public void editGuZhang(GuZhangStudyEntity guZhangStudyEntity) {
        guZhangStudyDao.editGuZhang(guZhangStudyEntity);
    }



    @Override
    public void deletGuZhangList(Long[] ids) {
        guZhangStudyDao.deletGuZhangList(ids);
    }


    @Override
    public List<GuZhangStudyEntity> GuZhangListExcelDownloads(Long[] ids) {
        return guZhangStudyDao.GuZhangListExcelDownloads(ids);


    }

    @Override
    public List<GuZhangStudyEntity> GuZhangListExcelDownloadsById(Long id) {
        return guZhangStudyDao.GuZhangListExcelDownloadsById(id);
    }

    @Override
    public List<XianDuanEntity> getXianDuan(XianDuanEntity xianDuanEntity) {
        return guZhangStudyDao.getXianDuan(xianDuanEntity);
    }

    @Override
    public List<CheZhanEntity> getCheZhanByXid(Long xid) {
        return guZhangStudyDao.getCheZhanByXid(xid);
    }

    @Override
    public List<QuDuanBaseEntity> getQuDuanByCid(Long cid) {
        return guZhangStudyDao.getQuDuanByCid(cid);
    }





    @Override
    public List<QuDuanInfoEntity> findGuZhangKuData(Integer id, Integer page, Integer size,String tableName) {

        List<QuDuanInfoEntity> quDuanInfoEntities= quDuanInfoDaoV2.findGuZhangKuData(id,tableName);

        return quDuanInfoEntities;
    }

    @Override
    public List<QuDuanBaseEntity> findFristId(Integer id) {
        return  guZhangStudyDao.findFristId(id);


    }

    @Override
    public List<QuDuanBaseEntity> findLastId(Integer id) {
        return guZhangStudyDao.findLastId(id);
    }

    @Override
    public void exportFile(ServletOutputStream outputStream, Integer[] ids)throws IOException {
        //excel标题
        String title = "故障信息列表";
        //excel表名
        String[] headers = {"序号", "故障记录日期", "故障学习记录名称", "故障类型", "故障简要描述", "故障发生车站", "发生日期和时刻", "神经网络代号" };
        //获取数据
        List<GuZhangStudyEntity> guZhangStudyEntities = guZhangStudyDao.selectByCondition(ids);
        guZhangStudyEntities = guZhangStudyEntities.stream()
                .sorted(Comparator.comparing(GuZhangStudyEntity::getId).reversed())
                .collect(Collectors.toList());
        //excel元素
        Integer j=0;
        String[][] content = new String[guZhangStudyEntities.size()][headers.length];
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < guZhangStudyEntities.size(); i++) {
            j++;
            GuZhangStudyEntity guZhangStudyEntity = guZhangStudyEntities.get(i);
            content[i][0] =j.toString() ;
            content[i][1] = formatter1.format(guZhangStudyEntity.getGuzhangjiluTime());
            content[i][2] = guZhangStudyEntity.getGuzhangjiluName();
            content[i][3] = guZhangStudyEntity.getGuzhangType();
            content[i][4] = guZhangStudyEntity.getGuzhangmiaoshu();
            content[i][5] = guZhangStudyEntity.getGuzhangChezhan();
            content[i][6] = formatter2.format(guZhangStudyEntity.getGuzhangStartTime());
            content[i][7] = guZhangStudyEntity.getShenjingwangluoCode();
        }
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }


}
