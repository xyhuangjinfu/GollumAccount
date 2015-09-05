package cn.hjf.gollumaccount.asynctask;

import java.util.List;

import cn.hjf.gollumaccount.business.ConsumeTypeManagerBusiness;
import cn.hjf.gollumaccount.businessmodel.ConsumeType;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class LoadConsumeTypeTask extends AsyncTask<Void, Void, List<ConsumeType>> {
    
    private ConsumeTypeOperateListener mListener;
    private Context mContext;
    private ConsumeTypeManagerBusiness mConsumeTypeManagerBusiness;
    
    public LoadConsumeTypeTask(Context context, ConsumeTypeOperateListener listener) {
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
        mListener.OnTypeLoadCompleted(result);
        mListener = null;
    }

}
