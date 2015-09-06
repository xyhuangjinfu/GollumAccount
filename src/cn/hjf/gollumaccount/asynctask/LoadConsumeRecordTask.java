package cn.hjf.gollumaccount.asynctask;

import java.util.List;

import cn.hjf.gollumaccount.business.ConsumeRecordManagerBusiness;
import cn.hjf.gollumaccount.businessmodel.ConsumeRecord;
import cn.hjf.gollumaccount.businessmodel.QueryInfo;
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
	private LoadConsumeRecordListener mListener; // 查询结果回调对象
	private Context mContext; // 上下文对象
	
	public interface LoadConsumeRecordListener {
	    public abstract void OnLoadRecordCompleted(List<ConsumeRecord> consumeRecords);
	}

	public LoadConsumeRecordTask(Context context, LoadConsumeRecordListener listener) {
		this.mContext = context;
		this.mListener = listener;
		mConsumeRecordManagerBusines = new ConsumeRecordManagerBusiness(mContext);
	}

	@Override
	protected List<ConsumeRecord> doInBackground(QueryInfo... params) {
	    return mConsumeRecordManagerBusines.queryRecords(params[0]);
	}

	@Override
	protected void onPostExecute(List<ConsumeRecord> result) {
		super.onPostExecute(result);
		mListener.OnLoadRecordCompleted(result);

	}
}
