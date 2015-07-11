package cn.hjf.gollumaccount.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 处理时间的工具类
 * 
 * @author huangjinfu
 * 
 */
public final class TimeUtil {

	/**
	 * 把long类型的时间转换成字符串
	 * 
	 * @param time
	 * @return
	 */
	public static String getTimeString(long time) {
		Date date = new Date(time);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	/**
	 * 把Date类型的时间转换成字符串
	 * 
	 * @param time
	 * @return
	 */
	public static String getTimeString(Date time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(time);
	}
	
	/**
	 * 返回日期显示字符串
	 * @param time
	 * @return
	 */
	public static String getDateString(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(time);
	}
	
	/**
	 * 返回时间显示字符串
	 * @param time
	 * @return
	 */
	public static String getShowTimeString(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.format(time);
	}

	/**
	 * 获取当前年份
	 * 
	 * @return
	 */
	public static int getNowYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * 获取当前的月份
	 * 
	 * @return
	 */
	public static int getNowMonth() {
		return Calendar.getInstance().get(Calendar.MONTH);
	}

	/**
	 * 判断一个年份是否是闰年
	 * 
	 * @param year
	 * @return
	 */
	public static boolean isLeapYear(int year) {
		return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
	}

	/**
	 * 根据年份和月份，返回这个月的第一天的日期
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getFirstDay(int year, int month) {
		return 1;
	}

	/**
	 * 根据年份和月份，返回这个月的最后一天的日期
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getLastDay(int year, int month) {
		int lastDay = 0;
		switch (month) {
		case 1:
			lastDay = 31;
			break;
		case 2:
			if (isLeapYear(year)) {
				lastDay = 29;
			} else {
				lastDay = 28;
			}
			break;
		case 3:
			lastDay = 31;
			break;
		case 4:
			lastDay = 30;
			break;
		case 5:
			lastDay = 31;
			break;
		case 6:
			lastDay = 30;
			break;
		case 7:
			lastDay = 31;
			break;
		case 8:
			lastDay = 31;
			break;
		case 9:
			lastDay = 30;
			break;
		case 10:
			lastDay = 31;
			break;
		case 11:
			lastDay = 30;
			break;
		case 12:
			lastDay = 31;
			break;
		default:
			break;
		}
		return lastDay;

	}
}
