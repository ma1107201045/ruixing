package com.yintu.ruixing.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * @Author Mr.liu
 * @Date 2021/4/7 15:23
 * @Version 1.0
 * 需求:
 */

public class DateUtils {

    /**
     * 获得指定时间格式的Date时间
     *
     * @param time 时间字符串
     * @param plan 时间格式
     * @return 时间
     * @author wangzhe@piesat.cn
     * @date 2018年7月21日
     */
    public static Date getDateByString(String time, String plan) {
        Long times = getLongTimeByString(time, plan);
        return new Date(times);
    }

    /**
     * 获得指定时间格式的时间
     *
     * @param time 时间字符串
     * @param plan 时间格式
     * @return 时间
     * @author wangzhe@piesat.cn
     * @date 2018年7月21日
     */
    public static String getStringDateByString(Date time, String plan) {
        DateFormat format = new SimpleDateFormat(plan);
        return format.format(time);
    }

    /**
     * 获得指定时间格式的毫秒值
     *
     * @param time 时间字符串
     * @param plan 时间格式
     * @return 毫秒值
     * @author wangzhe@piesat.cn
     * @date 2018年7月17日
     */
    public static Long getLongTimeByString(String time, String plan) {
        DateFormat format = new SimpleDateFormat(plan);
        Long result = null;
        try {
            result = format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

}















