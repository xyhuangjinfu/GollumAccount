package cn.hjf.gollumaccount.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.hjf.gollumaccount.FragmentIdConsts;
import cn.hjf.gollumaccount.R;
import cn.hjf.gollumaccount.activity.MainActivity;
import cn.hjf.gollumaccount.business.ConsumeItemService;
import cn.hjf.gollumaccount.business.ConsumeRecordService;
import cn.hjf.gollumaccount.model.ConsumeItem;
import cn.hjf.gollumaccount.model.ConsumeRecord;
import cn.hjf.gollumaccount.util.TimeUtil;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * 添加消费记录的界面
 * @author huangjinfu
 *
 */
public class ModifyRecordFragment extends Fragment {
	
	private EditText mRecordNameEditText; //记录名称
	private EditText mRecordPriceEditText; //消费金额
	private Spinner mRecordItemSpinner; //消费类别
	private EditText mRecordRemarksEditText; //消费备注
	private Button mCancelButton; //取消按钮
	private Button mCreateButton; //创建按钮
	private ScrollView mParentScrollView; //顶级滚动组件
	private DatePicker mRecordDateDatePicker; //消费日期选择器
	private TimePicker mRecordTimePicker; //消费时间选择器
	
	private TextView mRecordDateEditText;
	private TextView mRecordTimeEditText;
	
	private DatePickerDialog mDatePickerDialog;
	private TimePickerDialog mTimePickerDialog;
	private boolean mDateFlag = false;
	private boolean mTimeFlag = false;
	
	private int[] mRecordTime = new int[5];
	
	private ArrayAdapter mArrayAdapter; //mRecordItemSpinner的适配器
	private OnModifyRecordCallback mListener; //事件监听器
	
	private ConsumeRecordService mConsumeRecordService;
	
	private ArrayList<String> mItems;
	private ConsumeItemService mConsumeItemService;
	
	private ConsumeRecord mConsumeRecord;
	
	/**
	 * 对外提供的回调接口
	 * @author huangjinfu
	 *
	 */
	public interface OnModifyRecordCallback {
		public abstract void onModifyCanceled();
		public abstract void onModifySubmit(ConsumeRecord record);
	}
	
	public void init(ConsumeRecord record) {
		this.mConsumeRecord = record;
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
		
		mConsumeRecordService = new ConsumeRecordService(this.getActivity());
		mConsumeItemService = new ConsumeItemService(this.getActivity());
		
		View view = inflater.inflate(R.layout.fragment_add_record, null);
		initWidget(view);
		
		initViewValue();
		
		initItemSpinner();
		
		mArrayAdapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, mItems);
		mRecordItemSpinner.setAdapter(mArrayAdapter);
		
		mCancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCancelButton.setEnabled(false);
				mListener.onModifyCanceled();	
			}
		});
		
		mCreateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (validateInputValues()) {
					mCreateButton.setEnabled(false);
					mConsumeRecordService.updateRecord(constructRecord());
					mListener.onModifySubmit(null);
				}
				
			}
		});
		
