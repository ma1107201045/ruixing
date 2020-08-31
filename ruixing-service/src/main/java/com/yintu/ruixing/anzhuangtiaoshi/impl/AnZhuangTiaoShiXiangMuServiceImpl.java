package com.yintu.ruixing.anzhuangtiaoshi.impl;

import com.yintu.ruixing.anzhuangtiaoshi.*;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuDao;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiXiangMuService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Mr.liu
 * @Date 2020/7/11 10:44
 * @Version 1.0
 * 需求:安装调试模块
 */
@Service
@Transactional
public class AnZhuangTiaoShiXiangMuServiceImpl implements AnZhuangTiaoShiXiangMuService {


    @Autowired
    private AnZhuangTiaoShiXiangMuDao anZhuangTiaoShiXiangMuDao;

    @Autowired
    private AnZhuangTiaoShiCheZhanXiangMuTypeDao anZhuangTiaoShiCheZhanXiangMuTypeDao;

    @Autowired
    private ChanPinJiaoFuXiangMuDao chanPinJiaoFuXiangMuDao;

    @Autowired
    private AnZhuangTiaoShiFileDao anZhuangTiaoShiFileDao;

    @Autowired
    private AnZhuangTiaoShiCheZhanDao anZhuangTiaoShiCheZhanDao;

    @Override
    public List<AnZhuangTiaoShiXiangMuEntity> findLastMonthXiangMu(String today, String lastMothDay) {
        return anZhuangTiaoShiXiangMuDao.findLastMonthXiangMu(today,lastMothDay);
    }

