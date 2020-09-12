package com.yintu.ruixing.yunxingweihu.impl;

import cn.hutool.core.date.DateUtil;
import com.yintu.ruixing.common.ScheduleJobEntity;
import com.yintu.ruixing.common.ScheduleJobService;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.common.util.FileUtil;
import com.yintu.ruixing.common.util.ImportExcelUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
import com.yintu.ruixing.yunxingweihu.*;
import com.yintu.ruixing.weixiudaxiu.EquipmentEntity;
import com.yintu.ruixing.weixiudaxiu.EquipmentService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author:mlf
 * @date:2020/7/11 11:54
 */
@Service
@Transactional
public class SparePartsServiceImpl implements SparePartsService {

    @Autowired
    private SparePartsDao sparePartsDao;
    @Autowired
    private ScheduleJobService scheduleJobService;
    @Autowired
    private DataStatsService dataStatsService;

    @Override
    public void add(SparePartsEntity entity) {
        String cronExpression = null;
        if (entity.getExecutionMode() == (short) 1) {
            if (!entity.getExecutionTime().after(DateUtil.date()))
                throw new BaseRuntimeException("执行时间不能小于等于当前时间");
            String cycleDescription = "在" + DateUtil.format(entity.getExecutionTime(), "yyyy-MM-dd") +
                    "的" + DateUtil.format(entity.getExecutionTime(), "hh:mm:ss") + "时执行一次";
            cronExpression = String.format("%d %d %d %d %d ? *",
                    DateUtil.second(entity.getExecutionTime()),
                    DateUtil.minute(entity.getExecutionTime()),
                    DateUtil.hour(entity.getExecutionTime(), true),
                    DateUtil.dayOfMonth(entity.getExecutionTime()),
                    DateUtil.month(entity.getExecutionTime()) + 1);

            entity.setCycleDescription(cycleDescription);
        } else if (entity.getExecutionMode() == (short) 2) {
            String cycleDescription = null;
            switch (entity.getCycleType()) {
                case 1://每日
                    cycleDescription = "在每天的" + DateUtil.format(entity.getExecutionTime(), "hh:mm:ss") + "执行，执行日期：" + DateUtil.format(entity.getExecutionTime(), "yyyy-MM-dd");
                    cronExpression = String.format("%d %d %d * * ? *",
                            DateUtil.second(entity.getExecutionTime()),
                            DateUtil.minute(entity.getExecutionTime()),
                            DateUtil.hour(entity.getExecutionTime(), true));
                    break;
                case 2://每周
                    String cycleWeekValue = entity.getCycleValue();
                    String[] weekArray = cycleWeekValue.split(",");
                    StringBuilder weekStr = new StringBuilder();
                    for (String s : weekArray) {
                        switch (Integer.parseInt(s)) {
                            case 1:
                                weekStr.append("星期一、");
                                break;
                            case 2:
                                weekStr.append("星期二、");
                                break;
                            case 3:
                                weekStr.append("星期三、");
                                break;
                            case 4:
                                weekStr.append("星期四、");
                                break;
                            case 5:
                                weekStr.append("星期五、");
                                break;
                            case 6:
                                weekStr.append("星期六、");
                                break;
                            case 7:
                                weekStr.append("星期日、");
                                break;
                        }
                    }
                    cycleDescription = "在每周的" + weekStr.substring(0, weekStr.length() - 1) + "的" + DateUtil.format(entity.getExecutionTime(), "hh:mm:ss") + "执行，执行日期：" + DateUtil.format(entity.getExecutionTime(), "yyyy-MM-dd");
                    cronExpression = String.format("%d %d %d ? * %s *",
                            DateUtil.second(entity.getExecutionTime()),
                            DateUtil.minute(entity.getExecutionTime()),
                            DateUtil.hour(entity.getExecutionTime(), true), cycleWeekValue);
                    break;
                case 3://每月
                    cycleDescription = "在每月的" + DateUtil.dayOfMonth(entity.getExecutionTime()) + "日的" + DateUtil.format(entity.getExecutionTime(), "hh:mm:ss") + "执行，执行日期：" + DateUtil.format(entity.getExecutionTime(), "yyyy-MM-dd");
                    cronExpression = String.format("%d %d %d %d * ? *",
                            DateUtil.second(entity.getExecutionTime()),
                            DateUtil.minute(entity.getExecutionTime()),
                            DateUtil.hour(entity.getExecutionTime(), true),
                            DateUtil.dayOfMonth(entity.getExecutionTime()));
                    break;
                case 4://每年
                    String cycleMonthValue = entity.getCycleValue();
                    String[] monthArray = cycleMonthValue.split(",");
                    StringBuilder monthStr = new StringBuilder();
                    for (String s : monthArray) {
                        switch (Integer.parseInt(s)) {
                            case 1:
                                monthStr.append("一月、");
                                break;
                            case 2:
                                monthStr.append("二月、");
                                break;
                            case 3:
                                monthStr.append("三月、");
                                break;
                            case 4:
                                monthStr.append("四月、");
                                break;
                            case 5:
                                monthStr.append("五月、");
                                break;
                            case 6:
                                monthStr.append("六月、");
                                break;
                            case 7:
                                monthStr.append("七月、");
                                break;
                            case 8:
                                monthStr.append("八月、");
                                break;
                            case 9:
                                monthStr.append("九月、");
                                break;
                            case 10:
                                monthStr.append("十月、");
                                break;
                            case 11:
                                monthStr.append("十一月、");
                                break;
                            case 12:
                                monthStr.append("十二月、");
                                break;
                        }
                    }
                    cycleDescription = "在每年的" + monthStr.substring(0, monthStr.length() - 1) + "的" + DateUtil.dayOfMonth(entity.getExecutionTime()) + "日的" + DateUtil.format(entity.getExecutionTime(), "hh:mm:ss") + "执行，执行日期：" + DateUtil.format(entity.getExecutionTime(),
                            "yyyy-MM-dd");
                    cronExpression = String.format("%d %d %d %d %s ? *",
                            DateUtil.second(entity.getExecutionTime()),
                            DateUtil.minute(entity.getExecutionTime()),
                            DateUtil.hour(entity.getExecutionTime(), true),
                            DateUtil.dayOfMonth(entity.getExecutionTime()), cycleMonthValue);
                    break;
            }
            entity.setCycleDescription(cycleDescription);
        }
        sparePartsDao.insertSelective(entity);
        if (cronExpression == null)
            throw new BaseRuntimeException("周期表达式有误");
        ScheduleJobEntity scheduleJobEntity = new ScheduleJobEntity();
        scheduleJobEntity.setCreateBy(entity.getCreateBy());
        scheduleJobEntity.setCreateTime(entity.getCreateTime());
        scheduleJobEntity.setModifiedBy(entity.getModifiedBy());
        scheduleJobEntity.setModifiedTime(entity.getModifiedTime());
        scheduleJobEntity.setExecutionTime(entity.getExecutionTime());
        scheduleJobEntity.setJobName(TaskEnum.SPARETEST.getValue() + "-" + entity.getId());
        scheduleJobEntity.setCronExpression(cronExpression);
        scheduleJobEntity.setBeanName(TaskEnum.SPARETEST.getValue());
        scheduleJobEntity.setMethodName("execute");
        scheduleJobEntity.setStatus(1);
        scheduleJobEntity.setDeleteFlag(false);
        scheduleJobService.add(scheduleJobEntity);
    }

