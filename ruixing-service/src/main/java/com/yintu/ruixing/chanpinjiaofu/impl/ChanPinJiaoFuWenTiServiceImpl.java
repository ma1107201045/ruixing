package com.yintu.ruixing.chanpinjiaofu.impl;

import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuWenTiFileEntity;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.master.chanpinjiaofu.ChanPinJiaoFuWenTiDao;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuWenTiEntity;
import com.yintu.ruixing.master.chanpinjiaofu.ChanPinJiaoFuWenTiFileDao;
import com.yintu.ruixing.xitongguanli.DepartmentEntity;
import com.yintu.ruixing.chanpinjiaofu.ChanPinJiaoFuWenTiService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Mr.liu
 * @Date 2020/7/3 20:40
 * @Version 1.0
 * 需求:产品交付的问题反馈
 */
@Service
@Transactional
public class ChanPinJiaoFuWenTiServiceImpl implements ChanPinJiaoFuWenTiService {
    @Autowired
    private ChanPinJiaoFuWenTiDao chanPinJiaoFuWenTiDao;
    @Autowired
    private ChanPinJiaoFuWenTiFileDao chanPinJiaoFuWenTiFileDao;

    @Override
    public List<ChanPinJiaoFuWenTiEntity> findAllNotOverWenTi() {
        return chanPinJiaoFuWenTiDao.findAllNotOverWenTi();
    }

    @Override
    public void deleteWenTiFileByid(Integer id) {
        chanPinJiaoFuWenTiFileDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<ChanPinJiaoFuWenTiFileEntity> findWenTiFileByType(Integer wtid, Integer fileType) {
        return chanPinJiaoFuWenTiFileDao.findWenTiFileByType(wtid, fileType);
    }

    @Override
    public void addWenTiFile(ChanPinJiaoFuWenTiFileEntity chanPinJiaoFuWenTiFileEntity, String username) {
        chanPinJiaoFuWenTiFileEntity.setCreatename(username);
        chanPinJiaoFuWenTiFileEntity.setCreatetime(new Date());
        chanPinJiaoFuWenTiFileDao.insertSelective(chanPinJiaoFuWenTiFileEntity);
        if (chanPinJiaoFuWenTiFileEntity.getFileType() == 2) {
            ChanPinJiaoFuWenTiEntity wenTiEntity = new ChanPinJiaoFuWenTiEntity();
            wenTiEntity.setId(chanPinJiaoFuWenTiFileEntity.getWtid());
            wenTiEntity.setState(1);
            chanPinJiaoFuWenTiDao.editWenTiById(wenTiEntity);
        }
    }

    @Override
    public void exportFile(ServletOutputStream outputStream, Integer[] ids) throws IOException {
        //excel标题
        String title = "交付情况问题反馈列表";
        //excel表名
        String[] headers = {"序号", "项目编号", "项目名称", "问题环节", "存在问题", "配合部门", "需协调的外部单位", "工作计划", "状态更新", "问题状态"};
        //获取数据
        List<ChanPinJiaoFuWenTiEntity> chanPinJiaoFuWenTiEntities = chanPinJiaoFuWenTiDao.selectByCondition(ids);
        chanPinJiaoFuWenTiEntities = chanPinJiaoFuWenTiEntities.stream()
                .sorted(Comparator.comparing(ChanPinJiaoFuWenTiEntity::getId).reversed())
                .collect(Collectors.toList());
        //excel元素
        Integer j = 0;
        String[][] content = new String[chanPinJiaoFuWenTiEntities.size()][headers.length];
        for (int i = 0; i < chanPinJiaoFuWenTiEntities.size(); i++) {
            j++;
            ChanPinJiaoFuWenTiEntity chanPinJiaoFuWenTiEntity = chanPinJiaoFuWenTiEntities.get(i);
            content[i][0] = j.toString();
            content[i][1] = chanPinJiaoFuWenTiEntity.getXiangmuBianhao();
            content[i][2] = chanPinJiaoFuWenTiEntity.getXiangmuName();
            content[i][3] = chanPinJiaoFuWenTiEntity.getWentihuanjie();
            content[i][4] = chanPinJiaoFuWenTiEntity.getCunzaiwenti();
            content[i][5] = chanPinJiaoFuWenTiEntity.getPeihebumen();
            content[i][6] = chanPinJiaoFuWenTiEntity.getWaibudanwei();
            content[i][7] = chanPinJiaoFuWenTiEntity.getJihua();
            if (chanPinJiaoFuWenTiEntity.getState() == 1) {
                content[i][8] = "已更新";
            }
            if (chanPinJiaoFuWenTiEntity.getState() == 2) {
                content[i][8] = "未更新";
            }
            if (chanPinJiaoFuWenTiEntity.getWentiState() == 1) {
                content[i][9] = "打开";
            }
            if (chanPinJiaoFuWenTiEntity.getWentiState() == 2) {
                content[i][9] = "关闭";
            }
        }
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public List<ChanPinJiaoFuWenTiEntity> downLoadByIds(Integer[] ids) {
        return chanPinJiaoFuWenTiDao.downLoadByIds(ids);
    }

    @Override
    public List<ChanPinJiaoFuWenTiEntity> findXiangMuMing() {
        return chanPinJiaoFuWenTiDao.findXiangMuMing();
    }

    @Override
    public void deletWenTiByIds(Integer[] ids) {
        chanPinJiaoFuWenTiDao.deletWenTiByIds(ids);
    }

    @Override
    public List<ChanPinJiaoFuWenTiEntity> findWenTiByName(String xiangMuName, Integer page, Integer size) {
        return chanPinJiaoFuWenTiDao.findWenTiByName(xiangMuName);
    }

    @Override
    public void editWenTiById(ChanPinJiaoFuWenTiEntity chanPinJiaoFuWenTiEntity) {
        chanPinJiaoFuWenTiDao.editWenTiById(chanPinJiaoFuWenTiEntity);
    }

    @Override
    public void addWenTi(ChanPinJiaoFuWenTiEntity chanPinJiaoFuWenTiEntity) {
        chanPinJiaoFuWenTiDao.addWenTi(chanPinJiaoFuWenTiEntity);
    }

    @Override
    public List<DepartmentEntity> findAllDepartment() {
        return chanPinJiaoFuWenTiDao.findAllDepartment();
    }

    @Override
    public List<ChanPinJiaoFuWenTiEntity> findAllData(Integer page, Integer size, String xiangMuNumber, Integer wenTiState) {
        if ( wenTiState != null && wenTiState == 0 ) {
            wenTiState = null;
        }
        return chanPinJiaoFuWenTiDao.findAllData(xiangMuNumber, wenTiState);
    }
}
