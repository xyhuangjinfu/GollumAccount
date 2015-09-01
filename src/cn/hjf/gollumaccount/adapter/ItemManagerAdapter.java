package cn.hjf.gollumaccount.adapter;

import java.util.ArrayList;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.model.ConsumeType;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemManagerAdapter extends BaseAdapter {
	
	private ArrayList<ConsumeType> mItemList;
	private Context mContext;
	
	public ItemManagerAdapter(Context context, ArrayList<ConsumeType> itemList) {
		this.mItemList = itemList;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return mItemList.size();
	}

	@Override
	public Object getItem(int position) {
		return mItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		ConsumeType consumeItem = mItemList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_item_manager_list, null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.iv_item_icon);
			viewHolder.mTextView = (TextView) convertView.findViewById(R.id.tv_item_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		switch (consumeItem.getId()) {
		case 1:
			viewHolder.mImageView.setImageResource(R.drawable.clothes);
			viewHolder.mTextView.setText(consumeItem.getName());
			break;
		case 2:
			viewHolder.mImageView.setImageResource(R.drawable.food);
			viewHolder.mTextView.setText(consumeItem.getName());
			break;
		case 3:
			viewHolder.mImageView.setImageResource(R.drawable.house);
			viewHolder.mTextView.setText(consumeItem.getName());
			break;
		case 4:
			viewHolder.mImageView.setImageResource(R.drawable.traffic);
			viewHolder.mTextView.setText(consumeItem.getName());
			break;
		case 5:
			viewHolder.mImageView.setImageResource(R.drawable.entertainment);
			viewHolder.mTextView.setText(consumeItem.getName());
			break;
		case 6:
			viewHolder.mImageView.setImageResource(R.drawable.work);
			viewHolder.mTextView.setText(consumeItem.getName());
			break;
		case 7:
			viewHolder.mImageView.setImageResource(R.drawable.contact);
			viewHolder.mTextView.setText(consumeItem.getName());
			break;
		case 8:
			viewHolder.mImageView.setImageResource(R.drawable.other);
			viewHolder.mTextView.setText(consumeItem.getName());
			break;
		default:
			viewHolder.mImageView.setImageResource(R.drawable.unknown);
			viewHolder.mTextView.setText("�Զ������");
			break;
		}
		return convertView;
	}
	
	private class ViewHolder {
		public ImageView mImageView;
		public TextView mTextView;
	}

}
