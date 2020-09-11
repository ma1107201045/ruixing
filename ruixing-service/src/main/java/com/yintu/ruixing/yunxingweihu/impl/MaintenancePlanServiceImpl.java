package com.yintu.ruixing.yunxingweihu.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.cron.CronUtil;
import com.yintu.ruixing.common.ScheduleJobEntity;
import com.yintu.ruixing.common.ScheduleJobService;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.common.util.FileUtil;
import com.yintu.ruixing.common.util.ImportExcelUtil;
import com.yintu.ruixing.common.util.StringUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
import com.yintu.ruixing.yunxingweihu.MaintenancePlanDao;
import com.yintu.ruixing.yunxingweihu.MaintenancePlanEntity;
import com.yintu.ruixing.yunxingweihu.MaintenancePlanService;
import com.yintu.ruixing.yunxingweihu.TaskEnum;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author:mlf
 * @date:2020/7/8 15:51
 */
@Service
@Transactional
public class MaintenancePlanServiceImpl implements MaintenancePlanService {

    @Autowired
    private MaintenancePlanDao maintenancePlanDao;
    @Autowired
    private ScheduleJobService scheduleJobService;
    @Autowired
    private DataStatsService dataStatsService;

    @Override
    public void add(MaintenancePlanEntity entity) {
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
                    cycleDescription = "在每周的" + weekStr.toString().substring(0, weekStr.length() - 1) + "的" + DateUtil.format(entity.getExecutionTime(), "hh:mm:ss") + "执行，执行日期：" + DateUtil.format(entity.getExecutionTime(), "yyyy-MM-dd");
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
                    cycleDescription = "在每年的" + monthStr.toString().substring(0, monthStr.length() - 1) + "的" + DateUtil.dayOfMonth(entity.getExecutionTime()) + "日的" + DateUtil.format(entity.getExecutionTime(), "hh:mm:ss") + "执行，执行日期：" + DateUtil.format(entity.getExecutionTime(),
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
        maintenancePlanDao.insertSelective(entity);
        if (cronExpression == null)
            throw new BaseRuntimeException("周期表达式有误");
        ScheduleJobEntity scheduleJobEntity = new ScheduleJobEntity();
        scheduleJobEntity.setCreateBy(entity.getCreateBy());
        scheduleJobEntity.setCreateTime(entity.getCreateTime());
        scheduleJobEntity.setModifiedBy(entity.getModifiedBy());
        scheduleJobEntity.setModifiedTime(entity.getModifiedTime());
        scheduleJobEntity.setJobName(TaskEnum.MAINTENANCEPLAN.getValue() + "-" + entity.getId());
        scheduleJobEntity.setCronExpression(cronExpression);
        scheduleJobEntity.setBeanName(TaskEnum.MAINTENANCEPLAN.getValue());
        scheduleJobEntity.setMethodName("execute");
        scheduleJobEntity.setStatus(1);
        scheduleJobEntity.setDeleteFlag(false);
        scheduleJobService.add(scheduleJobEntity);
    }

    @Override
    public void remove(Integer id) {
        maintenancePlanDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(MaintenancePlanEntity entity) {
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
        maintenancePlanDao.updateByPrimaryKeySelective(entity);
        if (cronExpression == null)
            throw new BaseRuntimeException("周期表达式有误");
        List<ScheduleJobEntity> scheduleJobEntities = scheduleJobService.findByJobName(TaskEnum.MAINTENANCEPLAN.getValue() + "-" + entity.getId());
        if (!scheduleJobEntities.isEmpty()) {
            ScheduleJobEntity scheduleJobEntity = scheduleJobEntities.get(0);
            scheduleJobEntity.setModifiedBy(entity.getModifiedBy());
            scheduleJobEntity.setModifiedTime(entity.getModifiedTime());
            scheduleJobEntity.setCronExpression(cronExpression);
            scheduleJobService.edit(scheduleJobEntity);
        }

    }

    @Override
    public MaintenancePlanEntity findById(Integer id) {
        List<MaintenancePlanEntity> maintenancePlanEntities = maintenancePlanDao.selectByExample(new Integer[]{id}, null);
        return maintenancePlanEntities.isEmpty() ? null : maintenancePlanEntities.get(0);
    }

    @Override
    public void batchAdd(List<MaintenancePlanEntity> maintenancePlanEntities) {
        maintenancePlanDao.muchInsert(maintenancePlanEntities);
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
    public List<MaintenancePlanEntity> findByExample(Integer[] ids, String name) {
        return maintenancePlanDao.selectByExample(ids, name);
    }

    @Override
    public void importFile(InputStream inputStream, String fileName, String loginUsername) throws IOException {
        //excel标题
        String title = "维护计划列表";
        String[][] content;
        if ("xls".equals(FileUtil.getExtensionName(fileName))) {
            content = ImportExcelUtil.getHSSFData(title, new HSSFWorkbook(inputStream));
        } else if ("xlsx".equals(FileUtil.getExtensionName(fileName))) {
            content = ImportExcelUtil.getXSSFData(title, new XSSFWorkbook(inputStream));
        } else {
            throw new BaseRuntimeException("文件格式有误");
        }
        List<MaintenancePlanEntity> maintenancePlanEntities = new ArrayList<>();
        List<ScheduleJobEntity> scheduleJobEntities = new ArrayList<>();
        for (String[] rows : content) {
            MaintenancePlanEntity maintenancePlanEntity = new MaintenancePlanEntity();
            maintenancePlanEntity.setCreateBy(loginUsername);
            maintenancePlanEntity.setCreateTime(new Date());
            maintenancePlanEntity.setModifiedBy(loginUsername);
            maintenancePlanEntity.setModifiedTime(new Date());
            maintenancePlanEntity.setName(rows[1]);
            maintenancePlanEntity.setContext(rows[2]);
            //四级联动的参数校对
            String tljName = rows[3];
            List<TieLuJuEntity> tieLuJuEntities = dataStatsService.findAllTieLuJuByName(tljName);
            if (tieLuJuEntities.isEmpty())
                throw new BaseRuntimeException("没有此铁路局");
            long tid = tieLuJuEntities.get(0).getTid();
            maintenancePlanEntity.setRailwaysBureauId((int) tid);

            String dwdName = rows[4];
            List<DianWuDuanEntity> dianWuDuanEntities = dataStatsService.findDianWuDuanByName(dwdName);
            if (tieLuJuEntities.isEmpty())
                throw new BaseRuntimeException("没有此电务段");
            long tidOfDid = dianWuDuanEntities.get(0).getTid();
            if (tid != tidOfDid)
                throw new BaseRuntimeException("铁路局下边没有此电务段");
            long did = dianWuDuanEntities.get(0).getDid();
            maintenancePlanEntity.setSignalDepotId((int) did);

            String xdName = rows[5];
            List<XianDuanEntity> xianDuanEntities = dataStatsService.findAllXianDuanByName(xdName);
            if (xianDuanEntities.isEmpty())
                throw new BaseRuntimeException("没有此线段");
            long didOfDid = xianDuanEntities.get(0).getDid();
            if (did != didOfDid)
                throw new BaseRuntimeException("电务段下边没有此线段");
            long xid = xianDuanEntities.get(0).getXid();
            maintenancePlanEntity.setSpecialRailwayLineId((int) xid);

            String czName = rows[6];
            List<CheZhanEntity> cheZhanEntities = dataStatsService.findallChezhanByName(czName);
            if (cheZhanEntities.isEmpty())
                throw new BaseRuntimeException("没有此车站");
            long xidOfCid = cheZhanEntities.get(0).getXid();
            if (xid != xidOfCid)
                throw new BaseRuntimeException("线段下边没有此车站");
            maintenancePlanEntity.setStationId((int) cheZhanEntities.get(0).getCid());

            if (!"一次".equals(rows[7]) && !"重复".equals(rows[7]))
                throw new BaseRuntimeException("执行方式有误");
            maintenancePlanEntity.setExecutionMode("一次".equals(rows[7]) ? (short) 1 : (short) 2);
            maintenancePlanEntity.setExecutionTime(DateUtil.parseDateTime(rows[8]));

            String cronExpression = null;
            if (maintenancePlanEntity.getExecutionMode() == (short) 1) {
                if (!maintenancePlanEntity.getExecutionTime().after(DateUtil.date()))
                    throw new BaseRuntimeException("执行时间不能小于等于当前时间");
                String cycleDescription = "在" + DateUtil.format(maintenancePlanEntity.getExecutionTime(), "yyyy-MM-dd") +
                        "的" + DateUtil.format(maintenancePlanEntity.getExecutionTime(), "hh:mm:ss") + "时执行一次";
                maintenancePlanEntity.setCycleDescription(cycleDescription);
            } else if (maintenancePlanEntity.getExecutionMode() == (short) 2) {
                String cycleType = rows[9];
                if (!"每日".equals(cycleType) && !"每周".equals(cycleType) && !"每月".equals(cycleType) && !"每年".equals(cycleType))
                    throw new BaseRuntimeException("周期类型有误");
                String cycleDescription = null;
                switch (cycleType) {
                    case "每日"://1
                        cycleDescription = "在每天的" + DateUtil.format(maintenancePlanEntity.getExecutionTime(), "hh:mm:ss") + "执行，执行日期：" +
                                DateUtil.format(maintenancePlanEntity.getExecutionTime(), "yyyy-MM-dd");
                        cronExpression = String.format("%d %d %d %d %d ? *",
                                DateUtil.second(maintenancePlanEntity.getExecutionTime()),
                                DateUtil.minute(maintenancePlanEntity.getExecutionTime()),
                                DateUtil.hour(maintenancePlanEntity.getExecutionTime(), true),
                                DateUtil.dayOfMonth(maintenancePlanEntity.getExecutionTime()),
                                DateUtil.month(maintenancePlanEntity.getExecutionTime()) + 1);
                        break;
                    case "每周"://2
                        String weekStrArray = rows[10];
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
                        maintenancePlanEntity.setCycleValue(cycleValueOfWeek.toString());
                        cycleDescription = "在每周的" + weekStrArray + "的" + DateUtil.format(maintenancePlanEntity.getExecutionTime(), "hh:mm:ss") + "执行，执行日期：" + DateUtil.format(maintenancePlanEntity.getExecutionTime(), "yyyy-MM-dd");
                        cronExpression = String.format("%d %d %d ? * %s *",
                                DateUtil.second(maintenancePlanEntity.getExecutionTime()),
                                DateUtil.minute(maintenancePlanEntity.getExecutionTime()),
                                DateUtil.hour(maintenancePlanEntity.getExecutionTime(), true), maintenancePlanEntity.getCycleValue());
                        break;
                    case "每月"://3
                        cycleDescription = "在每月的" + DateUtil.dayOfMonth(maintenancePlanEntity.getExecutionTime()) + "日的" + DateUtil.format(maintenancePlanEntity.getExecutionTime(), "hh:mm:ss") + "执行，执行日期：" + DateUtil.format(maintenancePlanEntity.getExecutionTime(), "yyyy-MM-dd");
                        cronExpression = String.format("%d %d %d %d * ? *",
                                DateUtil.second(maintenancePlanEntity.getExecutionTime()),
                                DateUtil.minute(maintenancePlanEntity.getExecutionTime()),
                                DateUtil.hour(maintenancePlanEntity.getExecutionTime(), true),
                                DateUtil.dayOfMonth(maintenancePlanEntity.getExecutionTime()));
                        break;
                    case "每年"://4
                        String monthStrArray = rows[10];
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
                        maintenancePlanEntity.setCycleValue(cycleValueOfYear.toString());
                        cycleDescription = "在每年的" + cycleValueOfYear + "的" + DateUtil.dayOfMonth(maintenancePlanEntity.getExecutionTime()) + "日的" + DateUtil.format(maintenancePlanEntity.getExecutionTime(), "hh:mm:ss") + "执行，执行日期：" + DateUtil.format(maintenancePlanEntity.getExecutionTime(),
                                "yyyy-MM-dd");
                        cronExpression = String.format("%d %d %d %d %s ? *",
                                DateUtil.second(maintenancePlanEntity.getExecutionTime()),
                                DateUtil.minute(maintenancePlanEntity.getExecutionTime()),
                                DateUtil.hour(maintenancePlanEntity.getExecutionTime(), true),
                                DateUtil.dayOfMonth(maintenancePlanEntity.getExecutionTime()), maintenancePlanEntity.getCycleValue());
                        break;
                }
                maintenancePlanEntity.setCycleDescription(cycleDescription);
            }
            maintenancePlanEntities.add(maintenancePlanEntity);
            if (cronExpression == null)
                throw new BaseRuntimeException("周期表达式有误");
            ScheduleJobEntity scheduleJobEntity = new ScheduleJobEntity();
            scheduleJobEntity.setCreateBy(maintenancePlanEntity.getCreateBy());
            scheduleJobEntity.setCreateTime(maintenancePlanEntity.getCreateTime());
            scheduleJobEntity.setModifiedBy(maintenancePlanEntity.getModifiedBy());
            scheduleJobEntity.setModifiedTime(maintenancePlanEntity.getModifiedTime());
            scheduleJobEntity.setCronExpression(cronExpression);
            //scheduleJobEntity.setJobName(TaskEnum.MAINTENANCEPLAN.getValue() + "-" + entity.getId());
            scheduleJobEntity.setCronExpression(cronExpression);
            scheduleJobEntity.setBeanName(TaskEnum.MAINTENANCEPLAN.getValue());
            scheduleJobEntity.setMethodName("execute");
            scheduleJobEntity.setStatus(1);
            scheduleJobEntity.setDeleteFlag(false);
            scheduleJobEntities.add(scheduleJobEntity);
        }
        if (!maintenancePlanEntities.isEmpty() && !scheduleJobEntities.isEmpty()) {
            this.batchAdd(maintenancePlanEntities);
            for (int i = 0; i < maintenancePlanEntities.size(); i++) {
                scheduleJobEntities.get(i).setJobName(TaskEnum.MAINTENANCEPLAN.getValue() + "-" + maintenancePlanEntities.get(i).getId());
            }
            scheduleJobService.batchAdd(scheduleJobEntities);
        }
    }

    @Override
    public void templateFile(OutputStream outputStream) throws IOException {
        //excel标题
        String title = "维护计划列表";
        //excel表名
        String[] headers = {"序号", "项目名称", "维护内容", "铁路局", "电务段", "线段", "车站", "执行方式", "执行时间", "周期类型", "周期值"};
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, new String[0][0]);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public void exportFile(OutputStream outputStream, Integer[] ids) throws IOException {
        //excel标题
        String title = "维护计划列表";
        //excel表名
        String[] headers = {"序号", "铁路局", "电务段", "线段", "项目名称", "维护内容", "车站", "执行方式", "执行时间", "周期类型", "周期值", "周期描述"};
        //获取数据
        List<MaintenancePlanEntity> maintenancePlanEntities = this.findByExample(ids, null);
        maintenancePlanEntities = maintenancePlanEntities.stream()
                .sorted(Comparator.comparing(MaintenancePlanEntity::getId).reversed())
                .collect(Collectors.toList());
        //excel元素
        String[][] content = new String[maintenancePlanEntities.size()][headers.length];
        for (int i = 0; i < maintenancePlanEntities.size(); i++) {
            MaintenancePlanEntity maintenancePlanEntity = maintenancePlanEntities.get(i);
            content[i][0] = maintenancePlanEntity.getId().toString();
            content[i][1] = maintenancePlanEntity.getName();
            content[i][2] = maintenancePlanEntity.getContext();
            content[i][3] = maintenancePlanEntity.getTieLuJuEntity().getTljName();
            content[i][4] = maintenancePlanEntity.getDianWuDuanEntity().getDwdName();
            content[i][5] = maintenancePlanEntity.getXianDuanEntity().getXdName();
            content[i][6] = maintenancePlanEntity.getCheZhanEntity().getCzName();
            content[i][7] = maintenancePlanEntity.getExecutionMode() == 1 ? "一次" : maintenancePlanEntity.getExecutionMode() == 2 ? "重复" : "";
            content[i][8] = DateUtil.formatDateTime(maintenancePlanEntity.getExecutionTime());
            content[i][9] = maintenancePlanEntity.getCycleType() == 1 ? "每天" : maintenancePlanEntity.getCycleType() == 2 ? "每周" : maintenancePlanEntity.getCycleType() == 3 ? "每月" : maintenancePlanEntity.getCycleType() == 4 ? "每年" : "";
            if (maintenancePlanEntity.getCycleType() == 2) {
                String cycleWeekValue = maintenancePlanEntity.getCycleValue();
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
            } else if (maintenancePlanEntity.getCycleType() == 4) {
                String cycleMonthValue = maintenancePlanEntity.getCycleValue();
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
                content[i][10] = maintenancePlanEntity.getCycleValue();
            }
            content[i][11] = maintenancePlanEntity.getCycleDescription();
        }
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }
}
