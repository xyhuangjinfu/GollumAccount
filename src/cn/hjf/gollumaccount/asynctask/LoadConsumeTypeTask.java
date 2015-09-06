package cn.hjf.gollumaccount.asynctask;

import java.util.List;

import cn.hjf.gollumaccount.business.ConsumeTypeManagerBusiness;
import cn.hjf.gollumaccount.businessmodel.ConsumeType;

import android.content.Context;
import android.os.AsyncTask;

/**
 * 加载所有消费类型的任务
 * @author huangjinfu
 *
 */
public class LoadConsumeTypeTask extends AsyncTask<Void, Void, List<ConsumeType>> {
    
    private OnLoadConsumeTypeListener mListener;
    private Context mContext;
    private ConsumeTypeManagerBusiness mConsumeTypeManagerBusiness;
    
    public interface OnLoadConsumeTypeListener {
        public abstract void OnLoadConsumeTypeCompleted(List<ConsumeType> consumeTypes);
    }
    
    public LoadConsumeTypeTask(Context context, OnLoadConsumeTypeListener listener) {
        this.mContext = context;
        this.mListener = listener;
        mConsumeTypeManagerBusiness = new ConsumeTypeManagerBusiness(mContext);
    }
    
    @Override
    protected List<ConsumeType> doInBackground(Void... params) {
        return mConsumeTypeManagerBusiness.getAllType();
    }
    
    @Override
    protected void onPostExecute(List<ConsumeType> result) {
        mListener.OnLoadConsumeTypeCompleted(result);
        mListener = null;
    }

}