//		mParentScrollView.scrollTo(0, 0);
//		mParentScrollView.smoothScrollTo(0, 0);
		
		mRecordDateEditText.setOnClickListener(OnDateClickListener);
		mRecordTimeEditText.setOnClickListener(OnTimeClickListener);
		
		return view;
	}
	
	OnClickListener OnDateClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Calendar calendar = Calendar.getInstance();
			mDatePickerDialog = new DatePickerDialog(getActivity(), new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					mDateFlag = true;
					mRecordTime[0] = year;
					mRecordTime[1] = monthOfYear;
					mRecordTime[2] = dayOfMonth;
					mRecordDateEditText.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
				}
			}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			mDatePickerDialog.show();
		}
	};
	
	OnClickListener OnTimeClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Calendar calendar = Calendar.getInstance();
			mTimePickerDialog = new TimePickerDialog(getActivity(), new OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					mTimeFlag = true;
					mRecordTime[3] = hourOfDay;
					mRecordTime[4] = minute;
					mRecordTimeEditText.setText(hourOfDay+":"+minute+":"+"00");
				}
			}, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
			mTimePickerDialog.show();
		}
	};
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).refreshMenuForFragment(FragmentIdConsts.FRAGMENT_MODIFY_RECORD);
		mListener = (OnModifyRecordCallback) activity;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		((MainActivity) this.getActivity()).refreshMenuForFragment(FragmentIdConsts.FRAGMENT_CONSUME);
	}
	
	/**
	 * 初始化Spinner的数据
	 */
	public void initItemSpinner() {
		mItems = mConsumeItemService.getAllItemName();
		
	}
	
	/**
	 * 初始化各控件，findViewById
	 */
	private void initWidget(View view) {
		mRecordNameEditText = (EditText) view.findViewById(R.id.et_record_name_modify);
		mRecordPriceEditText = (EditText) view.findViewById(R.id.et_record_price_modify);
		mRecordItemSpinner = (Spinner) view.findViewById(R.id.spn_record_item_modify);
		mRecordRemarksEditText = (EditText) view.findViewById(R.id.et_record_remarks_modify);
		mCancelButton = (Button) view.findViewById(R.id.btn_record_cancel_modify);
		mCreateButton = (Button) view.findViewById(R.id.btn_record_create_modify);
		mRecordDateEditText = (TextView) view.findViewById(R.id.et_record_date_modify);
		mRecordTimeEditText = (TextView) view.findViewById(R.id.et_record_time_modify);
	}
	
	/**
	 * 初始化各控件的值
	 */
	private void initViewValue() {
		mRecordNameEditText.setText(mConsumeRecord.getRecordName());
		mRecordPriceEditText.setText(String.valueOf(mConsumeRecord.getRecordPrice()));
		mRecordItemSpinner.setSelection(mConsumeRecord.getRecordItem());
		mRecordRemarksEditText.setText(mConsumeRecord.getRecordRemarks());
		mRecordDateEditText.setText(TimeUtil.getDateString(mConsumeRecord.getRecordTime()));
		mRecordTimeEditText.setText(TimeUtil.getShowTimeString(mConsumeRecord.getRecordTime()));
	}
	
	/**
	 * 根据名称找到索引
	 * @param name
	 * @return
	 */
	private int findIndexByName(String name) {
		int index = 0;
		for (int i = 0; i < mItems.size(); i++) {
			if (mItems.get(i).equals(name)) {
				index = i;
			}
		}
		return index;
	}
	
	/**
	 * 创建将要被创建的消费记录对象
	 * @return
	 */
	private ConsumeRecord constructRecord() {
		mConsumeRecord.setRecordName(mRecordNameEditText.getText().toString());
		mConsumeRecord.setRecordPrice(Float.valueOf(mRecordPriceEditText.getText().toString()));
		mConsumeRecord.setRecordItem(mRecordItemSpinner.getSelectedItemPosition() + 1);
		mConsumeRecord.setRecordRemarks(mRecordRemarksEditText.getText().toString());
		mConsumeRecord.setRecordTime(this.getRecordTime().getTime());
		mConsumeRecord.setCreateTime(System.currentTimeMillis());
		return mConsumeRecord;
	}
	
	/**
	 * 根据DatePicker 和 TimePicker 获得消费记录创建时间
	 * @return
	 */
	private Date getRecordTime() {
		int year = mRecordTime[0];
		int month = mRecordTime[1];
		int day = mRecordTime[2];
		int hour = mRecordTime[3];
		int minute = mRecordTime[4];
		Calendar calendar = Calendar.getInstance(); 
		calendar.set(year, month, day, hour, minute);
		Date date = calendar.getTime();
		return date;
		
	}
	
	/**
	 * 验证用户输入信息的合法性，主要是空内容的验证，并且给出提示
	 * @return false:不合法的输入
	 */
	private boolean validateInputValues() {
		boolean result = false;
		if (mRecordNameEditText.getText().toString().equals("") || mRecordNameEditText.getText().toString() == null) {
			Toast.makeText(this.getActivity(), "消费名称为空！", Toast.LENGTH_SHORT).show();
			return result;
		} else if (mRecordPriceEditText.getText().toString().equals("") || mRecordPriceEditText.getText().toString() == null) {
			Toast.makeText(this.getActivity(), "消费金额为空！", Toast.LENGTH_SHORT).show();
			return result;
		} else if (!mDateFlag) {
			Toast.makeText(this.getActivity(), "还未设置日期！", Toast.LENGTH_SHORT).show();
			return result;
		} else if (!mTimeFlag) {
			Toast.makeText(this.getActivity(), "还未设置时间！", Toast.LENGTH_SHORT).show();
			return result;
		} else {
			result = true;
		}
		return result;
	}
	
}
