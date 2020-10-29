package com.yintu.ruixing.anzhuangtiaoshi.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.anzhuangtiaoshi.*;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuDao;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuXiangMuFileEntity;
import com.yintu.ruixing.common.MessageDao;
import com.yintu.ruixing.common.MessageEntity;
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

    @Autowired
    private AnZhuangTiaoShiXiangMuServiceChooseDao anZhuangTiaoShiXiangMuServiceChooseDao;

    @Autowired
    private AnZhuangTiaoShiXiangMuServiceStatusDao anZhuangTiaoShiXiangMuServiceStatusDao;

    @Autowired
    private AnZhuangTiaoShiXiangMuServiceStatusChooseDao anZhuangTiaoShiXiangMuServiceStatusChooseDao;

    @Autowired
    private MessageDao messageDao;

    @Override
    public List<MessageEntity> findXiaoXi(Integer senderid) {
        Integer type = 3;
        return messageDao.findXiaoXi(senderid, type);
    }

    @Override
    public List<AnZhuangTiaoShiXiangMuEntity> findLastMonthXiangMu(String today, String lastMothDay) {
        return anZhuangTiaoShiXiangMuDao.findLastMonthXiangMu(today, lastMothDay);
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
    public JSONObject findXianDuanBySomedata(Integer num, Integer size, String xdname, String year, String xdtype, Integer xdleixing) {
        JSONObject titleAndData = new JSONObject();
        List<AnZhuangTiaoShiXiangMuServiceStatusEntity> anZhuangTiaoShiXiangMuServiceStatusEntities = anZhuangTiaoShiXiangMuServiceStatusDao.findAllServiceStatus();
        titleAndData.put("title", anZhuangTiaoShiXiangMuServiceStatusEntities);

        Page<Object> page = PageHelper.startPage(num, size);
        List<AnZhuangTiaoShiXiangMuEntity> xiangMuEntities = anZhuangTiaoShiXiangMuDao.findXianDuanBySomedata(xdname, year, xdtype, xdleixing);
        JSONArray ja = new JSONArray();
        for (AnZhuangTiaoShiXiangMuEntity xiangMuEntity : xiangMuEntities) {
            JSONObject jo = (JSONObject) JSONObject.toJSON(xiangMuEntity);
            Long czCount = anZhuangTiaoShiXiangMuServiceChooseDao.countChenzhanByXdId(xiangMuEntity.getXdId());
            jo.put("czCount", czCount);
            for (AnZhuangTiaoShiXiangMuServiceStatusEntity anZhuangTiaoShiXiangMuServiceStatusEntity : anZhuangTiaoShiXiangMuServiceStatusEntities) {
                Integer timeType = anZhuangTiaoShiXiangMuServiceStatusEntity.getTimetype();
                Integer serid = anZhuangTiaoShiXiangMuServiceStatusEntity.getId();
                if (timeType == null) {
                    List<AnZhuangTiaoShiXiangMuServiceStatusChooseEntity> anZhuangTiaoShiXiangMuServiceStatusChooseEntities = anZhuangTiaoShiXiangMuServiceStatusChooseDao.findOneChooseBySidid(serid);
                    JSONArray jsonArray = new JSONArray();
                    for (AnZhuangTiaoShiXiangMuServiceStatusChooseEntity anZhuangTiaoShiXiangMuServiceStatusChooseEntity : anZhuangTiaoShiXiangMuServiceStatusChooseEntities) {
                        Integer choid = anZhuangTiaoShiXiangMuServiceStatusChooseEntity.getId();
                        Long eachMuchSelectCount = anZhuangTiaoShiXiangMuServiceChooseDao.countMuchSelectByXdId(xiangMuEntity.getXdId(), choid);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("name", anZhuangTiaoShiXiangMuServiceStatusChooseEntity.getName());
                        jsonObject.put("count", eachMuchSelectCount);
                        jsonArray.add(jsonObject);
                    }
                    jo.put(serid.toString(), jsonArray);
                } else {
                    Long eachOneSelectCount = anZhuangTiaoShiXiangMuServiceChooseDao.countOneSelectByXdId(xiangMuEntity.getXdId(), serid);
                    jo.put(serid.toString(), eachOneSelectCount);
                }
            }
            ja.add(jo);
        }
        page.clear();
        page.addAll(ja);
        PageInfo<Object> pageInfo = new PageInfo<>(page);
        titleAndData.put("tableData", pageInfo);
        return titleAndData;
    }


    @Override
    public List<AnZhuangTiaoShiXiangMuEntity> findXianDuanNameAndYear() {
        return anZhuangTiaoShiXiangMuDao.findXianDuanNameAndYear();
    }

    @Override
    public List<AnZhuangTiaoShiXiangMuEntity> findXianDuanDataByLeiXing(Integer leiXingId, Integer page, Integer size) {
        List<AnZhuangTiaoShiXiangMuEntity> xiangMuEntities = anZhuangTiaoShiXiangMuDao.findXianDuanDataByLeiXing(leiXingId);
        for (AnZhuangTiaoShiXiangMuEntity xiangMuEntity : xiangMuEntities) {
            List<AnZhuangTiaoShiXiangMuServiceStatusEntity> titleList = new ArrayList<>();
            Integer id = xiangMuEntity.getId();//线段id
            //获取线段下面车站的个数
            Integer chezhantotal = anZhuangTiaoShiXiangMuServiceChooseDao.findCheZhanTotal(id);
            xiangMuEntity.setCheZhanTotal(chezhantotal);
            //查询属性的完成和到货个数
            List<Integer> serId = anZhuangTiaoShiXiangMuServiceChooseDao.findAllSeridByXDid(id);
            if (serId.size() > 1) {
                for (Integer serid : serId) {//5 9 12
                    List<AnZhuangTiaoShiXiangMuServiceStatusChooseEntity> chooseList = new ArrayList<>();
                    List<AnZhuangTiaoShiXiangMuServiceStatusChooseEntity> chooseEntity = anZhuangTiaoShiXiangMuServiceStatusChooseDao.findOneChooseBySidid(serid);
                    if (chooseEntity.size() == 0) {
                        Integer total = anZhuangTiaoShiXiangMuServiceChooseDao.findTitleTotal(serid, id);
                        AnZhuangTiaoShiXiangMuServiceStatusEntity xiangMuServiceStatusEntityy = anZhuangTiaoShiXiangMuServiceStatusDao.selectByPrimaryKey(serid);
                        xiangMuServiceStatusEntityy.setTitleTotal(total);
                        titleList.add(xiangMuServiceStatusEntityy);
                    }
                    if (chooseEntity.size() != 0) {
                        AnZhuangTiaoShiXiangMuServiceStatusEntity xiangMuServiceStatusEntity = null;
                        for (AnZhuangTiaoShiXiangMuServiceStatusChooseEntity statusChooseEntity : chooseEntity) {
                            System.out.println("123" + statusChooseEntity);
                            Integer chooseid = statusChooseEntity.getId();
                            Integer total = anZhuangTiaoShiXiangMuServiceChooseDao.findChooseTotal(chooseid, id);
                            Integer sid = statusChooseEntity.getSid();
                            statusChooseEntity.setChooseTotal(total);
                            xiangMuServiceStatusEntity = anZhuangTiaoShiXiangMuServiceStatusDao.selectByPrimaryKey(sid);
                            chooseList.add(statusChooseEntity);
                            xiangMuServiceStatusEntity.setList(chooseList);
                        }
                        titleList.add(xiangMuServiceStatusEntity);
                    }
                    xiangMuEntity.setTitlelist(titleList);
                    System.out.println("cccccccc" + xiangMuEntity);
                }
            }
        }
        return xiangMuEntities;
    }


     /*List<Integer> choId = anZhuangTiaoShiXiangMuServiceChooseDao.findAllChoidBySerid(serid);
                if (choId.size() == 0 || choId.get(0) == null) {
                    Integer total = anZhuangTiaoShiXiangMuServiceChooseDao.findTitleTotal(serid, id);
                    AnZhuangTiaoShiXiangMuServiceStatusEntity xiangMuServiceStatusEntity = anZhuangTiaoShiXiangMuServiceStatusDao.selectByPrimaryKey(serid);
                    xiangMuServiceStatusEntity.setTitleTotal(total);
                    titleList.add(xiangMuServiceStatusEntity);
                }
                if (choId.size() != 0 && choId.get(0) != null) {
                    for (Integer choid : choId) {
                        Integer total = anZhuangTiaoShiXiangMuServiceChooseDao.findChooseTotal(choid, id);
                        AnZhuangTiaoShiXiangMuServiceStatusChooseEntity xiangMuServiceStatusChooseEntity = anZhuangTiaoShiXiangMuServiceStatusChooseDao.selectByPrimaryKey(choid);
                        xiangMuServiceStatusChooseEntity.setChooseTotal(total);
                        chooseList.add(xiangMuServiceStatusChooseEntity);
                    }
                }*/
    /*Integer jiGuitotal = anZhuangTiaoShiCheZhanDao.findJiGuiTotal(id);
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
            xiangMuEntity.setKaiTongTotal(kaitongtotal);*/


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
            Integer xmid = anZhuangTiaoShiXiangMuEntity.getId();
            treeNodeUtil.setValue(xmid.toString());
            treeNodeUtil.setId((long) anZhuangTiaoShiXiangMuEntity.getId());
            treeNodeUtil.setLabel(anZhuangTiaoShiXiangMuEntity.getXdFenlei().toString());
            List<AnZhuangTiaoShiXiangMuEntity> anZhuangTiaoShiXiangMuEntities1 = anZhuangTiaoShiXiangMuDao.findDiErJi(anZhuangTiaoShiXiangMuEntity.getXdFenlei());
            List<TreeNodeUtil> treeNodeUtilss = new ArrayList<>();
            for (AnZhuangTiaoShiXiangMuEntity zhuangTiaoShiXiangMuEntity : anZhuangTiaoShiXiangMuEntities1) {
                //第二级
                TreeNodeUtil treeNodeUtil1 = new TreeNodeUtil();
                Map<String, Object> map = new HashMap();
                Integer id = zhuangTiaoShiXiangMuEntity.getId() + 1568;
                treeNodeUtil1.setValue(id.toString());
                treeNodeUtil1.setId((long) zhuangTiaoShiXiangMuEntity.getId());
                treeNodeUtil1.setLabel(zhuangTiaoShiXiangMuEntity.getXdName());
                map.put("xiangmu", anZhuangTiaoShiXiangMuDao.findOneXiangMU(zhuangTiaoShiXiangMuEntity.getId()));
                treeNodeUtil1.setLi_attr(map);
                treeNodeUtilss.add(treeNodeUtil1);
                treeNodeUtil.setChildren(treeNodeUtilss);
                //第三级
                Integer idd = id + 2365;
                Integer iddd = id + 1457;
                List<TreeNodeUtil> treeNodeUtilss2 = new ArrayList<>();
                List<TreeNodeUtil> treeNodeUtilss3 = new ArrayList<>();
                TreeNodeUtil treeNodeUtil2 = new TreeNodeUtil();
                TreeNodeUtil treeNodeUtil3 = new TreeNodeUtil();
                treeNodeUtil2.setId((long) 1);
                treeNodeUtil2.setLabel("输入文件");
                treeNodeUtil2.setValue(idd.toString());
                treeNodeUtil2.setChildren(treeNodeUtilss3);
                treeNodeUtil3.setId((long) 2);
                treeNodeUtil3.setLabel("输出文件");
                treeNodeUtil3.setValue(iddd.toString());
                treeNodeUtil3.setChildren(treeNodeUtilss3);
                treeNodeUtilss2.add(treeNodeUtil2);
                treeNodeUtilss2.add(treeNodeUtil3);
                treeNodeUtil1.setChildren(treeNodeUtilss2);
            }
            treeNodeUtils.add(treeNodeUtil);
        }

        Integer a = 0;
        Integer b = 0;
        Integer c = 0;
        for (TreeNodeUtil nodeUtil : treeNodeUtils) {
            if (nodeUtil.getLabel().equals("1")) {
                a++;
            }
            if (nodeUtil.getLabel().equals("2")) {
                b++;
            }
            if (nodeUtil.getLabel().equals("3")) {
                c++;
            }
        }

        if (a == 0) {
            TreeNodeUtil treeNodeUtil1 = new TreeNodeUtil();
            treeNodeUtil1.setLabel("正在进行");
            treeNodeUtils.add(0, treeNodeUtil1);
        }
        if (b == 0) {
            TreeNodeUtil treeNodeUtil2 = new TreeNodeUtil();
            treeNodeUtil2.setLabel("已完成");
            treeNodeUtils.add(1, treeNodeUtil2);
        }
        if (c == 0) {
            TreeNodeUtil treeNodeUtil3 = new TreeNodeUtil();
            treeNodeUtil3.setLabel("长期停滞");
            treeNodeUtils.add(2, treeNodeUtil3);
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
