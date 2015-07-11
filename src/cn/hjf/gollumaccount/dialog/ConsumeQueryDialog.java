package cn.hjf.gollumaccount.dialog;

import java.util.ArrayList;
import java.util.Calendar;

import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.business.ConsumeItemService;
import cn.hjf.gollumaccount.util.TimeUtil;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * 消费记录查询对话框
 * @author huangjinfu
 *
 */
public class ConsumeQueryDialog {
	
	private static AlertDialog mQueryDialog; //消费查询对话框
	private View mDialogView; //对话框加载的布局文件
	private Spinner mYearSpinner; //年份选择
	private Spinner mMonthSpinner; //月份选择
	private Spinner mItemSpinner; //分类选择
	private Button mCancelButton; //取消按钮
	private Button mQueryButton; //查询按钮
	
	private Context mContext; //上下文对象
	private OnQueryListener mListener; //事件回调接口
	
	private ArrayList<String> mYearData ; //年份数据
	private ArrayList<String> mMonthData ; //月份数据
	private ArrayList<String> mItemData; //分类数据
	private ConsumeItemService mConsumeItemService; //分类实体的业务逻辑对象
	private int mYear; //年份
	private int mMonth; //月份
	private int mItem; //分类
	
	/**
	 * 事件回调接口
	 * @author huangjinfu
	 *
	 */
	public interface OnQueryListener {
		public abstract void onQuery(int year, int month, int item);
	}
	
	public ConsumeQueryDialog(Context context) {
		this.mContext = context;
	}
	
	/**
	 * 显示对话框
	 * @param context
	 */
	public void show() {
		//加载布局文件
		mDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_query_consume, null);
		
		initView(mDialogView);
		
		initViewEvent();
		
		initViewValue();
		
		
		//初始化Dialog
		if (mQueryDialog == null) {
			mQueryDialog = new AlertDialog.Builder(mContext).setView(mDialogView).create();
		}
		mQueryDialog.show();
	}
	
	/**
	 * 实例化各控件
	 * @param view
	 */
	private void initView(View view) {
		mYearSpinner = (Spinner) view.findViewById(R.id.spn_query_dialog_year);
		mMonthSpinner = (Spinner) view.findViewById(R.id.spn_query_dialog_month);
		mItemSpinner = (Spinner) view.findViewById(R.id.spn_query_dialog_item);
		mCancelButton = (Button) view.findViewById(R.id.btn_query_dialog_cancel);
		mQueryButton = (Button) view.findViewById(R.id.btn_query_dialog_query);
	}
	
	/**
	 * 初始化各控件的值
	 */
	private void initViewValue() {
		//年份数据
		mYearData = new ArrayList<String>();
		for (int i = TimeUtil.getNowYear(); i >= 1980 ; i--) {
			mYearData.add(String.valueOf(i));
		}
		mYearSpinner.setAdapter(new ArrayAdapter<>(mContext, R.layout.item_spinner, mYearData));
		
		//月份数据
		mMonthData = new ArrayList<String>();
		for (int j = 1; j <= 12; j++) {
			mMonthData.add(String.valueOf(j));
		}
		mMonthSpinner.setAdapter(new ArrayAdapter<>(mContext, R.layout.item_spinner, mMonthData));
		mMonthSpinner.setSelection(TimeUtil.getNowMonth(), true);
		
		//分类数据
		mItemData = new ArrayList<String>();
		mConsumeItemService = new ConsumeItemService(mContext); //实例化业务逻辑对象
		mItemData = mConsumeItemService.getAllItemName();
		mItemData.add("汇总");
		mItemSpinner.setAdapter(new ArrayAdapter<>(mContext, R.layout.item_spinner, mItemData));
		mItemSpinner.setSelection(mItemData.size() - 1, true);
	}
	
	/**
	 * 设置各控件的事件
	 */
	private void initViewEvent() {
		//取消按钮事件
		mCancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ConsumeQueryDialog.this.close();
				
			}
		});
		
		//查询按钮事件
		mQueryButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ConsumeQueryDialog.this.close();
				if (ConsumeQueryDialog.this.mListener != null) {
					ConsumeQueryDialog.this.mListener.onQuery(mYear, mMonth, mItem);
				}
				
			}
		});
		
		//年份选择事件
		mYearSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mYear = Calendar.getInstance().get(Calendar.YEAR) - position;
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//月份选择事件
		mMonthSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mMonth = position;
//				mMonth = 1 + position;
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//分类选择事件
		mItemSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mItem = position + 1;
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	/**
	 * 关闭对话框
	 */
	private void close() {
		if ((mQueryDialog != null) && mQueryDialog.isShowing()) {
			mQueryDialog.cancel();
		}
	}
	
	/**
	 * 设置回调监听
	 * @param listener
	 */
	public void setOnQueryListener(OnQueryListener listener) {
		this.mListener = listener;
	}

}
