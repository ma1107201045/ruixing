package com.yintu.ruixing.chanpinjiaofu;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.SessionController;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuWenTiEntity;
import com.yintu.ruixing.xitongguanli.DepartmentEntity;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuWenTiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author Mr.liu
 * @Date 2020/7/3 20:39
 * @Version 1.0
 * 需求:产品交付的问题反馈
 */
@RestController
@RequestMapping("/chanPinJiaoFuWenTiAll")
public class ChanPinJiaoFuWenTiController extends SessionController {
    @Autowired
    private ChanPinJiaoFuWenTiService chanPinJiaoFuWenTiService;


    //查询所有的数据
    @GetMapping("/findAllData")
    public Map<String, Object> findAllData(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<ChanPinJiaoFuWenTiEntity> chanPinJiaoFuWenTiEntities = chanPinJiaoFuWenTiService.findAllData(page, size);
        PageInfo<ChanPinJiaoFuWenTiEntity> pageInfo = new PageInfo<>(chanPinJiaoFuWenTiEntities);
        return ResponseDataUtil.ok("查询数据成功", pageInfo);
    }

    //查询所有的部门
    @GetMapping("/findAllDepartment")
    public Map<String, Object> findAllDepartment() {
        List<DepartmentEntity> departmentEntities = chanPinJiaoFuWenTiService.findAllDepartment();
        return ResponseDataUtil.ok("查询部门成功", departmentEntities);
    }

    //查询符合条件的项目名
    @GetMapping("/findXiangMuMing")
    public Map<String, Object> findXiangMuMing() {
        List<ChanPinJiaoFuWenTiEntity> chanPinJiaoFuWenTiEntities = chanPinJiaoFuWenTiService.findXiangMuMing();
        return ResponseDataUtil.ok("查询项目名字成功", chanPinJiaoFuWenTiEntities);
    }

    //新增问题
    @PostMapping("/addWenTi")
    public Map<String, Object> addWenTi(ChanPinJiaoFuWenTiEntity chanPinJiaoFuWenTiEntity) {
        chanPinJiaoFuWenTiService.addWenTi(chanPinJiaoFuWenTiEntity);
        return ResponseDataUtil.ok("新增问题成功");
    }

    //根据id  编辑问题
    @PutMapping("/editWenTiById/{id}")
    public Map<String, Object> editWenTiById(@PathVariable Integer id, ChanPinJiaoFuWenTiEntity chanPinJiaoFuWenTiEntity) {
        chanPinJiaoFuWenTiService.editWenTiById(chanPinJiaoFuWenTiEntity);
        return ResponseDataUtil.ok("编辑问题成功");
    }

    //根据项目名称  进行模糊查询
    @GetMapping("/findWenTiByName")
    public Map<String, Object> findWenTiByName(String xiangMuName, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<ChanPinJiaoFuWenTiEntity> chanPinJiaoFuWenTiEntities = chanPinJiaoFuWenTiService.findWenTiByName(xiangMuName, page, size);
        PageInfo<ChanPinJiaoFuWenTiEntity> pageInfo = new PageInfo<>(chanPinJiaoFuWenTiEntities);
        return ResponseDataUtil.ok("查询成功", pageInfo);
    }

    //根据id  进行单个或者批量删除
    @DeleteMapping("/deletWenTiByIds/{ids}")
    public Map<String, Object> deletWenTiByIds(@PathVariable Integer[] ids) {
        chanPinJiaoFuWenTiService.deletWenTiByIds(ids);
        return ResponseDataUtil.ok("删除数据成功");
    }

    //根据id 进行单个或者批量下载到Excel
    @GetMapping("/downLoadByIds/{ids}")
    public void exportFile(@PathVariable Integer[] ids, HttpServletResponse response) throws IOException {
        String fileName = "交付情况问题反馈列表" + System.currentTimeMillis() + ".xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        chanPinJiaoFuWenTiService.exportFile(response.getOutputStream(), ids);
    }

    //新增文件
    @PostMapping("/addWenTiFile")
    public Map<String, Object> addWenTiFile(ChanPinJiaoFuWenTiFileEntity chanPinJiaoFuWenTiFileEntity) {
        String username = this.getLoginUser().getTrueName();
        chanPinJiaoFuWenTiService.addWenTiFile(chanPinJiaoFuWenTiFileEntity, username);
        return ResponseDataUtil.ok("新增文件成功");
    }

    //查看文件
    @GetMapping("findWenTiFileByType")
    public Map<String,Object>findWenTiFileByType(Integer fileType){
        List<ChanPinJiaoFuWenTiFileEntity> wenTiFileEntityList=chanPinJiaoFuWenTiService.findWenTiFileByType(fileType);
        return ResponseDataUtil.ok("查询文件成功",wenTiFileEntityList);
    }
    /*//根据id 进行单个或者批量下载到Excel

    @GetMapping("/downLoadByIds/{ids}")
    public void downLoadByIds(@PathVariable Integer[] ids, HttpServletResponse response) throws IOException{
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("交付情况问题反馈");
        List<ChanPinJiaoFuWenTiEntity> wenTiEntityList=chanPinJiaoFuWenTiService.downLoadByIds(ids);
        String fileName = "WenTiBiao" + ".xls";//设置要导出的文件的名字
        //新增数据行，并且设置单元格数据
        int rowNum = 1;
        String[] headers = {"序号", "项目名称", "问题环节", "存在问题", "配合部门", "需协调的外部单位", "工作计划", "状态更新","问题状态"};
        //headers表示excel表中第一行的表头
        HSSFRow row = sheet.createRow(0);
        //在excel表中添加表头
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
            // cell.setCellStyle(style);
        }
        //在表中存放查询到的数据放入对应的列
        int j = 0;
        for (ChanPinJiaoFuWenTiEntity wenTiEntity : wenTiEntityList) {
            HSSFRow row1 = sheet.createRow(rowNum);
            j++;
            row1.createCell(0).setCellValue(j);
            row1.createCell(1).setCellValue(wenTiEntity.getXiangmuName());
            row1.createCell(2).setCellValue(wenTiEntity.getWentihuanjie());
            row1.createCell(3).setCellValue(wenTiEntity.getCunzaiwenti());
            row1.createCell(4).setCellValue(wenTiEntity.getPeihebumen());
            row1.createCell(5).setCellValue(wenTiEntity.getWaibudanwei());
            row1.createCell(6).setCellValue(wenTiEntity.getJihua());
            if (wenTiEntity.getState()==1){
                row1.createCell(7).setCellValue("已更新");
            }
            if(wenTiEntity.getState()==2){
                row1.createCell(7).setCellValue("未更新");
            }
            if (wenTiEntity.getWentiState()==1){
                row1.createCell(8).setCellValue("打开");
            }
            if (wenTiEntity.getWentiState()==2){
                row1.createCell(8).setCellValue("关闭");
            }
            rowNum++;
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }*/

}