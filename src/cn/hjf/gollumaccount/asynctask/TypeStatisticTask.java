package cn.hjf.gollumaccount.asynctask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.tencent.bugly.proguard.p;

import cn.hjf.gollumaccount.business.ConsumeRecordManagerBusiness;
import cn.hjf.gollumaccount.businessmodel.ConsumeType;


import android.content.Context;
import android.os.AsyncTask;

/**
 * 按类型统计的AsyncTask
 * 
 * @author huangjinfu
 * 
 */
public class TypeStatisticTask extends
		AsyncTask<Calendar, Void, Map<ConsumeType, Double>> {

	private Context mContext; // 上下文对象
	private OnTypeCompareListener mListener; // 类型统计计算结果监听对象
	private ConsumeRecordManagerBusiness mConsumeRecordManagerBusiness; // 消费记录管理的业务逻辑

	public interface OnTypeCompareListener {
		public abstract void onItemCompareSuccess(Map<ConsumeType, Double> result);
	}

	public TypeStatisticTask(Context context, OnTypeCompareListener listener) {
		this.mContext = context;
		this.mListener = listener;
		mConsumeRecordManagerBusiness = new ConsumeRecordManagerBusiness(mContext);
	}

	@Override
	protected Map<ConsumeType, Double> doInBackground(Calendar... params) {
	    return mConsumeRecordManagerBusiness.statisticByType(params[0], params[1]);
	}

	@Override
	protected void onPostExecute(Map<ConsumeType, Double> result) {
		super.onPostExecute(result);
		mListener.onItemCompareSuccess(result);
	}

}
