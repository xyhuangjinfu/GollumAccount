package cn.hjf.gollumaccount.fragment;

import java.util.ArrayList;
import java.util.Calendar;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import cn.hjf.gollumaccount.FragmentIdConsts;
import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.activity.MainActivity;
import cn.hjf.gollumaccount.adapter.ConsumeRecordAdapter;
import cn.hjf.gollumaccount.asynctask.LoadConsumeRecordTask;
import cn.hjf.gollumaccount.business.ConsumeRecordService;
import cn.hjf.gollumaccount.dialog.ConsumeQueryDialog;
import cn.hjf.gollumaccount.dialog.LoadDialog;
import cn.hjf.gollumaccount.model.ConsumeRecord;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 消费记录界面
 * 
 * @author huangjinfu
 * 
 */
public class ConsumeFragment extends Fragment implements
		LoadConsumeRecordTask.OnRecordLoadCallback,
		ConsumeQueryDialog.OnQueryListener {

	private PullToRefreshListView mRecordListView; // 消费记录显示的ListView
	private ListView mActualRecordListView; // 实际的消费记录显示的ListView
	private View mEmptyView; // ListView中没有数据时显示的View

	private ArrayList<ConsumeRecord> mRecords; // 存放显示消费记录的数据

	private ConsumeRecordAdapter mConsumeRecordAdapter; // ListView的适配器
	private OnConsumeFragmentCallback mListener; // 该Fragment用于与Activity交互的接口

	private int mCurrentPage = 1; // 当前数据显示到第几页，默认显示第一页
	private LoadConsumeRecordTask mLoadConsumeRecordTask; // 加载消费记录数据的AsyncTask

	private boolean mNeedRefreshFlag = true; // 是否需要刷新页面数据

	private static final int NUM_PER_PAGE = 10; // 每页显示的数据条数

	private boolean mIsInRefresh = false; // 是否正在执行刷新操作，如果正在执行刷新操作，则下拉不会刷新数据

	private int mQueryYear = 0; // 查询年份
	private int mQueryMonth = 0; // 查询月份
	
	private ConsumeQueryDialog mConsumeQueryDialog; //查询对话框
	private int mCurrentQueryItem = 9; //当前查看的分类

	/**
	 * 与Activity交互的接口
	 * 
	 * @author huangjinfu
	 * 
	 */
	public interface OnConsumeFragmentCallback {
		public abstract void onConsumeItemClick(ConsumeRecord record);

		public abstract void onConsumeItemModify(ConsumeRecord record);
	}

	public ConsumeFragment() {
		mRecords = new ArrayList<ConsumeRecord>();
	}
	
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
		// 加载布局文件
		View view = inflater.inflate(R.layout.fragment_consume, null);
		mEmptyView = inflater.inflate(R.layout.view_consume_list_no_data, null);
		// 实例化控件
		initView(view);

		// 实例化适配器，绑定适配器
		mConsumeRecordAdapter = new ConsumeRecordAdapter(this.getActivity(),
				mRecords);
		mRecordListView.setAdapter(mConsumeRecordAdapter);

		mQueryYear = Calendar.getInstance().get(Calendar.YEAR);
		mQueryMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;

		if (mNeedRefreshFlag) {
			// 异步加载数据
			Integer[] params = new Integer[5];
			params[0] = mQueryYear;
			params[1] = mQueryMonth;
			params[2] = mCurrentPage;
			params[3] = NUM_PER_PAGE;
			params[4] = mCurrentQueryItem;
			Log.i("hjf", "ConsumeFragment - onCreateView - params[0]:"
					+ params[0]);
			Log.i("hjf", "ConsumeFragment - onCreateView - params[1]:"
					+ params[1]);
			Log.i("hjf", "ConsumeFragment - onCreateView - params[2]:"
					+ params[2]);
			Log.i("hjf", "ConsumeFragment - onCreateView - params[3]:"
					+ params[3]);
			Log.i("hjf", "ConsumeFragment - onCreateView - params[4]:"
					+ params[4]);
			if (!mIsInRefresh) {
				mIsInRefresh = true;
				LoadDialog.show(this.getActivity());
				new LoadConsumeRecordTask(this.getActivity(), this)
						.execute(params);
			}

		}

		// 监听点击事件
		mRecordListView.setOnItemClickListener(mItemClickListener);
		// 监听上拉下拉事件，刷新数据
		mRecordListView.setOnRefreshListener(mOnRefreshListener);

		this.getActivity().registerForContextMenu(mRecordListView);

		return view;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_query:
//			Calendar calendar = Calendar.getInstance();
//			new DatePickerDialog(this, new OnDateSetListener() {
//				@Override
//				public void onDateSet(DatePicker view, int year, int monthOfYear,
//						int dayOfMonth) {
//					mConsumeFragment.refreshData(year, monthOfYear);
//				}
//			}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
//			View QueryDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_query_consume, null);
//			Spinner yearSpinner = (Spinner) QueryDialogView.findViewById(R.id.spn_query_dialog_year);
//			Spinner monthSpinner = (Spinner) QueryDialogView.findViewById(R.id.spn_query_dialog_month);
//			ArrayList<String> yearData = new ArrayList<String>();
//			yearData.add("2015");
//			yearData.add("2014");
//			yearData.add("2013");
//			ArrayList<String> monthData = new ArrayList<String>();
//			monthData.add("12");
//			monthData.add("11");
//			monthData.add("10");
//			yearSpinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.item_spinner, yearData));
//			monthSpinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.item_spinner, monthData));
//			new AlertDialog.Builder(getActivity()).setView(QueryDialogView)
//			.setIcon(android.R.drawable.ic_dialog_info).setTitle("查询")
//			.setNegativeButton("取 消", new OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					
//				}
//			})
//			.setPositiveButton("查 询", new OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					
//				}
//			})
//			.create().show();
			mConsumeQueryDialog = new ConsumeQueryDialog(getActivity());
			mConsumeQueryDialog.setOnQueryListener(this);
			mConsumeQueryDialog.show();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity)
				.refreshMenuForFragment(FragmentIdConsts.FRAGMENT_CONSUME);
		mListener = (OnConsumeFragmentCallback) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mNeedRefreshFlag = true;
		mCurrentPage = 1;
		mRecords.clear();
		mConsumeRecordAdapter.notifyDataSetChanged();
		mCurrentQueryItem = 9;
	}

	/**
	 * 初始化布局文件中的各控件
	 * 
	 * @param view
	 */
	private void initView(View view) {
		mRecordListView = (PullToRefreshListView) view
				.findViewById(R.id.ptflv_consume_list);
		mActualRecordListView = mRecordListView.getRefreshableView();
		mActualRecordListView.setEmptyView(mEmptyView);
	}

	/**
	 * ListView Item 单击事件
	 */
	OnItemClickListener mItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mListener.onConsumeItemClick(mRecords.get(position - 1));
		}
	};

	/**
	 * ListView上拉下拉事件
	 */
	OnRefreshListener2<ListView> mOnRefreshListener = new OnRefreshListener2<ListView>() {
		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			Integer[] params = new Integer[5];
			params[0] = mQueryYear;
			params[1] = mQueryMonth;
			params[2] = mCurrentPage;
			params[3] = NUM_PER_PAGE;
			params[4] = mCurrentQueryItem;
			Log.i("hjf", "ConsumeFragment - onPullUpToRefresh - params[0]:"
					+ params[0]);
			Log.i("hjf", "ConsumeFragment - onPullUpToRefresh - params[1]:"
					+ params[1]);
			Log.i("hjf", "ConsumeFragment - onPullUpToRefresh - params[2]:"
					+ params[2]);
			Log.i("hjf", "ConsumeFragment - onPullUpToRefresh - params[3]:"
					+ params[3]);
			Log.i("hjf", "ConsumeFragment - onPullUpToRefresh - params[4]:"
					+ params[4]);
			if (!mIsInRefresh) {
				mIsInRefresh = true;
				new LoadConsumeRecordTask(ConsumeFragment.this.getActivity(),
						ConsumeFragment.this).execute(params);
			}

		}
	};

	/**
	 * 设置是否需要刷新数据，需要刷新数据，则从第一页开始查询
	 * 
	 * @param flag
	 */
	public void setmNeedRefreshFlag(boolean flag) {
		this.mNeedRefreshFlag = flag;
		mCurrentPage = 1;
		mRecords.clear();
		mConsumeRecordAdapter.notifyDataSetChanged();
	}

	/**
	 * 刷新数据
	 * 
	 * @param year
	 * @param month
	 */
	public void refreshData(int year, int month, int item) {
		mCurrentQueryItem = item;
		mCurrentPage = 1;
		mQueryYear = year;
		mQueryMonth = month + 1;
		mRecords.clear();
		mConsumeRecordAdapter.notifyDataSetChanged();
		Integer[] params = new Integer[5];
		params[0] = mQueryYear;
		params[1] = mQueryMonth;
		params[2] = mCurrentPage;
		params[3] = NUM_PER_PAGE;
		params[4] = mCurrentQueryItem;
		Log.i("hjf", "ConsumeFragment - refreshData - params[0]:" + params[0]);
		Log.i("hjf", "ConsumeFragment - refreshData - params[1]:" + params[1]);
		Log.i("hjf", "ConsumeFragment - refreshData - params[2]:" + params[2]);
		Log.i("hjf", "ConsumeFragment - refreshData - params[3]:" + params[3]);
		Log.i("hjf", "ConsumeFragment - refreshData - params[4]:" + params[4]);
		if (!mIsInRefresh) {
			mIsInRefresh = true;
			LoadDialog.show(this.getActivity());
			new LoadConsumeRecordTask(ConsumeFragment.this.getActivity(),
					ConsumeFragment.this).execute(params);
		}
	}

	@Override
	public void onRecordLoadCompleted(ArrayList<ConsumeRecord> result) {
		LoadDialog.close();
		
		//没有数据
		if (result.size() == 0) {
			Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
		} else {
			// 更新下次加载页码
			mCurrentPage++;
		}
		
		// 添加查询结果到数据集中，通知ListView更新数据
		mRecords.addAll(result);
		mConsumeRecordAdapter.notifyDataSetChanged();
		// PullToRefreshListView刷新结束
		mRecordListView.onRefreshComplete();
		// 是否正在刷新数据标志
		mIsInRefresh = false;
		// 是否需要刷新数据标志
		if (mNeedRefreshFlag) {
			mNeedRefreshFlag = false;
		}

	}

	/**
	 * 消费查询对话框中，点击查询按钮时调用
	 */
	@Override
	public void onQuery(int year, int month, int item) {
//		Toast.makeText(getActivity(), "年份：" + year + "  月份:" + month + "  分类：" + item, 1).show();
		refreshData(year, month, item);
		
	}
}
