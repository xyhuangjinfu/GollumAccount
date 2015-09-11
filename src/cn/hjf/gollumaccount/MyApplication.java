package cn.hjf.gollumaccount;

import com.tencent.bugly.crashreport.CrashReport;

import cn.hjf.gollumaccount.util.SharedPreferencesUtil;

import android.app.Application;

/**
 * 自定义的Application类
 * 
 * @author huangjinfu
 * 
 */
public class MyApplication extends Application {
	
	private String mBuglyAppID = "900002004"; //Bugly使用的AppID
	private String mBuglyAppKey = "WRHDP8yQOdXUINId"; //Bugly使用的AppKey

	@Override
	public void onCreate() {
		super.onCreate();
		
		
//		SharedPreferencesUtil.getSharedPreferences(this).edit().putString("theme", "blue");
		
		//崩溃信息上传到Bugly
//		CrashReport.initCrashReport(this.getApplicationContext(), mBuglyAppID, false);
		
	}
}
