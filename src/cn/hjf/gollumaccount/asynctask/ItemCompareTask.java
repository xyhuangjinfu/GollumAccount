package cn.hjf.gollumaccount.asynctask;

import java.util.HashMap;

import cn.hjf.gollumaccount.business.ConsumeRecordService;

import android.content.Context;
import android.os.AsyncTask;

/**
 * 计算按分类查看消费情况的数据查询的AsyncTask
 * 
 * @author huangjinfu
 * 
 */
public class ItemCompareTask extends
		AsyncTask<Long, Void, HashMap<Integer, Double>> {

	private Context mContext; // 上下文对象
	private ConsumeRecordService mConsumeRecordService; // ConsumeRecord业务逻辑对象
	private OnItemCompareSuccessCallback mListener; // 回调监听对象

	/**
	 * 数据加载完成调用的监听器
	 * 
	 * @author huangjinfu
	 * 
	 */
	public interface OnItemCompareSuccessCallback {
		public abstract void onItemCompareSuccess(
				HashMap<Integer, Double> result);
	}

	public ItemCompareTask(Context context,
			OnItemCompareSuccessCallback listener) {
		this.mContext = context;
		this.mListener = listener;
		mConsumeRecordService = new ConsumeRecordService(mContext);
	}

	@Override
	protected HashMap<Integer, Double> doInBackground(Long... params) {
		return mConsumeRecordService.findPriceSumByItem(params[0], params[1]);
	}

	@Override
	protected void onPostExecute(HashMap<Integer, Double> result) {
		super.onPostExecute(result);
		mListener.onItemCompareSuccess(result);
	}

}
