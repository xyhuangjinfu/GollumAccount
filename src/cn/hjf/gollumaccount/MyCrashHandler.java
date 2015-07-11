package cn.hjf.gollumaccount;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Looper;

/**
 * �Զ���Crash�����࣬ʵ�ֳ����Ѻ��˳�
 * 
 * @author huangjinfu
 * 
 */
public class MyCrashHandler implements UncaughtExceptionHandler {

	private Context mContext; // �����Ķ�����Ҫ������ʼ���Ի���
	private static MyCrashHandler mMyCrashHandler; // �Զ���Crash�����࣬����ģʽ

	// ˽�й��췽��
	private MyCrashHandler(Context context) {
		this.mContext = context;
	}

	// �õ�ʵ��
	public static MyCrashHandler getInstance(Context context) {
		if (mMyCrashHandler == null) {
			mMyCrashHandler = new MyCrashHandler(context);
		}
		return mMyCrashHandler;
	}

	// ����Crash
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				new AlertDialog.Builder(mContext)
						.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("��ʾ")
						.setCancelable(false)
						.setMessage(
								"��Ŷ...����������أ���Ҫ̫����Ŷ��\n����Ա���ᾡ���޸�������⣬�ǵ�ȥӦ���г������°汾Ŷ��")
						.setNeutralButton("��֪����", new OnClickListener() {
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
