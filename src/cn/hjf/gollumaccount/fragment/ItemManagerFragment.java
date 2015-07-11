package cn.hjf.gollumaccount.fragment;

import java.util.ArrayList;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import cn.hjf.gollumaccount.FragmentIdConsts;
import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.activity.MainActivity;
import cn.hjf.gollumaccount.adapter.ItemManagerAdapter;
import cn.hjf.gollumaccount.business.ConsumeItemService;
import cn.hjf.gollumaccount.model.ConsumeItem;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ItemManagerFragment extends Fragment {
	
	private PullToRefreshListView mItemList; //账目显示列表
	private TextView mEmptyInfo; //列表为空时的显示信息
	private ConsumeItemService mConsumeItemService; //ConsumeItem实体的业务逻辑对象
	private ArrayList<ConsumeItem> mDataList; //ConsumeItem实体列表
	private ArrayList<String> mDataNameList = new ArrayList<String>(); //ConsumeItem名称显示列表
	private ArrayAdapter<String> mAdapter; //ListView适配器
	private ItemManagerAdapter mItemManagerAdapter;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		mConsumeItemService = new ConsumeItemService(this.getActivity());
		
		View view = inflater.inflate(R.layout.fragment_item_manager, null);
		mItemList = (PullToRefreshListView) view.findViewById(R.id.ptflv_item_list);
		mEmptyInfo = (TextView) view.findViewById(R.id.tv_item_empty);
		
//		mDataList = mConsumeItemService.findItemAll();
//		if (mDataList != null) {
//			Log.i("hjf", "mDataList != null" + mDataList.size());
//			for (int i = 0; i < mDataList.size(); i++) {
//				ConsumeItem consumeItem = mDataList.get(i);
////				mDataNameList.add(consumeItem.getItemName());
////				Log.i("hjf", consumeItem.getItemName());
//			}
//		}
//		Log.i("hjf", "mAdapter before");
//		mAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, mDataNameList);
//		mItemList.setAdapter(mAdapter);
//		Log.i("hjf", "mAdapter after");
//		
		mEmptyInfo.setVisibility(View.GONE); 
		
		mDataList = mConsumeItemService.findItemAll();
		mItemManagerAdapter = new ItemManagerAdapter(this.getActivity(), mDataList);
		mItemList.setAdapter(mItemManagerAdapter);
		
		
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).refreshMenuForFragment(FragmentIdConsts.FRAGMENT_ITEM_MANAGER);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mDataList.clear();
		mItemManagerAdapter.notifyDataSetChanged();
		
	}
	
	public void refreshItem() {
		mDataList = mConsumeItemService.findItemAll();
		if (mDataList != null) {
			for (int i = 0; i < mDataList.size(); i++) {
//				mDataNameList.add(mDataList.get(i).getItemName());
			}
		}
		mAdapter.notifyDataSetChanged();
	}

	
}
