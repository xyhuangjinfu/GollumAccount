package cn.hjf.gollumaccount.asynctask;

import java.util.HashMap;

import cn.hjf.gollumaccount.business.ConsumeRecordService;

import android.content.Context;
import android.os.AsyncTask;

/**
 * ���㰴����鿴����������ݲ�ѯ��AsyncTask
 * 
 * @author huangjinfu
 * 
 */
public class ItemCompareTask extends
		AsyncTask<Long, Void, HashMap<Integer, Double>> {

	private Context mContext; // �����Ķ���
	private ConsumeRecordService mConsumeRecordService; // ConsumeRecordҵ���߼�����
	private OnItemCompareSuccessCallback mListener; // �ص��������

	/**
	 * ��ݼ�����ɵ��õļ�����
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
//		mConsumeRecordService = new ConsumeRecordService(mContext);
	}

	@Override
	protected HashMap<Integer, Double> doInBackground(Long... params) {
//		return mConsumeRecordService.findPriceSumByItem(params[0], params[1]);
	    return null;
	}

	@Override
	protected void onPostExecute(HashMap<Integer, Double> result) {
		super.onPostExecute(result);
		mListener.onItemCompareSuccess(result);
	}

}
