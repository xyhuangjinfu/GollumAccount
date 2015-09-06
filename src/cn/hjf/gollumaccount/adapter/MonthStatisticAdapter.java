package cn.hjf.gollumaccount.adapter;

import java.util.List;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.businessmodel.MonthStatisticData;
import cn.hjf.gollumaccount.businessmodel.TypeStatisticData;
import cn.hjf.gollumaccount.util.NumberUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MonthStatisticAdapter extends BaseAdapter {
    
    private Context mContext;
    private List<MonthStatisticData> mDatas;
    
    public MonthStatisticAdapter(Context context, List<MonthStatisticData> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_month_statistic, parent, false);
            holder.month = (TextView) convertView.findViewById(R.id.tv_month);
            holder.typeName = (TextView) convertView.findViewById(R.id.tv_type);
            holder.ratio = (TextView) convertView.findViewById(R.id.tv_ratio);
            holder.sum = (TextView) convertView.findViewById(R.id.tv_sum);
            holder.ratioBar = (ProgressBar) convertView.findViewById(R.id.pb_ratio);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
//        holder.typeName.setText(mDatas.get(position).getConsumeType().getName());
        holder.month.setText(mDatas.get(position).getConsumeMonth() + 1 + "月");
        holder.sum.setText(NumberUtil.formatValue(mDatas.get(position).getTypeSum()));
        holder.ratio.setText(NumberUtil.formatValue(mDatas.get(position).getTypeSum() * 100 / mDatas.get(position).getAllSum()) + "%" );
        holder.ratioBar.setMax((int) mDatas.get(position).getAllSum());
        holder.ratioBar.setProgress((int) mDatas.get(position).getTypeSum());
        
        return convertView;
    }
    
    private class ViewHolder {
        TextView month, typeName, ratio, sum;
        ProgressBar ratioBar;
    }

}
