package cn.hjf.gollumaccount.adapter;

import java.util.List;
import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.businessmodel.ConsumeRecord;
import cn.hjf.gollumaccount.util.TimeUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ConsumeRecordAdapter extends BaseAdapter {
	
	private List<ConsumeRecord> mItemList;
	private Context mContext;
	
	public ConsumeRecordAdapter(Context context, List<ConsumeRecord> itemList) {
		this.mItemList = itemList;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return mItemList == null ? 0 : mItemList.size();
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
		ConsumeRecord record = mItemList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_consume_record_list, null);
			viewHolder = new ViewHolder();
			viewHolder.mRecordIcon = (ImageView) convertView.findViewById(R.id.iv_consume_icon);
			viewHolder.mRecordName = (TextView) convertView.findViewById(R.id.tv_consume_name);
			viewHolder.mRecordPrice = (TextView) convertView.findViewById(R.id.tv_consume_price);
			viewHolder.mRecordTime = (TextView) convertView.findViewById(R.id.tv_consume_time);
			viewHolder.mRecordInfo = (TextView) convertView.findViewById(R.id.tv_consume_info);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		initViewValue(record, viewHolder);

		return convertView;
	}
	
	private class ViewHolder {
		public ImageView mRecordIcon;
		public TextView mRecordPrice;
		public TextView mRecordName;
		public TextView mRecordTime;
		public TextView mRecordInfo;
	}
	
	/**
	 * @param record
	 * @param viewHolder
	 */
	private void initViewValue(ConsumeRecord record, ViewHolder viewHolder) {
		String displayName = null;
		String displayPrice = null;
		displayName = record.getRecordName();
		displayPrice = String.valueOf(record.getRecordPrice());
		viewHolder.mRecordPrice.setText(displayPrice);
		viewHolder.mRecordName.setText(displayName);
		viewHolder.mRecordTime.setText(TimeUtil.getDateString(record.getConsumeTime()));
		viewHolder.mRecordInfo.setText(record.getRecordRemark());
		switch (record.getRecordType().getId()) {
		case 1:
			viewHolder.mRecordIcon.setImageResource(R.drawable.ic_clothes);
			break;
		case 2:
			viewHolder.mRecordIcon.setImageResource(R.drawable.ic_food);
			break;
		case 3:
			viewHolder.mRecordIcon.setImageResource(R.drawable.ic_house);
			break;
		case 4:
			viewHolder.mRecordIcon.setImageResource(R.drawable.ic_traffic);
			break;
	    case 5:
	        viewHolder.mRecordIcon.setImageResource(R.drawable.ic_social);
	        break;
		case 6:
			viewHolder.mRecordIcon.setImageResource(R.drawable.ic_entertainment);
			break;
		case 7:
			viewHolder.mRecordIcon.setImageResource(R.drawable.ic_work);
			break;
		case 8:
			viewHolder.mRecordIcon.setImageResource(R.drawable.ic_study);
			break;
		default:
			viewHolder.mRecordIcon.setImageResource(R.drawable.ic_custom_type);
			break;
		}
	}

}
