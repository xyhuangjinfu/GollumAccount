package cn.hjf.gollumaccount;

import com.tencent.bugly.crashreport.CrashReport;

import android.app.Application;
import android.os.Environment;

/**
 * 自定义Application类
 * 
 * @author huangjinfu
 * 
 */
public class MyApplication extends Application {
	
	private String mBuglyAppID = "900002004"; //Bugly的AppID
	private String mBuglyAppKey = "WRHDP8yQOdXUINId"; //Bugly的AppKey

	@Override
	public void onCreate() {
		super.onCreate();
		
		//开始追踪Bug，放在第一步执行
		CrashReport.initCrashReport(this.getApplicationContext(), mBuglyAppID, false);
		
		
		// 初始化项目中的所有路径
		initPath();
		

	}

	/**
	 * 初始化项目中所有的路径
	 */
	private void initPath() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			PathHelper.setDatabasePath(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/gollum/account/database");
		} else {
			PathHelper.setDatabasePath(this.getDatabasePath("account.db")
					.getParent());
		}
	}

}
