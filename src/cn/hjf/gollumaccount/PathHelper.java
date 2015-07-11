package cn.hjf.gollumaccount;

import android.util.Log;

/**
 * 定义项目中每一个路径的常量
 * @author huangjinfu
 *
 */
public final class PathHelper {

	private static String mDatabasePath = null; //数据库存放路径

	public static String getDatabasePath() {
		return mDatabasePath;
	}

	public static void setDatabasePath(String databasePath) {
		mDatabasePath = databasePath;
		Log.i("hjf", "PathHelper - setDatabasePath - mDatabasePath:" + mDatabasePath);
	}	
}
