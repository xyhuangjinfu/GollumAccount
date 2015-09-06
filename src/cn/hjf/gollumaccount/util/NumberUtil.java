package cn.hjf.gollumaccount.util;

import java.text.DecimalFormat;

/**
 * 字符串和各类型数值之间的转换与处理工具
 * 
 * @author xfujohn
 * 
 */
public class NumberUtil {
    
    /**
     * 把数字转换为两位数字
     * @param intValue
     * @return
     */
    public static String formatTwoInt(String intValue) {
        return formatValue(intValue, "#00");
    }
    
    /**
     * 把数字转换为两位数字
     * @param intValue
     * @return
     */
    public static String formatTwoInt(int intValue) {
        return formatValue(intValue, "#00");
    }

    /**
     * 把double类型的值格式化为指定格式，默认保留小数点后2位
     * 
     * @param number
     *            要格式化额数据
     * @return
     */
    public static String formatValue(double number) {
        return formatValue(number, "#0.00");
    }

    /**
     * 把double类型的值格式化为指定格式
     * 
     * @param number
     *            要格式化额数据
     * @param format
     *            格式
     * @return
     */
    public static String formatValue(double number, String format) {
        String result = null;
        if (format == null) {
            return result;
        }
        DecimalFormat decimalFormat = new DecimalFormat(format);
        result = decimalFormat.format(number);
        return result;
    }

    /**
     * 把String类型的值格式化为指定格式，默认保留小数点后2位
     * 
     * @param number
     *            要格式化额数据
     * @return
     */
    public static String formatValue(String number)
            throws NumberFormatException {
        return formatValue(number, "#0.00");
    }

    /**
     * 把String类型的值格式化为指定格式
     * 
     * @param number
     *            要格式化额数据
     * @param format
     *            格式
     * @return
     */
    public static String formatValue(String number, String format)
            throws NumberFormatException {
        String result = null;
        if (format == null) {
            return result;
        }
        DecimalFormat decimalFormat = new DecimalFormat(format);
        result = decimalFormat.format(getDoubleFromString(number));
        return result;
    }

    /**
     * 把String类型转换成Double类型
     * 
     * @param number
     *            待转换的字符串
     * @return double类型的值
     * @throws NumberFormatException
     */
    public static double getDoubleFromString(String number)
            throws NumberFormatException {
        double result = 0d;
        if (number == null || "".equals(number)) {
            return result;
        }
        try {
            result = Double.valueOf(number);
        } catch (NumberFormatException e) {
            throw e;
        }
        return result;
    }

    /**
     * 把String类型转换成int类型
     * 
     * @param number
     *            待转换的字符串
     * @return int类型的值
     */
    public static int getIntFromString(String number)
            throws NumberFormatException {
        int result = 0;
        if (number == null || "".equals(number)) {
            return result;
        }
        try {
            result = Integer.valueOf(number);
        } catch (NumberFormatException e) {
            throw e;
        }
        return result;
    }

}
