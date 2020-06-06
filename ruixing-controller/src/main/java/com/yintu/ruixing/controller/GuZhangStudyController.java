package com.yintu.ruixing.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.util.ColumnTitleMap;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.entity.*;
import com.yintu.ruixing.service.GuZhangStudyService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author:lcy
 * @date:2020-06-04 16
 * 故障学习
 */
@RestController
@RequestMapping("/guzhangstudy")
public class GuZhangStudyController {
    @Autowired
    private GuZhangStudyService guZhangStudyService;

    //查询所有的数据并分页
    @GetMapping("/findGuZhangList")
    public Map<String, Object> findGuZhangList(@RequestParam Integer page, @RequestParam Integer size) {
        JSONObject js = new JSONObject();
        List<GuZhangStudyEntity> guZhangStudyEntity = guZhangStudyService.findGuZhangList(page, size);
        js.put("guZhangStudyEntity", guZhangStudyEntity);
        PageHelper.startPage(page, size);
        List<GuZhangStudyEntity> guZhangList = guZhangStudyService.findGuZhangList(page, size);
        PageInfo<GuZhangStudyEntity> pageInfo = new PageInfo<GuZhangStudyEntity>(guZhangList);
        js.put("pageInfo", pageInfo);
        return ResponseDataUtil.ok("查询所有信息成功", js);
    }

    //根据id查询对应的数据
    @GetMapping("/findGuZhangById/{id}")
    public Map<String, Object> findGuZhangById(@PathVariable Long id) {
        GuZhangStudyEntity guZhangStudyEntity = guZhangStudyService.findGuZhangById(id);
        return ResponseDataUtil.ok("查询本条信息成功", guZhangStudyEntity);
    }

    //添加数据
    @PostMapping("/addGuZhang")
    public Map<String, Object> addGuZhang(GuZhangStudyEntity guZhangStudyEntity) {
        guZhangStudyService.addGuZhang(guZhangStudyEntity);
        return ResponseDataUtil.ok("耶，添加成功");
    }

    //根据id修改数据
    @PutMapping("/editGuZhang/{id}")
    public Map<String, Object> editGuZhang(@PathVariable Long id) {
        guZhangStudyService.editGuZhang(id);
        return ResponseDataUtil.ok("修改数据成功");
    }

    //根据id删除对应的数据
    @DeleteMapping("/deletGuZhang/{id}")
    public Map<String, Object> deletGuZhang(@PathVariable Long id) {
        guZhangStudyService.deletGuZhang(id);
        return ResponseDataUtil.ok("删除数据成功");
    }

    //根据id数组 批量删除数据
    @DeleteMapping("/deletGuZhangList/{ids}")
    public Map<String, Object> deletGuZhangList(@PathVariable int[] ids) {
        guZhangStudyService.deletGuZhangList(ids);
        return ResponseDataUtil.ok("批量删除数据成功");
    }

    //查询线段信息
    @GetMapping("/getXianDuan")
    public Map<String, Object> getXianDuan(XianDuanEntity xianDuanEntity) {
        List<XianDuanEntity> xianDuanEntities = guZhangStudyService.getXianDuan(xianDuanEntity);
        return ResponseDataUtil.ok("查询线段成功", xianDuanEntities);
    }

    //根据线段的id查询对应的车站
    @GetMapping("/getCheZhanByXid/{xid}")
    public Map<String,Object>getCheZhanByXid(@PathVariable Long xid){
        List<CheZhanEntity> cheZhanEntity=guZhangStudyService.getCheZhanByXid(xid);
        return ResponseDataUtil.ok("查询对应车站信息成功",cheZhanEntity);
    }

    //根据车站的cid查询对应的区段
    @GetMapping("/getQuDuanByCid/{cid}")
    public Map<String,Object>getQuDuanByXid(@PathVariable Long cid){
        List<QuDuanBaseEntity> quDuanBaseEntities=guZhangStudyService.getQuDuanByXid(cid);
        return ResponseDataUtil.ok("查询对应区段信息成功",quDuanBaseEntities);
    }


    //下载所有数据到Excel中
    @GetMapping("/GuZhangListExcelDownloads")
    public void GuZhangListExcelDownloads(HttpServletResponse response) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
       /* // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
         font.setFontHeightInPoints((short)10);
        // 字体加粗
        // font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
       // style.setBorderBottom(new HSSFCellStyle().getBorderBottomEnum().BORDER_THIN);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框;
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框;
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框;
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);*/

