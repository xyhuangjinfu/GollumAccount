package cn.hjf.gollumaccount.asynctask;

import java.util.Calendar;
import java.util.HashMap;

import cn.hjf.gollumaccount.business.ConsumeItemService;
import cn.hjf.gollumaccount.business.ConsumeRecordService;
import cn.hjf.gollumaccount.util.TimeUtil;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class MonthCompareTask extends
		AsyncTask<Integer, Void, HashMap<Integer, Double>> {

	private Context mContext; // �����Ķ���
	private ConsumeRecordService mConsumeRecordService; // ConsumeRecordҵ���߼�����
	private ConsumeItemService mConsumeItemService; // ConsumeItemҵ���߼�����
	private OnMonthCompareSuccessCallback mListener; // ��ݼ�����ɼ������

	/**
	 * ��ݼ�����ɵ��õļ�����
	 * 
	 * @author huangjinfu
	 * 
	 */
	public interface OnMonthCompareSuccessCallback {
		public abstract void onMonthCompareSuccess(
				HashMap<Integer, Double> result);
	}

	public MonthCompareTask(Context context,
			OnMonthCompareSuccessCallback listener) {
		this.mContext = context;
		this.mListener = listener;
//		mConsumeRecordService = new ConsumeRecordService(mContext);
//		mConsumeItemService = new ConsumeItemService(mContext);
	}

	@Override
	protected HashMap<Integer, Double> doInBackground(Integer... params) {
		HashMap<Integer, Double> map = new HashMap<Integer, Double>();
		Log.i("hjf", "MonthCompareTask - doInBackground - params[0]:" + params[0]);
		Log.i("hjf", "MonthCompareTask - doInBackground - params[1]:" + params[1]);
		if (params[1] == 9) {
			for (int i = 0; i < 12; i++) {
				long[] time = getAnalyseTime(params[0], i + 1);
//				map.put(i, mConsumeRecordService.findPriceSum(time[0], time[1]));
			}
		} else {
			for (int i = 0; i < 12; i++) {
				long[] time = getAnalyseTime(params[0], i + 1);
//				map.put(i, mConsumeRecordService.findPriceSum(time[0], time[1], params[1]));
			}
		}

		return map;
	}

	@Override
	protected void onPostExecute(HashMap<Integer, Double> result) {
		mListener.onMonthCompareSuccess(result);
	};

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
