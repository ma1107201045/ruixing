package com.yintu.ruixing.common.util;

/**
 * @author:mlf
 * @date:2020/6/28 11:12
 */

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

/**
 * 导出报表工具类
 */
public class ExportExcelUtil {
    /**
     * 创建后缀为.xls的表格文件
     *
     * @param title   标题
     * @param headers 表头
     * @param values  表中元素
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String title, String[] headers, String[][] values) {

        //创建一个HSSFWorkbook，对应一个Excel文件
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();

        //在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet hssfSheet = hssfWorkbook.createSheet(title);

        //创建标题合并行
        hssfSheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 0, (short) headers.length - 1));

        //设置标题样式
        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);   //设置居中样式
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置标题字体
        Font titleFont = hssfWorkbook.createFont();
        titleFont.setFontHeightInPoints((short) 14);
        style.setFont(titleFont);

        //设置值表头样式 设置表头居中
        HSSFCellStyle hssfCellStyle = hssfWorkbook.createCellStyle();
        hssfCellStyle.setAlignment(HorizontalAlignment.CENTER);   //设置居中样式
        hssfCellStyle.setBorderBottom(BorderStyle.THIN);
        hssfCellStyle.setBorderLeft(BorderStyle.THIN);
        hssfCellStyle.setBorderRight(BorderStyle.THIN);
        hssfCellStyle.setBorderTop(BorderStyle.THIN);

        //设置表内容样式
        //创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style1 = hssfWorkbook.createCellStyle();
        style1.setBorderBottom(BorderStyle.THIN);
        style1.setBorderLeft(BorderStyle.THIN);
        style1.setBorderRight(BorderStyle.THIN);
        style1.setBorderTop(BorderStyle.THIN);

        //产生标题行
        HSSFRow hssfRow = hssfSheet.createRow(0);
        HSSFCell cell = hssfRow.createCell(0);
        cell.setCellValue(title);
        cell.setCellStyle(style);


        //产生表头
        HSSFRow row1 = hssfSheet.createRow(1);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell hssfCell = row1.createCell(i);
            hssfCell.setCellValue(headers[i]);
            hssfCell.setCellStyle(hssfCellStyle);
        }

        //创建内容
        for (int i = 0; i < values.length; i++) {
            row1 = hssfSheet.createRow(i + 2);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应列对象
                HSSFCell hssfCell = row1.createCell(j);
                hssfCell.setCellValue(values[i][j]);
                hssfCell.setCellStyle(style1);
            }
        }
        return hssfWorkbook;
    }

    /**
     * 创建后缀为.xlsx的表格文件
     *
     * @param title   标题
     * @param headers 表头
     * @param values  表中元素
     * @return
     */
    public static XSSFWorkbook getXSSFWorkbook(String title, String[] headers, String[][] values) {

        //创建一个HSSFWorkbook，对应一个Excel文件
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();

        //在workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet xssfSheet = xssfWorkbook.createSheet(title);
        //自适应列宽度
        xssfSheet.autoSizeColumn(1, true);

        //创建标题合并行
        xssfSheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 0, (short) headers.length - 1));

        //设置标题样式
        XSSFCellStyle style = xssfWorkbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);   //设置居中样式
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置标题字体
        Font titleFont = xssfWorkbook.createFont();
        titleFont.setFontHeightInPoints((short) 14);
        style.setFont(titleFont);

        //设置值表头样式 设置表头居中
        XSSFCellStyle xssfCellStyle = xssfWorkbook.createCellStyle();
        xssfCellStyle.setAlignment(HorizontalAlignment.CENTER);   //设置居中样式
        xssfCellStyle.setBorderBottom(BorderStyle.THIN);
        xssfCellStyle.setBorderLeft(BorderStyle.THIN);
        xssfCellStyle.setBorderRight(BorderStyle.THIN);
        xssfCellStyle.setBorderTop(BorderStyle.THIN);

        //设置表内容样式
        //创建单元格，并设置值表头 设置表头居中
        XSSFCellStyle style1 = xssfWorkbook.createCellStyle();
        style1.setBorderBottom(BorderStyle.THIN);
        style1.setBorderLeft(BorderStyle.THIN);
        style1.setBorderRight(BorderStyle.THIN);
        style1.setBorderTop(BorderStyle.THIN);

        //产生标题行
        XSSFRow xssfRow = xssfSheet.createRow(0);
        XSSFCell cell = xssfRow.createCell(0);
        cell.setCellValue(title);
        cell.setCellStyle(style);


        //产生表头
        XSSFRow row1 = xssfSheet.createRow(1);
        for (int i = 0; i < headers.length; i++) {
            XSSFCell xssfCell = row1.createCell(i);
            xssfCell.setCellValue(headers[i]);
            xssfCell.setCellStyle(xssfCellStyle);
            //设置表头列的宽度
            xssfSheet.setColumnWidth(i, (int) (headers[i].getBytes().length * 1.5 * 256));
        }

        //创建内容
        for (int i = 0; i < values.length; i++) {
            row1 = xssfSheet.createRow(i + 2);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应列对象
                XSSFCell xssfCell = row1.createCell(j);
                xssfCell.setCellValue(values[i][j]);
                xssfCell.setCellStyle(style1);
            }
        }
        return xssfWorkbook;
    }
}
