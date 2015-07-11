package cn.hjf.gollumaccount;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Looper;

/**
 * 自定义Crash处理类，实现程序友好退出
 * 
 * @author huangjinfu
 * 
 */
public class MyCrashHandler implements UncaughtExceptionHandler {

	private Context mContext; // 上下文对象，主要用来初始化对话框
	private static MyCrashHandler mMyCrashHandler; // 自定义Crash处理类，单例模式

	// 私有构造方法
	private MyCrashHandler(Context context) {
		this.mContext = context;
	}

	// 得到实例
	public static MyCrashHandler getInstance(Context context) {
		if (mMyCrashHandler == null) {
			mMyCrashHandler = new MyCrashHandler(context);
		}
		return mMyCrashHandler;
	}

	// 处理Crash
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				new AlertDialog.Builder(mContext)
						.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("提示")
						.setCancelable(false)
						.setMessage(
								"啊哦...程序崩溃了呢，不要太懊恼哦。\n程序员哥哥会尽快修复这个问题，记得去应用市场下载新版本哦！")
						.setNeutralButton("我知道了", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								System.exit(0);
							}
						}).create().show();
				Looper.loop();
			}
		}.start();

	}

}