    @Override
    public void remove(Integer id) {
        sparePartsDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(SparePartsEntity entity) {
        SparePartsEntity source = this.findById(entity.getId());
        if (source != null) {
            String cronExpression = null;
            if (entity.getExecutionMode() == (short) 1) {
                if (!entity.getExecutionTime().after(DateUtil.date()))
                    throw new BaseRuntimeException("执行时间不能小于等于当前时间");
                String cycleDescription = "在" + DateUtil.format(entity.getExecutionTime(), "yyyy-MM-dd") +
                        "的" + DateUtil.format(entity.getExecutionTime(), "hh:mm:ss") + "时执行一次";
                entity.setCycleDescription(cycleDescription);
                cronExpression = String.format("%d %d %d %d %d ? *",
                        DateUtil.second(entity.getExecutionTime()),
                        DateUtil.minute(entity.getExecutionTime()),
                        DateUtil.hour(entity.getExecutionTime(), true),
                        DateUtil.dayOfMonth(entity.getExecutionTime()),
                        DateUtil.month(entity.getExecutionTime()) + 1);

            } else if (entity.getExecutionMode() == (short) 2) {
                String cycleDescription = null;
                switch (entity.getCycleType()) {
                    case 1://每日
                        cycleDescription = "在每天的" + DateUtil.format(entity.getExecutionTime(), "hh:mm:ss") + "执行，执行日期：" + DateUtil.format(entity.getExecutionTime(), "yyyy-MM-dd");

                        cronExpression = String.format("%d %d %d * * ? *",
                                DateUtil.second(entity.getExecutionTime()),
                                DateUtil.minute(entity.getExecutionTime()),
                                DateUtil.hour(entity.getExecutionTime(), true));
                        break;
                    case 2://每周
                        String cycleWeekValue = entity.getCycleValue();
                        String[] weekArray = cycleWeekValue.split(",");
                        StringBuilder weekStr = new StringBuilder();
                        for (String s : weekArray) {
                            switch (Integer.parseInt(s)) {
                                case 1:
                                    weekStr.append("星期一、");
                                    break;
                                case 2:
                                    weekStr.append("星期二、");
                                    break;
                                case 3:
                                    weekStr.append("星期三、");
                                    break;
                                case 4:
                                    weekStr.append("星期四、");
                                    break;
                                case 5:
                                    weekStr.append("星期五、");
                                    break;
                                case 6:
                                    weekStr.append("星期六、");
                                    break;
                                case 7:
                                    weekStr.append("星期日、");
                                    break;
                            }
                        }
                        cycleDescription = "在每周的" + weekStr.substring(0, weekStr.length() - 1) + "的" + DateUtil.format(entity.getExecutionTime(), "hh:mm:ss") + "执行，执行日期：" + DateUtil.format(entity.getExecutionTime(), "yyyy-MM-dd");
                        cronExpression = String.format("%d %d %d ? * %s *",
                                DateUtil.second(entity.getExecutionTime()),
                                DateUtil.minute(entity.getExecutionTime()),
                                DateUtil.hour(entity.getExecutionTime(), true), cycleWeekValue);
                        break;
                    case 3://每月
                        cycleDescription = "在每月的" + DateUtil.dayOfMonth(entity.getExecutionTime()) + "日的" + DateUtil.format(entity.getExecutionTime(), "hh:mm:ss") + "执行，执行日期：" + DateUtil.format(entity.getExecutionTime(), "yyyy-MM-dd");
                        cronExpression = String.format("%d %d %d %d * ? *",
                                DateUtil.second(entity.getExecutionTime()),
                                DateUtil.minute(entity.getExecutionTime()),
                                DateUtil.hour(entity.getExecutionTime(), true),
                                DateUtil.dayOfMonth(entity.getExecutionTime()));
                        break;
                    case 4://每年
                        String cycleMonthValue = entity.getCycleValue();
                        String[] monthArray = cycleMonthValue.split(",");
                        StringBuilder monthStr = new StringBuilder();
                        for (String s : monthArray) {
                            switch (Integer.parseInt(s)) {
                                case 1:
                                    monthStr.append("一月、");
                                    break;
                                case 2:
                                    monthStr.append("二月、");
                                    break;
                                case 3:
                                    monthStr.append("三月、");
                                    break;
                                case 4:
                                    monthStr.append("四月、");
                                    break;
                                case 5:
                                    monthStr.append("五月、");
                                    break;
                                case 6:
                                    monthStr.append("六月、");
                                    break;
                                case 7:
                                    monthStr.append("七月、");
                                    break;
                                case 8:
                                    monthStr.append("八月、");
                                    break;
                                case 9:
                                    monthStr.append("九月、");
                                    break;
                                case 10:
                                    monthStr.append("十月、");
                                    break;
                                case 11:
                                    monthStr.append("十一月、");
                                    break;
                                case 12:
                                    monthStr.append("十二月、");
                                    break;
                            }
                        }
                        cycleDescription = "在每年的" + monthStr.substring(0, monthStr.length() - 1) + "的" + DateUtil.dayOfMonth(entity.getExecutionTime()) + "日的" + DateUtil.format(entity.getExecutionTime(), "hh:mm:ss") + "执行，执行日期：" + DateUtil.format(entity.getExecutionTime(),
                                "yyyy-MM-dd");
                        cronExpression = String.format("%d %d %d %d %s ? *",
                                DateUtil.second(entity.getExecutionTime()),
                                DateUtil.minute(entity.getExecutionTime()),
                                DateUtil.hour(entity.getExecutionTime(), true),
                                DateUtil.dayOfMonth(entity.getExecutionTime()), cycleMonthValue);
                        break;
                }
                entity.setCycleDescription(cycleDescription);
            }
            sparePartsDao.updateByPrimaryKeySelective(entity);
            if (cronExpression == null)
                throw new BaseRuntimeException("周期表达式有误");
            List<ScheduleJobEntity> scheduleJobEntities = scheduleJobService.findByJobName(TaskEnum.SPARETEST.getValue() + "-" + entity.getId());
            if (!scheduleJobEntities.isEmpty()) {
                ScheduleJobEntity scheduleJobEntity = scheduleJobEntities.get(0);
                scheduleJobEntity.setModifiedBy(entity.getModifiedBy());
                scheduleJobEntity.setModifiedTime(entity.getModifiedTime());
                scheduleJobEntity.setExecutionTime(entity.getExecutionTime());
                scheduleJobEntity.setCronExpression(cronExpression);
                scheduleJobService.edit(scheduleJobEntity);
            } else if (source.getExecutionMode() == 1) {  //执行一次的结束的需要再添加任务
                ScheduleJobEntity scheduleJobEntity = new ScheduleJobEntity();
                scheduleJobEntity.setCreateBy(entity.getCreateBy());
                scheduleJobEntity.setCreateTime(entity.getCreateTime());
                scheduleJobEntity.setModifiedBy(entity.getModifiedBy());
                scheduleJobEntity.setModifiedTime(entity.getModifiedTime());
                scheduleJobEntity.setExecutionTime(entity.getExecutionTime());
                scheduleJobEntity.setJobName(TaskEnum.SPARETEST.getValue() + "-" + entity.getId());
                scheduleJobEntity.setCronExpression(cronExpression);
                scheduleJobEntity.setBeanName(TaskEnum.SPARETEST.getValue());
                scheduleJobEntity.setMethodName("execute");
                scheduleJobEntity.setStatus(1);
                scheduleJobEntity.setDeleteFlag(false);
                scheduleJobService.add(scheduleJobEntity);
            }
        }
    }

    @Override
    public SparePartsEntity findById(Integer id) {
        List<SparePartsEntity> sparePartsEntities = sparePartsDao.selectByExample(new Integer[]{id}, null);
        return sparePartsEntities.isEmpty() ? null : sparePartsEntities.get(0);
    }

    @Override
    public void batchAdd(List<SparePartsEntity> sparePartsEntities) {
        sparePartsDao.muchInsert(sparePartsEntities);
    }

    @Override
    public void remove(Integer[] ids) {
        for (Integer id : ids) {
            this.remove(id);
            List<ScheduleJobEntity> scheduleJobEntities = scheduleJobService.findByJobName(TaskEnum.MAINTENANCEPLAN.getValue() + "-" + id);
            for (ScheduleJobEntity scheduleJobEntity : scheduleJobEntities) {
                scheduleJobService.remove(scheduleJobEntity.getId());
            }
        }
    }

    @Override
    public List<SparePartsEntity> findByExample(Integer[] ids, String name) {
        return sparePartsDao.selectByExample(ids, name);
    }


    @Override
    public String[][] importFile(InputStream inputStream, String fileName) throws IOException {
        //excel标题
        String title = "备品试验列表";
        String[][] content;
        if ("xls".equals(FileUtil.getExtensionName(fileName))) {
            content = ImportExcelUtil.getHSSFData(title, new HSSFWorkbook(inputStream));
        } else if ("xlsx".equals(FileUtil.getExtensionName(fileName))) {
            content = ImportExcelUtil.getXSSFData(title, new XSSFWorkbook(inputStream));
        } else {
            throw new BaseRuntimeException("文件格式有误");
        }
        return content;

    }

    @Override
    public void importDate(String[][] context, String loginUsername) {
        List<SparePartsEntity> sparePartsEntities = new ArrayList<>();
        List<ScheduleJobEntity> scheduleJobEntities = new ArrayList<>();
        for (int i = 0; i < context.length; i++) {
            String[] row = context[i];
            SparePartsEntity sparePartsEntity = new SparePartsEntity();
            sparePartsEntity.setCreateBy(loginUsername);
            sparePartsEntity.setCreateTime(new Date());
            sparePartsEntity.setModifiedBy(loginUsername);
            sparePartsEntity.setModifiedTime(new Date());
            sparePartsEntity.setName(row[5]);
            sparePartsEntity.setContext(row[6]);
            //四级联动的参数校对
            String tljName = row[1];
            List<TieLuJuEntity> tieLuJuEntities = dataStatsService.findAllTieLuJuByName(tljName);
            if (tieLuJuEntities.isEmpty())
                throw new BaseRuntimeException("第" + (i + 1) + "行数据有误，原因：" + "没有此铁路局");
            long tid = tieLuJuEntities.get(0).getTid();
            sparePartsEntity.setRailwaysBureauId((int) tid);

            String dwdName = row[2];
            List<DianWuDuanEntity> dianWuDuanEntities = dataStatsService.findDianWuDuanByName(dwdName);
            if (tieLuJuEntities.isEmpty())
                throw new BaseRuntimeException("第" + (i + 1) + "行数据有误，原因：" + "没有此电务段");
            long tidOfDid = dianWuDuanEntities.get(0).getTljDwdId();
            if (tid != tidOfDid)
                throw new BaseRuntimeException("第" + (i + 1) + "行数据有误，原因：" + "铁路局下边没有此电务段");
            long did = dianWuDuanEntities.get(0).getDid();
            sparePartsEntity.setSignalDepotId((int) did);

            String xdName = row[3];
            List<XianDuanEntity> xianDuanEntities = dataStatsService.findAllXianDuanByName(xdName);
            if (xianDuanEntities.isEmpty())
                throw new BaseRuntimeException("第" + (i + 1) + "行数据有误，原因：" + "没有此线段");
            //一个线段有可能归属于多个电务段，需要逐个判断
            int index = 0;
            boolean flag = false;
            for (int j = 0; j < xianDuanEntities.size(); j++) {
                long didOfDid = xianDuanEntities.get(j).getDwdXdId();
                if (did == didOfDid) {
                    flag = true;
                    index = j;
                }
            }
            if (!flag)
                throw new BaseRuntimeException("第" + (i + 1) + "行数据有误，原因：" + "电务段下边没有此线段");
            long xid = xianDuanEntities.get(index).getXid();
            sparePartsEntity.setSpecialRailwayLineId((int) xid);


            String czName = row[4];
            List<CheZhanEntity> cheZhanEntities = dataStatsService.findallChezhanByName(czName);
            if (cheZhanEntities.isEmpty())
                throw new BaseRuntimeException("第" + (i + 1) + "行数据有误，原因：" + "没有此车站");
            long xidOfCid = cheZhanEntities.get(0).getXdCzId();
            if (xid != xidOfCid)
                throw new BaseRuntimeException("第" + (i + 1) + "行数据有误，原因：" + "线段下边没有此车站");
            sparePartsEntity.setStationId((int) cheZhanEntities.get(0).getCid());

            if (!"一次".equals(row[7]) && !"重复".equals(row[7]))
                throw new BaseRuntimeException("第" + (i + 1) + "行数据有误，原因：" + "执行方式有误");
            sparePartsEntity.setExecutionMode("一次".equals(row[7]) ? (short) 1 : (short) 2);
            sparePartsEntity.setExecutionTime(DateUtil.parseDateTime(row[8]));

            String cronExpression = null;
            if (sparePartsEntity.getExecutionMode() == (short) 1) {
                if (!sparePartsEntity.getExecutionTime().after(DateUtil.date()))
                    throw new BaseRuntimeException("第" + (i + 1) + "行数据有误，原因：" + "执行时间不能小于等于当前时间");
                String cycleDescription = "在" + DateUtil.format(sparePartsEntity.getExecutionTime(), "yyyy-MM-dd") +
                        "的" + DateUtil.format(sparePartsEntity.getExecutionTime(), "hh:mm:ss") + "时执行一次";
                sparePartsEntity.setCycleDescription(cycleDescription);
                cronExpression = String.format("%d %d %d %d %d ? *",
                        DateUtil.second(sparePartsEntity.getExecutionTime()),
                        DateUtil.minute(sparePartsEntity.getExecutionTime()),
                        DateUtil.hour(sparePartsEntity.getExecutionTime(), true),
                        DateUtil.dayOfMonth(sparePartsEntity.getExecutionTime()),
                        DateUtil.month(sparePartsEntity.getExecutionTime()) + 1);
            } else if (sparePartsEntity.getExecutionMode() == (short) 2) {
                String cycleType = row[9];
                if (!"每日".equals(cycleType) && !"每周".equals(cycleType) && !"每月".equals(cycleType) && !"每年".equals(cycleType))
                    throw new BaseRuntimeException("第" + (i + 1) + "行数据有误，原因：" + "周期类型有误");
                String cycleDescription = null;
                switch (cycleType) {
                    case "每日"://1
                        cycleDescription = "在每天的" + DateUtil.format(sparePartsEntity.getExecutionTime(), "hh:mm:ss") + "执行，执行日期：" +
                                DateUtil.format(sparePartsEntity.getExecutionTime(), "yyyy-MM-dd");
                        cronExpression = String.format("%d %d %d * * ? *",
                                DateUtil.second(sparePartsEntity.getExecutionTime()),
                                DateUtil.minute(sparePartsEntity.getExecutionTime()),
                                DateUtil.hour(sparePartsEntity.getExecutionTime(), true));
                        break;
                    case "每周"://2
                        String weekStrArray = row[10];
                        String[] weekStr = weekStrArray.split("、");
                        StringBuilder cycleValueOfWeek = new StringBuilder();
                        for (String s : weekStr) {
                            switch (s) {
                                case "星期一":
                                    cycleValueOfWeek.append("1,");
                                    break;
                                case "星期二":
                                    cycleValueOfWeek.append("2,");
                                    break;
                                case "星期三":
                                    cycleValueOfWeek.append("3,");
                                    break;
                                case "星期四":
                                    cycleValueOfWeek.append("4,");
                                    break;
                                case "星期五":
                                    cycleValueOfWeek.append("5,");
                                    break;
                                case "星期六":
                                    cycleValueOfWeek.append("6,");
                                    break;
                                case "星期日":
                                    cycleValueOfWeek.append("7,");
                                    break;
                            }
                        }
                        sparePartsEntity.setCycleValue(cycleValueOfWeek.toString());
                        cycleDescription = "在每周的" + weekStrArray + "的" + DateUtil.format(sparePartsEntity.getExecutionTime(), "hh:mm:ss") + "执行，执行日期：" + DateUtil.format(sparePartsEntity.getExecutionTime(), "yyyy-MM-dd");
                        cronExpression = String.format("%d %d %d ? * %s *",
                                DateUtil.second(sparePartsEntity.getExecutionTime()),
                                DateUtil.minute(sparePartsEntity.getExecutionTime()),
                                DateUtil.hour(sparePartsEntity.getExecutionTime(), true), sparePartsEntity.getCycleValue());
                        break;
                    case "每月"://3
                        cycleDescription = "在每月的" + DateUtil.dayOfMonth(sparePartsEntity.getExecutionTime()) + "日的" + DateUtil.format(sparePartsEntity.getExecutionTime(), "hh:mm:ss") + "执行，执行日期：" + DateUtil.format(sparePartsEntity.getExecutionTime(), "yyyy-MM-dd");
                        cronExpression = String.format("%d %d %d %d * ? *",
                                DateUtil.second(sparePartsEntity.getExecutionTime()),
                                DateUtil.minute(sparePartsEntity.getExecutionTime()),
                                DateUtil.hour(sparePartsEntity.getExecutionTime(), true),
                                DateUtil.dayOfMonth(sparePartsEntity.getExecutionTime()));
                        break;
                    case "每年"://4
                        String monthStrArray = row[10];
                        String[] monthArray = monthStrArray.split("、");
                        StringBuilder cycleValueOfYear = new StringBuilder();
                        for (String s : monthArray) {
                            switch (s) {
                                case "一月":
                                    cycleValueOfYear.append("1,");
                                    break;
                                case "二月":
                                    cycleValueOfYear.append("2,");
                                    break;
                                case "三月":
                                    cycleValueOfYear.append("3,");
                                    break;
                                case "四月":
                                    cycleValueOfYear.append("4,");
                                    break;
                                case "五月":
                                    cycleValueOfYear.append("5,");
                                    break;
                                case "六月":
                                    cycleValueOfYear.append("6,");
                                    break;
                                case "七月":
                                    cycleValueOfYear.append("7,");
                                    break;
                                case "八月":
                                    cycleValueOfYear.append("8,");
                                    break;
                                case "九月":
                                    cycleValueOfYear.append("9,");
                                    break;
                                case "十月":
                                    cycleValueOfYear.append("10,");
                                    break;
                                case "十一月":
                                    cycleValueOfYear.append("11,");
                                    break;
                                case "十二月":
                                    cycleValueOfYear.append("12,");
                                    break;
                            }
                        }
                        sparePartsEntity.setCycleValue(cycleValueOfYear.toString());
                        cycleDescription = "在每年的" + monthStrArray + "的" + DateUtil.dayOfMonth(sparePartsEntity.getExecutionTime()) + "日的" + DateUtil.format(sparePartsEntity.getExecutionTime(), "hh:mm:ss") + "执行，执行日期：" + DateUtil.format(sparePartsEntity.getExecutionTime(),
                                "yyyy-MM-dd");
                        cronExpression = String.format("%d %d %d %d %s ? *",
                                DateUtil.second(sparePartsEntity.getExecutionTime()),
                                DateUtil.minute(sparePartsEntity.getExecutionTime()),
                                DateUtil.hour(sparePartsEntity.getExecutionTime(), true),
                                DateUtil.dayOfMonth(sparePartsEntity.getExecutionTime()), sparePartsEntity.getCycleValue());
                        break;
                }
                sparePartsEntity.setCycleDescription(cycleDescription);
            }
            sparePartsEntity.setIsStart((short) 1);
            sparePartsEntities.add(sparePartsEntity);
            if (cronExpression == null)
                throw new BaseRuntimeException("第" + (i + 1) + "行数据有误，原因：" + "周期表达式有误");
            ScheduleJobEntity scheduleJobEntity = new ScheduleJobEntity();
            scheduleJobEntity.setCreateBy(sparePartsEntity.getCreateBy());
            scheduleJobEntity.setCreateTime(sparePartsEntity.getCreateTime());
            scheduleJobEntity.setModifiedBy(sparePartsEntity.getModifiedBy());
            scheduleJobEntity.setModifiedTime(sparePartsEntity.getModifiedTime());
            scheduleJobEntity.setExecutionTime(sparePartsEntity.getExecutionTime());
            scheduleJobEntity.setCronExpression(cronExpression);
            //scheduleJobEntity.setJobName(TaskEnum.SPARETEST.getValue() + "-" + entity.getId());
            scheduleJobEntity.setBeanName(TaskEnum.SPARETEST.getValue());
            scheduleJobEntity.setMethodName("execute");
            scheduleJobEntity.setStatus(1);
            scheduleJobEntity.setDeleteFlag(false);
            scheduleJobEntities.add(scheduleJobEntity);
        }
        if (!sparePartsEntities.isEmpty() && !scheduleJobEntities.isEmpty()) {
            this.batchAdd(sparePartsEntities);
            for (int i = 0; i < sparePartsEntities.size(); i++) {
                scheduleJobEntities.get(i).setJobName(TaskEnum.SPARETEST.getValue() + "-" + sparePartsEntities.get(i).getId());
            }
            scheduleJobService.batchAdd(scheduleJobEntities);
        }
    }


    @Override
    public void templateFile(OutputStream outputStream) throws IOException {
        //excel标题
        String title = "备品试验列表";
        //excel表名
        String[] headers = {"序号", "铁路局", "电务段", "线段", "车站", "器材名称", "实验内容", "执行方式", "执行时间", "周期类型", "周期值", "周期描述"};
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, new String[0][0]);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public void exportFile(OutputStream outputStream, Integer[] ids) throws IOException {
        //excel标题
        String title = "备品试验列表";
        //excel表名
        String[] headers = {"序号", "铁路局", "电务段", "线段", "车站", "器材名称", "实验内容", "执行方式", "执行时间", "周期类型", "周期值", "周期描述"};
        //获取数据
        List<SparePartsEntity> sparePartsEntities = this.findByExample(ids, null);
        sparePartsEntities = sparePartsEntities.stream()
                .sorted(Comparator.comparing(SparePartsEntity::getId).reversed())
                .collect(Collectors.toList());
        //excel元素
        String[][] content = new String[sparePartsEntities.size()][headers.length];
        for (int i = 0; i < sparePartsEntities.size(); i++) {
            SparePartsEntity sparePartsEntity = sparePartsEntities.get(i);
            content[i][0] = sparePartsEntity.getId().toString();
            content[i][1] = sparePartsEntity.getTieLuJuEntity().getTljName();
            content[i][2] = sparePartsEntity.getDianWuDuanEntity().getDwdName();
            content[i][3] = sparePartsEntity.getXianDuanEntity().getXdName();
            content[i][4] = sparePartsEntity.getCheZhanEntity().getCzName();
            content[i][5] = sparePartsEntity.getName();
            content[i][6] = sparePartsEntity.getContext();
            content[i][7] = sparePartsEntity.getExecutionMode() == 1 ? "一次" : sparePartsEntity.getExecutionMode() == 2 ? "重复" : "";
            content[i][8] = DateUtil.formatDateTime(sparePartsEntity.getExecutionTime());
            if (sparePartsEntity.getExecutionMode() == 2) {
                content[i][9] = sparePartsEntity.getCycleType() == 1 ? "每日" : sparePartsEntity.getCycleType() == 2 ? "每周" : sparePartsEntity.getCycleType() == 3 ? "每月" : sparePartsEntity.getCycleType() == 4 ? "每年" : "";
                if (sparePartsEntity.getCycleType() == 2) {
                    String cycleWeekValue = sparePartsEntity.getCycleValue();
                    String[] weekArray = cycleWeekValue.split(",");
                    StringBuilder weekStr = new StringBuilder();
                    for (String s : weekArray) {
                        switch (Integer.parseInt(s)) {
                            case 1:
                                weekStr.append("星期一、");
                                break;
                            case 2:
                                weekStr.append("星期二、");
                                break;
                            case 3:
                                weekStr.append("星期三、");
                                break;
                            case 4:
                                weekStr.append("星期四、");
                                break;
                            case 5:
                                weekStr.append("星期五、");
                                break;
                            case 6:
                                weekStr.append("星期六、");
                                break;
                            case 7:
                                weekStr.append("星期日、");
                                break;
                        }
                    }
                    content[i][10] = weekStr.substring(0, weekStr.length() - 1);
                } else if (sparePartsEntity.getCycleType() == 4) {
                    String cycleMonthValue = sparePartsEntity.getCycleValue();
                    String[] monthArray = cycleMonthValue.split(",");
                    StringBuilder monthStr = new StringBuilder();
                    for (String s : monthArray) {
                        switch (Integer.parseInt(s)) {
                            case 1:
                                monthStr.append("一月、");
                                break;
                            case 2:
                                monthStr.append("二月、");
                                break;
                            case 3:
                                monthStr.append("三月、");
                                break;
                            case 4:
                                monthStr.append("四月、");
                                break;
                            case 5:
                                monthStr.append("五月、");
                                break;
                            case 6:
                                monthStr.append("六月、");
                                break;
                            case 7:
                                monthStr.append("七月、");
                                break;
                            case 8:
                                monthStr.append("八月、");
                                break;
                            case 9:
                                monthStr.append("九月、");
                                break;
                            case 10:
                                monthStr.append("十月、");
                                break;
                            case 11:
                                monthStr.append("十一月、");
                                break;
                            case 12:
                                monthStr.append("十二月、");
                                break;
                        }
                    }
                    content[i][10] = monthStr.substring(0, monthStr.length() - 1);
                } else {
                    content[i][10] = sparePartsEntity.getCycleValue();
                }
            } else {
                content[i][9] = "";
                content[i][10] = "";
            }
            content[i][11] = sparePartsEntity.getCycleDescription();
        }
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }
}
