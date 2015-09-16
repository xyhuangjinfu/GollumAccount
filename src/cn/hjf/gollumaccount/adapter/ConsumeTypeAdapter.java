package cn.hjf.gollumaccount.adapter;

import java.util.List;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.businessmodel.ConsumeType;
import cn.hjf.gollumaccount.util.BitmapUtil;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 选择消费类型页面，类型显示的适配器
 * @author huangjinfu
 *
 */
public class ConsumeTypeAdapter extends BaseAdapter {
    
    private List<ConsumeType> mTypes;
    private TypedArray mIcons;
    private Context mContext;
    
    public ConsumeTypeAdapter(Context context, List<ConsumeType> types) {
        this.mContext = context;
        this.mTypes = types;
        mIcons = mContext.getResources().obtainTypedArray(R.array.consume_types_icon);
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
        
        
        holder.icon.setImageResource(BitmapUtil.getDrawableIdByName(R.drawable.class, mTypes.get(position).getIcon()));
        holder.name.setText(mTypes.get(position).getName());
        
        return convertView;
    }
    
    private class ViewHolder {
        ImageView icon;
        TextView name;
    }
    
    public void release() {
        mIcons.recycle();
    }

}
