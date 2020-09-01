package com.yintu.ruixing.jiejuefangan.impl;

import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.jiejuefangan.BiddingEntity;
import com.yintu.ruixing.jiejuefangan.BiddingFileAuditorEntity;
import com.yintu.ruixing.jiejuefangan.BiddingFileDao;
import com.yintu.ruixing.jiejuefangan.BiddingFileEntity;
import com.yintu.ruixing.jiejuefangan.BiddingFileAuditorService;
import com.yintu.ruixing.jiejuefangan.BiddingFileService;
import com.yintu.ruixing.jiejuefangan.BiddingService;
import com.yintu.ruixing.xitongguanli.UserService;
import com.yintu.ruixing.xitongguanli.UserEntity;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author:mlf
 * @date:2020/7/2 14:25
 */
@Service
@Transactional
public class BiddingFileServiceImpl implements BiddingFileService {

    @Autowired
    private BiddingFileDao biddingFileDao;
    @Autowired
    private BiddingService biddingService;
    @Autowired
    private BiddingFileAuditorService biddingFileAuditorService;
    @Autowired
    private UserService userService;

    @Override
    public void add(BiddingFileEntity entity) {
        entity.setUploadDatetime(new Date());
        biddingFileDao.insertSelective(entity);
    }

    @Override
    public void remove(Integer id) {
        biddingFileDao.deleteByPrimaryKey(id);
        biddingFileAuditorService.removeByBiddingFileId(id);
    }

    @Override
    public void edit(BiddingFileEntity entity) {
        biddingFileDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public BiddingFileEntity findById(Integer id) {
        return biddingFileDao.selectByPrimaryKey(id);
    }

    @Override
    public void add(BiddingFileEntity biddingFileEntity, Integer[] auditorIds, String trueName) {
        this.add(biddingFileEntity);
        Integer id = biddingFileEntity.getId();
        if (auditorIds != null) {
            List<BiddingFileAuditorEntity> biddingFileAuditorEntities = new ArrayList<>(auditorIds.length);
            for (Integer auditorId : auditorIds) {
                if (auditorId != null) {
                    BiddingFileAuditorEntity biddingFileAuditorEntity = new BiddingFileAuditorEntity();
                    biddingFileAuditorEntity.setBiddingFileId(id);
                    biddingFileAuditorEntity.setAuditorId(auditorId);
                    biddingFileAuditorEntity.setIsPass((short) 1);
                    biddingFileAuditorEntities.add(biddingFileAuditorEntity);
                }
            }
            if (biddingFileAuditorEntities.size() > 0)
                biddingFileAuditorService.addMuch(biddingFileAuditorEntities);
        }
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
        }
    }

    @Override
    public void edit(BiddingFileEntity biddingFileEntity, Integer[] auditorIds, String trueName) {
        this.edit(biddingFileEntity);
        Integer id = biddingFileEntity.getId();
        biddingFileAuditorService.removeByBiddingFileId(id); //删除
        if (auditorIds != null) {
            List<BiddingFileAuditorEntity> biddingFileAuditorEntities = new ArrayList<>(auditorIds.length);
            for (Integer auditorId : auditorIds) {
                if (auditorId != null) {
                    BiddingFileAuditorEntity biddingFileAuditorEntity = new BiddingFileAuditorEntity();
                    biddingFileAuditorEntity.setBiddingFileId(id);
                    biddingFileAuditorEntity.setAuditorId(auditorId);
                    biddingFileAuditorEntity.setIsPass((short) 0);
                    biddingFileAuditorEntities.add(biddingFileAuditorEntity);
                }
            }
            if (biddingFileAuditorEntities.size() > 0)
                biddingFileAuditorService.addMuch(biddingFileAuditorEntities);
        }
    }

    @Override
    public BiddingFileEntity findBiddingById(Integer id) {
        BiddingFileEntity biddingFileEntity = this.findById(id);
        Integer biddingId = biddingFileEntity.getBiddingId();
        if (biddingId != null) {
            BiddingEntity biddingEntity = biddingService.findById(biddingId);
            biddingFileEntity.setBiddingEntity(biddingEntity);
        }
        List<BiddingFileAuditorEntity> biddingFileAuditorEntities = biddingFileAuditorService.findByBiddingFileIdId(id);
        biddingFileEntity.setBiddingFileAuditorEntities(biddingFileAuditorEntities);
        return biddingFileEntity;
    }

    @Override
    public List<BiddingFileEntity> findByBiddingIdAndNameAndType(Integer biddingId, String name, String type, Integer userId) {
        return biddingFileDao.selectByCondition(biddingId, null, name, type == null || "".equals(type) ? null : "输入文件".equals(type) ? (short) 1 : (short) 2, userId, (short) 2);
    }

    @Override
    public void exportFile(OutputStream outputStream, Integer[] ids, Integer userId) throws IOException {
        //excel标题
        String title = "投招标技术支持列表";
        //excel表名
        String[] headers = {"序号", "年份", "项目名称", "招标人", "项目状态", "任务状态", "文件类型", "文件名称"};
        //获取数据
        List<BiddingFileEntity> biddingFileEntities = biddingFileDao.selectByCondition(null, ids, null, null, userId, (short) 2);
        biddingFileEntities = biddingFileEntities.stream()
                .sorted(Comparator.comparing(BiddingFileEntity::getId).reversed())
                .collect(Collectors.toList());
        //excel元素
        String[][] content = new String[biddingFileEntities.size()][headers.length];
        for (int i = 0; i < biddingFileEntities.size(); i++) {
            BiddingFileEntity biddingFileEntity = biddingFileEntities.get(i);
            BiddingEntity biddingEntity = biddingFileEntity.getBiddingEntity();
            content[i][0] = biddingFileEntity.getId().toString();
            content[i][1] = Integer.valueOf(biddingEntity.getProjectDate().getYear() + 1900).toString();
            content[i][2] = biddingEntity.getProjectName();
            content[i][3] = biddingEntity.getBidder();
            Short projectStatus = biddingEntity.getProjectStatus();
            content[i][4] = projectStatus.equals((short) 1) ? "未知" : projectStatus.equals((short) 2) ? "后续招标" : projectStatus.equals((short) 3) ? "确定采用" : projectStatus.equals((short) 4) ? "关闭" : "未知";
            Short taskStatus = biddingEntity.getTaskStatus();
            content[i][5] = taskStatus.equals((short) 1) ? "正在进行" : taskStatus.equals((short) 2) ? "已完成" : "正在进行";
            Short type = biddingFileEntity.getType();
            content[i][6] = type.equals((short) 1) ? "输入文件" : type.equals((short) 2) ? "输出文件" : "输入文件";
            content[i][7] = biddingFileEntity.getName();
        }
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }


}
