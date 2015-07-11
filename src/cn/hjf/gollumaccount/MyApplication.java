package cn.hjf.gollumaccount;

import com.tencent.bugly.crashreport.CrashReport;

import android.app.Application;
import android.os.Environment;

/**
 * �Զ���Application��
 * 
 * @author huangjinfu
 * 
 */
public class MyApplication extends Application {
	
	private String mBuglyAppID = "900002004"; //Bugly��AppID
	private String mBuglyAppKey = "WRHDP8yQOdXUINId"; //Bugly��AppKey

	@Override
	public void onCreate() {
		super.onCreate();
		
		//��ʼ׷��Bug�����ڵ�һ��ִ��
//		CrashReport.initCrashReport(this.getApplicationContext(), mBuglyAppID, false);
		
		
		// ��ʼ����Ŀ�е�����·��
		initPath();
		

	}

	/**
	 * ��ʼ����Ŀ�����е�·��
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
