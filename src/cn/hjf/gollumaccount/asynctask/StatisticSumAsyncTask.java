package cn.hjf.gollumaccount.asynctask;

import java.util.Calendar;

import cn.hjf.gollumaccount.business.ConsumeStatisticBusiness;

import android.content.Context;
import android.os.AsyncTask;

public class StatisticSumAsyncTask extends AsyncTask<Calendar, Void, Double>{
    
    private Context mContext;
    private OnStatisticSumListener mListener;
    private ConsumeStatisticBusiness mConsumeStatisticBusiness;
    
    public StatisticSumAsyncTask(Context context, OnStatisticSumListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mConsumeStatisticBusiness = new ConsumeStatisticBusiness(mContext);
    }
    
    
    public interface OnStatisticSumListener {
        public abstract void onStatisticSumCompleted(Double sum);
    }

    @Override
    protected Double doInBackground(Calendar... params) {
        return mConsumeStatisticBusiness.getSumByMonth(params[0]);
    }

    @Override
    protected void onPostExecute(Double result) {
        mListener.onStatisticSumCompleted(result);
    }
}
