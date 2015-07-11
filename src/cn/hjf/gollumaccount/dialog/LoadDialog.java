package cn.hjf.gollumaccount.dialog;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * ���ݼ��ؽ������Ի���
 * 
 * @author huangjinfu
 * 
 */
public class LoadDialog {

	private static ProgressDialog mProgressDialog; // ʵ����ʾ�Ľ������Ի���

	/**
	 * ��Context���󴴽�һ���������Ի���
	 * 
	 * @param context
	 * @return
	 */
	public static void show(Context context) {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(context);
		}
		mProgressDialog.setMessage("������...");
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();

	}

	/**
	 * ȡ����ʾ�������Ի���
	 */
	public static void close() {
		if (mProgressDialog != null) {
			mProgressDialog.cancel();
		}
	}

}
