package cn.hjf.gollumaccount.adapter;

import java.util.List;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.businessmodel.ConsumeType;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ConsumeTypeAdapter extends BaseAdapter {
    
    private List<ConsumeType> mTypes;
    private Context mContext;
    
    public ConsumeTypeAdapter(Context context, List<ConsumeType> types) {
        this.mContext = context;
        this.mTypes = types;
    }

    @Override
    public int getCount() {
        return mTypes == null ? 0 : mTypes.size();
    }

    @Override
    public Object getItem(int position) {
        return mTypes.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_consume_type, parent, false);
            holder.icon = (ImageView) convertView.findViewById(R.id.iv_type_icon);
            holder.name = (TextView) convertView.findViewById(R.id.tv_type_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //控制按钮，添加类型
        if (mTypes.get(position).getType() == ConsumeType.Type.CONTROL) {
            holder.icon.setImageResource(R.drawable.ic_add_type);
        }
        //自定义类型
        if (mTypes.get(position).getType() == ConsumeType.Type.CUSTOME) {
            holder.icon.setImageResource(R.drawable.ic_custom_type);
        }
        if ("衣服".equals(mTypes.get(position).getName())) {
            holder.icon.setImageResource(R.drawable.ic_clothes);
        }
        if ("食物".equals(mTypes.get(position).getName())) {
            holder.icon.setImageResource(R.drawable.ic_food);
        }
        if ("住宿".equals(mTypes.get(position).getName())) {
            holder.icon.setImageResource(R.drawable.ic_house);
        }
        if ("交通".equals(mTypes.get(position).getName())) {
            holder.icon.setImageResource(R.drawable.ic_traffic);
        }
        if ("社交".equals(mTypes.get(position).getName())) {
            holder.icon.setImageResource(R.drawable.ic_social);
        }
        if ("娱乐".equals(mTypes.get(position).getName())) {
            holder.icon.setImageResource(R.drawable.ic_entertainment);
        }
        if ("办公".equals(mTypes.get(position).getName())) {
            holder.icon.setImageResource(R.drawable.ic_work);
        }
        if ("学习".equals(mTypes.get(position).getName())) {
            holder.icon.setImageResource(R.drawable.ic_study);
        }
        holder.name.setText(mTypes.get(position).getName());
        return convertView;
    }
    
    private class ViewHolder {
        ImageView icon;
        TextView name;
    }

}
