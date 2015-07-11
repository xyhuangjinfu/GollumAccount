package cn.hjf.gollumaccount;

import android.util.Log;

/**
 * ������Ŀ��ÿһ��·���ĳ���
 * @author huangjinfu
 *
 */
public final class PathHelper {

	private static String mDatabasePath = null; //���ݿ���·��

	public static String getDatabasePath() {
		return mDatabasePath;
	}

	public static void setDatabasePath(String databasePath) {
		mDatabasePath = databasePath;
		Log.i("hjf", "PathHelper - setDatabasePath - mDatabasePath:" + mDatabasePath);
	}	
}
