package cn.hjf.gollumaccount.asynctask;

import java.util.ArrayList;
import java.util.Calendar;

import cn.hjf.gollumaccount.business.ConsumeRecordService;
import cn.hjf.gollumaccount.model.ConsumeRecord;
import cn.hjf.gollumaccount.util.TimeUtil;
import android.content.Context;
import android.os.AsyncTask;

/**
 * 分页加载消费记录数据的AsyncTask
 * 
 * @author huangjinfu
 * 
 */
public class LoadConsumeRecordTask extends
		AsyncTask<Integer, Void, ArrayList<ConsumeRecord>> {

	private ConsumeRecordService mConsumeRecordService; // ConsumeRecord业务逻辑对象
	private OnRecordLoadCallback mListener; // 数据加载完成监听对象
	private Context mContext; // 上下文对象

	/**
	 * 数据加载完成调用的监听器
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
		mConsumeRecordService = new ConsumeRecordService(mContext);
	}

	@Override
	protected ArrayList<ConsumeRecord> doInBackground(Integer... params) {
		ArrayList<ConsumeRecord> result = new ArrayList<ConsumeRecord>();
		long[] times = getAnalyseTime(params[0], params[1]);
		if (params[4] == 9) {
			result = mConsumeRecordService.findRecordByPage(times[0], times[1], params[2], params[3]);
		} else {
			result = mConsumeRecordService.findRecordByPage(times[0], times[1], params[2], params[3], params[4]);
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
	 * 根据年份和月份计算查询时间
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
