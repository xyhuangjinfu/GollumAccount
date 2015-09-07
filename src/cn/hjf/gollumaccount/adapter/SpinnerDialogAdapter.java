package cn.hjf.gollumaccount.adapter;

import java.util.List;

import cn.hjf.gollumaccount.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class SpinnerDialogAdapter extends BaseAdapter {
    
    private Context mContext;
    private List<String> list;
    

    public SpinnerDialogAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_spinner_dialog, parent, false);
            holder = new ViewHolder();
            holder.mName = (TextView) convertView.findViewById(R.id.dialog_txt_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mName.setText(list.get(position));
        return convertView;
    }
    
    private class ViewHolder {
        TextView mName;
    }

}
