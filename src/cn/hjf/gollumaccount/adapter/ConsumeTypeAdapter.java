package cn.hjf.gollumaccount.adapter;

import java.util.List;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.model.ConsumeType;

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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_item_consume_type, parent, false);
            holder.icon = (ImageView) convertView.findViewById(R.id.iv_type_icon);
            holder.name = (TextView) convertView.findViewById(R.id.tv_type_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mTypes.get(position).getName().equals("添加类型")) {
            holder.icon.setImageResource(R.drawable.ic_add_type);
        }
        holder.name.setText(mTypes.get(position).getName());
        return convertView;
    }
    
    private class ViewHolder {
        ImageView icon;
        TextView name;
    }

}