    @Override
    public void exportFile(ServletOutputStream outputStream, Integer[] ids) throws IOException {
        //excel标题
        String title = "安装调试线段列表";
        //excel表名
        String[] headers = {"序号", "线段名称", "项目年份", "车站数量", "项目类型", "机柜到货的数量", "室内卡板到货的数量",
                "室外设备到货的数量", "完成配线的数量", "具备上电条件的数量", "完成静态验收的数量", "完成动态验收的数量",
                "完成联调联试的数量", "完成试运行的数量", "开通的数量"};
        //获取数据
        List<AnZhuangTiaoShiXiangMuEntity> xiangMuEntities = anZhuangTiaoShiXiangMuDao.findXiangMuData(ids);
        xiangMuEntities = xiangMuEntities.stream()
                .sorted(Comparator.comparing(AnZhuangTiaoShiXiangMuEntity::getId).reversed())
                .collect(Collectors.toList());
        //excel元素
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Integer j = 0;
        String[][] content = new String[xiangMuEntities.size()][headers.length];
        for (int i = 0; i < xiangMuEntities.size(); i++) {
            j++;
            AnZhuangTiaoShiXiangMuEntity anZhuangTiaoShiXiangMuEntity = xiangMuEntities.get(i);
            Integer id = anZhuangTiaoShiXiangMuEntity.getId();
            content[i][0] = j.toString();
            content[i][1] = anZhuangTiaoShiXiangMuEntity.getXdName();
            content[i][2] = format.format(anZhuangTiaoShiXiangMuEntity.getXianduantime());
            Integer chezhantotal = anZhuangTiaoShiCheZhanDao.findCheZhanTotal(id);
            content[i][3] = chezhantotal.toString();
            content[i][4] = anZhuangTiaoShiXiangMuEntity.getXdType();
            Integer jiGuitotal = anZhuangTiaoShiCheZhanDao.findJiGuiTotal(id);
            content[i][5] = jiGuitotal.toString();
            Integer indoorKaBantotal = anZhuangTiaoShiCheZhanDao.findIndoorKaBantotal(id);
            content[i][6] = indoorKaBantotal.toString();
            Integer outdoorSheBeitotal = anZhuangTiaoShiCheZhanDao.findOutdoorSheBeiTotal(id);
            content[i][7] = outdoorSheBeitotal.toString();
            Integer peiXiantotal = anZhuangTiaoShiCheZhanDao.findPeiXianTotal(id);
            content[i][8] = peiXiantotal.toString();
            Integer shangDiantotal = anZhuangTiaoShiCheZhanDao.findShangDianTotal(id);
            content[i][9] = shangDiantotal.toString();
            Integer jingTaiYanShoutotal = anZhuangTiaoShiCheZhanDao.findJingTaiYanShouTotal(id);
            content[i][10] = jingTaiYanShoutotal.toString();
            Integer dongTaiYanShoutotal = anZhuangTiaoShiCheZhanDao.findDongTaiYanShouTotal(id);
            content[i][11] = dongTaiYanShoutotal.toString();
            Integer liantiaolianshitotal = anZhuangTiaoShiCheZhanDao.findLianTiaoLianShiTotal(id);
            content[i][12] = liantiaolianshitotal.toString();
            Integer shiyunxingtotal = anZhuangTiaoShiCheZhanDao.findShiYunXingTotal(id);
            content[i][13] = shiyunxingtotal.toString();
            Integer kaitongtotal = anZhuangTiaoShiCheZhanDao.findKaiTongTotal(id);
            content[i][14] = kaitongtotal.toString();
        }
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public Integer findCheZhanTotal(Integer id) {
        return anZhuangTiaoShiCheZhanDao.findCheZhanTotal(id);
    }

    @Override
    public List<AnZhuangTiaoShiXiangMuEntity> findXianDuanBySomedata(Integer page, Integer size, String xdname, String year, String xdtype, Integer xdleixing) {
        List<AnZhuangTiaoShiXiangMuEntity> xiangMuEntities = anZhuangTiaoShiXiangMuDao.findXianDuanBySomedata(xdname, year, xdtype, xdleixing);
        for (AnZhuangTiaoShiXiangMuEntity xiangMuEntity : xiangMuEntities) {
            Integer id = xiangMuEntity.getId();
            Integer chezhantotal = anZhuangTiaoShiCheZhanDao.findCheZhanTotal(id);
            xiangMuEntity.setCheZhanTotal(chezhantotal);
            Integer jiGuitotal = anZhuangTiaoShiCheZhanDao.findJiGuiTotal(id);
            xiangMuEntity.setJiGuiTotal(jiGuitotal);
            Integer indoorKaBantotal = anZhuangTiaoShiCheZhanDao.findIndoorKaBantotal(id);
            xiangMuEntity.setIndoorKaBanTotal(indoorKaBantotal);
            Integer outdoorSheBeitotal = anZhuangTiaoShiCheZhanDao.findOutdoorSheBeiTotal(id);
            xiangMuEntity.setOutdoorSheBeiTotal(outdoorSheBeitotal);
            Integer peiXiantotal = anZhuangTiaoShiCheZhanDao.findPeiXianTotal(id);
            xiangMuEntity.setPeiXianTotal(peiXiantotal);
            Integer shangDiantotal = anZhuangTiaoShiCheZhanDao.findShangDianTotal(id);
            xiangMuEntity.setShangDianTotal(shangDiantotal);
            Integer jingTaiYanShoutotal = anZhuangTiaoShiCheZhanDao.findJingTaiYanShouTotal(id);
            xiangMuEntity.setJingTaiYanShouTotal(jingTaiYanShoutotal);
            Integer dongTaiYanShoutotal = anZhuangTiaoShiCheZhanDao.findDongTaiYanShouTotal(id);
            xiangMuEntity.setDongTaiYanShouTotal(dongTaiYanShoutotal);
            Integer liantiaolianshitotal = anZhuangTiaoShiCheZhanDao.findLianTiaoLianShiTotal(id);
            xiangMuEntity.setLianTiaoLianShiTotal(liantiaolianshitotal);
            Integer shiyunxingtotal = anZhuangTiaoShiCheZhanDao.findShiYunXingTotal(id);
            xiangMuEntity.setShiYunXingTotal(shiyunxingtotal);
            Integer kaitongtotal = anZhuangTiaoShiCheZhanDao.findKaiTongTotal(id);
            xiangMuEntity.setKaiTongTotal(kaitongtotal);
        }
        return xiangMuEntities;

    }


    @Override
    public List<AnZhuangTiaoShiXiangMuEntity> findXianDuanNameAndYear() {
        return anZhuangTiaoShiXiangMuDao.findXianDuanNameAndYear();
    }

    @Override
    public List<AnZhuangTiaoShiXiangMuEntity> findXianDuanDataByLeiXing(Integer leiXingId, Integer page, Integer size) {
        List<AnZhuangTiaoShiXiangMuEntity> xiangMuEntities = anZhuangTiaoShiXiangMuDao.findXianDuanDataByLeiXing(leiXingId);
        for (AnZhuangTiaoShiXiangMuEntity xiangMuEntity : xiangMuEntities) {
            Integer id = xiangMuEntity.getId();
            Integer chezhantotal = anZhuangTiaoShiCheZhanDao.findCheZhanTotal(id);
            xiangMuEntity.setCheZhanTotal(chezhantotal);
            Integer jiGuitotal = anZhuangTiaoShiCheZhanDao.findJiGuiTotal(id);
            xiangMuEntity.setJiGuiTotal(jiGuitotal);
            Integer indoorKaBantotal = anZhuangTiaoShiCheZhanDao.findIndoorKaBantotal(id);
            xiangMuEntity.setIndoorKaBanTotal(indoorKaBantotal);
            Integer outdoorSheBeitotal = anZhuangTiaoShiCheZhanDao.findOutdoorSheBeiTotal(id);
            xiangMuEntity.setOutdoorSheBeiTotal(outdoorSheBeitotal);
            Integer peiXiantotal = anZhuangTiaoShiCheZhanDao.findPeiXianTotal(id);
            xiangMuEntity.setPeiXianTotal(peiXiantotal);
            Integer shangDiantotal = anZhuangTiaoShiCheZhanDao.findShangDianTotal(id);
            xiangMuEntity.setShangDianTotal(shangDiantotal);
            Integer jingTaiYanShoutotal = anZhuangTiaoShiCheZhanDao.findJingTaiYanShouTotal(id);
            xiangMuEntity.setJingTaiYanShouTotal(jingTaiYanShoutotal);
            Integer dongTaiYanShoutotal = anZhuangTiaoShiCheZhanDao.findDongTaiYanShouTotal(id);
            xiangMuEntity.setDongTaiYanShouTotal(dongTaiYanShoutotal);
            Integer liantiaolianshitotal = anZhuangTiaoShiCheZhanDao.findLianTiaoLianShiTotal(id);
            xiangMuEntity.setLianTiaoLianShiTotal(liantiaolianshitotal);
            Integer shiyunxingtotal = anZhuangTiaoShiCheZhanDao.findShiYunXingTotal(id);
            xiangMuEntity.setShiYunXingTotal(shiyunxingtotal);
            Integer kaitongtotal = anZhuangTiaoShiCheZhanDao.findKaiTongTotal(id);
            xiangMuEntity.setKaiTongTotal(kaitongtotal);
        }
        return xiangMuEntities;
    }

    @Override
    public AnZhuangTiaoShiFileEntity findById(Integer id) {
        return anZhuangTiaoShiFileDao.selectByPrimaryKey(id);
    }

    @Override
    public List<TreeNodeUtil> findSanJiShu() {
        List<AnZhuangTiaoShiXiangMuEntity> anZhuangTiaoShiXiangMuEntities = anZhuangTiaoShiXiangMuDao.findSanJiShu();
        List<TreeNodeUtil> treeNodeUtils = new ArrayList<>();
        for (AnZhuangTiaoShiXiangMuEntity anZhuangTiaoShiXiangMuEntity : anZhuangTiaoShiXiangMuEntities) {
            TreeNodeUtil treeNodeUtil = new TreeNodeUtil();
            //第一级
            treeNodeUtil.setId((long) anZhuangTiaoShiXiangMuEntity.getId());
            treeNodeUtil.setLabel(anZhuangTiaoShiXiangMuEntity.getXdFenlei().toString());
            List<AnZhuangTiaoShiXiangMuEntity> anZhuangTiaoShiXiangMuEntities1 = anZhuangTiaoShiXiangMuDao.findDiErJi(anZhuangTiaoShiXiangMuEntity.getXdFenlei());
            List<TreeNodeUtil> treeNodeUtilss = new ArrayList<>();
            for (AnZhuangTiaoShiXiangMuEntity zhuangTiaoShiXiangMuEntity : anZhuangTiaoShiXiangMuEntities1) {
                //第二级
                TreeNodeUtil treeNodeUtil1 = new TreeNodeUtil();
                Map<String, Object> map = new HashMap();
                treeNodeUtil1.setId((long) zhuangTiaoShiXiangMuEntity.getId());
                treeNodeUtil1.setLabel(zhuangTiaoShiXiangMuEntity.getXdName());
                map.put("xiangmu", anZhuangTiaoShiXiangMuDao.findOneXiangMU(zhuangTiaoShiXiangMuEntity.getId()));
                treeNodeUtil1.setLi_attr(map);
                treeNodeUtilss.add(treeNodeUtil1);
                treeNodeUtil.setChildren(treeNodeUtilss);
                //第三级
                List<TreeNodeUtil> treeNodeUtilss2 = new ArrayList<>();
                List<TreeNodeUtil> treeNodeUtilss3 = new ArrayList<>();
                TreeNodeUtil treeNodeUtil2 = new TreeNodeUtil();
                TreeNodeUtil treeNodeUtil3 = new TreeNodeUtil();
                treeNodeUtil2.setId((long) 1);
                treeNodeUtil2.setLabel("输入文件");
                treeNodeUtil2.setChildren(treeNodeUtilss3);
                treeNodeUtil3.setId((long) 2);
                treeNodeUtil3.setLabel("输出文件");
                treeNodeUtil3.setChildren(treeNodeUtilss3);
                treeNodeUtilss2.add(treeNodeUtil2);
                treeNodeUtilss2.add(treeNodeUtil3);
                treeNodeUtil1.setChildren(treeNodeUtilss2);
            }
            treeNodeUtils.add(treeNodeUtil);
        }
        return treeNodeUtils;
    }

    @Override
    public void addSanJiShuXiangMu(AnZhuangTiaoShiXiangMuEntity anZhuangTiaoShiXiangMuEntity) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date xianduantime = anZhuangTiaoShiXiangMuEntity.getXianduantime();
        String format = simpleDateFormat.format(xianduantime);
        Date parse = simpleDateFormat.parse(format);
        anZhuangTiaoShiXiangMuEntity.setXianduantime(parse);
        anZhuangTiaoShiXiangMuDao.addSanJiShuXiangMu(anZhuangTiaoShiXiangMuEntity);
    }

    @Override
    public void editSanJiShu(AnZhuangTiaoShiXiangMuEntity anZhuangTiaoShiXiangMuEntity) {
        anZhuangTiaoShiXiangMuDao.editSanJiShu(anZhuangTiaoShiXiangMuEntity);
    }

    @Override
    public List<AnZhuangTiaoShiCheZhanXiangMuTypeEntity> findAllXiangMuType() {
        return anZhuangTiaoShiCheZhanXiangMuTypeDao.findAllXiangMuType();
    }

    @Override
    public void deletSanJiShuById(Integer id) {
        anZhuangTiaoShiXiangMuDao.deletSanJiShuById(id);
    }

    @Override
    public List<ChanPinJiaoFuXiangMuFileEntity> findXiangMuAndBianHao() {
        return chanPinJiaoFuXiangMuDao.findXiangMuAndBianHao();
    }
}