        HSSFSheet sheet = workbook.createSheet("故障库学习表");
        List<GuZhangStudyEntity> guzhangList = guZhangStudyService.GuZhangListExcelDownloads();
        String fileName = "guzhang" + ".xls";//设置要导出的文件的名字
        //新增数据行，并且设置单元格数据
        int rowNum = 1;
        String[] headers = {"序号", "故障记录日期", "故障学习记录名称", "故障类型", "故障简要描述", "故障发生车站", "发生日期和时刻", "神经网络代号"};
        //headers表示excel表中第一行的表头
        HSSFRow row = sheet.createRow(0);
        //在excel表中添加表头
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
            // cell.setCellStyle(style);
        }
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //在表中存放查询到的数据放入对应的列
        int j = 0;
        for (GuZhangStudyEntity guZhangStudyEntity : guzhangList) {
            HSSFRow row1 = sheet.createRow(rowNum);
            // System.out.println("hahahahah"+row1.getCell(1));
            // row1.createCell("1",style).setCellValue();
            //row1.setRowStyle(style);
            j++;
            row1.createCell(0).setCellValue(j);
            row1.createCell(1).setCellValue(formatter1.format(guZhangStudyEntity.getGuzhangjiluTime()));
            row1.createCell(2).setCellValue(guZhangStudyEntity.getGuzhangjiluName());
            row1.createCell(3).setCellValue(guZhangStudyEntity.getGuzhangType());
            row1.createCell(4).setCellValue(guZhangStudyEntity.getGuzhangmiaoshu());
            row1.createCell(5).setCellValue(guZhangStudyEntity.getGuzhangChezhan());
            row1.createCell(6).setCellValue(formatter2.format(guZhangStudyEntity.getGuzhangStartTime()));
            row1.createCell(7).setCellValue(guZhangStudyEntity.getShenjingwangluoCode());
            rowNum++;
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }

    //根据id下载对应数据到Excel中
    @GetMapping("/GuZhangListExcelDownloadsById/{id}")
    public void GuZhangListExcelDownloadsById(@PathVariable Long id, HttpServletResponse response) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("故障库学习表");
        List<GuZhangStudyEntity> guzhangList = guZhangStudyService.GuZhangListExcelDownloadsById(id);
        String fileName = "guzhang" + ".xls";//设置要导出的文件的名字
        //新增数据行，并且设置单元格数据
        int rowNum = 1;
        String[] headers = {"序号", "故障记录日期", "故障学习记录名称", "故障类型", "故障简要描述", "故障发生车站", "发生日期和时刻", "神经网络代号"};
        //headers表示excel表中第一行的表头
        HSSFRow row = sheet.createRow(0);
        //在excel表中添加表头
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
            // cell.setCellStyle(style);
        }
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //在表中存放查询到的数据放入对应的列
        int j = 0;
        for (GuZhangStudyEntity guZhangStudyEntity : guzhangList) {
            HSSFRow row1 = sheet.createRow(rowNum);
            // System.out.println("hahahahah"+row1.getCell(1));
            // row1.createCell("1",style).setCellValue();
            //row1.setRowStyle(style);
            j++;
            row1.createCell(0).setCellValue(j);
            row1.createCell(1).setCellValue(formatter1.format(guZhangStudyEntity.getGuzhangjiluTime()));
            row1.createCell(2).setCellValue(guZhangStudyEntity.getGuzhangjiluName());
            row1.createCell(3).setCellValue(guZhangStudyEntity.getGuzhangType());
            row1.createCell(4).setCellValue(guZhangStudyEntity.getGuzhangmiaoshu());
            row1.createCell(5).setCellValue(guZhangStudyEntity.getGuzhangChezhan());
            row1.createCell(6).setCellValue(formatter2.format(guZhangStudyEntity.getGuzhangStartTime()));
            row1.createCell(7).setCellValue(guZhangStudyEntity.getShenjingwangluoCode());
            rowNum++;
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }

    //故障库查看
    @GetMapping("/findGuZhangKuData/{id}")
    public Map<String,Object>findGuZhangKuData(@PathVariable Long id){
        List<QuDuanInfoEntity> quDuanInfoEntityList=guZhangStudyService.findGuZhangKuData(id);
        return ResponseDataUtil.ok("查询数据成功",quDuanInfoEntityList);
    }


}
