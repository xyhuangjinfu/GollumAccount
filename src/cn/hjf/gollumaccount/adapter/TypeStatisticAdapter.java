package cn.hjf.gollumaccount.adapter;

import java.util.List;

import cn.hjf.gollumaccount.R;
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

public class TypeStatisticAdapter extends BaseAdapter {
    
    private Context mContext;
    private List<TypeStatisticData> mDatas;
    
    public TypeStatisticAdapter(Context context, List<TypeStatisticData> datas) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_type_statistic, parent, false);
            holder.typeIcon = (ImageView) convertView.findViewById(R.id.iv_type);
            holder.typeName = (TextView) convertView.findViewById(R.id.tv_type);
            holder.ratio = (TextView) convertView.findViewById(R.id.tv_ratio);
            holder.sum = (TextView) convertView.findViewById(R.id.tv_sum);
            holder.ratioBar = (ProgressBar) convertView.findViewById(R.id.pb_ratio);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.typeName.setText(mDatas.get(position).getConsumeType().getName());
        holder.sum.setText(NumberUtil.formatValue(mDatas.get(position).getTypeSum()));
        switch (mDatas.get(position).getConsumeType().getId()) {
        case 1:
            holder.typeIcon.setImageResource(R.drawable.ic_clothes_white);
            break;
        case 2:
            holder.typeIcon.setImageResource(R.drawable.ic_food_white);
            break;
        case 3:
            holder.typeIcon.setImageResource(R.drawable.ic_house_white);
            break;
        case 4:
            holder.typeIcon.setImageResource(R.drawable.ic_traffic_white);
            break;
        case 5:
            holder.typeIcon.setImageResource(R.drawable.ic_social_white);
            break;
        case 6:
            holder.typeIcon.setImageResource(R.drawable.ic_entertainment_white);
            break;
        case 7:
            holder.typeIcon.setImageResource(R.drawable.ic_work_white);
            break;
        case 8:
            holder.typeIcon.setImageResource(R.drawable.ic_study_white);
            break;
        case 9:
            holder.typeIcon.setImageResource(R.drawable.ic_other_type_white);
            break;
        default:
            holder.typeIcon.setImageResource(R.drawable.ic_other_type_white);
            break;
        }
        holder.ratio.setText(NumberUtil.formatValue(mDatas.get(position).getTypeSum() * 100 / mDatas.get(position).getAllSum()) + "%" );
        holder.ratioBar.setMax((int) mDatas.get(position).getAllSum());
        holder.ratioBar.setProgress((int) mDatas.get(position).getTypeSum());
        
        return convertView;
    }
    
    private class ViewHolder {
        ImageView typeIcon;
        TextView typeName, ratio, sum;
        ProgressBar ratioBar;
    }

}
