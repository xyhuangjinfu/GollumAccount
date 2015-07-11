package cn.hjf.gollumaccount.dialog;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 数据加载进度条对话框
 * 
 * @author huangjinfu
 * 
 */
public class LoadDialog {

	private static ProgressDialog mProgressDialog; // 实际显示的进度条对话框

	/**
	 * 用Context对象创建一个进度条对话框
	 * 
	 * @param context
	 * @return
	 */
	public static void show(Context context) {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(context);
		}
		mProgressDialog.setMessage("加载中...");
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();

	}

	/**
	 * 取消显示进度条对话框
	 */
	public static void close() {
		if (mProgressDialog != null) {
			mProgressDialog.cancel();
		}
	}

}
