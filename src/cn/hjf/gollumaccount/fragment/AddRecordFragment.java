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
 * ������Ѽ�¼�Ľ���
 * @author huangjinfu
 *
 */
public class AddRecordFragment extends Fragment {
	
	private EditText mRecordNameEditText; //��¼����
	private EditText mRecordPriceEditText; //���ѽ��
	private Spinner mRecordItemSpinner; //�������
	private EditText mRecordRemarksEditText; //���ѱ�ע
	private Button mCancelButton; //ȡ����ť
	private Button mCreateButton; //������ť
	private ScrollView mParentScrollView; //�����������
	private DatePicker mRecordDateDatePicker; //��������ѡ����
	private TimePicker mRecordTimePicker; //����ʱ��ѡ����
	
	private TextView mRecordDateTextView;
	private TextView mRecordTimeTextView;
	
	private DatePickerDialog mDatePickerDialog;
	private TimePickerDialog mTimePickerDialog;
	private boolean mDateFlag = false;
	private boolean mTimeFlag = false;
	
	private int[] mRecordTime = new int[5];
	
	private ArrayAdapter mArrayAdapter; //mRecordItemSpinner��������
	private OnAddRecordCallback mListener; //�¼�������
	
	private ConsumeRecordService mConsumeRecordService;
	
	private ArrayList<String> mItems;
	private ConsumeItemService mConsumeItemService;
	
	/**
	 * �����ṩ�Ļص��ӿ�
	 * @author huangjinfu
	 *
	 */
	public interface OnAddRecordCallback {
		public abstract void onRecordCanceled();
		public abstract void onRecordCreated(ConsumeRecord record);
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
		
		
		
		initItemSpinner();
		
		mArrayAdapter = new ArrayAdapter(this.getActivity(), R.layout.item_spinner, mItems);
		mRecordItemSpinner.setAdapter(mArrayAdapter);
		
		mCancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCancelButton.setEnabled(false);
				mListener.onRecordCanceled();	
			}
		});
		
		mCreateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (validateInputValues()) {
					mCreateButton.setEnabled(false);
					mConsumeRecordService.saveRecord(constructRecord());
					mListener.onRecordCreated(null);
				}
				
			}
		});
		
		mRecordDateTextView.setOnClickListener(OnDateClickListener);
		mRecordTimeTextView.setOnClickListener(OnTimeClickListener);
		
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
					mRecordDateTextView.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
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
					mRecordTimeTextView.setText(hourOfDay+":"+minute+":"+"00");
				}
			}, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
			mTimePickerDialog.show();
		}
	};
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).refreshMenuForFragment(FragmentIdConsts.FRAGMENT_ADD_RECORD);
		mListener = (OnAddRecordCallback) activity;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		((MainActivity) this.getActivity()).refreshMenuForFragment(FragmentIdConsts.FRAGMENT_CONSUME);
	}
	
	/**
	 * ��ʼ��Spinner������
	 */
	public void initItemSpinner() {
		mItems = mConsumeItemService.getAllItemName();
		
	}
	
	/**
	 * ��ʼ�����ؼ���findViewById
	 */
	private void initWidget(View view) {
		mRecordNameEditText = (EditText) view.findViewById(R.id.et_record_name);
		mRecordPriceEditText = (EditText) view.findViewById(R.id.et_record_price);
		mRecordItemSpinner = (Spinner) view.findViewById(R.id.spn_record_item);
		mRecordRemarksEditText = (EditText) view.findViewById(R.id.et_record_remarks);
		mCancelButton = (Button) view.findViewById(R.id.btn_record_cancel);
		mCreateButton = (Button) view.findViewById(R.id.btn_record_create);
		
		mRecordDateTextView = (TextView) view.findViewById(R.id.et_record_date);
		mRecordTimeTextView = (TextView) view.findViewById(R.id.et_record_time);
	}
	
	/**
	 * ������Ҫ�����������Ѽ�¼����
	 * @return
	 */
	private ConsumeRecord constructRecord() {
		ConsumeRecord record = new ConsumeRecord();
		record.setRecordName(mRecordNameEditText.getText().toString());
		record.setRecordPrice(Float.valueOf(mRecordPriceEditText.getText().toString()));
		record.setRecordItem(mRecordItemSpinner.getSelectedItemPosition() + 1);
		record.setRecordRemarks(mRecordRemarksEditText.getText().toString());
		record.setRecordTime(this.getRecordTime().getTime());
		record.setCreateTime(System.currentTimeMillis());
		return record;
	}
	
	/**
	 * ����DatePicker �� TimePicker ������Ѽ�¼����ʱ��
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
	 * ��֤�û�������Ϣ�ĺϷ��ԣ���Ҫ�ǿ����ݵ���֤�����Ҹ�����ʾ
	 * @return false:���Ϸ�������
	 */
	private boolean validateInputValues() {
		boolean result = false;
		if (mRecordNameEditText.getText().toString().equals("") || mRecordNameEditText.getText().toString() == null) {
			Toast.makeText(this.getActivity(), "��������Ϊ�գ�", Toast.LENGTH_SHORT).show();
			return result;
		} else if (mRecordPriceEditText.getText().toString().equals("") || mRecordPriceEditText.getText().toString() == null) {
			Toast.makeText(this.getActivity(), "���ѽ��Ϊ�գ�", Toast.LENGTH_SHORT).show();
			return result;
		} else if (!mDateFlag) {
			Toast.makeText(this.getActivity(), "��δ�������ڣ�", Toast.LENGTH_SHORT).show();
			return result;
		} else if (!mTimeFlag) {
			Toast.makeText(this.getActivity(), "��δ����ʱ�䣡", Toast.LENGTH_SHORT).show();
			return result;
		} else {
			result = true;
		}
		return result;
	}
	
}
