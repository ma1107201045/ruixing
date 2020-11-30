package com.yintu.ruixing.anzhuangtiaoshi.impl;

import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.master.anzhuangtiaoshi.AnZhuangTiaoShiCheZhanDao;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiCheZhanEntity;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiCheZhanService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Mr.liu
 * @Date 2020/7/11 16:47
 * @Version 1.0
 * 需求:安装调试的车站
 */
@Service
@Transactional
public class AnZhuangTiaoShiCheZhanServiceImpl implements AnZhuangTiaoShiCheZhanService {

    @Autowired
    private AnZhuangTiaoShiCheZhanDao anZhuangTiaoShiCheZhanDao;

    @Override
    public void exportFile(ServletOutputStream outputStream, Integer[] ids) throws IOException {
        //excel标题
        String title = "安装调试车站信息列表";
        //excel表名
        String[] headers = {"序号", "车站名称", "项目类型",  "机柜是否到货", "室内卡板是否到货", "室外设备是否到货",
                "完成配线的计划开始日期","完成配线的计划结束日期", "完成配线的实际开始日期", "完成配线的实际结束日期",
                "具备上电条件的计划开始日期","具备上电条件的计划结束日期", "具备上电条件的实际开始日期", "具备上电条件的实际结束日期",
                "完成静态验收的计划开始日期","完成静态验收的计划结束日期", "完成静态验收的实际开始日期", "完成静态验收的实际结束日期",
                "完成动态验收的计划开始日期","完成动态验收的计划结束日期", "完成动态验收的实际开始日期", "完成动态验收的实际结束日期",
                "完成联调联试的计划开始日期","完成联调联试的计划结束日期", "完成联调联试的实际开始日期", "完成联调联试的实际结束日期",
                "完成试运行的计划开始日期","完成试运行的计划结束日期", "完成试运行的实际开始日期", "完成试运行的实际结束日期",
                "开通的计划开始日期","开通的计划结束日期", "开通的实际开始日期", "开通的实际结束日期"};
        //获取数据
        List<AnZhuangTiaoShiCheZhanEntity> cheZhanEntities=anZhuangTiaoShiCheZhanDao.findCheZhanData(ids);
        cheZhanEntities=cheZhanEntities.stream()
                .sorted(Comparator.comparing(AnZhuangTiaoShiCheZhanEntity::getId).reversed())
                .collect(Collectors.toList());
        //excel元素
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Integer j = 0;
        String[][] content = new String[cheZhanEntities.size()][headers.length];
        for (int i = 0; i < cheZhanEntities.size(); i++) {
            j++;
            AnZhuangTiaoShiCheZhanEntity cheZhanEntity=cheZhanEntities.get(i);
            content[i][1] = j.toString();
            content[i][2] = cheZhanEntity.getCzName();
            content[i][3] = cheZhanEntity.getCzType();
            if (cheZhanEntity.getJigui()==1){
                content[i][4] = "已到货";
            }
            if (cheZhanEntity.getJigui()==0){
                content[i][4] = "暂未到货";
            }
            if (cheZhanEntity.getIndoorkaban()==1){
                content[i][5] = "已到货";
            }
            if (cheZhanEntity.getIndoorkaban()==0){
                content[i][5] = "暂未到货";
            }
            if (cheZhanEntity.getOutdoorshebei()==1){
                content[i][6] = "已到货";
            }
            if (cheZhanEntity.getOutdoorshebei()==0){
                content[i][6] = "暂未到货";
            }
            content[i][7] = format.format(cheZhanEntity.getWanchengpeixianPlanStartTime());
            content[i][8] = format.format(cheZhanEntity.getWanchengpeixianPlanEndTime());
            if (cheZhanEntity.getWanchengpeixianActualStartTime()==null ||cheZhanEntity.getWanchengpeixianActualStartTime().toString().equals("")){
                content[i][9] = "实际开始时间待编辑";
            }else {
                content[i][9] = format.format(cheZhanEntity.getWanchengpeixianActualStartTime());
            }
            if (cheZhanEntity.getWanchengpeixianActualEndTime()==null){
                content[i][10] = "实际结束时间待编辑";
            }else {
                content[i][10] = format.format(cheZhanEntity.getWanchengpeixianActualEndTime());
            }
            content[i][11] =format.format(cheZhanEntity.getShangdiantiaojianPlanStartTime());
            content[i][12] = format.format(cheZhanEntity.getShangdiantiaojianPlanEndTime());

            if (cheZhanEntity.getShangdiantiaojianActualStartTime()==null){
                content[i][13] = "实际开始时间待编辑";
            }else {
                content[i][13] = format.format(cheZhanEntity.getShangdiantiaojianActualStartTime());
            }
            if (cheZhanEntity.getShangdiantiaojianActualEndTime()==null){
                content[i][14] = "实际结束时间待编辑";
            }else {
                content[i][14] = format.format(cheZhanEntity.getShangdiantiaojianActualEndTime());
            }

            content[i][15] = format.format(cheZhanEntity.getJingtaiyanshouPlanStartTime());
            content[i][16] = format.format(cheZhanEntity.getJingtaiyanshouPlanEndTime());
            if (cheZhanEntity.getJingtaiyanshouActualStartTime()==null){
                content[i][17] = "实际开始时间待编辑";
            }else {
                content[i][17] = format.format(cheZhanEntity.getJingtaiyanshouActualStartTime());
            }
            if (cheZhanEntity.getJingtaiyanshouActualEndTime()==null){
                content[i][18] = "实际结束时间待编辑";
            }else {
                content[i][18] = format.format(cheZhanEntity.getJingtaiyanshouActualEndTime());
            }

            content[i][19] = format.format(cheZhanEntity.getDongtaiyanshouPlanStartTime());
            content[i][20] = format.format(cheZhanEntity.getDongtaiyanshouPlanEndTime());
            if (cheZhanEntity.getDongtaiyanshouActualStartTime()==null){
                content[i][21] = "实际开始时间待编辑";
            }else {
                content[i][21] = format.format(cheZhanEntity.getDongtaiyanshouActualStartTime());
            }
            if (cheZhanEntity.getDongtaiyanshouActualEndTime()==null){
                content[i][22] = "实际结束时间待编辑";
            }else {
                content[i][22] = format.format(cheZhanEntity.getDongtaiyanshouActualEndTime());
            }

            content[i][23] = format.format(cheZhanEntity.getLiantiaolianshiPlanStartTime());
            content[i][24] = format.format(cheZhanEntity.getLiantiaolianshiPlanEndTime());
            if (cheZhanEntity.getLiantiaolianshiActualStartTime()==null){
                content[i][25] = "实际开始时间待编辑";
            }else {
                content[i][25] = format.format(cheZhanEntity.getLiantiaolianshiActualStartTime());
            }
            if (cheZhanEntity.getLiantiaolianshiActualEndTime()==null){
                content[i][26] = "实际结束时间待编辑";
            }else {
                content[i][26] = format.format(cheZhanEntity.getLiantiaolianshiActualEndTime());
            }

            content[i][27] = format.format(cheZhanEntity.getShiyunxingPlanStartTime());
            content[i][28] = format.format(cheZhanEntity.getShiyunxingPlanEndTime());
            if (cheZhanEntity.getShiyunxingActualStartTime()==null){
                content[i][29] = "实际开始时间待编辑";
            }else {
                content[i][29] = format.format(cheZhanEntity.getShiyunxingActualStartTime());
            }
            if (cheZhanEntity.getShiyunxingActualEndTime()==null){
                content[i][30] = "实际结束时间待编辑";
            }else {
                content[i][30] = format.format(cheZhanEntity.getShiyunxingActualEndTime());
            }

            content[i][31] = format.format(cheZhanEntity.getKaitongPlanStartTime());
            content[i][32] = format.format(cheZhanEntity.getKaitongPlanEndTime());
            if (cheZhanEntity.getKaitongActualStartTime()==null){
                content[i][33] = "实际开始时间待编辑";
            }else {
                content[i][33] = format.format(cheZhanEntity.getKaitongActualStartTime());
            }
            if (cheZhanEntity.getKaitongActualEndTime()==null){
                content[i][34] = "实际结束时间待编辑";
            }else {
                content[i][34] = format.format(cheZhanEntity.getKaitongActualEndTime());
            }
        }
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public List<AnZhuangTiaoShiCheZhanEntity> findCheZhanByName(String czname, Integer page, Integer size) {
        return anZhuangTiaoShiCheZhanDao.findCheZhanByName(czname);
    }

    @Override
    public void deleteCheZhanByIds(Integer[] ids) {
        anZhuangTiaoShiCheZhanDao.deleteCheZhanByIds(ids);
    }

    @Override
    public void editCheZhanById(AnZhuangTiaoShiCheZhanEntity anZhuangTiaoShiCheZhanEntity) {

        anZhuangTiaoShiCheZhanDao.editCheZhanById(anZhuangTiaoShiCheZhanEntity);
    }

    @Override
    public List<AnZhuangTiaoShiCheZhanEntity> findCheZhanById(Integer id, Integer page, Integer size) {
        List<AnZhuangTiaoShiCheZhanEntity> cheZhanEntities=anZhuangTiaoShiCheZhanDao.findCheZhanById(id);
       /* //获取当天的日期
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM.dd");
        String today = simpleDateFormat.format( new Date() );*/

        for (AnZhuangTiaoShiCheZhanEntity cheZhanEntity : cheZhanEntities) {
            if (cheZhanEntity.getWanchengpeixianPlanStartTime()!=null){
                Date wanchengpeixianPlanStartTime = cheZhanEntity.getWanchengpeixianPlanStartTime();
                Date wanchengpeixianPlanEndTime = cheZhanEntity.getWanchengpeixianPlanEndTime();
                int wanchengpeixianPlanToalTime=(int) ((wanchengpeixianPlanEndTime.getTime()-wanchengpeixianPlanStartTime.getTime())/(1000*3600*24))+1;
                int wanchengpeixianPlanOneTime=(int) ((new Date().getTime()-wanchengpeixianPlanStartTime.getTime())/(1000*3600*24))+1;
                cheZhanEntity.setWanchengpeixianPlanToalTime(wanchengpeixianPlanToalTime);
                cheZhanEntity.setWanchengpeixianPlanOneTime(wanchengpeixianPlanOneTime);
            }
            if (cheZhanEntity.getShangdiantiaojianPlanStartTime() !=null){
                Date shangdiantiaojianPlanStartTime = cheZhanEntity.getShangdiantiaojianPlanStartTime();
                Date shangdiantiaojianPlanEndTime = cheZhanEntity.getShangdiantiaojianPlanEndTime();
                int shangdiantiaojianPlanToalTime = (int) ((shangdiantiaojianPlanEndTime.getTime() - shangdiantiaojianPlanStartTime.getTime()) / (1000*3600*24))+1;
                int shangdiantiaojianPlanOneTime = (int) ((new Date().getTime() - shangdiantiaojianPlanStartTime.getTime()) / (1000*3600*24))+1;
                cheZhanEntity.setShangdiantiaojianPlanToalTime(shangdiantiaojianPlanToalTime);
                cheZhanEntity.setShangdiantiaojianPlanOneTime(shangdiantiaojianPlanOneTime);
            }
            if (cheZhanEntity.getJingtaiyanshouPlanStartTime()!=null){
                Date jingtaiyanshouPlanStartTime = cheZhanEntity.getJingtaiyanshouPlanStartTime();
                Date jingtaiyanshouPlanEndTime = cheZhanEntity.getJingtaiyanshouPlanEndTime();
                int jingtaiyanshouPlanToalTime=(int) ((jingtaiyanshouPlanEndTime.getTime()-jingtaiyanshouPlanStartTime.getTime())/(1000*3600*24))+1;
                int jingtaiyanshouPlanOneTime=(int) ((new Date().getTime()-jingtaiyanshouPlanStartTime.getTime())/(1000*3600*24))+1;
                cheZhanEntity.setJingtaiyanshouPlanToalTime(jingtaiyanshouPlanToalTime);
                cheZhanEntity.setJingtaiyanshouPlanOneTime(jingtaiyanshouPlanOneTime);
            }
            if (cheZhanEntity.getDongtaiyanshouPlanStartTime()!=null){
                Date dongtaiyanshouPlanStartTime = cheZhanEntity.getDongtaiyanshouPlanStartTime();
                Date dongtaiyanshouPlanEndTime = cheZhanEntity.getDongtaiyanshouPlanEndTime();
                int dongtaiyanshouPlanToalTime=(int) ((dongtaiyanshouPlanEndTime.getTime()-dongtaiyanshouPlanStartTime.getTime())/(1000*3600*24))+1;
                int dongtaiyanshouPlanOneTime=(int) ((new Date().getTime()-dongtaiyanshouPlanStartTime.getTime())/(1000*3600*24))+1;
                cheZhanEntity.setDongtaiyanshouPlanToalTime(dongtaiyanshouPlanToalTime);
                cheZhanEntity.setDongtaiyanshouPlanOneTime(dongtaiyanshouPlanOneTime);
            }
            if (cheZhanEntity.getLiantiaolianshiPlanStartTime()!=null){
                Date liantiaolianshiPlanStartTime = cheZhanEntity.getLiantiaolianshiPlanStartTime();
                Date liantiaolianshiPlanEndTime = cheZhanEntity.getLiantiaolianshiPlanEndTime();
                int liantiaolianshiPlanToalTime=(int) ((liantiaolianshiPlanEndTime.getTime()-liantiaolianshiPlanStartTime.getTime())/(1000*3600*24))+1;
                int liantiaolianshiPlanOneTime=(int) ((new Date().getTime()-liantiaolianshiPlanStartTime.getTime())/(1000*3600*24))+1;
                cheZhanEntity.setLiantiaolianshiPlanToalTime(liantiaolianshiPlanToalTime);
                cheZhanEntity.setLiantiaolianshiPlanOneTime(liantiaolianshiPlanOneTime);
            }
            if (cheZhanEntity.getShiyunxingPlanStartTime()!=null){
                Date shiyunxingPlanStartTime = cheZhanEntity.getShiyunxingPlanStartTime();
                Date shiyunxingPlanEndTime = cheZhanEntity.getShiyunxingPlanEndTime();
                int shiyunxingPlanToalTime=(int) ((shiyunxingPlanEndTime.getTime()-shiyunxingPlanStartTime.getTime())/(1000*3600*24))+1;
                int shiyunxingPlanOneTime=(int) ((new Date().getTime()-shiyunxingPlanStartTime.getTime())/(1000*3600*24))+1;
                cheZhanEntity.setShiyunxingPlanToalTime(shiyunxingPlanToalTime);
                cheZhanEntity.setShiyunxingPlanOneTime(shiyunxingPlanOneTime);
            }
            if (cheZhanEntity.getKaitongPlanStartTime()!=null){
                Date kaitongPlanStartTime = cheZhanEntity.getKaitongPlanStartTime();
                Date kaitongPlanEndTime = cheZhanEntity.getKaitongPlanEndTime();
                int kaitongPlanToalTime=(int) ((kaitongPlanEndTime.getTime()-kaitongPlanStartTime.getTime())/(1000*3600*24))+1;
                int kaitongPlanOneTime=(int) ((new Date().getTime()-kaitongPlanStartTime.getTime())/(1000*3600*24))+1;
                cheZhanEntity.setKaitongPlanToalTime(kaitongPlanToalTime);
                cheZhanEntity.setKaitongPlanOneTime(kaitongPlanOneTime);
            }
        }
        return cheZhanEntities;
    }

    @Override
    public void addCheZhan(AnZhuangTiaoShiCheZhanEntity anZhuangTiaoShiCheZhanEntity) {
        anZhuangTiaoShiCheZhanEntity.setDongtaiyanshouIsNo(0);
        anZhuangTiaoShiCheZhanEntity.setJingtaiyanshouIsNo(0);
        anZhuangTiaoShiCheZhanEntity.setKaitongIsNo(0);
        anZhuangTiaoShiCheZhanEntity.setShiyunxingIsNo(0);
        anZhuangTiaoShiCheZhanEntity.setLiantiaolianshiIsNo(0);
        anZhuangTiaoShiCheZhanEntity.setShangdiantiaojianIsNo(0);
        anZhuangTiaoShiCheZhanEntity.setWanchengpeixianIsNo(0);


        /*anZhuangTiaoShiCheZhanEntity.setDongtaiyanshouActualEndTime(new Date());
        anZhuangTiaoShiCheZhanEntity.setDongtaiyanshouActualStartTime(new Date());
        anZhuangTiaoShiCheZhanEntity.setJingtaiyanshouActualEndTime(new Date());
        anZhuangTiaoShiCheZhanEntity.setJingtaiyanshouActualStartTime(new Date());
        anZhuangTiaoShiCheZhanEntity.setKaitongActualEndTime(new Date());
        anZhuangTiaoShiCheZhanEntity.setKaitongActualStartTime(new Date());
        anZhuangTiaoShiCheZhanEntity.setLiantiaolianshiActualEndTime(new Date());
        anZhuangTiaoShiCheZhanEntity.setLiantiaolianshiActualStartTime(new Date());
        anZhuangTiaoShiCheZhanEntity.setShangdiantiaojianActualEndTime(new Date());
        anZhuangTiaoShiCheZhanEntity.setShangdiantiaojianActualStartTime(new Date());
        anZhuangTiaoShiCheZhanEntity.setShiyunxingActualEndTime(new Date());
        anZhuangTiaoShiCheZhanEntity.setShiyunxingActualStartTime(new Date());
        anZhuangTiaoShiCheZhanEntity.setWanchengpeixianActualEndTime(new Date());
        anZhuangTiaoShiCheZhanEntity.setWanchengpeixianActualStartTime(new Date());*/



        anZhuangTiaoShiCheZhanDao.addCheZhan(anZhuangTiaoShiCheZhanEntity);
    }
}
