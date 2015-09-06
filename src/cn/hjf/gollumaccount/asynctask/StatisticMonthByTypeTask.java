package cn.hjf.gollumaccount.asynctask;

import java.util.Calendar;
import java.util.Map;

import cn.hjf.gollumaccount.business.ConsumeStatisticBusiness;
import cn.hjf.gollumaccount.businessmodel.ConsumeType;

import android.content.Context;
import android.os.AsyncTask;

public class StatisticMonthByTypeTask extends
        AsyncTask<Object, Void, Map<Integer, Double>> {
    
    private Context mContext;
    private ConsumeStatisticBusiness mConsumeStatisticBusiness;
    private OnStatisticMonthByTypeListener mListener;
    
    public interface OnStatisticMonthByTypeListener {
        public abstract void onStatisticMonthByTypeCompleted(Map<Integer, Double> result);
    }
    
    public StatisticMonthByTypeTask(Context context, OnStatisticMonthByTypeListener listener) {
        this.mContext = context;
        this.mListener = listener;
        mConsumeStatisticBusiness = new ConsumeStatisticBusiness(mContext);
    }

    @Override
    protected Map<Integer, Double> doInBackground(Object... params) {
        return mConsumeStatisticBusiness.getMonthSumByType((Calendar)params[0], (ConsumeType)params[1]);
    }

    
    @Override
    protected void onPostExecute(Map<Integer, Double> result) {
        mListener.onStatisticMonthByTypeCompleted(result);
    }
}
