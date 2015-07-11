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
 * ��Ѽ�¼����
 * 
 * @author huangjinfu
 * 
 */
public class ConsumeFragment extends Fragment implements
		LoadConsumeRecordTask.OnRecordLoadCallback,
		ConsumeQueryDialog.OnQueryListener {

	private PullToRefreshListView mRecordListView; // ��Ѽ�¼��ʾ��ListView
	private ListView mActualRecordListView; // ʵ�ʵ���Ѽ�¼��ʾ��ListView
	private View mEmptyView; // ListView��û�����ʱ��ʾ��View

	private ArrayList<ConsumeRecord> mRecords; // �����ʾ��Ѽ�¼�����

	private ConsumeRecordAdapter mConsumeRecordAdapter; // ListView��������
	private OnConsumeFragmentCallback mListener; // ��Fragment������Activity�����Ľӿ�

	private int mCurrentPage = 1; // ��ǰ�����ʾ���ڼ�ҳ��Ĭ����ʾ��һҳ
	private LoadConsumeRecordTask mLoadConsumeRecordTask; // ������Ѽ�¼��ݵ�AsyncTask

	private boolean mNeedRefreshFlag = true; // �Ƿ���Ҫˢ��ҳ�����

	private static final int NUM_PER_PAGE = 10; // ÿҳ��ʾ���������

	private boolean mIsInRefresh = false; // �Ƿ�����ִ��ˢ�²������������ִ��ˢ�²���������������ˢ�����

	private int mQueryYear = 0; // ��ѯ���
	private int mQueryMonth = 0; // ��ѯ�·�
	
	private ConsumeQueryDialog mConsumeQueryDialog; //��ѯ�Ի���
	private int mCurrentQueryItem = 9; //��ǰ�鿴�ķ���

	/**
	 * ��Activity�����Ľӿ�
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
		// ���ز����ļ�
		View view = inflater.inflate(R.layout.fragment_consume, null);
		mEmptyView = inflater.inflate(R.layout.view_consume_list_no_data, null);
		// ʵ��ؼ�
		initView(view);

		// ʵ������������������
		mConsumeRecordAdapter = new ConsumeRecordAdapter(this.getActivity(),
				mRecords);
		mRecordListView.setAdapter(mConsumeRecordAdapter);

		mQueryYear = Calendar.getInstance().get(Calendar.YEAR);
		mQueryMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;

		if (mNeedRefreshFlag) {
			// �첽�������
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

		// �������¼�
		mRecordListView.setOnItemClickListener(mItemClickListener);
		// �������������¼���ˢ�����
		mRecordListView.setOnRefreshListener(mOnRefreshListener);

		this.getActivity().registerForContextMenu(mRecordListView);

		return view;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_query:
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
	 * ��ʼ�������ļ��еĸ��ؼ�
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
	 * ListView Item �����¼�
	 */
	OnItemClickListener mItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mListener.onConsumeItemClick(mRecords.get(position - 1));
		}
	};

	/**
	 * ListView���������¼�
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
	 * �����Ƿ���Ҫˢ����ݣ���Ҫˢ����ݣ���ӵ�һҳ��ʼ��ѯ
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
	 * ˢ�����
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
		
		//û�����
		if (result.size() == 0) {
			Toast.makeText(getActivity(), "û�и�����", Toast.LENGTH_SHORT).show();
		} else {
			// �����´μ���ҳ��
			mCurrentPage++;
		}
		
		// ��Ӳ�ѯ�����ݼ��У�֪ͨListView�������
		mRecords.addAll(result);
		mConsumeRecordAdapter.notifyDataSetChanged();
		// PullToRefreshListViewˢ�½���
		mRecordListView.onRefreshComplete();
		// �Ƿ�����ˢ����ݱ�־
		mIsInRefresh = false;
		// �Ƿ���Ҫˢ����ݱ�־
		if (mNeedRefreshFlag) {
			mNeedRefreshFlag = false;
		}

	}

	/**
	 * ��Ѳ�ѯ�Ի����У������ѯ��ťʱ����
	 */
	@Override
	public void onQuery(int year, int month, int item) {
//		Toast.makeText(getActivity(), "��ݣ�" + year + "  �·�:" + month + "  ���ࣺ" + item, 1).show();
		refreshData(year, month, item);
		
	}
}
