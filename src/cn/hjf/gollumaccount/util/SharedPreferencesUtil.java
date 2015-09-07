package cn.hjf.gollumaccount.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    
	private static String SHARED_NAME = "gollum_account";
	
	/**
	 * 得到SharedPreferences
	 * @param context
	 * @return
	 */
	public static SharedPreferences getSharedPreferences(Context context) {
		return context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE); 
	}
	
//	/**
//	 * 存储字符串类型的数据
//	 * @param context
//	 * @param key
//	 * @param value
//	 */
//	public static void putString(Context context, String key, String value) {
//		Editor editor = getSharedPreferences(context).edit();
//		editor.putString(key, value);
//		editor.commit();
//	}
//	
//	/**
//	 * 得到字符串类型的数据
//	 * @param context
//	 * @param key
//	 * @param defValue
//	 * @return
//	 */
//	public static String getString(Context context, String key, String defValue) {
//		return getSharedPreferences(context).getString(key, defValue);
//	}
//	
//	/**
//	 * 存储Boolean类型的数据
//	 * @param context
//	 * @param key
//	 * @param value
//	 */
//	public static void putBoolean(Context context, String key, Boolean value) {
//		Editor editor = getSharedPreferences(context).edit();
//		editor.putBoolean(key, value);
//		editor.commit();
//	}
//	
//	/**
//	 * 得到Boolean类型的数据
//	 * @param context
//	 * @param key
//	 * @param defValue
//	 * @return
//	 */
//	public static Boolean getBoolean(Context context, String key, Boolean defValue) {
//		return getSharedPreferences(context).getBoolean(key, defValue);
//	}
//	
//	/**
//	 * 存储整型的数据
//	 * @param context
//	 * @param key
//	 * @param value
//	 */
//	public static void putInt(Context context, String key, Integer value) {
//		Editor editor = getSharedPreferences(context).edit();
//		editor.putInt(key, value);
//		editor.commit();
//	}
//	
//	/**
//	 * 得到整型的数据
//	 * @param context
//	 * @param key
//	 * @param defValue
//	 * @return
//	 */
//	public static Integer getInt(Context context, String key, Integer defValue) {
//		return getSharedPreferences(context).getInt(key, defValue);
//	}
}
