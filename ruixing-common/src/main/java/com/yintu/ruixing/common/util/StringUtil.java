package com.yintu.ruixing.common.util;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @author:mlf
 * @date:2020/5/22 15:09
 */
public class StringUtil {

    /**
     * @param s 字符串
     * @return true or false
     */
    public static boolean isNumber(String s) {
        if (s != null)
            return s.trim().matches("^[0-9]*$");
        else
            return false;
    }

    public static <T> String arrayToStrWithComma(T[] array) {
        StringBuilder sb = new StringBuilder();
        for (T t : array) {
            sb.append(t.toString()).append(",");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }

    public static String getTableName(int czId, Date time) {
        int month = DateUtil.month(time) + 1;
        String monthStr = Integer.toString(month).length() == 1 ? "0" + month : Integer.toString(month);
        return "data_applydata_" + czId + "_" + DateUtil.year(time) + monthStr;
    }

    public static String getBaoJingYuJingTableName(int czId, Date time) {
        int month = DateUtil.month(time) + 1;
        String monthStr = Integer.toString(month).length() == 1 ? "0" + month : Integer.toString(month);
        return "alarm_" + czId + "_" + DateUtil.year(time) + monthStr;
    }

}
