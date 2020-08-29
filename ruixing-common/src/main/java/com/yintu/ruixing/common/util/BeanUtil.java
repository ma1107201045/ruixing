package com.yintu.ruixing.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author:mlf
 * @date:2020/8/29 9:13
 */
public class BeanUtil {
    /**
     * 获取两个bean中不相同的值
     *
     * @param source
     * @param target
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T compareFieldValues(Object source, Object target, Class<T> type) {
        if (source == null || target == null)
            return null;
        Class<?> c1 = source.getClass();
        Class<?> c2 = target.getClass();
        if (c1 != c2 || c1 != type)
            return null;
        try {
            Field[] fields1 = c1.getDeclaredFields();
            Field[] fields2 = c2.getDeclaredFields();
            for (int i = 0; i < fields2.length; i++) {
                String methodName = fields2[i].getName();
                String methodNameUpperCase = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
                String getMethodName = "get" + methodNameUpperCase;
                String setMethodName = "set" + methodNameUpperCase;
                Method[] methods = c2.getDeclaredMethods();
                boolean getFlag = false;
                boolean setFlag = false;
                for (Method method : methods) {
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    if (method.getName().equals(getMethodName))
                        getFlag = true;
                    if (method.getName().equals(setMethodName))
                        setFlag = true;
                }
                if (!getFlag || !setFlag)
                    continue;


                if (!fields2[i].isAccessible()) {
                    fields1[i].setAccessible(true);
                    fields2[i].setAccessible(true);
                }
                Object value1 = fields1[i].get(source);
                Object value2 = fields2[i].get(target);
                if (value2 == null && value1 != null) {
                    fields2[i].set(target, value1);
                    continue;
                }
                if (value2 == null) {
                    continue;
                }
                if (value2.equals(value1)) {
                    fields2[i].set(target, null);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return (T) target;
    }
}
