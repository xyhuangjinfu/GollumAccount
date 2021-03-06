package cn.hjf.gollumaccount.adapter;

import java.util.ArrayList;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.businessmodel.ConsumeType;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SideMenuAdapter extends BaseAdapter {
	
	private ArrayList<String> mDrawerList;
	private Context mContext;
	
	public SideMenuAdapter(Context context, ArrayList<String> drawerList) {
		this.mContext = context;
		this.mDrawerList = drawerList;
	}

	@Override
	public int getCount() {
		return mDrawerList.size();
	}

	@Override
	public Object getItem(int position) {
		return mDrawerList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		String drawerName = mDrawerList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_side_menu_list, null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.iv_drawer_icon);
			viewHolder.mTextView = (TextView) convertView.findViewById(R.id.iv_drawer_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		switch (findIndexByName(drawerName)) {
		case 1:
			viewHolder.mImageView.setImageResource(R.drawable.ic_record);
			viewHolder.mTextView.setText(drawerName);
			break;
		case 2:
			viewHolder.mImageView.setImageResource(R.drawable.ic_statistic);
			viewHolder.mTextView.setText(drawerName);
			break;
		case 3:
			viewHolder.mImageView.setImageResource(R.drawable.ic_setting);
			viewHolder.mTextView.setText(drawerName);
		break;
		}
		return convertView;
	}
	
	private int findIndexByName(String drawerName) {
		int index = 0;
		if ("消费记录".equals(drawerName)) {
			index = 1;
		} else if ("消费统计".equals(drawerName)) {
			index = 2;
		} else if ("设置".equals(drawerName)) {
			index = 3;
		}
		return index;
	}
	
	private class ViewHolder {
		public ImageView mImageView;
		public TextView mTextView;
	}

}
