package cn.hjf.gollumaccount.asynctask;

import java.util.List;

import cn.hjf.gollumaccount.business.ConsumeTypeManagerBusiness;
import cn.hjf.gollumaccount.businessmodel.ConsumeType;

import android.content.Context;
import android.os.AsyncTask;

/**
 * 添加自定义消费类型的任务
 * @author huangjinfu
 *
 */
public class CreateConsumeTypeTask extends AsyncTask<ConsumeType, Void, Boolean> {
    
    private OnCreateConsumeTypeListener mListener;
    private Context mContext;
    private ConsumeTypeManagerBusiness mConsumeTypeManagerBusiness;
    
    public interface OnCreateConsumeTypeListener {
        public abstract void OnCreateConsumeTypeCompleted(boolean isCreateSucess);
    }
    
    public CreateConsumeTypeTask(Context context, OnCreateConsumeTypeListener listener) {
        this.mContext = context;
        this.mListener = listener;
        mConsumeTypeManagerBusiness = new ConsumeTypeManagerBusiness(mContext);
    }
    
    @Override
    protected Boolean doInBackground(ConsumeType... params) {
        return mConsumeTypeManagerBusiness.addType(params[0]);
    }
    
    @Override
    protected void onPostExecute(Boolean isCreateSucess) {
        mListener.OnCreateConsumeTypeCompleted(isCreateSucess);
        mListener = null;
    }

}
