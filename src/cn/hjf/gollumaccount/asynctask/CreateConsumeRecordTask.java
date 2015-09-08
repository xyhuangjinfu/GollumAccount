package cn.hjf.gollumaccount.asynctask;

import cn.hjf.gollumaccount.business.ConsumeRecordManagerBusiness;
import cn.hjf.gollumaccount.businessmodel.ConsumeRecord;
import android.content.Context;
import android.os.AsyncTask;

/**
 * 新建消费记录任务
 * @author huangjinfu
 *
 */
public class CreateConsumeRecordTask extends
        AsyncTask<ConsumeRecord, Void, Boolean> {
    
    private Context mContext;
    private ConsumeRecordManagerBusiness mConsumeRecordManagerBusiness;
    private OnCreateConsumeRecordListener mListener;
    
    public interface OnCreateConsumeRecordListener {
        public abstract void OnCreateConsumeRecordCompleted(boolean isCreateSuccess);
    }
    
    public CreateConsumeRecordTask(Context context, OnCreateConsumeRecordListener listener) {
        this.mContext = context;
        this.mListener = listener;
        mConsumeRecordManagerBusiness = new ConsumeRecordManagerBusiness(mContext);
    }

    @Override
    protected Boolean doInBackground(ConsumeRecord... params) {
        return mConsumeRecordManagerBusiness.addRecord(params[0]);
    }
    
    @Override
    protected void onPostExecute(Boolean isCreateSuccess) {
        mListener.OnCreateConsumeRecordCompleted(isCreateSuccess);
    }

}
