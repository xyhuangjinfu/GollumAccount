package cn.hjf.gollumaccount.asynctask;

import cn.hjf.gollumaccount.business.ConsumeRecordManagerBusiness;
import android.content.Context;
import android.os.AsyncTask;

/**
 * 删除消费记录任务
 * @author huangjinfu
 *
 */
public class DeleteConsumeRecordTask extends
        AsyncTask<Integer, Void, Boolean> {
    
    private Context mContext;
    private ConsumeRecordManagerBusiness mConsumeRecordManagerBusiness;
    private OnDeleteConsumeRecordListener mListener;
    
    public interface OnDeleteConsumeRecordListener {
        public abstract void OnDeleteConsumeRecordCompleted(boolean isCreateSuccess);
    }
    
    public DeleteConsumeRecordTask(Context context, OnDeleteConsumeRecordListener listener) {
        this.mContext = context;
        this.mListener = listener;
        mConsumeRecordManagerBusiness = new ConsumeRecordManagerBusiness(mContext);
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        return mConsumeRecordManagerBusiness.deleteRecord(params[0]);
    }
    
    @Override
    protected void onPostExecute(Boolean isCreateSuccess) {
        mListener.OnDeleteConsumeRecordCompleted(isCreateSuccess);
    }

}
