package cn.hjf.gollumaccount.adapter;

import java.util.List;
import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.businessmodel.ConsumeRecord;
import cn.hjf.gollumaccount.util.BitmapUtil;
import cn.hjf.gollumaccount.util.NumberUtil;
import cn.hjf.gollumaccount.util.TimeUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 首页消费记录列表适配器
 * @author huangjinfu
 *
 */
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_consume_record_list, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.mRecordIcon = (ImageView) convertView.findViewById(R.id.iv_consume_icon);
			viewHolder.mRecordName = (TextView) convertView.findViewById(R.id.tv_consume_name);
			viewHolder.mRecordPrice = (TextView) convertView.findViewById(R.id.tv_consume_price);
			viewHolder.mRecordDate = (TextView) convertView.findViewById(R.id.tv_consume_date);
			viewHolder.mDelete = (Button) convertView.findViewById(R.id.btn_delete);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		initViewValue(record, viewHolder, position);

		return convertView;
	}
	
	private class ViewHolder {
		public ImageView mRecordIcon;
		public TextView mRecordPrice;
		public TextView mRecordName;
		public TextView mRecordDate;
		public Button mDelete;
	}
	
	/**
	 * @param record
	 * @param viewHolder
	 */
	private void initViewValue(ConsumeRecord record, ViewHolder viewHolder, int position) {
	    viewHolder.mDelete.setTag(position);
		viewHolder.mRecordPrice.setText(NumberUtil.formatValue(record.getRecordPrice()));
		viewHolder.mRecordName.setText(record.getRecordName());
		viewHolder.mRecordDate.setText(TimeUtil.getDateString(record.getConsumeTime()));
		viewHolder.mRecordIcon.setImageResource(BitmapUtil.getDrawableIdByName(R.drawable.class, record.getRecordType().getIcon()));
	}

}
