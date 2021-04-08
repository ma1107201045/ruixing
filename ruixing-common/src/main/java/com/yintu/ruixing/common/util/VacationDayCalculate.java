package com.yintu.ruixing.common.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 从12月1号起计算一年中的假期
 */
public class VacationDayCalculate {

    // 3天节假日规则=节日在周几，放假日在周几_放假之前的多少天上班_放假后的多少天上班;
    private String vacationWeek = "1,6-7-1_0_0;2,7-1-2_1_0;3,1-2-3_2_0;4,4-5-6_0_1;5,5-6-7_0_0;6,6-7-1_0_0;7,6-7-1_0_0";

    // 7天节假日规则=节日在周几，放假之前的多少天上班_放假后的多少天上班;
    private String vacationMax = "1,2_0;2,2_0;3,3_0;4,0_3;5,0_2;6,0_2;7,1_1";

    /**
     * 一个时间集合，放假则为true，工作日为false 放假包括国家法定节假日和双休日
     */
    public HashMap<String, Boolean> yearVacationDay(Integer year) {
        HashMap<String, Boolean> dates = weekVacation(year - 1);

        // 3天假日在周几放假规则
        HashMap<Integer, String> weeks = new HashMap<>();
        String[] weeksTemp = vacationWeek.split(";");
        for (String weekStr : weeksTemp) {
            String[] week = weekStr.split(",");
            weeks.put(Integer.parseInt(week[0]), week[1]);
        }

        // 元旦节公历一月一日，放假3天
        String vacationDay = year + "-01-01";
        setVacationThreeDay(vacationDay, dates, weeks);

        // 清明节(不分日历，有规律)年份能整除4的年和下一年为4月4日，再下两年为4月5日，放假3天
        int temp = year & 3;
        if (temp < 2) {
            vacationDay = year + "-04-04";
        } else {
            vacationDay = year + "-04-05";
        }
        setVacationThreeDay(vacationDay, dates, weeks);

        // 劳动节公历五月一日，放假一天，没有倒休
       // dates.put("05-01", true);

         vacationDay = year + "-05-01";
        setVacationThreeDay(vacationDay, dates, weeks);

        // 端午节农历五月初五，放假3天
        vacationDay = lunar(year.toString(), 5, 5);
        setVacationThreeDay(vacationDay, dates, weeks);

        // 中秋节农历八月十五，放假3天
        vacationDay = lunar(year.toString(), 8, 15);
        setVacationThreeDay(vacationDay, dates, weeks);

        // 7天假日在周几放假规则
        weeks.clear();
        weeksTemp = vacationMax.split(";");
        for (String weekStr : weeksTemp) {
            String[] week = weekStr.split(",");
            weeks.put(Integer.parseInt(week[0]), week[1]);
        }

        // 春节农历一月初一，放假从前一天除夕开始，放假7天
        vacationDay = lunar(year.toString(), 1, 1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Long time = format.parse(vacationDay).getTime() - 86399000;
            vacationDay = format.format(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setVacationSevenDay(vacationDay, dates, weeks);

        // 国庆节公历十月一号，放假7天
        vacationDay = year + "-10-01";
        setVacationSevenDay(vacationDay, dates, weeks);

        return dates;
    }

    /**
     * 从21月1日起放入之后一年的时间 注：可通过代码的注释和释放选择是否加入双休日
     * @return map集合，key为日期(不包括年)，value是true为休息日，false为工作日
     */
    private HashMap<String, Boolean> weekVacation(Integer year) {
        // 放入一年的时间
        HashMap<String, Boolean> dates = new LinkedHashMap<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.getDateByString(year + "-12-1 12:00:00", "yyyy-MM-dd hh:mm:ss"));

        Integer days = 365;
        if ((year & 3) == 0) {
            days = 366;
        }
        for (int i = 0, j = 0; i < days; i++, j = 1) {
            cal.add(Calendar.DAY_OF_YEAR, j);
            String date = DateUtils.getStringDateByString(cal.getTime(), "yyyy-MM-dd");
            // 这里加入双休
			Integer ifVacation = dayForWeek(cal.getTime().getTime());
			if (ifVacation == 6 || ifVacation == 7) {
				dates.put(date, true);
			} else {
				dates.put(date, false);
			}
            // 若不需要双休，只需要法定假日，则将上面的几行注释掉，放开下面这一行
         //   dates.put(date, false);
        }
        return dates;
    }

    /**
     * 3天假期 计算放假日期和上班日期并修改
     * @param vacationDay 节日
     * @param dates 时间集合
     * @param weeks 放假周规律集合
     */
    private void setVacationThreeDay(String vacationDay, HashMap<String, Boolean> dates,
                                     HashMap<Integer, String> weeks) {
        Integer week = dayForWeek(vacationDay);
        String[] vacation = weeks.get(week).split("_");
        int indexOf = vacation[0].indexOf(week.toString());
        Integer[] interval = dayForWeekThree(indexOf);
        Integer incr = Integer.parseInt(vacation[1]);
        Integer decr = Integer.parseInt(vacation[2]);
        List<String> vacationDate = dayForWeek(vacationDay, interval[0], interval[1]);
        for (String day : vacationDate) {
            dates.put(day, true);
        }
        List<String> workDate = dayForWork(vacationDay, interval[0], interval[1], incr, decr);
        for (String day : workDate) {
            dates.put(day, false);
        }
    }

    /**
     * 7天假期 计算放假日期和上班日期并修改
     * @param vacationDay 节日
     * @param dates 时间集合
     * @param weeks 放假周规律集合
     */
    private void setVacationSevenDay(String vacationDay, HashMap<String, Boolean> dates,
                                     HashMap<Integer, String> weeks) {
        Integer week = dayForWeek(vacationDay);
        String[] vacation = weeks.get(week).split("_");
        Integer incr = Integer.parseInt(vacation[0]);
        Integer decr = Integer.parseInt(vacation[1]);
        List<String> vacationDate = dayForWeek(vacationDay, 0, 6);
        for (String day : vacationDate) {
            dates.put(day, true);
        }
        List<String> workDate = dayForWork(vacationDay, 0, 6, incr, decr);
        for (String day : workDate) {
            dates.put(day, false);
        }
    }

    /**
     * 获得指定字符串的时间是星期几
     * @param pTime 时间字符串
     */
    private Integer dayForWeek(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        try {
            Date tmpDate = format.parse(pTime);
            cal.setTime(tmpDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        if (w == 0)
            w = 7;
        return w;
    }

    /**
     * 获得指定毫秒数的时间是星期几
     * @param pTime 时间字符串
     */
    private Integer dayForWeek(Long pTime) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(new Date(pTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        if (w == 0)
            w = 7;
        return w;
    }

    /**
     * 放假三天的假日放假时间
     * @param indexOf 节日在周几
     * @return 放假时间范围在节日之前开始和几日之后结束
     */
    private Integer[] dayForWeekThree(Integer indexOf) {
        Integer incr, decr;
        if (indexOf == 0) {
            incr = 0;
            decr = 2;
        } else if (indexOf == 2) {
            incr = 1;
            decr = 1;
        } else {
            incr = 2;
            decr = 0;
        }
        return new Integer[] { incr, decr };
    }

    /**
     * 获得指定时间前几天的日期或者后几天的日期
     * @param pTime 时间
     * @param incr 之前几天
     * @param decr 之后几天
     * @return 放假时间日期集合
     */
    private List<String> dayForWeek(String pTime, Integer incr, Integer decr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat rformat = new SimpleDateFormat("yyyy-MM-dd");
        List<String> result = new ArrayList<>(10);
        Calendar cal = Calendar.getInstance();
        try {
            Date tmpDate = format.parse(pTime);
            result.add(rformat.format(tmpDate));
            cal.setTime(tmpDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < incr; i++) {
            cal.add(Calendar.DAY_OF_YEAR, -1);
            result.add(rformat.format(cal.getTime()));
        }

        cal.add(Calendar.DAY_OF_YEAR, 1 * incr);
        for (int i = 0; i < decr; i++) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            result.add(rformat.format(cal.getTime()));
        }
        return result;
    }

    /**
     * 获得放假之前和之后需要上班的时间
     * @param pTime 节日时间
     * @param v_incr 节日之前几天开始放假
     * @param v_decr 节日之后几天开始放假
     * @param w_incr 节日之前工作几天
     * @param w_decr 节日之后工作几天
     * @return 节假日前后需要上班的周六日时间
     */
    private List<String> dayForWork(String pTime, Integer v_incr, Integer v_decr, Integer w_incr, Integer w_decr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat rformat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        try {
            Date tmpDate = format.parse(pTime);
            cal.setTime(tmpDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> result = new ArrayList<>(5);

        cal.add(Calendar.DAY_OF_YEAR, v_incr * -1);
        for (int i = 0; i < w_incr; i++) {
            cal.add(Calendar.DAY_OF_YEAR, -1);
            result.add(rformat.format(cal.getTime()));
        }

        cal.add(Calendar.DAY_OF_YEAR, v_incr + w_incr + v_decr);
        for (int i = 0; i < w_decr; i++) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            result.add(rformat.format(cal.getTime()));
        }
        return result;
    }

    /**
     * 根据传入的农历日期计算公历日期
     * @param year 年
     * @param month 月
     * @param day 日
     * @return "年-月-日"
     */
    public String lunar(String year, Integer month, Integer day) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            cal.setTime(sdf.parse(year + "-" + month + "-" + day));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calcuLunar(cal, month, day, new Lunar(true));
        return year + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 农历转公历计算
     * @param cal 日期对象
     * @param month 月
     * @param day 日
     */
    public static void calcuLunar(Calendar cal, int month, int day, Lunar l) {
        Lunar lunar = new Lunar(cal);
        if (lunar.getMonth() != month) {
            cal.add(Calendar.MONTH, 1);
            calcuLunar(cal, month, day, l);
        }
        if (lunar.getDay() != day && l.isLeap()) {
            if (lunar.getDay() > day) {
                cal.add(Calendar.DAY_OF_YEAR, -1);
            } else {
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
            calcuLunar(cal, month, day, l);
            if (l.isLeap()) {
                l.setLeap(false);
            }
        }
    }

    /**
     * 进行测试，指定月份打印，要不然数字太多，正确性不好比对
     */
    public static void main(String[] args) {
        HashMap<String, Boolean> map = new VacationDayCalculate().yearVacationDay(2021);
        System.out.println("map="+map);
        Collection<Boolean> values = map.values();
        System.out.println("values="+values);
        Set<String> keySet = map.keySet();
        System.out.println("keyset="+keySet);
        for (String key : keySet) {
            Boolean v = map.get(key);
            System.out.println("vvvvvv"+v);
            if (v && key.startsWith("11")) {
                System.out.println(key);
            }

        }
    }
}
