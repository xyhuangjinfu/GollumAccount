package cn.hjf.gollumaccount.asynctask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.hjf.gollumaccount.business.ConsumeRecordManagerBusiness;
import cn.hjf.gollumaccount.businessmodel.ConsumeRecord;
import cn.hjf.gollumaccount.businessmodel.QueryInfo;
import cn.hjf.gollumaccount.util.TimeUtil;
import android.content.Context;
import android.os.AsyncTask;

/**
 * 消费记录查询的AsyncTask
 * 
 * @author huangjinfu
 * 
 */
public class LoadConsumeRecordTask extends
		AsyncTask<QueryInfo, Void, List<ConsumeRecord>> {

	private ConsumeRecordManagerBusiness mConsumeRecordManagerBusines; //消费记录管理业务逻辑
	private OnRecordLoadCallback mListener; // 查询结果回调对象
	private Context mContext; // 上下文对象

	/**
	 * 查询结果回调接口
	 * 
	 * @author huangjinfu
	 * 
	 */
	public interface OnRecordLoadCallback {
		public abstract void onRecordLoadCompleted(List<ConsumeRecord> records);
	}

	public LoadConsumeRecordTask(Context context, OnRecordLoadCallback listener) {
		this.mContext = context;
		this.mListener = listener;
		mConsumeRecordManagerBusines = new ConsumeRecordManagerBusiness(mContext);
	}

	@Override
	protected List<ConsumeRecord> doInBackground(QueryInfo... params) {
	    try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	    return mConsumeRecordManagerBusines.queryRecords(params[0]);
	}

	@Override
	protected void onPostExecute(List<ConsumeRecord> result) {
		super.onPostExecute(result);
		mListener.onRecordLoadCompleted(result);

	}
	
	/**
	 * 得到查询时间
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
