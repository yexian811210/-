package com.manager.utils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public class AssertUtils {

    //如果不是true，则抛异常
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new RuntimeException(message);
        }
    }


    //如果不是false，则抛异常
    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new RuntimeException(message);
        }
    }

    /**
     * 功能描述:
     * 〈判断数组是否为空〉
     */
    public static boolean isEmpty(Object[] array) {
        return null == array || 0 == array.length;
    }

    /**
     * 功能描述:
     * 数组不为 null 或者长度不为 0，抛出异常
     */
    public static void isEmpty(Object[] array,String message) {
        if (isNotEmpty(array)){
            throw new RuntimeException(message);
        }
    }

    /**
     * 功能描述:
     * 〈判断数组是否不为空〉
     */
    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }


    /**
     * 功能描述:
     * 数组不为 null且长度不等于0，抛出异常
     */
    public static void isNotEmpty(Object[] array,String message) {
        if (isEmpty(array)){
            throw new RuntimeException(message);
        }
    }

    /**
     * 功能描述:
     * 〈判断对象是否为空〉
     */
    public static boolean isNull(Object obj) {
        return null == obj;
    }

    /**
     功能描述:
     * 〈判断对象是否为空,不为空抛出异常〉
     */
    public static void isNull(Object obj,String message) {
        if (isNotNull(obj)){
            throw new RuntimeException(message);
        }
    }

    /**
     * 功能描述:
     * 〈判断对象是否不为空〉
     */
    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    /**
     功能描述:
     * 〈判断对象是否不为空,为空抛出异常〉
     */
    public static void isNotNull(Object obj,String message) {
        if (isNull(obj)){
            throw new RuntimeException(message);
        }
    }

    /**
     * 功能描述:
     * 〈字符串是否为空〉
     */
    public static boolean isEmpty(String str) {
        return null == str || "".equals(str);
    }

    /**
     功能描述:
     * 〈判断字符串是否为 null 或者空串,不为 null且不为空串，则抛出异常〉
     */
    public static void isEmpty(String str,String message) {
        if (isNotEmpty(str)){
            throw new RuntimeException(message);
        }
    }

    /**
     * 功能描述:
     * 〈字符串是否不为空〉
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }


    /**
     功能描述:
     * 〈判断字符串是否为不null且不为空串,为 null或者空串，则抛出异常〉
     */
    public static void isNotEmpty(String str,String message) {
        if (isEmpty(str)){
            throw new RuntimeException(message);
        }
    }


    /**
     * 功能描述:
     * 〈判断集合是否为空〉
     */
    public static boolean isEmpty(Collection<?> collection) {
        return null == collection || collection.isEmpty();
    }

    /**
     功能描述:
     * 〈判断collection是否为null或者长度等于0,为 不null且长度不等于0，则抛出异常〉
     */
    public static void isEmpty(Collection<?> collection,String message) {
        if (isNotEmpty(collection)){
            throw new RuntimeException(message);
        }
    }


    /**
     * 功能描述:
     * 〈判断集合是否不为空〉
     */
    public static  boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     功能描述:
     * 〈判断collection是否为不为null且长度不等于0,为null或者长度等于0，则抛出异常〉
     */
    public static void isNotEmpty(Collection<?> collection,String message) {
        if (isEmpty(collection)){
            throw new RuntimeException(message);
        }
    }

    /**
     * 功能描述:
     * 〈判断map是否为空〉
     */
    public static boolean isEmpty(Map<Object, Object> map) {
        return null == map || map.isEmpty();
    }

    /**
     功能描述:
     * 〈判断Map是否为null或者长度等于0,为 不null且长度不等于0，则抛出异常〉
     */
    public static void isEmpty(Map<Object,Object> map,String message) {
        if (isNotEmpty(map)){
            throw new RuntimeException(message);
        }
    }

    /**
     * 功能描述:
     * 〈判断map是否不为空〉
     */
    public static boolean isNotEmpty(Map<Object, Object> map) {
        return !isEmpty(map);
    }

    /**
     功能描述:
     * 〈判断Map是否为不为null且长度不等于0,为null或者长度等于0，则抛出异常〉
     */
    public static void isNotEmpty(Map<Object, Object> map,String message) {
        if (isEmpty(map)){
            throw new RuntimeException(message);
        }
    }

    /**
     * 功能描述:
     * 〈可变参数,判断是否所有对象都为空〉
     */
    public static boolean isAllNull(Object... objects) {
        for (Object object : objects) {
            if (!isNull(object)) {
                return false;
            }
        }
        return true;
    }



    /**
     * 功能描述:
     * 〈可变参数 -判断是否所有参数都不为空〉
     */
    public static boolean isAllNotNull(Object... objects) {
        for (Object object : objects) {
            if (isNull(object)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 功能描述:
     * 〈可变参数-判断只要有任意一个对象为空,则为true〉
     */
    public static boolean isAnyNull(Object... objects) {
        for (Object object : objects) {
            if (isNull(object)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断对象属性是否全为 null 或 ""
     *
     * @param obj 目标对象
     * @return
     */
    public static boolean fieldAllIsEmpty(Object obj) throws Exception {
        Field[] items = obj.getClass().getDeclaredFields();
        for (Field item : items) {
            item.setAccessible(true);
            Object field = item.get(obj);
            if (field != null && !"".equals(field)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 功能描述:
     * 〈字符串是否是数字〉
     */
    public static boolean isNumber(String str) {
        return str.matches("\\d+");
    }

    /**
     * 功能描述:
     * 〈字符串是否是数字,不为数字抛出异常〉
     */
    public static void isNumber(String str,String message) {
       if (isNotNumber(str)){
           throw new RuntimeException(message);
       }
    }

    /**
     * 功能描述:
     * 〈字符串是否不是数字〉
     */
    public static boolean isNotNumber(String str) {
        return !isNumber(str);
    }

    /**
     * 功能描述:
     * 〈字符串是否不是数字,是数字抛出异常〉
     */
    public static void isNotNumber(String str,String message) {
        if (isNumber(str)){
            throw new RuntimeException(message);
        }
    }

}
