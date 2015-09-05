package cn.hjf.gollumaccount.asynctask;

import cn.hjf.gollumaccount.business.ConsumeRecordManagerBusiness;
import cn.hjf.gollumaccount.businessmodel.ConsumeRecord;
import android.content.Context;
import android.os.AsyncTask;

/**
 * 消费记录更新的AsyncTask
 * 
 * @author huangjinfu
 * 
 */
public class UpdateConsumeRecordTask extends
		AsyncTask<ConsumeRecord, Void, Boolean> {

	private ConsumeRecordManagerBusiness mConsumeRecordManagerBusines; //消费记录管理业务逻辑
	private OnUpdateConsumeRecordListener mListener; // 查询结果回调对象
	private Context mContext; // 上下文对象
	
	public interface OnUpdateConsumeRecordListener {
	    public abstract void OnConsumeRecordUpdated(boolean result);
	}

	public UpdateConsumeRecordTask(Context context, OnUpdateConsumeRecordListener listener) {
		this.mContext = context;
		this.mListener = listener;
		mConsumeRecordManagerBusines = new ConsumeRecordManagerBusiness(mContext);
	}

	@Override
	protected Boolean doInBackground(ConsumeRecord... params) {
	    return mConsumeRecordManagerBusines.modifyRecord(params[0]);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		mListener.OnConsumeRecordUpdated(result);
	}
}
