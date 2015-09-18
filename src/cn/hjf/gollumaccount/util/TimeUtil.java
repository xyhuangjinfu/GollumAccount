package cn.hjf.gollumaccount.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

/**
 * 时间转换工具类
 * 
 * @author huangjinfu
 * 
 */
public final class TimeUtil {
    
    public static long getFirstDayMillsOfMonth(Calendar monthCalendar) {
        monthCalendar.set(Calendar.DAY_OF_MONTH, getFirstDay(monthCalendar.get(Calendar.YEAR), monthCalendar.get(Calendar.MONTH)));
        monthCalendar.set(Calendar.HOUR_OF_DAY, 0);
        monthCalendar.set(Calendar.MINUTE, 0);
        monthCalendar.set(Calendar.SECOND, 0);
        return monthCalendar.getTimeInMillis();
    }
    
    public static long getLastDayMillsOfMonth(Calendar monthCalendar) {
        monthCalendar.set(Calendar.DAY_OF_MONTH, getLastDay(monthCalendar.get(Calendar.YEAR), monthCalendar.get(Calendar.MONTH)));
        monthCalendar.set(Calendar.HOUR_OF_DAY, 23);
        monthCalendar.set(Calendar.MINUTE, 59);
        monthCalendar.set(Calendar.SECOND, 59);
        return monthCalendar.getTimeInMillis();
    }
    
    public static long getFirstDayOfDate(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }
    
    public static long getLastDayOfDate(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTimeInMillis();
    }
    
    public static String getDateString(String time) {
        Long longTime = Long.valueOf(time);
        Date date = new Date(longTime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    
    public static String getTimeString(String time) {
        Long longTime = Long.valueOf(time);
        Date date = new Date(longTime);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(date);
    }
    
    public static String getDateTimeString(String time) {
        Long longTime = Long.valueOf(time);
        Date date = new Date(longTime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
    
    public static String getDateString(Calendar time) {
        Date date = time.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy - MM - dd");
        return format.format(date);
    }
    
    public static String getTimeString(Calendar time) {
        Date date = time.getTime();
        SimpleDateFormat format = new SimpleDateFormat("HH : mm : ss");
        return format.format(date);
    }
    
    public static String getDateTimeString(Calendar time) {
        Date date = time.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy - MM - dd  HH : mm : ss");
        return format.format(date);
    }

	/**
	 * 根据long时间得到显示的字符串
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
	 * 根据Date时间得到显示的字符串
	 * 
	 * @param time
	 * @return
	 */
	public static String getTimeString(Date time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(time);
	}
	
	/**
	 * 根据long时间得到日期显示的字符串
	 * @param time
	 * @return
	 */
	public static String getDateString(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(time);
	}
	
	/**
	 * 根据long时间得到时分秒显示的字符串
	 * @param time
	 * @return
	 */
	public static String getShowTimeString(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.format(time);
	}

	/**
	 * 获得当前年
	 * 
	 * @return
	 */
	public static int getNowYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * 获得当前月
	 * 
	 * @return
	 */
	public static int getNowMonth() {
		return Calendar.getInstance().get(Calendar.MONTH);
	}

	/**
	 * 是否是闰年
	 * 
	 * @param year
	 * @return
	 */
	public static boolean isLeapYear(int year) {
		return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
	}

	/**
	 * 获得某个月第一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getFirstDay(int year, int month) {
		return 1;
	}

	/**
	 * 获得某个月最后一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getLastDay(int year, int month) {
	    int realMonth = month + 1;
		int lastDay = 0;
		switch (realMonth) {
		case 1:
		case 3:
		case 5:
        case 7:
        case 8:
        case 10:
        case 12:
			lastDay = 31;
			break;
		case 2:
			if (isLeapYear(year)) {
				lastDay = 29;
			} else {
				lastDay = 28;
			}
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			lastDay = 30;
			break;
		default:
			break;
		}
		return lastDay;

	}
}
