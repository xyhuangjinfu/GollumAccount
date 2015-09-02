package cn.hjf.gollumaccount.asynctask;

import java.util.ArrayList;
import java.util.Calendar;

import cn.hjf.gollumaccount.business.ConsumeRecordService;
import cn.hjf.gollumaccount.model.ConsumeRecord;
import cn.hjf.gollumaccount.util.TimeUtil;
import android.content.Context;
import android.os.AsyncTask;

/**
 * ��ҳ������Ѽ�¼��ݵ�AsyncTask
 * 
 * @author huangjinfu
 * 
 */
public class LoadConsumeRecordTask extends
		AsyncTask<Integer, Void, ArrayList<ConsumeRecord>> {

	private ConsumeRecordService mConsumeRecordService; // ConsumeRecordҵ���߼�����
	private OnRecordLoadCallback mListener; // ��ݼ�����ɼ������
	private Context mContext; // �����Ķ���

	/**
	 * ��ݼ�����ɵ��õļ�����
	 * 
	 * @author huangjinfu
	 * 
	 */
	public interface OnRecordLoadCallback {
		public abstract void onRecordLoadCompleted(
				ArrayList<ConsumeRecord> records);
	}

	public LoadConsumeRecordTask(Context context, OnRecordLoadCallback listener) {
		this.mContext = context;
		this.mListener = listener;
//		mConsumeRecordService = new ConsumeRecordService(mContext);
	}

	@Override
	protected ArrayList<ConsumeRecord> doInBackground(Integer... params) {
		ArrayList<ConsumeRecord> result = new ArrayList<ConsumeRecord>();
		long[] times = getAnalyseTime(params[0], params[1]);
		if (params[4] == 9) {
//			result = mConsumeRecordService.findRecordByPage(times[0], times[1], params[2], params[3]);
		} else {
//			result = mConsumeRecordService.findRecordByPage(times[0], times[1], params[2], params[3], params[4]);
		}
		return result;
//		return mConsumeRecordService.findRecordByPage(params[0], params[1]);
	}

	@Override
	protected void onPostExecute(ArrayList<ConsumeRecord> result) {
		super.onPostExecute(result);
		mListener.onRecordLoadCompleted(result);

	}
	
	/**
	 * �����ݺ��·ݼ����ѯʱ��
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private long[] getAnalyseTime(int year, int month) {
		long[] time = new long[2];

		Calendar cs = Calendar.getInstance();
		cs.set(year, month - 1, TimeUtil.getFirstDay(year, month), 0, 0, 0);
		time[0] = cs.getTime().getTime();

		Calendar ce = Calendar.getInstance();
		ce.set(year, month - 1, TimeUtil.getLastDay(year, month), 23, 59, 59);
		time[1] = ce.getTime().getTime();

		return time;
	}

}
